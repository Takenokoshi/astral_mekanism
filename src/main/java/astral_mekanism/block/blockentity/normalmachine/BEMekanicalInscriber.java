package astral_mekanism.block.blockentity.normalmachine;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import appeng.recipes.handlers.InscriberRecipe;
import astral_mekanism.block.blockentity.base.BlockEntityProgressMachine;
import astral_mekanism.block.blockentity.interf.IMekanicalInscriber;
import astral_mekanism.generalrecipe.cachedrecipe.ICachedRecipe;
import astral_mekanism.generalrecipe.cachedrecipe.MEkanicalInscribeCachedRecipe;
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
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.computercraft.ComputerConstants;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BEMekanicalInscriber extends BlockEntityProgressMachine<InscriberRecipe>
        implements IMekanicalInscriber<BEMekanicalInscriber> {

    private MachineEnergyContainer<BEMekanicalInscriber> energyContainer;
    private InputInventorySlot topSlot;
    private InputInventorySlot middleSlot;
    private InputInventorySlot bottomSlot;
    private OutputInventorySlot outputSlot;
    private EnergyInventorySlot energySlot;

    private final IInputHandler<ItemStack> topHandler;
    private final IInputHandler<ItemStack> middleHandler;
    private final IInputHandler<ItemStack> bottomHandler;
    private final IOutputHandler<ItemStack> outputHandler;

    public BEMekanicalInscriber(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES, 200);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.ENERGY);
        ConfigInfo itemConfig = configComponent.getConfig(TransmissionType.ITEM);
        itemConfig.addSlotInfo(DataType.INPUT_1, new InventorySlotInfo(true, false, middleSlot));
        itemConfig.addSlotInfo(DataType.INPUT_2, new InventorySlotInfo(true, false, topSlot, bottomSlot));
        itemConfig.addSlotInfo(DataType.INPUT, new InventorySlotInfo(true, false, topSlot, middleSlot, bottomSlot));
        itemConfig.addSlotInfo(DataType.OUTPUT, new InventorySlotInfo(false, true, outputSlot));
        itemConfig.addSlotInfo(DataType.INPUT_OUTPUT,
                new InventorySlotInfo(true, true, topSlot, middleSlot, bottomSlot, bottomSlot));
        itemConfig.setDefaults();
        itemConfig.setCanEject(true);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);
        topHandler = InputHelper.getInputHandler(topSlot, NOT_ENOUGH_TOP_INPUT);
        middleHandler = InputHelper.getInputHandler(middleSlot, NOT_ENOUGH_MIDDLE_INPUT);
        bottomHandler = InputHelper.getInputHandler(bottomSlot, NOT_ENOUGH_BOTTOM_INPUT);
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

    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection,
                this::getConfig);
        builder.addSlot(topSlot = InputInventorySlot.at(
                stack -> containsInputTMB(stack, middleSlot.getStack(), bottomSlot.getStack()),
                this::containsInputT, recipeCacheListener, 46, 17));
        builder.addSlot(middleSlot = InputInventorySlot.at(
                stack -> containsInputMTB(topSlot.getStack(), stack, bottomSlot.getStack()),
                this::containsInputM, recipeCacheListener, 64, 35));
        builder.addSlot(bottomSlot = InputInventorySlot.at(
                stack -> containsInputBTM(topSlot.getStack(), middleSlot.getStack(), stack),
                this::containsInputB, recipeCacheListener, 46, 53));
        builder.addSlot(outputSlot = OutputInventorySlot.at(listener, 116, 35));
        builder.addSlot(
                energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 21, 35));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        recipeCacheLookupMonitor.updateAndProcess();
    }

    @Override
    public @Nullable InscriberRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(topHandler, middleHandler, bottomHandler);
    }

    @Override
    public @NotNull ICachedRecipe<InscriberRecipe> createNewCachedRecipe(@NotNull InscriberRecipe recipe,
            int cacheIndex) {
        return new MEkanicalInscribeCachedRecipe(recipe, recheckAllRecipeErrors, topHandler, middleHandler,
                bottomHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(this::setOperatingTicks);
    }

    @Override
    public double getProgressScaled() {
        return getScaledProgress();
    }

    @Override
    public MachineEnergyContainer<BEMekanicalInscriber> getEnergyContainer() {
        return energyContainer;
    }

    @ComputerMethod(methodDescription = ComputerConstants.DESCRIPTION_GET_ENERGY_USAGE)
    FloatingLong getEnergyUsage() {
        return getActive() ? energyContainer.getEnergyPerTick() : FloatingLong.ZERO;
    }
}