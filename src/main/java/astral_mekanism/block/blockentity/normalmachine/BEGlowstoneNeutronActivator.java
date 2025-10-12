package astral_mekanism.block.blockentity.normalmachine;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.cache.OneInputCachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.slot.chemical.GasInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.ISingleRecipeLookupHandler.ChemicalRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleChemical;
import mekanism.common.tile.base.SubstanceType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.interfaces.IBoundingBlock;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BEGlowstoneNeutronActivator extends TileEntityRecipeMachine<GasToGasRecipe>
		implements IBoundingBlock, ChemicalRecipeLookupHandler<Gas, GasStack, GasToGasRecipe> {

	private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
			RecipeError.NOT_ENOUGH_INPUT,
			RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
			RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);
	public static final long MAX_GAS = 10_000;

	public IGasTank inputTank;
	public IGasTank outputTank;

	private float peakProductionRate;
	private float productionRate;
	private boolean settingsChecked;
	private final IOutputHandler<@NotNull GasStack> outputHandler;
	private final IInputHandler<@NotNull GasStack> inputHandler;

	GasInventorySlot inputSlot;
	GasInventorySlot outputSlot;

	public BEGlowstoneNeutronActivator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
		super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
		configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.GAS);
		configComponent.setupIOConfig(TransmissionType.ITEM, inputSlot, outputSlot, RelativeSide.FRONT);
		configComponent.setupIOConfig(TransmissionType.GAS, inputTank, outputTank, RelativeSide.FRONT, false,
				true).setEjecting(true);
		ejectorComponent = new TileComponentEjector(this);
		ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM, TransmissionType.GAS)
				.setCanTankEject(tank -> tank != inputTank);
		inputHandler = InputHelper.getInputHandler(inputTank, RecipeError.NOT_ENOUGH_INPUT);
		outputHandler = OutputHelper.getOutputHandler(outputTank, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
	}

	@NotNull
	@Override
	public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener,
			IContentsListener recipeCacheListener) {
		ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper
				.forSideGasWithConfig(this::getDirection, this::getConfig);
		builder.addTank(inputTank = ChemicalTankBuilder.GAS.create(MAX_GAS,
				ChemicalTankHelper.radioactiveInputTankPredicate(() -> outputTank),
				ChemicalTankBuilder.GAS.alwaysTrueBi, this::containsRecipe,
				ChemicalAttributeValidator.ALWAYS_ALLOW, recipeCacheListener));
		builder.addTank(outputTank = ChemicalTankBuilder.GAS.output(MAX_GAS, listener));
		return builder.build();
	}

	@NotNull
	@Override
	protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
			IContentsListener recipeCacheListener) {
		InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection,
				this::getConfig);
		builder.addSlot(inputSlot = GasInventorySlot.fill(inputTank, listener, 5, 56));
		builder.addSlot(outputSlot = GasInventorySlot.drain(outputTank, listener, 155, 56));
		inputSlot.setSlotType(ContainerSlotType.INPUT);
		inputSlot.setSlotOverlay(SlotOverlay.MINUS);
		outputSlot.setSlotType(ContainerSlotType.OUTPUT);
		outputSlot.setSlotOverlay(SlotOverlay.PLUS);
		return builder.build();
	}

	private void recheckSettings() {
		Level world = getLevel();
		if (world == null) {
			return;
		}
		peakProductionRate = MekanismConfig.general.maxSolarNeutronActivatorRate.get() * 2;
		settingsChecked = true;
	}

	@Override
	protected void onUpdateServer() {
		super.onUpdateServer();
		if (!settingsChecked) {
			recheckSettings();
		}
		inputSlot.fillTank();
		outputSlot.drainTank();
		productionRate = recalculateProductionRate();
		recipeCacheLookupMonitor.updateAndProcess();
	}

	@NotNull
	@Override
	public IMekanismRecipeTypeProvider<GasToGasRecipe, SingleChemical<Gas, GasStack, GasToGasRecipe>> getRecipeType() {
		return MekanismRecipeType.ACTIVATING;
	}

	@Nullable
	@Override
	public GasToGasRecipe getRecipe(int cacheIndex) {
		return findFirstRecipe(inputHandler);
	}

	private boolean canFunction() {
		return MekanismUtils.canFunction(this);
	}

	private float recalculateProductionRate() {
		Level world = getLevel();
		if (world == null || !canFunction()) {
			return 0;
		}
		return peakProductionRate;
	}

	@NotNull
	@Override
	public CachedRecipe<GasToGasRecipe> createNewCachedRecipe(@NotNull GasToGasRecipe recipe, int cacheIndex) {
		return OneInputCachedRecipe
				.chemicalToChemical(recipe, recheckAllRecipeErrors, inputHandler, outputHandler)
				.setErrorsChanged(this::onErrorsChanged)
				.setCanHolderFunction(this::canFunction)
				.setActive(this::setActive)
				.setOnFinish(this::markForSave)
				.setRequiredTicks(() -> 1)
				.setBaselineMaxOperations(() -> (int) productionRate);
	}

	@Override
	public int getRedstoneLevel() {
		return MekanismUtils.redstoneLevelFromContents(inputTank.getStored(), inputTank.getCapacity());
	}

	@Override
	protected boolean makesComparatorDirty(@Nullable SubstanceType type) {
		return type == SubstanceType.GAS;
	}
}