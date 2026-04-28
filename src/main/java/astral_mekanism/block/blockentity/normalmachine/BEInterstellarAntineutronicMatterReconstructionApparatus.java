package astral_mekanism.block.blockentity.normalmachine;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.interf.IHasCustomSizeContainer;
import astral_mekanism.enums.AMEUpgrade;
import astral_mekanism.recipes.cachedRecipe.ReconstructionCachedRecipe;
import astral_mekanism.recipes.recipe.ReconstructionRecipe;
import astral_mekanism.registries.AMERecipeTypes;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.Upgrade;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.PressurizedReactionRecipe.PressurizedReactionRecipeOutput;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableBoolean;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.ITripleRecipeLookupHandler.ItemFluidChemicalRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.ItemFluidChemical;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityProgressMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BEInterstellarAntineutronicMatterReconstructionApparatus
        extends TileEntityProgressMachine<ReconstructionRecipe> implements
        ItemFluidChemicalRecipeLookupHandler<Gas, GasStack, ReconstructionRecipe>,
        IHasCustomSizeContainer {

    public static final RecipeError NOT_ENOUGH_ITEM_INPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_FLUID_INPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_GAS_INPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_SPACE_ITEM_OUTPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_SPACE_GAS_OUTPUT_ERROR = RecipeError.create();
    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            NOT_ENOUGH_ITEM_INPUT_ERROR,
            NOT_ENOUGH_FLUID_INPUT_ERROR,
            NOT_ENOUGH_GAS_INPUT_ERROR,
            NOT_ENOUGH_SPACE_ITEM_OUTPUT_ERROR,
            NOT_ENOUGH_SPACE_GAS_OUTPUT_ERROR,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    private BasicFluidTank inputFluidTank;
    private IGasTank inputGasTank;
    private IGasTank outputGasTank;
    private FloatingLong recipeEnergyRequired = FloatingLong.ZERO;
    private final IOutputHandler<@NotNull PressurizedReactionRecipeOutput> outputHandler;
    private final IInputHandler<@NotNull ItemStack> itemInputHandler;
    private final IInputHandler<@NotNull FluidStack> fluidInputHandler;
    private final IInputHandler<@NotNull GasStack> gasInputHandler;
    private int recipeTicksRequired = 1000;
    private int baselineMaxOperations = 1;
    private boolean itemNotComsumed = false;
    InputInventorySlot inputSlot;
    OutputInventorySlot outputSlot;
    MachineEnergyContainer<BEInterstellarAntineutronicMatterReconstructionApparatus> energyContainer;

    public BEInterstellarAntineutronicMatterReconstructionApparatus(IBlockProvider blockProvider, BlockPos pos,
            BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES, 1000);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.ENERGY,
                TransmissionType.FLUID, TransmissionType.GAS);
        configComponent.setupIOConfig(TransmissionType.ITEM, inputSlot, outputSlot, RelativeSide.RIGHT);
        configComponent.setupInputConfig(TransmissionType.FLUID, inputFluidTank);
        configComponent
                .setupIOConfig(TransmissionType.GAS, inputGasTank, outputGasTank, RelativeSide.RIGHT, false, true)
                .setEjecting(true);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);

        ejectorComponent = new TileComponentEjector(this, () -> Long.MAX_VALUE);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM, TransmissionType.GAS)
                .setCanTankEject(tank -> tank != inputGasTank);

        itemInputHandler = InputHelper.getInputHandler(inputSlot, NOT_ENOUGH_ITEM_INPUT_ERROR);
        fluidInputHandler = InputHelper.getInputHandler(inputFluidTank, NOT_ENOUGH_FLUID_INPUT_ERROR);
        gasInputHandler = InputHelper.getInputHandler(inputGasTank, NOT_ENOUGH_GAS_INPUT_ERROR);
        outputHandler = OutputHelper.getOutputHandler(outputSlot, NOT_ENOUGH_SPACE_ITEM_OUTPUT_ERROR, outputGasTank,
                NOT_ENOUGH_SPACE_GAS_OUTPUT_ERROR);
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper
                .forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputGasTank = ChemicalTankBuilder.GAS.create(Long.MAX_VALUE,
                ChemicalTankHelper.radioactiveInputTankPredicate(() -> outputGasTank),
                (gas, automationType) -> containsRecipeCAB(inputSlot.getStack(), inputFluidTank.getFluid(), gas),
                this::containsRecipeC,
                ChemicalAttributeValidator.ALWAYS_ALLOW, recipeCacheListener));
        builder.addTank(outputGasTank = ChemicalTankBuilder.GAS.output(Long.MAX_VALUE, listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputFluidTank = BasicFluidTank.input(Integer.MAX_VALUE,
                fluid -> containsRecipeBAC(inputSlot.getStack(), fluid, inputGasTank.getStack()),
                this::containsRecipeB, recipeCacheListener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(inputSlot = InputInventorySlot.at(
                item -> containsRecipeABC(item, inputFluidTank.getFluid(), inputGasTank.getStack()),
                this::containsRecipeA,
                recipeCacheListener, 51, 40))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE,
                        getWarningCheck(NOT_ENOUGH_ITEM_INPUT_ERROR)));
        builder.addSlot(outputSlot = OutputInventorySlot.at(listener, 155, 40))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_SPACE_IN_OUTPUT,
                        getWarningCheck(NOT_ENOUGH_SPACE_ITEM_OUTPUT_ERROR)));
        return builder.build();
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        recipeCacheLookupMonitor.updateAndProcess();
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<ReconstructionRecipe, ItemFluidChemical<Gas, GasStack, ReconstructionRecipe>> getRecipeType() {
        return AMERecipeTypes.RECONSTRUCTION;
    }

    @Override
    public @Nullable ReconstructionRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(itemInputHandler, fluidInputHandler, gasInputHandler);
    }

    @Override
    public @NotNull CachedRecipe<ReconstructionRecipe> createNewCachedRecipe(@NotNull ReconstructionRecipe recipe,
            int cacheIndex) {
        return new ReconstructionCachedRecipe(recipe, recheckAllRecipeErrors, itemInputHandler, fluidInputHandler,
                gasInputHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setBaselineMaxOperations(this::getBaselineMaxOperations)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(this::setOperatingTicks);
    }

    @Override
    public void onCachedRecipeChanged(@Nullable CachedRecipe<ReconstructionRecipe> cachedRecipe, int cacheIndex) {
        super.onCachedRecipeChanged(cachedRecipe, cacheIndex);
        recipeEnergyRequired = cachedRecipe.getRecipe().getEnergyRequired();
        recipeTicksRequired = cachedRecipe.getRecipe().getDuration();
        itemNotComsumed = cachedRecipe.getRecipe().getItemNotConsumed();
        energyContainer.setEnergyPerTick(recipeEnergyRequired.add(2000000000));
        recalculateSpeed();
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == AMEUpgrade.STARDUST_SPEED.getValue()) {
            recalculateSpeed();
        }
    }

    private void recalculateSpeed() {
        int st = upgradeComponent.getUpgrades(AMEUpgrade.STARDUST_SPEED.getValue());
        ticksRequired = Math.max(1, recipeTicksRequired / (1 << st));
        baselineMaxOperations = Math.max(1, (1 << st) / recipeTicksRequired);
    }

    private int getBaselineMaxOperations() {
        return baselineMaxOperations;
    }

    public double getProcessingSpeed() {
        if (!getActive()) {
            return 0;
        }
        return ticksRequired > 1 ? 1 / (double) ticksRequired : baselineMaxOperations;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableInt.create(this::getBaselineMaxOperations, v -> baselineMaxOperations = v));
        container.track(SyncableInt.create(() -> recipeTicksRequired, v -> recipeTicksRequired = v));
        container.track(SyncableBoolean.create(() -> itemNotComsumed, v -> itemNotComsumed = v));
    }

    public boolean getItemNotConsumed() {
        return itemNotComsumed;
    }

    public int getInventoryXOffset() {
        return IHasCustomSizeContainer.super.getInventoryXOffset() + 23;
    }

    public int getInventoryYOffset() {
        return IHasCustomSizeContainer.super.getInventoryYOffset() + 16;
    }

    public BasicFluidTank getFluidTank() {
        return inputFluidTank;
    }

    public IGasTank getInputGasTank() {
        return inputGasTank;
    }

    public IGasTank getOutputGasTank() {
        return outputGasTank;
    }

    public MachineEnergyContainer<BEInterstellarAntineutronicMatterReconstructionApparatus> getEnergyContainer() {
        return energyContainer;
    }

}
