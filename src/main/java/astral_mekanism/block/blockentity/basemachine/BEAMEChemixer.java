package astral_mekanism.block.blockentity.basemachine;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fr.iglee42.evolvedmekanism.interfaces.ChemixerCachedRecipe;
import fr.iglee42.evolvedmekanism.interfaces.EMInputRecipeCache;
import fr.iglee42.evolvedmekanism.recipes.ChemixerRecipe;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
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
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.ITripleRecipeLookupHandler;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BEAMEChemixer extends TileEntityRecipeMachine<ChemixerRecipe> implements
        ITripleRecipeLookupHandler.ObjectObjectChemicalRecipeLookupHandler<ItemStack, ItemStack, Gas, GasStack, ChemixerRecipe, EMInputRecipeCache.ItemItemChemical<Gas, GasStack, ChemixerRecipe>> {

    public static final RecipeError NOT_ENOUGH_ITEM_INPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_SECONDARY_INPUT = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_GAS_INPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_SPACE_ITEM_OUTPUT_ERROR = RecipeError.create();
    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            NOT_ENOUGH_ITEM_INPUT_ERROR,
            NOT_ENOUGH_SECONDARY_INPUT,
            NOT_ENOUGH_GAS_INPUT_ERROR,
            NOT_ENOUGH_SPACE_ITEM_OUTPUT_ERROR,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    private IGasTank inputGasTank;
    private final IOutputHandler<@NotNull ItemStack> outputHandler;
    private final IInputHandler<@NotNull ItemStack> itemInputHandler;
    private final IInputHandler<@NotNull ItemStack> extraInputHandler;
    private final IInputHandler<@NotNull GasStack> gasInputHandler;
    private InputInventorySlot mainInputSlot;
    private InputInventorySlot extraInputSlot;
    private OutputInventorySlot outputSlot;
    private EnergyInventorySlot energySlot;
    private MachineEnergyContainer<BEAMEChemixer> energyContainer;

    public BEAMEChemixer(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.ENERGY,
                TransmissionType.GAS);
        configComponent.setupItemIOExtraConfig(mainInputSlot, outputSlot, extraInputSlot, energySlot);
        configComponent.setupInputConfig(TransmissionType.GAS, inputGasTank);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM, TransmissionType.GAS)
                .setCanTankEject(tank -> tank != inputGasTank);

        itemInputHandler = InputHelper.getInputHandler(mainInputSlot, NOT_ENOUGH_ITEM_INPUT_ERROR);
        extraInputHandler = InputHelper.getInputHandler(extraInputSlot, NOT_ENOUGH_SECONDARY_INPUT);
        gasInputHandler = InputHelper.getInputHandler(inputGasTank, NOT_ENOUGH_GAS_INPUT_ERROR);
        outputHandler = OutputHelper.getOutputHandler(outputSlot, NOT_ENOUGH_SPACE_ITEM_OUTPUT_ERROR);
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper
                .forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(
                inputGasTank = createInputTank(
                        (gas, type) -> type != AutomationType.EXTERNAL,
                        (gas, automationType) -> containsRecipeCAB(mainInputSlot.getStack(), extraInputSlot.getStack(),
                                gas),
                        this::containsRecipeC,
                        ChemicalAttributeValidator.ALWAYS_ALLOW, recipeCacheListener));
        return builder.build();
    }

    protected abstract IGasTank createInputTank(BiPredicate<Gas, AutomationType> canExtract,
            BiPredicate<Gas, AutomationType> canInsert, Predicate<Gas> validator,
            @Nullable ChemicalAttributeValidator attributeValidator, @Nullable IContentsListener listener);

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
        builder.addSlot(mainInputSlot = InputInventorySlot.at(
                item -> containsRecipeABC(item, extraInputSlot.getStack(), inputGasTank.getStack()),
                this::containsRecipeA,
                recipeCacheListener, 64, 17))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE,
                        getWarningCheck(NOT_ENOUGH_ITEM_INPUT_ERROR)));
        builder.addSlot(extraInputSlot = InputInventorySlot.at(
                item -> containsRecipeBAC(mainInputSlot.getStack(), item, inputGasTank.getStack()),
                this::containsRecipeB, recipeCacheListener,
                64, 53))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE,
                        getWarningCheck(NOT_ENOUGH_SECONDARY_INPUT)));
        builder.addSlot(outputSlot = OutputInventorySlot.at(listener, 116, 35))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_SPACE_IN_OUTPUT,
                        getWarningCheck(NOT_ENOUGH_SPACE_ITEM_OUTPUT_ERROR)));
        builder.addSlot(
                energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 141, 35));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        recipeCacheLookupMonitor.updateAndProcess();
    }

    @NotNull
    @Override
    public IMekanismRecipeTypeProvider<ChemixerRecipe, EMInputRecipeCache.ItemItemChemical<Gas, GasStack, ChemixerRecipe>> getRecipeType() {
        return EMRecipeType.CHEMIXING;
    }

    @Nullable
    @Override
    public ChemixerRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(itemInputHandler, extraInputHandler, gasInputHandler);
    }

    public MachineEnergyContainer<BEAMEChemixer> getEnergyContainer() {
        return energyContainer;
    }

    public IGasTank getInputGasTank() {
        return inputGasTank;
    }

    FloatingLong getEnergyUsage() {
        return getActive() ? energyContainer.getEnergyPerTick() : FloatingLong.ZERO;
    }

    @Override
    public @NotNull CachedRecipe<ChemixerRecipe> createNewCachedRecipe(@NotNull ChemixerRecipe recipe, int cacheIndex) {
        return new ChemixerCachedRecipe(recipe, recheckAllRecipeErrors, itemInputHandler, extraInputHandler,
                gasInputHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setBaselineMaxOperations(this::getBaselineMaxOperations)
                .setOnFinish(this::markForSave);
    }

    protected abstract int getBaselineMaxOperations();

}