package astral_mekanism.block.blockentity.astralmachine;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.recipes.cachedRecipe.FormulizedSawingCachedRecipe;
import astral_mekanism.recipes.output.AMOutputHelper;
import astral_mekanism.recipes.output.DoubleItemOutput;
import mekanism.api.IContentsListener;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.SawmillRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
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
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.ISingleRecipeLookupHandler.ItemRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleItem;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.upgrade.SawmillUpgradeData;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralPrecisionSawmill extends TileEntityRecipeMachine<SawmillRecipe>
        implements ItemRecipeLookupHandler<SawmillRecipe> {

    public static final RecipeError NOT_ENOUGH_SPACE_SECONDARY_OUTPUT_ERROR = RecipeError.create();
    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            NOT_ENOUGH_SPACE_SECONDARY_OUTPUT_ERROR,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    private final IOutputHandler<@NotNull DoubleItemOutput> outputHandler;
    private final IInputHandler<@NotNull ItemStack> inputHandler;

    private MachineEnergyContainer<BEAstralPrecisionSawmill> energyContainer;
    InputInventorySlot inputSlot;
    OutputInventorySlot outputSlot;
    OutputInventorySlot secondaryOutputSlot;
    EnergyInventorySlot energySlot;

    public BEAstralPrecisionSawmill(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.ENERGY);
        configComponent.setupItemIOConfig(Collections.singletonList(inputSlot),
                List.of(outputSlot, secondaryOutputSlot), energySlot, false);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);

        inputHandler = InputHelper.getInputHandler(inputSlot, RecipeError.NOT_ENOUGH_INPUT);
        outputHandler = AMOutputHelper.getOutputHandler(outputSlot, RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
                secondaryOutputSlot, NOT_ENOUGH_SPACE_SECONDARY_OUTPUT_ERROR);
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
        builder.addSlot(inputSlot = InputInventorySlot.at(this::containsRecipe, recipeCacheListener, 56, 17))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE,
                        getWarningCheck(RecipeError.NOT_ENOUGH_INPUT)));
        builder.addSlot(outputSlot = OutputInventorySlot.at(listener, 116, 35));
        builder.addSlot(secondaryOutputSlot = OutputInventorySlot.at(listener, 132, 35));
        builder.addSlot(
                energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 56, 53));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        recipeCacheLookupMonitor.updateAndProcess();
    }

    @Override
    @NotNull
    public IMekanismRecipeTypeProvider<SawmillRecipe, SingleItem<SawmillRecipe>> getRecipeType() {
        return MekanismRecipeType.SAWING;
    }

    @Nullable
    @Override
    public SawmillRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandler);
    }

    @NotNull
    @Override
    public CachedRecipe<SawmillRecipe> createNewCachedRecipe(@NotNull SawmillRecipe recipe, int cacheIndex) {
        return new FormulizedSawingCachedRecipe(recipe, recheckAllRecipeErrors, inputHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setOnFinish(this::markForSave)
                .setBaselineMaxOperations(() -> Integer.MAX_VALUE);
    }

    @NotNull
    @Override
    public SawmillUpgradeData getUpgradeData() {
        return new SawmillUpgradeData(redstone, getControlType(), getEnergyContainer(), 1, energySlot, inputSlot,
                outputSlot, secondaryOutputSlot,
                getComponents());
    }

    public MachineEnergyContainer<BEAstralPrecisionSawmill> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public boolean isConfigurationDataCompatible(BlockEntityType<?> tileType) {
        return super.isConfigurationDataCompatible(tileType)
                || MekanismUtils.isSameTypeFactory(getBlockType(), tileType);
    }

    FloatingLong getEnergyUsage() {
        return getActive() ? energyContainer.getEnergyPerTick() : FloatingLong.ZERO;
    }
}