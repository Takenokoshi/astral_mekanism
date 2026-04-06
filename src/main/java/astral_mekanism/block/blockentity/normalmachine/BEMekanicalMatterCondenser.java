package astral_mekanism.block.blockentity.normalmachine;

import org.jetbrains.annotations.NotNull;

import appeng.api.config.CondenserOutput;
import appeng.api.stacks.AEFluidKey;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.ItemDefinition;
import astral_mekanism.integration.AMEEmpowered;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableDouble;
import mekanism.common.inventory.container.sync.SyncableEnum;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.interfaces.IHasMode;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BEMekanicalMatterCondenser extends TileEntityConfigurableMachine implements IHasMode {

    private InputInventorySlot inputSlot;
    private BasicFluidTank inputTank;
    private OutputInventorySlot outputSlot;

    private double storedPower;
    private CondenserOutput mode;

    private int baselineMaxOperations;

    public BEMekanicalMatterCondenser(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.FLUID);
        configComponent.setupIOConfig(TransmissionType.ITEM, inputSlot, outputSlot, RelativeSide.RIGHT);
        configComponent.setupInputConfig(TransmissionType.FLUID, inputTank);
        ejectorComponent = new TileComponentEjector(this).setOutputData(configComponent, TransmissionType.ITEM);
        baselineMaxOperations = 200;
        mode = CondenserOutput.TRASH;
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(inputSlot = InputInventorySlot.at(listener, 64, 35));
        builder.addSlot(outputSlot = OutputInventorySlot.at(listener, 116, 35));
        return builder.build();
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputTank = BasicFluidTank.input(0x7fffffff, BasicFluidTank.alwaysTrue, listener));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        if (MekanismUtils.canFunction(this)) {
            if (mode == CondenserOutput.TRASH) {
                inputSlot.setEmpty();
                inputTank.setEmpty();
            } else if (mode == CondenserOutput.MATTER_BALLS) {
                if (!outputSlot.isEmpty()
                        && !ItemStack.isSameItemSameTags(outputSlot.getStack(), AEItems.MATTER_BALL.stack())) {
                    return;
                }
                processItem();
                processFluid();
                handleOutput(CondenserOutput.MATTER_BALLS.requiredPower, AEItems.MATTER_BALL);
            } else if (mode == CondenserOutput.SINGULARITY) {
                if (!outputSlot.isEmpty()
                        && !ItemStack.isSameItemSameTags(outputSlot.getStack(), AEItems.SINGULARITY.stack())) {
                    return;
                }
                processItem();
                processFluid();
                handleOutput(CondenserOutput.SINGULARITY.requiredPower, AEItems.SINGULARITY);
            }
        }
    }

    private void processItem() {
        storedPower += inputSlot.getCount();
        inputSlot.setEmpty();
    }

    private void processFluid() {
        if (inputTank.isEmpty()) {
            return;
        }
        int q = AEFluidKey.of(inputTank.getFluid()).getAmountPerOperation();
        int p = Math.min(baselineMaxOperations, inputTank.getFluidAmount() / q);
        storedPower += p;
        inputTank.shrinkStack(p * q, Action.EXECUTE);
    }

    private void handleOutput(double requiredPower, ItemDefinition<?> definition) {
        int p = (int) Math.min(definition.asItem().getMaxStackSize(), storedPower / requiredPower);
        p -= outputSlot.insertItem(definition.stack(p), Action.EXECUTE, AutomationType.INTERNAL).getCount();
        storedPower -= requiredPower * p;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableDouble.create(() -> storedPower, v -> storedPower = v));
        container.track(SyncableEnum.create(i -> CondenserOutput.values()[i], CondenserOutput.TRASH,
                () -> mode, v -> mode = v));
        container.track(SyncableInt.create(() -> baselineMaxOperations, v -> baselineMaxOperations = v));
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        try {
            storedPower = nbt.getDouble("storedPower");
            mode = CondenserOutput.valueOf(nbt.getString("mode"));
        } catch (Exception e) {
            storedPower = 0;
            mode = CondenserOutput.TRASH;
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putDouble("storedPower", storedPower);
        nbt.putString("mode", mode.toString());
    }

    public double getScaledProgress() {
        return Math.min(storedPower / mode.requiredPower, 1);
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (AMEEmpowered.empoweredIsLoaded()) {
            baselineMaxOperations = 200 << AMEEmpowered.getAllSpeeds(this);
        } else if (upgrade == Upgrade.SPEED) {
            baselineMaxOperations = 200 << upgradeComponent.getUpgrades(Upgrade.SPEED);
        }
    }

    public void changeMode(boolean direction) {
        int p = direction ? 1 : -1;
        CondenserOutput[] values = CondenserOutput.values();
        mode = values[(mode.ordinal() + p) % values.length];
    }

    @Override
    public void nextMode() {
        changeMode(true);
    }

    @Override
    public void previousMode() {
        changeMode(false);
    }

    public BasicFluidTank getInputTank() {
        return inputTank;
    }

    public CondenserOutput getMode() {
        return mode;
    }
}
