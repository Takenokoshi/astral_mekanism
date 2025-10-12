package astral_mekanism.block.blockentity.prefab;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.cache.OneInputCachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableFloatingLong;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.chemical.GasInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.lookup.ISingleRecipeLookupHandler.ChemicalRecipeLookupHandler;
import mekanism.common.tile.base.SubstanceType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BEGasToGasMachine extends TileEntityRecipeMachine<GasToGasRecipe>
        implements ChemicalRecipeLookupHandler<Gas, GasStack, GasToGasRecipe> {
    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_ENERGY_REDUCED_RATE,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);
    MachineEnergyContainer<? extends BEGasToGasMachine> energyContainer;
    protected IGasTank inputTank;
    protected IGasTank outputTank;
    EnergyInventorySlot energySlot;
    GasInventorySlot inputSlot;
    GasInventorySlot outputSlot;
    private FloatingLong clientEnergyUsed = FloatingLong.ZERO;
    private int baselineMaxOperations = 1;

    private final IOutputHandler<@NotNull GasStack> outputHandler;
    private final IInputHandler<@NotNull GasStack> inputHandler;

    protected BEGasToGasMachine(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
        configComponent = new TileComponentConfig(this, TransmissionType.GAS, TransmissionType.ENERGY);
        configComponent.setupIOConfig(TransmissionType.GAS, inputTank, outputTank, RelativeSide.RIGHT)
                .setEjecting(true);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ejectorComponent = new TileComponentEjector(this, this::tankCapacity);
        ejectorComponent.setOutputData(configComponent, TransmissionType.GAS).setCanTankEject(t -> t == outputTank);
        baselineMaxOperations = 1;
        inputHandler = InputHelper.getInputHandler(inputTank, RecipeError.NOT_ENOUGH_INPUT);
        outputHandler = OutputHelper.getOutputHandler(outputTank, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
    }

    protected abstract long tankCapacity();

    protected abstract int maxOperation();

    @NotNull
    @Override
    protected IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper
                .forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputTank = ChemicalTankBuilder.GAS.create(tankCapacity(),
                ChemicalTankHelper.radioactiveInputTankPredicate(() -> outputTank),
                ChemicalTankBuilder.GAS.alwaysTrueBi, this::containsRecipe,
                ChemicalAttributeValidator.ALWAYS_ALLOW, recipeCacheListener));
        builder.addTank(outputTank = ChemicalTankBuilder.GAS.output(tankCapacity(), listener));
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

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(inputSlot = GasInventorySlot.fill(inputTank, listener, 5, 56));
        builder.addSlot(outputSlot = GasInventorySlot.drain(outputTank, listener, 155, 56));
        builder.addSlot(
                energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 155, 14));
        inputSlot.setSlotOverlay(SlotOverlay.MINUS);
        outputSlot.setSlotOverlay(SlotOverlay.PLUS);
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        inputSlot.fillTank();
        outputSlot.drainTank();
        clientEnergyUsed = recipeCacheLookupMonitor.updateAndProcess(energyContainer);
    }

    @NotNull
    @Override
    public CachedRecipe<GasToGasRecipe> createNewCachedRecipe(@NotNull GasToGasRecipe recipe, int cacheIndex) {
        return OneInputCachedRecipe.chemicalToChemical(recipe, recheckAllRecipeErrors, inputHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(() -> 1)
                .setBaselineMaxOperations(() -> baselineMaxOperations)
                .setOnFinish(this::markForSave);
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == Upgrade.SPEED) {
            baselineMaxOperations = (int) Math.pow(2, upgradeComponent.getUpgrades(Upgrade.SPEED))
                    * maxOperation();
        }
    }

    public FloatingLong getEnergyUsed() {
        return clientEnergyUsed;
    }

    @Nullable
    @Override
    public GasToGasRecipe getRecipe(int cacheIndex) {
        return (GasToGasRecipe) findFirstRecipe(inputHandler);
    }

    public abstract String getJEI();

    public MachineEnergyContainer<? extends BEGasToGasMachine> getEnergyContainer() {
        return this.energyContainer;
    }

    public IGasTank getInputTank() {
        return this.inputTank;
    }

    public IGasTank getOutputTank() {
        return this.outputTank;
    }

    @Override
    public int getRedstoneLevel() {
        return MekanismUtils.redstoneLevelFromContents(inputTank.getStored(), inputTank.getCapacity());
    }

    @Override
    protected boolean makesComparatorDirty(@Nullable SubstanceType type) {
        return type == SubstanceType.GAS;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableFloatingLong.create(this::getEnergyUsed, value -> clientEnergyUsed = value));
    }

}
