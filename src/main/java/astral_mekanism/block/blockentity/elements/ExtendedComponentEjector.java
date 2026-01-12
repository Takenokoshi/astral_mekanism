package astral_mekanism.block.blockentity.elements;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import mekanism.api.chemical.IChemicalTank;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.math.FloatingLongSupplier;
import mekanism.api.text.EnumColor;
import mekanism.common.config.MekanismConfig;
import mekanism.common.lib.inventory.TileTransitRequest;
import mekanism.common.lib.inventory.TransitRequest.TransitResponse;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo;
import mekanism.common.tile.component.config.slot.EnergySlotInfo;
import mekanism.common.tile.component.config.slot.FluidSlotInfo;
import mekanism.common.tile.component.config.slot.ISlotInfo;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.transmitter.TileEntityLogisticalTransporterBase;
import mekanism.common.util.CableUtils;
import mekanism.common.util.ChemicalUtil;
import mekanism.common.util.FluidUtils;
import mekanism.common.util.InventoryUtils;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ExtendedComponentEjector extends TileComponentEjector {

    private final TileEntityMekanism tile;
    private final Map<TransmissionType, ConfigInfo> configInfo = new EnumMap<>(TransmissionType.class);
    private final EnumColor[] inputColors = new EnumColor[6];
    private final LongSupplier chemicalEjectRate;
    private final IntSupplier fluidEjectRate;
    @Nullable
    private final FloatingLongSupplier energyEjectRate;
    @Nullable
    private BiPredicate<IChemicalTank<?, ?>, DataType> canChemicalTankEject;
    @Nullable
    private BiPredicate<IExtendedFluidTank, DataType> canFluidTankEject;
    @Nullable
    private BiPredicate<IInventorySlot, DataType> canInventorySlotEject;
    @Nullable
    private BiPredicate<TransmissionType, DataType> canTypeEject;
    private boolean strictInput;
    private EnumColor outputColor;

    public ExtendedComponentEjector(TileEntityMekanism tile) {
        this(tile, MekanismConfig.general.chemicalAutoEjectRate);
    }

    public ExtendedComponentEjector(TileEntityMekanism tile, LongSupplier chemicalEjectRate) {
        this(tile, chemicalEjectRate, MekanismConfig.general.fluidAutoEjectRate);
    }

    public ExtendedComponentEjector(TileEntityMekanism tile, IntSupplier fluidEjectRate) {
        this(tile, MekanismConfig.general.chemicalAutoEjectRate, fluidEjectRate, null);
    }

    public ExtendedComponentEjector(TileEntityMekanism tile, LongSupplier chemicalEjectRate,
            IntSupplier fluidEjectRate) {
        this(tile, chemicalEjectRate, fluidEjectRate, null);
    }

    public ExtendedComponentEjector(TileEntityMekanism tile, FloatingLongSupplier energyEjectRate) {
        this(tile, MekanismConfig.general.chemicalAutoEjectRate, MekanismConfig.general.fluidAutoEjectRate,
                energyEjectRate);
    }

    public ExtendedComponentEjector(TileEntityMekanism tile, LongSupplier chemicalEjectRate, IntSupplier fluidEjectRate,
            @Nullable FloatingLongSupplier energyEjectRate) {
        super(tile, chemicalEjectRate, fluidEjectRate, energyEjectRate);
        this.tile = tile;
        this.chemicalEjectRate = chemicalEjectRate;
        this.fluidEjectRate = fluidEjectRate;
        this.energyEjectRate = energyEjectRate;
    }

    public ExtendedComponentEjector setOutputData(TileComponentConfig config, TransmissionType... types) {
        for (TransmissionType type : types) {
            ConfigInfo info = config.getConfig(type);
            if (info != null) {
                configInfo.put(type, info);
            }
        }
        return this;
    }

    public ExtendedComponentEjector setCanEject(Predicate<TransmissionType> canEject) {
        super.setCanEject(canEject);
        return this;
    }

    public ExtendedComponentEjector setCanTypeEject(BiPredicate<TransmissionType, DataType> canTypeEject) {
        this.canTypeEject = canTypeEject;
        return this;
    }

    public ExtendedComponentEjector setCanFluidTankEject(BiPredicate<IExtendedFluidTank, DataType> canFluidTankEject) {
        this.canFluidTankEject = canFluidTankEject;
        return this;
    }

    public ExtendedComponentEjector setCanChemicalTankEject(
            BiPredicate<IChemicalTank<?, ?>, DataType> canChemicalTankEject) {
        this.canChemicalTankEject = canChemicalTankEject;
        return this;
    }

    public ExtendedComponentEjector setCanInventorySlotEject(
            BiPredicate<IInventorySlot, DataType> canInventorySlotEject) {
        this.canInventorySlotEject = canInventorySlotEject;
        return this;
    }

    @Override
    public void tickServer() {
        for (Map.Entry<TransmissionType, ConfigInfo> entry : configInfo.entrySet()) {
            TransmissionType type = entry.getKey();
            ConfigInfo info = entry.getValue();
            if (isEjecting(info, type)) {
                if (type == TransmissionType.ITEM) {
                    outputItems(info);
                } else if (type != TransmissionType.HEAT) {
                    eject(type, info);
                }
            }
        }
    }

    private void eject(TransmissionType type, ConfigInfo info) {
        Map<Object, Set<Direction>> outputData = null;
        for (DataType dataType : info.getSupportedDataTypes()) {
            if (canTypeEject != null && !canTypeEject.test(type, dataType)) {
                continue;
            }
            ISlotInfo slotInfo = info.getSlotInfo(dataType);
            if (slotInfo != null) {
                Set<Direction> outputSides = info.getSidesForData(dataType);
                if (!outputSides.isEmpty()) {
                    if (outputData == null) {
                        outputData = new HashMap<>();
                    }
                    if (type.isChemical() && slotInfo instanceof ChemicalSlotInfo<?, ?, ?> chemicalSlotInfo) {
                        for (IChemicalTank<?, ?> tank : chemicalSlotInfo.getTanks()) {
                            if (!tank.isEmpty() && (canChemicalTankEject == null
                                    || canChemicalTankEject.test(tank, dataType))) {
                                outputData.computeIfAbsent(tank, t -> EnumSet.noneOf(Direction.class))
                                        .addAll(outputSides);
                            }
                        }
                    } else if (type == TransmissionType.FLUID && slotInfo instanceof FluidSlotInfo fluidSlotInfo) {
                        for (IExtendedFluidTank tank : fluidSlotInfo.getTanks()) {
                            if (!tank.isEmpty()
                                    && (canFluidTankEject == null || canFluidTankEject.test(tank, dataType))) {
                                outputData.computeIfAbsent(tank, t -> EnumSet.noneOf(Direction.class))
                                        .addAll(outputSides);
                            }
                        }
                    } else if (type == TransmissionType.ENERGY
                            && slotInfo instanceof EnergySlotInfo energySlotInfo) {
                        for (IEnergyContainer container : energySlotInfo.getContainers()) {
                            if (!container.isEmpty()) {
                                outputData.computeIfAbsent(container, t -> EnumSet.noneOf(Direction.class))
                                        .addAll(outputSides);
                            }
                        }
                    }
                }
            }
        }
        if (outputData != null && !outputData.isEmpty()) {
            for (Map.Entry<Object, Set<Direction>> entry : outputData.entrySet()) {
                if (type.isChemical()) {
                    ChemicalUtil.emit(entry.getValue(), (IChemicalTank<?, ?>) entry.getKey(), tile,
                            chemicalEjectRate.getAsLong());
                } else if (type == TransmissionType.FLUID) {
                    FluidUtils.emit(entry.getValue(), (IExtendedFluidTank) entry.getKey(), tile,
                            fluidEjectRate.getAsInt());
                } else if (type == TransmissionType.ENERGY) {
                    IEnergyContainer container = (IEnergyContainer) entry.getKey();
                    CableUtils.emit(entry.getValue(), container, tile,
                            energyEjectRate == null ? container.getMaxEnergy() : energyEjectRate.get());
                }
            }
        }
    }

    private void outputItems(ConfigInfo info) {
        for (DataType dataType : info.getSupportedDataTypes()) {
            if (canTypeEject != null && !canTypeEject.test(TransmissionType.ITEM, dataType)) {
                continue;
            }
            ISlotInfo slotInfo = info.getSlotInfo(dataType);
            if (slotInfo instanceof InventorySlotInfo inventorySlotInfo) {
                Set<Direction> outputs = info.getSidesForData(dataType);
                if (!outputs.isEmpty()) {
                    EjectTransitRequest ejectMap = InventoryUtils.getEjectItemMap(
                            new EjectTransitRequest(tile, outputs.iterator().next()),
                            canInventorySlotEject == null ? inventorySlotInfo.getSlots()
                                    : inventorySlotInfo.getSlots().stream()
                                            .filter(slot -> canInventorySlotEject.test(slot, dataType)).toList());
                    if (!ejectMap.isEmpty()) {
                        for (Direction side : outputs) {
                            BlockEntity target = WorldUtils.getTileEntity(tile.getLevel(),
                                    tile.getBlockPos().relative(side));
                            if (target != null) {
                                ejectMap.side = side;
                                TransitResponse response;
                                if (target instanceof TileEntityLogisticalTransporterBase transporter) {
                                    response = transporter.getTransmitter().insert(tile, ejectMap, outputColor, true,
                                            0);
                                } else {
                                    response = ejectMap.addToInventory(target, side, 0, false);
                                }
                                if (!response.isEmpty()) {
                                    response.useAll();
                                    if (ejectMap.isEmpty()) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected static class EjectTransitRequest extends TileTransitRequest {

        public Direction side;

        public EjectTransitRequest(BlockEntity tile, Direction side) {
            super(tile, side);
            this.side = side;
        }

        @Override
        public Direction getSide() {
            return side;
        }
    }

}
