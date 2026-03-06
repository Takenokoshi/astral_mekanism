package astral_mekanism.block.blockentity.astralmachine;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.interf.IItemGasToItemMachine;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.NucleosynthesizingRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.cache.TwoInputCachedRecipe;
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
import mekanism.common.inventory.container.sync.SyncableFloatingLong;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.ItemChemical;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralAntiprotonicNucleosynthesizer extends TileEntityRecipeMachine<NucleosynthesizingRecipe>
        implements IItemGasToItemMachine<BEAstralAntiprotonicNucleosynthesizer, NucleosynthesizingRecipe> {

    private InputInventorySlot inputSlot;
    private IGasTank inputTank;
    private OutputInventorySlot outputSlot;
    private MachineEnergyContainer<BEAstralAntiprotonicNucleosynthesizer> energyContainer;
    private EnergyInventorySlot energySlot;
    private FloatingLong lastEnergyUsed = FloatingLong.ZERO;

    private final IInputHandler<ItemStack> itemInputHandler;
    private final IInputHandler<GasStack> gasInputHandler;
    private final IOutputHandler<ItemStack> outputHandler;

    public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    public BEAstralAntiprotonicNucleosynthesizer(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.GAS,
                TransmissionType.ENERGY);
        configComponent.setupItemIOConfig(inputSlot, outputSlot, energySlot);
        configComponent.setupInputConfig(TransmissionType.GAS, inputTank);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ejectorComponent = new TileComponentEjector(this).setOutputData(configComponent, TransmissionType.ITEM);
        itemInputHandler = InputHelper.getInputHandler(inputSlot, RecipeError.NOT_ENOUGH_INPUT);
        gasInputHandler = InputHelper.getInputHandler(inputTank, RecipeError.NOT_ENOUGH_SECONDARY_INPUT);
        outputHandler = OutputHelper.getOutputHandler(outputSlot, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(inputSlot = InputInventorySlot.at(stack -> containsRecipeAB(stack, inputTank.getStack()),
                this::containsRecipeA, recipeCacheListener, 28, 40));
        builder.addSlot(outputSlot = OutputInventorySlot.at(listener, 132, 40));
        builder.addSlot(energySlot = EnergyInventorySlot.fill(energyContainer, listener, 132, 18));
        return builder.build();
    }

    @NotNull
    @Override
    protected IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper
                .forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputTank = ChemicalTankBuilder.GAS.create(Long.MAX_VALUE,
                (gas, a) -> false,
                (gas, a) -> containsRecipeBA(inputSlot.getStack(), gas),
                this::containsRecipeB,
                ChemicalAttributeValidator.ALWAYS_ALLOW, recipeCacheListener));
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
        energySlot.fillContainerOrConvert();
        lastEnergyUsed = recipeCacheLookupMonitor.updateAndProcess(energyContainer);
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<NucleosynthesizingRecipe, ItemChemical<Gas, GasStack, NucleosynthesizingRecipe>> getRecipeType() {
        return MekanismRecipeType.NUCLEOSYNTHESIZING;
    }

    @Override
    public @Nullable NucleosynthesizingRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(itemInputHandler, gasInputHandler);
    }

    @Override
    public @NotNull CachedRecipe<NucleosynthesizingRecipe> createNewCachedRecipe(
            @NotNull NucleosynthesizingRecipe recipe, int cacheIndex) {
        return TwoInputCachedRecipe
                .itemChemicalToItem(recipe, recheckAllRecipeErrors, itemInputHandler, gasInputHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setOnFinish(this::markForSave)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setBaselineMaxOperations(() -> 0x7fffffff);
    }

    @Override
    public MachineEnergyContainer<BEAstralAntiprotonicNucleosynthesizer> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public double getProgressScaled() {
        return getActive() ? 1 : 0;
    }

    @Override
    public IGasTank getInputTank() {
        return inputTank;
    }

    @Override
    public FloatingLong getEnergyUsage() {
        return getActive() ? lastEnergyUsed : FloatingLong.ZERO;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableFloatingLong.create(() -> lastEnergyUsed, v -> lastEnergyUsed = v));
    }

}
