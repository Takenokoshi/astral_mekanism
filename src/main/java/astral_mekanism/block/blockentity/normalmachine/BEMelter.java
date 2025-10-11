package astral_mekanism.block.blockentity.normalmachine;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.Upgrade;
import mekanism.api.heat.HeatAPI.HeatTransfer;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.OneInputCachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.heat.BasicHeatCapacitor;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.heat.HeatCapacitorHelper;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableDouble;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.ISingleRecipeLookupHandler.ItemRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleItem;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityProgressMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BEMelter extends TileEntityProgressMachine<ItemStackToFluidRecipe>
		implements ItemRecipeLookupHandler<ItemStackToFluidRecipe> {

	public static final RecipeError NOT_ENOUGH_ITEM_INPUT_ERROR = RecipeError.create();
	public static final RecipeError NOT_ENOUGH_SPACE_FLUID_OUTPUT_ERROR = RecipeError.create();
	public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
			NOT_ENOUGH_ITEM_INPUT_ERROR,
			NOT_ENOUGH_SPACE_FLUID_OUTPUT_ERROR,
			RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

	InputInventorySlot inputSlot;
	public BasicFluidTank outputFluidTank;
	FluidInventorySlot fluidSlot;
	public BasicHeatCapacitor heatCapacitor;
	private final IInputHandler<@NotNull ItemStack> itemInputHandler;
	private final IOutputHandler<FluidStack> outputHandler;
	private double lastEnvironmentLoss;

	public BEMelter(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
		super(blockProvider, pos, state, TRACKED_ERROR_TYPES, 100);
		this.configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.FLUID,
				TransmissionType.HEAT);
		this.configComponent.setupInputConfig(TransmissionType.ITEM, inputSlot);
		this.configComponent.setupInputConfig(TransmissionType.HEAT, heatCapacitor);
		this.configComponent.setupOutputConfig(TransmissionType.FLUID, outputFluidTank, RelativeSide.BOTTOM);
		this.ejectorComponent = new TileComponentEjector(this, () -> Long.MAX_VALUE, () -> 2147483647,
				() -> FloatingLong.MAX_VALUE);
		this.ejectorComponent.setOutputData(configComponent, TransmissionType.FLUID);
		itemInputHandler = InputHelper.getInputHandler(inputSlot, NOT_ENOUGH_ITEM_INPUT_ERROR);
		outputHandler = OutputHelper.getOutputHandler(outputFluidTank, NOT_ENOUGH_SPACE_FLUID_OUTPUT_ERROR);
	}

	@NotNull
	@Override
	protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
			IContentsListener recipeCacheListener) {
		InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection,
				this::getConfig);
		builder.addSlot(inputSlot = InputInventorySlot.at(item -> containsRecipe(item), this::containsRecipe,
				recipeCacheListener, 54, 40));
		builder.addSlot(this.fluidSlot = FluidInventorySlot.drain(this.outputFluidTank, listener, 115, 10));
		return builder.build();
	}

	@NotNull
	@Override
	protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener,
			IContentsListener recipeCacheListener) {
		FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
		builder.addTank(outputFluidTank = BasicFluidTank.output(2147483647, recipeCacheListener));
		return builder.build();
	}

	@NotNull
	@Override
	protected IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener,
			IContentsListener recipeCacheListener, CachedAmbientTemperature ambientTemperature) {
		HeatCapacitorHelper builder = HeatCapacitorHelper.forSide(this::getDirection);
		builder.addCapacitor(
				heatCapacitor = BasicHeatCapacitor.create(100, 5, 100, ambientTemperature, listener));
		return builder.build();
	}

	public void onCachedRecipeChanged(@Nullable CachedRecipe<ItemStackToFluidRecipe> cachedRecipe, int cacheIndex) {
		super.onCachedRecipeChanged(cachedRecipe, cacheIndex);
		int recipeDuration = 100;
		boolean update = this.baseTicksRequired != recipeDuration;
		this.baseTicksRequired = recipeDuration;
		if (update) {
			this.recalculateUpgrades(Upgrade.SPEED);
		}
	}

	@Override
	public @NotNull CachedRecipe<ItemStackToFluidRecipe> createNewCachedRecipe(
			@NotNull ItemStackToFluidRecipe recipe,
			int cacheIndex) {
		CachedRecipe<ItemStackToFluidRecipe> cachedRecipe = OneInputCachedRecipe.itemToFluid(
				recipe, recheckAllRecipeErrors, itemInputHandler, outputHandler)
				.setErrorsChanged((arg0) -> this.onErrorsChanged(arg0))
				.setCanHolderFunction(() -> {
					return MekanismUtils.canFunction(this);
				})
				.setActive(this::setActive);
		return cachedRecipe
				.setRequiredTicks(this::getTicksRequired)
				.setOnFinish(this::markForSave)
				.setOperatingTicksChanged((arg1) -> {
					this.setOperatingTicks(arg1);
				});
	}

	protected void onUpdateServer() {
		super.onUpdateServer();
		this.fluidSlot.drainTank(fluidSlot);
		if (this.heatCapacitor.getTemperature() >= 10000) {
			this.recipeCacheLookupMonitor.updateAndProcess();
		}
		HeatTransfer transfer = simulate();
		lastEnvironmentLoss = transfer.environmentTransfer();
	}

	@Override
	public void addContainerTrackers(MekanismContainer container) {
		super.addContainerTrackers(container);
		container.track(SyncableDouble.create(this::getLastEnvironmentLoss,
				value -> lastEnvironmentLoss = value));
	}

	@Override
	public @Nullable ItemStackToFluidRecipe getRecipe(int arg0) {
		return (ItemStackToFluidRecipe) this.findFirstRecipe(this.itemInputHandler);
	}

	@Override
	public @NotNull IMekanismRecipeTypeProvider<ItemStackToFluidRecipe, SingleItem<ItemStackToFluidRecipe>> getRecipeType() {
		return AstralMekanismRecipeTypes.Melter_recipe;
	}

	public double getLastEnvironmentLoss() {
		return lastEnvironmentLoss;
	}

}
