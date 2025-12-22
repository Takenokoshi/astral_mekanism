package astral_mekanism.block.blockentity.prefab;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.core.BlockEntityUtils;
import astral_mekanism.block.blockentity.elements.AstralMekDataType;
import astral_mekanism.block.blockentity.interf.ITripleItemToItemMachine;
import astral_mekanism.recipes.cachedRecipe.TripleItemToItemCachedRecipe;
import astral_mekanism.recipes.recipe.TripleItemToItemRecipe;
import mekanism.api.IContentsListener;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BETripleItemToItemRecipeMachine<BE extends BETripleItemToItemRecipeMachine<BE>>
        extends TileEntityRecipeMachine<TripleItemToItemRecipe>
        implements ITripleItemToItemMachine<BE> {

    public static final RecipeError NOT_ENOUGH_THERD_INPUT = RecipeError.create();
    public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
            NOT_ENOUGH_THERD_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    protected MachineEnergyContainer<BE> energyContainer;
    protected InputInventorySlot inputSlotA;
    protected InputInventorySlot inputSlotB;
    protected InputInventorySlot inputSlotC;
    protected OutputInventorySlot outputSlot;
    protected EnergyInventorySlot energySlot;
    protected final IInputHandler<ItemStack> inputHandlerA;
    protected final IInputHandler<ItemStack> inputHandlerB;
    protected final IInputHandler<ItemStack> inputHandlerC;
    protected final IOutputHandler<ItemStack> outputHandler;

    protected BETripleItemToItemRecipeMachine(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
        configComponent = new TileComponentConfig(this, TransmissionType.ENERGY, TransmissionType.ITEM);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ConfigInfo itemInfo = configComponent.getConfig(TransmissionType.ITEM);
        itemInfo.addSlotInfo(DataType.INPUT_1, new InventorySlotInfo(true, false, inputSlotA));
        itemInfo.addSlotInfo(DataType.INPUT_2, new InventorySlotInfo(true, false, inputSlotB));
        itemInfo.addSlotInfo(AstralMekDataType.INPUT3, new InventorySlotInfo(true, false, inputSlotC));
        itemInfo.addSlotInfo(AstralMekDataType.INPUT1_OUTPUT, new InventorySlotInfo(true, false, inputSlotA));
        itemInfo.addSlotInfo(AstralMekDataType.INPUT2_OUTPUT, new InventorySlotInfo(true, false, inputSlotB));
        itemInfo.addSlotInfo(AstralMekDataType.INPUT3_OUTPUT, new InventorySlotInfo(true, false, inputSlotC));
        itemInfo.addSlotInfo(DataType.OUTPUT, new InventorySlotInfo(false, true, outputSlot));
        itemInfo.setCanEject(true);
        ejectorComponent = new TileComponentEjector(this);
        inputHandlerA = InputHelper.getInputHandler(inputSlotA, RecipeError.NOT_ENOUGH_INPUT);
        inputHandlerB = InputHelper.getInputHandler(inputSlotB, RecipeError.NOT_ENOUGH_SECONDARY_INPUT);
        inputHandlerC = InputHelper.getInputHandler(inputSlotC, NOT_ENOUGH_THERD_INPUT);
        outputHandler = OutputHelper.getOutputHandler(outputSlot, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(energyContainer = MachineEnergyContainer.input((BE) this, listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(inputSlotA = InputInventorySlot.at(
                stack -> containsRecipeABC(stack, inputSlotB.getStack(), inputSlotC.getStack()), this::containsRecipeA,
                recipeCacheListener, 28, 35))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE,
                        getWarningCheck(RecipeError.NOT_ENOUGH_INPUT)));
        builder.addSlot(inputSlotB = InputInventorySlot.at(
                stack -> containsRecipeBAC(inputSlotA.getStack(), stack, inputSlotC.getStack()), this::containsRecipeB,
                recipeCacheListener, 46, 35))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE,
                        getWarningCheck(RecipeError.NOT_ENOUGH_SECONDARY_INPUT)));
        builder.addSlot(inputSlotC = InputInventorySlot.at(
                stack -> containsRecipeCAB(inputSlotA.getStack(), inputSlotB.getStack(), stack), this::containsRecipeC,
                recipeCacheListener, 64, 35))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE,
                        getWarningCheck(NOT_ENOUGH_THERD_INPUT)));
        builder.addSlot(outputSlot = OutputInventorySlot.at(listener, 134, 35))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_SPACE_IN_OUTPUT,
                        getWarningCheck(RecipeError.NOT_ENOUGH_OUTPUT_SPACE)));
        builder.addSlot(
                energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 155, 14));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        recipeCacheLookupMonitor.updateAndProcess();
        BlockEntityUtils.itemEject(this,
                List.of(AstralMekDataType.INPUT1_OUTPUT, AstralMekDataType.INPUT2_OUTPUT,
                        AstralMekDataType.INPUT3_OUTPUT),
                DataType.OUTPUT);
    }

    @Nullable
    @Override
    public TripleItemToItemRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandlerA, inputHandlerB, inputHandlerC);
    }

    @NotNull
    @Override
    public CachedRecipe<TripleItemToItemRecipe> createNewCachedRecipe(@NotNull TripleItemToItemRecipe recipe,
            int cacheIndex) {
        return new TripleItemToItemCachedRecipe(recipe, recheckAllRecipeErrors, inputHandlerA, inputHandlerB,
                inputHandlerC, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setBaselineMaxOperations(this::getBaselineMaxOperations)
                .setOnFinish(this::markForSave);
    }

    public MachineEnergyContainer<BE> getEnergyContainer() {
        return energyContainer;
    }

    public FloatingLong getEnergyUsage() {
        return getActive() ? energyContainer.getEnergyPerTick() : FloatingLong.ZERO;
    }

    public double getProgressScaled() {
        return getActive() ? 1 : 0;
    }

    public abstract int getBaselineMaxOperations();

}
