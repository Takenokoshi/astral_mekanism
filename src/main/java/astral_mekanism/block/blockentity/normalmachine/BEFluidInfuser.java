package astral_mekanism.block.blockentity.normalmachine;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.core.BlockEntityUtils;
import astral_mekanism.block.blockentity.elements.AstralMekDataType;
import astral_mekanism.recipes.cachedRecipe.FluidFluidToFluidCachedRecipe;
import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.FluidFluid;
import astral_mekanism.recipes.lookup.AMIRecipeLookUpHandler;
import astral_mekanism.recipes.recipe.FluidFluidToFluidRecipe;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.IContentsListener;
import mekanism.api.Upgrade;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableFloatingLong;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.FluidSlotInfo;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BEFluidInfuser extends TileEntityRecipeMachine<FluidFluidToFluidRecipe>
		implements AMIRecipeLookUpHandler.FluidFluidRecipeLookupHandler<FluidFluidToFluidRecipe> {

	public static final RecipeError NOT_ENOUGH_FLUIDA_INPUT_ERROR = RecipeError.create();
	public static final RecipeError NOT_ENOUGH_FLUIDB_INPUT_ERROR = RecipeError.create();
	public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
			RecipeError.NOT_ENOUGH_ENERGY, NOT_ENOUGH_FLUIDA_INPUT_ERROR, NOT_ENOUGH_FLUIDB_INPUT_ERROR,
			RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
			RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

	public BasicFluidTank inputTankA;
	public BasicFluidTank inputTankB;
	public BasicFluidTank outputTank;
	MachineEnergyContainer<BEFluidInfuser> energyContainer;
	FluidInventorySlot inputSlotA;
	FluidInventorySlot inputSlotB;
	FluidInventorySlot outputSlot;
	EnergyInventorySlot energySlot;
	private final IInputHandler<FluidStack> inputHandlerA;
	private final IInputHandler<FluidStack> inputHandlerB;
	private final IOutputHandler<FluidStack> outputHandler;
	private FloatingLong clientEnergyUsed = FloatingLong.ZERO;
	private int baselineMaxOperations = 1;

	public BEFluidInfuser(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
		super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
		this.configComponent = new TileComponentConfig(this, TransmissionType.ENERGY, TransmissionType.FLUID);
		configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
		ConfigInfo fluidConfig = configComponent.getConfig(TransmissionType.FLUID);
		if (fluidConfig != null) {
			fluidConfig.addSlotInfo(DataType.INPUT_1, new FluidSlotInfo(true, false, inputTankA));
			fluidConfig.addSlotInfo(DataType.INPUT_2, new FluidSlotInfo(true, false, inputTankB));
			fluidConfig.addSlotInfo(DataType.OUTPUT, new FluidSlotInfo(false, true, outputTank));
			fluidConfig.addSlotInfo(AstralMekDataType.INPUT_OUTPUT_astral,
					new FluidSlotInfo(true, false, inputTankA, inputTankB));
			fluidConfig.addSlotInfo(AstralMekDataType.INPUT1_OUTPUT, new FluidSlotInfo(true, false, inputTankA));
			fluidConfig.addSlotInfo(AstralMekDataType.INPUT2_OUTPUT, new FluidSlotInfo(true, false, inputTankB));
			fluidConfig.setCanEject(true);
		}
		this.ejectorComponent = new TileComponentEjector(this, () -> Long.MAX_VALUE, () -> 200000,
				() -> FloatingLong.MAX_VALUE);
		inputHandlerA = InputHelper.getInputHandler(inputTankA, NOT_ENOUGH_FLUIDA_INPUT_ERROR);
		inputHandlerB = InputHelper.getInputHandler(inputTankB, NOT_ENOUGH_FLUIDB_INPUT_ERROR);
		outputHandler = OutputHelper.getOutputHandler(outputTank, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
	}

	@NotNull
	@Override
	protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
			IContentsListener recipeCacheListener) {
		InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection,
				this::getConfig);
		builder.addSlot(inputSlotA = FluidInventorySlot.fill(inputTankA, listener, 6, 56));
		builder.addSlot(inputSlotB = FluidInventorySlot.fill(inputTankB, listener, 154, 56));
		builder.addSlot(outputSlot = FluidInventorySlot.drain(outputTank, listener, 80, 65));
		builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel,
				listener, 154, 14));
		inputSlotA.setSlotType(ContainerSlotType.INPUT);
		inputSlotB.setSlotType(ContainerSlotType.INPUT);
		outputSlot.setSlotType(ContainerSlotType.OUTPUT);
		inputSlotA.setSlotOverlay(SlotOverlay.MINUS);
		inputSlotB.setSlotOverlay(SlotOverlay.MINUS);
		outputSlot.setSlotOverlay(SlotOverlay.PLUS);
		return builder.build();
	}

	@NotNull
	@Override
	protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener,
			IContentsListener recipeCacheListener) {
		FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
		builder.addTank(inputTankA = BasicFluidTank.input(200000,
				fluid -> containsRecipeAB(fluid, inputTankB.getFluid()), this::containsRecipeA,
				recipeCacheListener));
		builder.addTank(inputTankB = BasicFluidTank.input(200000,
				fluid -> containsRecipeBA(inputTankA.getFluid(), fluid), this::containsRecipeB,
				recipeCacheListener));
		builder.addTank(outputTank = BasicFluidTank.output(200000, recipeCacheListener));
		return builder.build();
	}

	@NotNull
	@Override
	protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener,
			IContentsListener recipeCacheListener) {
		EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection,
				this::getConfig);
		builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener));
		return builder.build();
	}

	@Override
	protected void onUpdateServer() {
		super.onUpdateServer();
		energySlot.fillContainerOrConvert();
		inputSlotA.fillTank(inputSlotA);
		inputSlotB.fillTank(inputSlotB);
		outputSlot.drainTank(outputSlot);
		clientEnergyUsed = recipeCacheLookupMonitor.updateAndProcess(energyContainer);
		BlockEntityUtils.fluidEject(this,
				List.of(AstralMekDataType.INPUT1_OUTPUT, AstralMekDataType.INPUT2_OUTPUT, AstralMekDataType.INPUT_OUTPUT_astral),
				outputTank);
	}

	@Override
	public void recalculateUpgrades(Upgrade upgrade) {
		super.recalculateUpgrades(upgrade);
		if (upgrade == Upgrade.SPEED) {
			baselineMaxOperations = (int) Math.pow(2, upgradeComponent.getUpgrades(Upgrade.SPEED)) * 5;
		}
	}

	@Override
	public @NotNull CachedRecipe<FluidFluidToFluidRecipe> createNewCachedRecipe(
			@NotNull FluidFluidToFluidRecipe recipe, int cacheIndex) {
		CachedRecipe<FluidFluidToFluidRecipe> cachedRecipe = new FluidFluidToFluidCachedRecipe(recipe,
				recheckAllRecipeErrors, inputHandlerA, inputHandlerB, outputHandler)
				.setErrorsChanged((x$0) -> {
					this.onErrorsChanged(x$0);
				}).setCanHolderFunction(() -> {
					return MekanismUtils.canFunction(this);
				}).setActive(this::setActive);
		MachineEnergyContainer<BEFluidInfuser> energyContainer = this.energyContainer;
		return cachedRecipe.setEnergyRequirements(energyContainer::getEnergyPerTick, this.energyContainer)
				.setBaselineMaxOperations(() -> baselineMaxOperations).setOnFinish(this::markForSave);
	}

	@Override
	public @Nullable FluidFluidToFluidRecipe getRecipe(int arg0) {
		return (FluidFluidToFluidRecipe) this.findFirstRecipe(inputHandlerA, inputHandlerB);
	}

	@Override
	public @NotNull IMekanismRecipeTypeProvider<FluidFluidToFluidRecipe, FluidFluid<FluidFluidToFluidRecipe>> getRecipeType() {
		return AstralMekanismRecipeTypes.FLUID_INFUSER_RECIPE;
	}

	public MachineEnergyContainer<BEFluidInfuser> getEnergyContainer() {
		return this.energyContainer;
	}

	public FloatingLong getEnergyUsed() {
		return clientEnergyUsed;
	}

	@Override
	public void addContainerTrackers(MekanismContainer container) {
		super.addContainerTrackers(container);
		container.track(SyncableFloatingLong.create(this::getEnergyUsed, value -> clientEnergyUsed = value));
	}

}
