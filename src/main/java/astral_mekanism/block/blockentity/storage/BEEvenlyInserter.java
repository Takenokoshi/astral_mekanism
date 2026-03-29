package astral_mekanism.block.blockentity.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

import org.jetbrains.annotations.NotNull;

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
import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo;
import mekanism.common.tile.component.config.slot.FluidSlotInfo;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BEEvenlyInserter extends TileEntityConfigurableMachine {

    private BasicInventorySlot inventorySlot;
    public BasicFluidTank fluidTank;
    public IGasTank gasTank;
    public IInfusionTank infusionTank;
    public IPigmentTank pigmentTank;
    public ISlurryTank slurryTank;

    public BEEvenlyInserter(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.FLUID,
                TransmissionType.GAS, TransmissionType.INFUSION, TransmissionType.PIGMENT, TransmissionType.SLURRY);
        configComponent.setupInputConfig(TransmissionType.ITEM, inventorySlot).addSlotInfo(DataType.OUTPUT,
                new InventorySlotInfo(false, true, inventorySlot));
        configComponent.setupInputConfig(TransmissionType.FLUID, fluidTank).addSlotInfo(DataType.OUTPUT,
                new FluidSlotInfo(false, true, fluidTank));
        configComponent.setupInputConfig(TransmissionType.GAS, gasTank).addSlotInfo(DataType.OUTPUT,
                new ChemicalSlotInfo.GasSlotInfo(false, true, gasTank));
        configComponent.setupInputConfig(TransmissionType.INFUSION, infusionTank).addSlotInfo(DataType.OUTPUT,
                new ChemicalSlotInfo.InfusionSlotInfo(false, true, infusionTank));
        configComponent.setupInputConfig(TransmissionType.PIGMENT, pigmentTank).addSlotInfo(DataType.OUTPUT,
                new ChemicalSlotInfo.PigmentSlotInfo(false, true, pigmentTank));
        configComponent.setupInputConfig(TransmissionType.SLURRY, slurryTank).addSlotInfo(DataType.OUTPUT,
                new ChemicalSlotInfo.SlurrySlotInfo(false, true, slurryTank));
        ejectorComponent = new EvenlyInserterEjector(this).setOutputData(configComponent, TransmissionType.ITEM,
                TransmissionType.FLUID, TransmissionType.GAS, TransmissionType.INFUSION, TransmissionType.PIGMENT,
                TransmissionType.SLURRY);
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(inventorySlot = BasicInventorySlot.at(stack -> stack.getMaxStackSize() > 1, listener, 8, 35));
        return builder.build();
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(fluidTank = BasicFluidTank.create(0x7fffffff, listener));
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper
                .forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(gasTank = ChemicalTankBuilder.GAS.createAllValid(Long.MAX_VALUE, listener));
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<InfuseType, InfusionStack, IInfusionTank> getInitialInfusionTanks(
            IContentsListener listener) {
        ChemicalTankHelper<InfuseType, InfusionStack, IInfusionTank> builder = ChemicalTankHelper
                .forSideInfusionWithConfig(this::getDirection, this::getConfig);
        builder.addTank(infusionTank = ChemicalTankBuilder.INFUSION.createAllValid(Long.MAX_VALUE, listener));
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Pigment, PigmentStack, IPigmentTank> getInitialPigmentTanks(
            IContentsListener listener) {
        ChemicalTankHelper<Pigment, PigmentStack, IPigmentTank> builder = ChemicalTankHelper
                .forSidePigmentWithConfig(this::getDirection, this::getConfig);
        builder.addTank(pigmentTank = ChemicalTankBuilder.PIGMENT.createAllValid(Long.MAX_VALUE, listener));
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Slurry, SlurryStack, ISlurryTank> getInitialSlurryTanks(IContentsListener listener) {
        ChemicalTankHelper<Slurry, SlurryStack, ISlurryTank> builder = ChemicalTankHelper
                .forSideSlurryWithConfig(this::getDirection, this::getConfig);
        builder.addTank(slurryTank = ChemicalTankBuilder.SLURRY.createAllValid(Long.MAX_VALUE, listener));
        return builder.build();
    }

    protected void onUpdateServer() {
        super.onUpdateServer();
        insertItem();
        insertFluid();
        insertChemical(gasTank, GasStack::copy, TileEntityMekanism::getGasTanks, TransmissionType.GAS);
        insertChemical(infusionTank, InfusionStack::copy, TileEntityMekanism::getInfusionTanks,
                TransmissionType.INFUSION);
        insertChemical(pigmentTank, PigmentStack::copy, TileEntityMekanism::getPigmentTanks, TransmissionType.PIGMENT);
        insertChemical(slurryTank, SlurryStack::copy, TileEntityMekanism::getSlurryTanks, TransmissionType.SLURRY);
    }

    private void insertItem() {
        if (inventorySlot.isEmpty()) {
            return;
        }
        ItemStack toInsert = inventorySlot.getStack().copyWithCount(1);
        List<IInventorySlot> targetSlots = new ArrayList<>();
        configComponent.getConfig(TransmissionType.ITEM).getSidesForData(DataType.OUTPUT).forEach(d -> {
            BlockEntity be = WorldUtils.getTileEntity(level, worldPosition.relative(d));
            if (be == null || !(be instanceof TileEntityMekanism bemek)) {
                return;
            }
            bemek.getInventorySlots(d.getOpposite()).forEach(slot -> {
                if (slot.insertItem(toInsert, Action.SIMULATE, AutomationType.EXTERNAL).isEmpty()) {
                    targetSlots.add(slot);
                }
            });
        });
        int size = targetSlots.size();
        if (size < 1 || inventorySlot.getCount() < size) {
            return;
        }
        int amount = inventorySlot.getCount() / size;
        int transferred = amount * size;
        toInsert.setCount(amount);
        for (int i = 0; i < size; i++) {
            transferred -= targetSlots.get(i).insertItem(toInsert, Action.EXECUTE, AutomationType.EXTERNAL).getCount();
        }
        inventorySlot.shrinkStack(transferred, Action.EXECUTE);
    }

    private void insertFluid() {
        if (fluidTank.isEmpty()) {
            return;
        }
        FluidStack toInsert = fluidTank.getFluid().copy();
        toInsert.setAmount(1);
        List<IExtendedFluidTank> targetTanks = new ArrayList<>();
        configComponent.getConfig(TransmissionType.FLUID).getSidesForData(DataType.OUTPUT).forEach(d -> {
            BlockEntity be = WorldUtils.getTileEntity(level, worldPosition.relative(d));
            if (be == null || !(be instanceof TileEntityMekanism bemek)) {
                return;
            }
            bemek.getFluidTanks(d.getOpposite()).forEach(t -> {
                if (t.insert(toInsert, Action.SIMULATE, AutomationType.EXTERNAL).isEmpty()) {
                    targetTanks.add(t);
                }
            });
        });
        int size = targetTanks.size();
        if (size < 1 || fluidTank.getFluidAmount() < size) {
            return;
        }
        int amount = fluidTank.getFluidAmount() / size;
        int transferred = amount * size;
        toInsert.setAmount(amount);
        for (int i = 0; i < size; i++) {
            transferred -= targetTanks.get(i).insert(toInsert, Action.EXECUTE, AutomationType.EXTERNAL).getAmount();
        }
        fluidTank.shrinkStack(transferred, Action.EXECUTE);
    }

    private <CHEMICAL extends Chemical<CHEMICAL>, STACK extends ChemicalStack<CHEMICAL>, TANK extends IChemicalTank<CHEMICAL, STACK>> void insertChemical(
            TANK chemicalTank,
            UnaryOperator<STACK> copy,
            BiFunction<TileEntityMekanism, Direction, List<TANK>> tankGetter,
            TransmissionType type) {
        if (chemicalTank.isEmpty()) {
            return;
        }
        STACK toInsert = copy.apply(chemicalTank.getStack());
        toInsert.setAmount(1);
        List<TANK> targetTanks = new ArrayList<>();
        configComponent.getConfig(type).getSidesForData(DataType.OUTPUT).forEach(d -> {
            BlockEntity be = WorldUtils.getTileEntity(level, worldPosition.relative(d));
            if (be == null || !(be instanceof TileEntityMekanism bemek)) {
                return;
            }
            tankGetter.apply(bemek, d.getOpposite()).forEach(t -> {
                if (t.insert(toInsert, Action.SIMULATE, AutomationType.EXTERNAL).isEmpty()) {
                    targetTanks.add(t);
                }
            });
        });
        int size = targetTanks.size();
        if (size < 1 || chemicalTank.getStored() < size) {
            return;
        }
        long amount = chemicalTank.getStored() / size;
        long transferred = amount * size;
        toInsert.setAmount(amount);
        for (int i = 0; i < size; i++) {
            transferred -= targetTanks.get(i).insert(toInsert, Action.EXECUTE, AutomationType.EXTERNAL).getAmount();
        }
        chemicalTank.shrinkStack(transferred, Action.EXECUTE);
    }

    private static class EvenlyInserterEjector extends TileComponentEjector {

        public EvenlyInserterEjector(BEEvenlyInserter inserter) {
            super(inserter);
        }

        @Override
        public void tickServer() {// Do nothing.
        }

    }
}
