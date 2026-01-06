package astral_mekanism.block.blockentity.normalmachine;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import appeng.recipes.handlers.ChargerRecipe;
import astral_mekanism.block.blockentity.base.BlockEntityProgressMachine;
import astral_mekanism.block.blockentity.interf.IEnergizedMachine;
import astral_mekanism.generalrecipe.GeneralRecipeType;
import astral_mekanism.generalrecipe.IUnifiedRecipeTypeProvider;
import astral_mekanism.generalrecipe.cachedrecipe.GeneralCachedRecipe;
import astral_mekanism.generalrecipe.cachedrecipe.MekanicalChargingCachedRecipe;
import astral_mekanism.generalrecipe.lookup.cache.recipe.SingleInputGeneralRecipeCache.GeneralSingleItem;
import astral_mekanism.generalrecipe.lookup.handler.IUnifiedSingelRecipeLookupHandler;
import mekanism.api.IContentsListener;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
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
import mekanism.common.upgrade.MachineUpgradeData;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BEMekanicalCharger extends BlockEntityProgressMachine<ChargerRecipe> implements
        IUnifiedSingelRecipeLookupHandler<ItemStack, ChargerRecipe, GeneralSingleItem<Container, ChargerRecipe>>,
        IEnergizedMachine<BEMekanicalCharger> {
    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    protected final IInputHandler<@NotNull ItemStack> inputHandler;
    protected final IOutputHandler<@NotNull ItemStack> outputHandler;
    InputInventorySlot inputSlot;
    OutputInventorySlot outputSlot;
    EnergyInventorySlot energySlot;
    private MachineEnergyContainer<BEMekanicalCharger> energyContainer;

    public BEMekanicalCharger(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES, 200);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.ENERGY);
        configComponent.setupItemIOConfig(inputSlot, outputSlot, energySlot);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);

        inputHandler = InputHelper.getInputHandler(inputSlot, RecipeError.NOT_ENOUGH_INPUT);
        outputHandler = OutputHelper.getOutputHandler(outputSlot, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
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
        builder.addSlot(inputSlot = InputInventorySlot.at(this::containsRecipe, recipeCacheListener, 64, 17))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE,
                        getWarningCheck(RecipeError.NOT_ENOUGH_INPUT)));
        builder.addSlot(outputSlot = OutputInventorySlot.at(listener, 116, 35))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_SPACE_IN_OUTPUT,
                        getWarningCheck(RecipeError.NOT_ENOUGH_OUTPUT_SPACE)));
        builder.addSlot(
                energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 64, 53));
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
    public MachineUpgradeData getUpgradeData() {
        return new MachineUpgradeData(redstone, getControlType(), getEnergyContainer(), getOperatingTicks(), energySlot,
                inputSlot, outputSlot, getComponents());
    }

    FloatingLong getEnergyUsage() {
        return getActive() ? energyContainer.getEnergyPerTick() : FloatingLong.ZERO;
    }

    @Override
    public boolean isConfigurationDataCompatible(BlockEntityType<?> tileType) {
        return super.isConfigurationDataCompatible(tileType)
                || MekanismUtils.isSameTypeFactory(getBlockType(), tileType);
    }

    @Override
    public @NotNull IUnifiedRecipeTypeProvider<ChargerRecipe,GeneralSingleItem<Container,ChargerRecipe>> getRecipeType() {
        return GeneralRecipeType.CHARGING;
    }

    @Override
    public @Nullable ChargerRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandler);
    }

    @Override
    public @NotNull GeneralCachedRecipe<ChargerRecipe> createNewCachedRecipe(@NotNull ChargerRecipe recipe,
            int cacheIndex) {
        return new MekanicalChargingCachedRecipe(recipe, recheckAllRecipeErrors, inputHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(this::setOperatingTicks);
    }

    @Override
    public MachineEnergyContainer<BEMekanicalCharger> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public double getProgressScaled() {
        return getScaledProgress();
    }

}
