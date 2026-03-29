package astral_mekanism.block.blockentity.storage;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.elements.slot.paged.PagedInputInventorySlot;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedOutputInventorySlot;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.pigment.IPigmentTank;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.ISlurryTank;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo;
import mekanism.common.tile.component.config.slot.FluidSlotInfo;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BERatioSeparator extends TileEntityConfigurableMachine {

    private PagedInputInventorySlot inputSlot;
    private PagedOutputInventorySlot outputSlotA;
    private PagedOutputInventorySlot outputSlotB;

    public int itemAValue;
    public int itemBValue;

    public BasicFluidTank inputFluidTank;
    public BasicFluidTank outputFluidTankA;
    public BasicFluidTank outputFluidTankB;

    public int fluidAValue;
    public int fluidBValue;

    public IGasTank inputGasTank;
    public IGasTank outputGasTankA;
    public IGasTank outputGasTankB;

    public long gasAValue;
    public long gasBValue;

    public IInfusionTank inputInfusionTank;
    public IInfusionTank outputInfusionTankA;
    public IInfusionTank outputInfusionTankB;

    public long infusionAValue;
    public long infusionBValue;

    public IPigmentTank inputPigmentTank;
    public IPigmentTank outputPigmentTankA;
    public IPigmentTank outputPigmentTankB;

    public long pigmentAValue;
    public long pigmentBValue;

    public ISlurryTank inputSlurryTank;
    public ISlurryTank outputSlurryTankA;
    public ISlurryTank outputSlurryTankB;

    public long slurryAValue;
    public long slurryBValue;

    public BERatioSeparator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.FLUID,
                TransmissionType.GAS, TransmissionType.INFUSION, TransmissionType.PIGMENT, TransmissionType.SLURRY);
        ConfigInfo itemInfo = configComponent.getConfig(TransmissionType.ITEM);
        itemInfo.addSlotInfo(DataType.INPUT, new InventorySlotInfo(true, false, inputSlot));
        itemInfo.addSlotInfo(DataType.OUTPUT_1, new InventorySlotInfo(false, true, outputSlotA));
        itemInfo.addSlotInfo(DataType.OUTPUT_2, new InventorySlotInfo(false, true, outputSlotB));
        itemInfo.setCanEject(true);
        ConfigInfo fluidInfo = configComponent.getConfig(TransmissionType.FLUID);
        fluidInfo.addSlotInfo(DataType.INPUT, new FluidSlotInfo(true, false, inputFluidTank));
        fluidInfo.addSlotInfo(DataType.OUTPUT_1, new FluidSlotInfo(false, true, outputFluidTankA));
        fluidInfo.addSlotInfo(DataType.OUTPUT_2, new FluidSlotInfo(false, true, outputFluidTankB));
        fluidInfo.setCanEject(true);
        ConfigInfo gasInfo = configComponent.getConfig(TransmissionType.GAS);
        gasInfo.addSlotInfo(DataType.INPUT, new ChemicalSlotInfo.GasSlotInfo(true, false, inputGasTank));
        gasInfo.addSlotInfo(DataType.OUTPUT_1, new ChemicalSlotInfo.GasSlotInfo(false, true, outputGasTankA));
        gasInfo.addSlotInfo(DataType.OUTPUT_2, new ChemicalSlotInfo.GasSlotInfo(false, true, outputGasTankB));
        gasInfo.setCanEject(true);
        ConfigInfo infusionInfo = configComponent.getConfig(TransmissionType.INFUSION);
        infusionInfo.addSlotInfo(DataType.INPUT, new ChemicalSlotInfo.InfusionSlotInfo(true, false, inputInfusionTank));
        infusionInfo.addSlotInfo(DataType.OUTPUT_1,
                new ChemicalSlotInfo.InfusionSlotInfo(false, true, outputInfusionTankA));
        infusionInfo.addSlotInfo(DataType.OUTPUT_2,
                new ChemicalSlotInfo.InfusionSlotInfo(false, true, outputInfusionTankB));
        infusionInfo.setCanEject(true);
        ConfigInfo pigmentInfo = configComponent.getConfig(TransmissionType.PIGMENT);
        pigmentInfo.addSlotInfo(DataType.INPUT, new ChemicalSlotInfo.PigmentSlotInfo(true, false, inputPigmentTank));
        pigmentInfo.addSlotInfo(DataType.OUTPUT_1,
                new ChemicalSlotInfo.PigmentSlotInfo(false, true, outputPigmentTankA));
        pigmentInfo.addSlotInfo(DataType.OUTPUT_2,
                new ChemicalSlotInfo.PigmentSlotInfo(false, true, outputPigmentTankB));
        pigmentInfo.setCanEject(true);
        ConfigInfo slurryInfo = configComponent.getConfig(TransmissionType.SLURRY);
        slurryInfo.addSlotInfo(DataType.INPUT, new ChemicalSlotInfo.SlurrySlotInfo(true, false, inputSlurryTank));
        slurryInfo.addSlotInfo(DataType.OUTPUT_1, new ChemicalSlotInfo.SlurrySlotInfo(false, true, outputSlurryTankA));
        slurryInfo.addSlotInfo(DataType.OUTPUT_2, new ChemicalSlotInfo.SlurrySlotInfo(false, true, outputSlurryTankB));
        slurryInfo.setCanEject(true);
        ejectorComponent = new TileComponentEjector(this, () -> Long.MAX_VALUE, () -> 0x7fffffff)
                .setOutputData(configComponent,
                        TransmissionType.ITEM, TransmissionType.FLUID,
                        TransmissionType.GAS, TransmissionType.INFUSION,
                        TransmissionType.PIGMENT, TransmissionType.SLURRY);
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(inputSlot = PagedInputInventorySlot.at(this::canItemInsert, listener, 8, 35, 0));
        builder.addSlot(outputSlotA = PagedOutputInventorySlot.at(listener, 89, 17, 0));
        builder.addSlot(outputSlotB = PagedOutputInventorySlot.at(listener, 89, 53, 0));
        return builder.build();
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputFluidTank = BasicFluidTank.input(0x7fffffff, f -> true, listener));
        builder.addTank(outputFluidTankA = BasicFluidTank.output(0x7fffffff, listener));
        builder.addTank(outputFluidTankB = BasicFluidTank.output(0x7fffffff, listener));
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper
                .forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputGasTank = ChemicalTankBuilder.GAS.input(Long.MAX_VALUE, g -> true, listener));
        builder.addTank(outputGasTankA = ChemicalTankBuilder.GAS.output(Long.MAX_VALUE, listener));
        builder.addTank(outputGasTankB = ChemicalTankBuilder.GAS.output(Long.MAX_VALUE, listener));
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<InfuseType, InfusionStack, IInfusionTank> getInitialInfusionTanks(
            IContentsListener listener) {
        ChemicalTankHelper<InfuseType, InfusionStack, IInfusionTank> builder = ChemicalTankHelper
                .forSideInfusionWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputInfusionTank = ChemicalTankBuilder.INFUSION.input(Long.MAX_VALUE, i -> true, listener));
        builder.addTank(outputInfusionTankA = ChemicalTankBuilder.INFUSION.output(Long.MAX_VALUE, listener));
        builder.addTank(outputInfusionTankB = ChemicalTankBuilder.INFUSION.output(Long.MAX_VALUE, listener));
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Pigment, PigmentStack, IPigmentTank> getInitialPigmentTanks(
            IContentsListener listener) {
        ChemicalTankHelper<Pigment, PigmentStack, IPigmentTank> builder = ChemicalTankHelper
                .forSidePigmentWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputPigmentTank = ChemicalTankBuilder.PIGMENT.input(Long.MAX_VALUE, p -> true, listener));
        builder.addTank(outputPigmentTankA = ChemicalTankBuilder.PIGMENT.output(Long.MAX_VALUE, listener));
        builder.addTank(outputPigmentTankB = ChemicalTankBuilder.PIGMENT.output(Long.MAX_VALUE, listener));
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Slurry, SlurryStack, ISlurryTank> getInitialSlurryTanks(IContentsListener listener) {
        ChemicalTankHelper<Slurry, SlurryStack, ISlurryTank> builder = ChemicalTankHelper
                .forSideSlurryWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputSlurryTank = ChemicalTankBuilder.SLURRY.input(Long.MAX_VALUE, s -> true, listener));
        builder.addTank(outputSlurryTankA = ChemicalTankBuilder.SLURRY.output(Long.MAX_VALUE, listener));
        builder.addTank(outputSlurryTankB = ChemicalTankBuilder.SLURRY.output(Long.MAX_VALUE, listener));
        return builder.build();
    }

    private boolean canItemInsert(ItemStack stack) {
        return stack.getMaxStackSize() >= itemAValue + itemBValue;
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        separateItem();
        separateFluid();
        separateChemical(inputGasTank, outputGasTankA, outputGasTankB, gasAValue, gasBValue);
        separateChemical(inputInfusionTank, outputInfusionTankA, outputInfusionTankB, infusionAValue, infusionBValue);
        separateChemical(inputPigmentTank, outputPigmentTankA, outputPigmentTankB, pigmentAValue, pigmentBValue);
        separateChemical(inputSlurryTank, outputSlurryTankA, outputSlurryTankB, slurryAValue, slurryBValue);
    }

    private void separateItem() {
        if (inputSlot.isEmpty()) {
            return;
        }
        if (itemAValue < 1) {
            moveItemToSlot(outputSlotB, itemBValue);
        } else if (itemBValue < 1) {
            moveItemToSlot(outputSlotA, itemAValue);
        } else {
            ItemStack separating = inputSlot.getStack();
            if (outputSlotA.insertItem(separating.copyWithCount(1), Action.SIMULATE, AutomationType.INTERNAL).isEmpty()
                    && outputSlotB.insertItem(separating.copyWithCount(1), Action.SIMULATE, AutomationType.INTERNAL)
                            .isEmpty()) {
                int total = itemAValue + itemBValue;
                int p = Math.min((separating.getMaxStackSize() - outputSlotA.getCount()) / itemAValue,
                        (separating.getMaxStackSize() - outputSlotB.getCount()) / itemBValue);
                p = Math.min(p, inputSlot.getCount() / total);
                if (p < 1) {
                    return;
                }
                outputSlotA.insertItem(separating.copyWithCount(p * itemAValue), Action.EXECUTE,
                        AutomationType.INTERNAL);
                outputSlotB.insertItem(separating.copyWithCount(p * itemBValue), Action.EXECUTE,
                        AutomationType.INTERNAL);
                inputSlot.shrinkStack(p * total, Action.EXECUTE);
            }
        }
    }

    private void moveItemToSlot(PagedOutputInventorySlot toMove, int value) {
        ItemStack separating = inputSlot.getStack();
        if (value < 1 || !toMove.insertItem(separating.copyWithCount(1), Action.SIMULATE, AutomationType.INTERNAL)
                .isEmpty()) {
            return;
        }
        int p = Math.min(inputSlot.getCount() / value, (separating.getMaxStackSize() - toMove.getCount()) / value);
        if (p < 1) {
            return;
        }
        toMove.insertItem(separating.copyWithCount(p * value), Action.EXECUTE, AutomationType.INTERNAL);
        inputSlot.shrinkStack(p * value, Action.EXECUTE);
    }

    private void separateFluid() {
        if (inputFluidTank.isEmpty()) {
            return;
        }
        if (fluidAValue < 1) {
            moveFluidToTank(outputFluidTankB, fluidBValue);
        } else if (fluidBValue < 1) {
            moveFluidToTank(outputFluidTankA, fluidAValue);
        } else {
            FluidStack separating = inputFluidTank.getFluid().copy();
            separating.setAmount(1);
            if (outputFluidTankA.insert(separating, Action.SIMULATE, AutomationType.INTERNAL).isEmpty()
                    && outputFluidTankB.insert(separating, Action.SIMULATE, AutomationType.INTERNAL).isEmpty()) {
                int total = fluidAValue + fluidBValue;
                int p = Math.min(inputFluidTank.getFluidAmount() / total, outputFluidTankA.getNeeded() / fluidAValue);
                p = Math.min(p, outputFluidTankB.getNeeded() / fluidBValue);
                if (p < 1) {
                    return;
                }
                outputFluidTankA.insert(new FluidStack(separating, p * fluidAValue), Action.EXECUTE,
                        AutomationType.INTERNAL);
                outputFluidTankB.insert(new FluidStack(separating, p * fluidBValue), Action.EXECUTE,
                        AutomationType.INTERNAL);
                inputFluidTank.shrinkStack(p * total, Action.EXECUTE);
            }
        }
    }

    private void moveFluidToTank(BasicFluidTank toMove, int value) {
        FluidStack separating = inputFluidTank.getFluid().copy();
        separating.setAmount(1);
        if (value < 1 || !toMove.insert(separating, Action.SIMULATE, AutomationType.INTERNAL).isEmpty()) {
            return;
        }
        int p = Math.min(inputFluidTank.getFluidAmount() / value, toMove.getNeeded() / value);
        if (p < 1) {
            return;
        }
        toMove.insert(new FluidStack(separating, p * value), Action.EXECUTE, AutomationType.INTERNAL);
        inputFluidTank.shrinkStack(p * value, Action.EXECUTE);
    }

    private <CHEMICAL extends Chemical<CHEMICAL>, STACK extends ChemicalStack<CHEMICAL>, TANK extends IChemicalTank<CHEMICAL, STACK>> void separateChemical(
            TANK inputTank, TANK outputTankA, TANK outputTankB, long valueA, long valueB) {
        if (inputTank.isEmpty()) {
            return;
        }
        if (valueA < 1) {
            moveChemicalToTank(inputTank, outputTankB, valueB);
        } else if (valueB < 1) {
            moveChemicalToTank(inputTank, outputTankA, valueA);
        } else {
            @SuppressWarnings("unchecked")
            STACK separating = (STACK) inputTank.getStack().copy();
            separating.setAmount(1);
            if (outputTankA.insert(separating, Action.SIMULATE, AutomationType.INTERNAL).isEmpty()
                    && outputTankB.insert(separating, Action.SIMULATE, AutomationType.INTERNAL).isEmpty()) {
                long total = valueA + valueB;
                long p = Math.min(inputTank.getStored() / total, outputTankA.getNeeded() / valueA);
                p = Math.min(outputTankB.getNeeded() / valueB, p);
                if (p < 1) {
                    return;
                }
                separating.setAmount(p * valueA);
                outputTankA.insert(separating, Action.EXECUTE, AutomationType.INTERNAL);
                separating.setAmount(p * valueB);
                outputTankB.insert(separating, Action.EXECUTE, AutomationType.INTERNAL);
                inputTank.shrinkStack(p * total, Action.EXECUTE);
            }
        }
    }

    private <CHEMICAL extends Chemical<CHEMICAL>, STACK extends ChemicalStack<CHEMICAL>, TANK extends IChemicalTank<CHEMICAL, STACK>> void moveChemicalToTank(
            TANK inputTank, TANK outputTank, long value) {
        @SuppressWarnings("unchecked")
        STACK separating = (STACK) inputTank.getStack().copy();
        separating.setAmount(1);
        if (value < 1 || !outputTank.insert(separating, Action.SIMULATE, AutomationType.INTERNAL).isEmpty()) {
            return;
        }
        long p = Math.min(inputTank.getStored() / value, outputTank.getNeeded() / value);
        if (p < 1) {
            return;
        }
        separating.setAmount(p * value);
        outputTank.insert(separating, Action.EXECUTE, AutomationType.INTERNAL);
        inputTank.shrinkStack(p * value, Action.EXECUTE);
    }

    private int getMaxStackSize() {
        return inputSlot.isEmpty() ? 0x7fffffff : inputSlot.getStack().getMaxStackSize();
    }

    public void setValue(TransmissionType type, boolean isA, long value) {
        switch (type) {
            case ITEM:
                if (isA) {
                    itemAValue = (int) Math.min(value, getMaxStackSize() - itemBValue);
                } else {
                    itemBValue = (int) Math.min(value, getMaxStackSize() - itemAValue);
                }
                break;
            case FLUID:
                if (isA) {
                    fluidAValue = (int) Math.min(value, 0x7fffffff - fluidBValue);
                } else {
                    fluidBValue = (int) Math.min(value, 0x7fffffff - fluidAValue);
                }
                break;
            case GAS:
                if (isA) {
                    gasAValue = Math.min(value, Long.MAX_VALUE - gasBValue);
                } else {
                    gasBValue = Math.min(value, Long.MAX_VALUE - gasAValue);
                }
                break;
            case INFUSION:
                if (isA) {
                    infusionAValue = Math.min(value, Long.MAX_VALUE - infusionBValue);
                } else {
                    infusionBValue = Math.min(value, Long.MAX_VALUE - infusionAValue);
                }
                break;
            case PIGMENT:
                if (isA) {
                    pigmentAValue = Math.min(value, Long.MAX_VALUE - pigmentBValue);
                } else {
                    pigmentBValue = Math.min(value, Long.MAX_VALUE - pigmentAValue);
                }
                break;
            case SLURRY:
                if (isA) {
                    slurryAValue = Math.min(value, Long.MAX_VALUE - slurryBValue);
                } else {
                    slurryBValue = Math.min(value, Long.MAX_VALUE - slurryAValue);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableInt.create(() -> itemAValue, v -> itemAValue = v));
        container.track(SyncableInt.create(() -> itemBValue, v -> itemBValue = v));
        container.track(SyncableInt.create(() -> fluidAValue, v -> fluidAValue = v));
        container.track(SyncableInt.create(() -> fluidBValue, v -> fluidBValue = v));
        container.track(SyncableLong.create(() -> gasAValue, v -> gasAValue = v));
        container.track(SyncableLong.create(() -> gasBValue, v -> gasBValue = v));
        container.track(SyncableLong.create(() -> infusionAValue, v -> infusionAValue = v));
        container.track(SyncableLong.create(() -> infusionBValue, v -> infusionBValue = v));
        container.track(SyncableLong.create(() -> pigmentAValue, v -> pigmentAValue = v));
        container.track(SyncableLong.create(() -> pigmentBValue, v -> pigmentBValue = v));
        container.track(SyncableLong.create(() -> slurryAValue, v -> slurryAValue = v));
        container.track(SyncableLong.create(() -> slurryBValue, v -> slurryBValue = v));
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        itemAValue = nbt.getInt("itemAValue");
        itemBValue = nbt.getInt("itemBValue");
        fluidAValue = nbt.getInt("fluidAValue");
        fluidBValue = nbt.getInt("fluidBValue");
        gasAValue = nbt.getLong("gasAValue");
        gasBValue = nbt.getLong("gasBValue");
        infusionAValue = nbt.getLong("infusionAValue");
        infusionBValue = nbt.getLong("infusionBValue");
        pigmentAValue = nbt.getLong("pigmentAValue");
        pigmentBValue = nbt.getLong("pigmentBValue");
        slurryAValue = nbt.getLong("slurryAValue");
        slurryBValue = nbt.getLong("slurryBValue");
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("itemAValue", itemAValue);
        nbt.putInt("itemBValue", itemBValue);
        nbt.putInt("fluidAValue", fluidAValue);
        nbt.putInt("fluidBValue", fluidBValue);
        nbt.putLong("gasAValue", gasAValue);
        nbt.putLong("gasBValue", gasBValue);
        nbt.putLong("infusionAValue", infusionAValue);
        nbt.putLong("infusionBValue", infusionBValue);
        nbt.putLong("pigmentAValue", pigmentAValue);
        nbt.putLong("pigmentBValue", pigmentBValue);
        nbt.putLong("slurryAValue", slurryAValue);
        nbt.putLong("slurryBValue", slurryBValue);
    }
}
