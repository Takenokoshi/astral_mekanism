package astral_mekanism.block.blockentity.astralmachine;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fr.iglee42.evolvedmekanism.interfaces.EMInputRecipeCache.TripleItem;
import fr.iglee42.evolvedmekanism.interfaces.ThreeInputCachedRecipe;
import fr.iglee42.evolvedmekanism.interfaces.TripleItemRecipeLookupHandler;
import fr.iglee42.evolvedmekanism.recipes.AlloyerRecipe;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import fr.iglee42.evolvedmekanism.tiles.LimitedInputInventorySlot;
import mekanism.api.IContentsListener;
import mekanism.api.inventory.IInventorySlot;
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
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
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

public class BEAstralAlloyer extends TileEntityRecipeMachine<AlloyerRecipe>
        implements TripleItemRecipeLookupHandler<AlloyerRecipe> {

    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);
    private final IOutputHandler<@NotNull ItemStack> outputHandler;
    private final IInputHandler<@NotNull ItemStack> inputHandler;
    private final IInputHandler<@NotNull ItemStack> extraInputHandler;
    private final IInputHandler<@NotNull ItemStack> secondExtraInputHandler;
    private MachineEnergyContainer<BEAstralAlloyer> energyContainer;
    InputInventorySlot mainInputSlot;
    LimitedInputInventorySlot extraInputSlot;
    LimitedInputInventorySlot secondExtraInputSlot;
    OutputInventorySlot outputSlot;
    EnergyInventorySlot energySlot;

    public BEAstralAlloyer(IBlockProvider blockProvider, BlockPos pos, BlockState state,
            List<RecipeError> errorTypes) {
        super(blockProvider, pos, state, errorTypes);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.ENERGY);
        setupItemIOExtraConfig(configComponent, mainInputSlot, outputSlot, extraInputSlot, secondExtraInputSlot,
                energySlot);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);
        inputHandler = InputHelper.getInputHandler(mainInputSlot, RecipeError.NOT_ENOUGH_INPUT);
        extraInputHandler = InputHelper.getInputHandler(extraInputSlot, RecipeError.NOT_ENOUGH_SECONDARY_INPUT);
        secondExtraInputHandler = InputHelper.getInputHandler(secondExtraInputSlot,
                RecipeError.NOT_ENOUGH_SECONDARY_INPUT);
        outputHandler = OutputHelper.getOutputHandler(outputSlot, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
    }

    private static ConfigInfo setupItemIOExtraConfig(TileComponentConfig config, IInventorySlot inputSlot,
            IInventorySlot outputSlot, IInventorySlot extraSlot, IInventorySlot secondaryExtraSlot,
            IInventorySlot energySlot) {
        ConfigInfo itemConfig = config.getConfig(TransmissionType.ITEM);
        if (itemConfig != null) {
            itemConfig.addSlotInfo(DataType.INPUT, new InventorySlotInfo(true, false, inputSlot));
            itemConfig.addSlotInfo(DataType.OUTPUT, new InventorySlotInfo(false, true, outputSlot));
            itemConfig.addSlotInfo(DataType.INPUT_OUTPUT, new InventorySlotInfo(true, true, inputSlot, outputSlot));
            itemConfig.addSlotInfo(DataType.EXTRA, new InventorySlotInfo(true, true, extraSlot, secondaryExtraSlot));
            itemConfig.addSlotInfo(DataType.ENERGY, new InventorySlotInfo(true, true, energySlot));
        }
        return itemConfig;
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
        builder.addSlot(mainInputSlot = InputInventorySlot.at(
                item -> containsRecipeABC(item, extraInputSlot.getStack(), secondExtraInputSlot.getStack()),
                this::containsRecipeA, recipeCacheListener,
                64, 17))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE,
                        getWarningCheck(RecipeError.NOT_ENOUGH_INPUT)));
        builder.addSlot(extraInputSlot = LimitedInputInventorySlot.at(
                item -> containsRecipeBAC(mainInputSlot.getStack(), item, secondExtraInputSlot.getStack()),
                this::containsRecipeB, recipeCacheListener,
                55, 53))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE,
                        getWarningCheck(RecipeError.NOT_ENOUGH_SECONDARY_INPUT)));
        builder.addSlot(secondExtraInputSlot = LimitedInputInventorySlot.at(
                item -> containsRecipeCAB(mainInputSlot.getStack(), extraInputSlot.getStack(), item),
                this::containsRecipeC, recipeCacheListener,
                75, 53))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE,
                        getWarningCheck(RecipeError.NOT_ENOUGH_SECONDARY_INPUT)));
        builder.addSlot(outputSlot = OutputInventorySlot.at(listener, 116, 35))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_SPACE_IN_OUTPUT,
                        getWarningCheck(RecipeError.NOT_ENOUGH_OUTPUT_SPACE)));
        builder.addSlot(
                energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 39, 35));
        extraInputSlot.setSlotType(ContainerSlotType.EXTRA);
        secondExtraInputSlot.setSlotType(ContainerSlotType.EXTRA);
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        recipeCacheLookupMonitor.updateAndProcess();
        return;
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<AlloyerRecipe, TripleItem<AlloyerRecipe>> getRecipeType() {
        return EMRecipeType.ALLOYING;
    }

    @Override
    public @Nullable AlloyerRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandler, extraInputHandler, secondExtraInputHandler);
    }

    @Override
    public @NotNull CachedRecipe<AlloyerRecipe> createNewCachedRecipe(@NotNull AlloyerRecipe recipe, int cacheIndex) {
        return ThreeInputCachedRecipe
                .alloyer(recipe, recheckAllRecipeErrors, inputHandler, extraInputHandler, secondExtraInputHandler,
                        outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setOnFinish(this::markForSave)
                .setBaselineMaxOperations(() -> 0x7fffffff);
    }

}
