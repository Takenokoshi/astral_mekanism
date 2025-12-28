package astral_mekanism.block.blockentity.compact;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.core.BlockEntityUtils;
import astral_mekanism.block.blockentity.elements.AstralMekDataType;
import astral_mekanism.block.blockentity.interf.IPacketReceiverSetLong;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.gas.attribute.GasAttributes.CooledCoolant;
import mekanism.api.heat.HeatAPI.HeatTransfer;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.heat.BasicHeatCapacitor;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.heat.HeatCapacitorHelper;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableDouble;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.inventory.slot.chemical.GasInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.registries.MekanismGases;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.slot.FluidSlotInfo;
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo.GasSlotInfo;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.util.HeatUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.generators.common.config.MekanismGeneratorsConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

public class BECompactFIR extends TileEntityConfigurableMachine implements IPacketReceiverSetLong {

    public BasicHeatCapacitor heatCapacitor;
    private double lastEnvironmentLoss;
    private double lastTransferLoss;

    IGasTank fissionFuelTank;
    IGasTank nuclearWasteTank;
    long efficiency;

    BasicFluidTank coolantFluidTank;
    IGasTank coolantGasTank;
    IGasTank heatedFluidCoolantGasTank;
    IGasTank heatedGasCoolantGasTank;

    GasInventorySlot fissionFuelSlot;
    GasInventorySlot nuclearWasteSlot;
    FluidInventorySlot fluidCoolantSlot;
    GasInventorySlot gasCoolantSlot;
    GasInventorySlot heatedFluidSlot;
    GasInventorySlot heatedGasSlot;

    BasicInventorySlot countSlot;

    public BECompactFIR(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        configComponent = new TileComponentConfig(this, TransmissionType.GAS, TransmissionType.FLUID,
                TransmissionType.HEAT);
        ConfigInfo gasConfig = configComponent.getConfig(TransmissionType.GAS);
        if (gasConfig != null) {
            gasConfig.addSlotInfo(AstralMekDataType.FISSION_FUEL, new GasSlotInfo(true, false, fissionFuelTank));
            gasConfig.addSlotInfo(AstralMekDataType.NUCLEAR_WASTE, new GasSlotInfo(false, true, nuclearWasteTank));
            gasConfig.addSlotInfo(AstralMekDataType.GAS_COOLANT, new GasSlotInfo(true, false, coolantGasTank));
            gasConfig.addSlotInfo(AstralMekDataType.HEATED_FLUID_COOLANT,
                    new GasSlotInfo(false, true, heatedFluidCoolantGasTank));
            gasConfig.addSlotInfo(AstralMekDataType.HEATED_GAS_COOLANT,
                    new GasSlotInfo(false, true, heatedGasCoolantGasTank));
            gasConfig.addSlotInfo(AstralMekDataType.DOUBLE_GAS_COOLANT, new GasSlotInfo(true, false, coolantGasTank));
            gasConfig.setCanEject(true);
        }
        ConfigInfo fluidConfig = configComponent.getConfig(TransmissionType.FLUID);
        if (fluidConfig != null) {
            fluidConfig.addSlotInfo(AstralMekDataType.FLUID_COOLANT, new FluidSlotInfo(true, false, coolantFluidTank));
            fluidConfig.setCanEject(false);
        }
        configComponent.setupOutputConfig(TransmissionType.HEAT, heatCapacitor);
        ejectorComponent = new TileComponentEjector(this, () -> Long.MAX_VALUE);
        lastEnvironmentLoss = 0d;
        lastTransferLoss = 0d;
        efficiency = 0l;
    }

    @NotNull
    @Override
    protected IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener,
            CachedAmbientTemperature ambientTemperature) {
        HeatCapacitorHelper builder = HeatCapacitorHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addCapacitor(
                heatCapacitor = BasicHeatCapacitor.create(1000000d, 20d, 10d, ambientTemperature, listener));
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper
                .forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(fissionFuelTank = ChemicalTankBuilder.GAS.input(Long.MAX_VALUE,
                gas -> gas == MekanismGases.FISSILE_FUEL.get(), listener));
        builder.addTank(nuclearWasteTank = ChemicalTankBuilder.GAS.output(Long.MAX_VALUE, listener));
        builder.addTank(coolantGasTank = ChemicalTankBuilder.GAS.input(Long.MAX_VALUE,
                gas -> gas.has(CooledCoolant.class), listener));
        builder.addTank(heatedFluidCoolantGasTank = ChemicalTankBuilder.GAS.output(Long.MAX_VALUE, listener));
        builder.addTank(heatedGasCoolantGasTank = ChemicalTankBuilder.GAS.output(Long.MAX_VALUE, listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(coolantFluidTank = BasicFluidTank.input(Integer.MAX_VALUE,
                (fluid) -> fluid.isFluidEqual(new FluidStack(Fluids.WATER, 1)), listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(fissionFuelSlot = GasInventorySlot.fill(fissionFuelTank, listener, 8, 64));
        builder.addSlot(nuclearWasteSlot = GasInventorySlot.drain(nuclearWasteTank, listener, 152, 64));
        builder.addSlot(fluidCoolantSlot = FluidInventorySlot.fill(coolantFluidTank, listener, 26, 64));
        builder.addSlot(gasCoolantSlot = GasInventorySlot.fill(coolantGasTank, listener, 44, 64));
        builder.addSlot(heatedFluidSlot = GasInventorySlot.drain(heatedFluidCoolantGasTank, listener, 134, 64));
        builder.addSlot(heatedGasSlot = GasInventorySlot.drain(heatedGasCoolantGasTank, listener, 116, 64));
        builder.addSlot(countSlot = BasicInventorySlot.at(listener, 80, 64));
        fissionFuelSlot.setSlotOverlay(SlotOverlay.MINUS);
        nuclearWasteSlot.setSlotOverlay(SlotOverlay.PLUS);
        fluidCoolantSlot.setSlotOverlay(SlotOverlay.MINUS);
        gasCoolantSlot.setSlotOverlay(SlotOverlay.MINUS);
        heatedFluidSlot.setSlotOverlay(SlotOverlay.PLUS);
        heatedGasSlot.setSlotOverlay(SlotOverlay.PLUS);
        countSlot.setSlotOverlay(SlotOverlay.SELECT);
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        efficiency = countSlot.getCount();
        fissionFuelSlot.fillTank();
        nuclearWasteSlot.drainTank();
        fluidCoolantSlot.fillTank();
        gasCoolantSlot.fillTank();
        heatedFluidSlot.drainTank();
        heatedGasSlot.drainTank();
        HeatTransfer loss = simulate();
        lastEnvironmentLoss = loss.environmentTransfer();
        lastTransferLoss = loss.adjacentTransfer();
        if (MekanismUtils.canFunction(this)) {
            if (heatCapacitor.getTemperature() < Double.MAX_VALUE / 4) {
                fission();
            }
            heatFluid();
            heatGas();
        }
        BlockEntityUtils.gasEject(this, List.of(AstralMekDataType.NUCLEAR_WASTE), nuclearWasteTank);
        BlockEntityUtils.gasEject(this, List.of(AstralMekDataType.HEATED_FLUID_COOLANT), heatedFluidCoolantGasTank);
        BlockEntityUtils.gasEject(this,
                List.of(AstralMekDataType.HEATED_GAS_COOLANT, AstralMekDataType.DOUBLE_GAS_COOLANT),
                heatedGasCoolantGasTank);
        super.onUpdateServer();
    }

    private void fission() {
        if (fissionFuelTank.isEmpty() || fissionFuelTank.getType() != MekanismGases.FISSILE_FUEL.get()) {
            return;
        }
        if ((!nuclearWasteTank.isEmpty()) && nuclearWasteTank.getType() != MekanismGases.NUCLEAR_WASTE.get()) {
            return;
        }
        long fuelCanUse = Math.min(efficiency, nuclearWasteTank.getNeeded());
        fuelCanUse = Math.min(fuelCanUse, fissionFuelTank.getStored());
        heatCapacitor
                .handleHeat(fuelCanUse * MekanismGeneratorsConfig.generators.energyPerFissionFuel.get().doubleValue());
        fissionFuelTank.extract(fuelCanUse, Action.EXECUTE, AutomationType.INTERNAL);
        nuclearWasteTank.insert(new GasStack(MekanismGases.NUCLEAR_WASTE, fuelCanUse), Action.EXECUTE,
                AutomationType.INTERNAL);
    }

    private void heatFluid() {
        if (coolantFluidTank.isEmpty()
                || (!coolantFluidTank.getFluid().isFluidEqual(new FluidStack(Fluids.WATER, 1)))) {
            return;
        }
        if ((!heatedFluidCoolantGasTank.isEmpty())
                && heatedFluidCoolantGasTank.getType() != MekanismGases.STEAM.get()) {
            return;
        }
        int waterCanUse = Math.min(coolantFluidTank.getFluidAmount(),
                (int) Math.min(heatedFluidCoolantGasTank.getNeeded(), Integer.MAX_VALUE));
        waterCanUse = Math.min(waterCanUse,
                (int) Math.min((heatCapacitor.getHeat() - 30000d) / HeatUtils.getWaterThermalEnthalpy(),
                        Integer.MAX_VALUE));
        heatCapacitor.handleHeat(-waterCanUse * HeatUtils.getWaterThermalEnthalpy());
        coolantFluidTank.extract(waterCanUse, Action.EXECUTE, AutomationType.INTERNAL);
        heatedFluidCoolantGasTank.insert(new GasStack(MekanismGases.STEAM, waterCanUse), Action.EXECUTE,
                AutomationType.INTERNAL);
    }

    private void heatGas() {
        if (coolantGasTank.isEmpty()) {
            return;
        }
        coolantGasTank.getStack().ifAttributePresent(CooledCoolant.class, (cooledCoolant) -> {
            if ((!heatedGasCoolantGasTank.isEmpty())
                    && heatedFluidCoolantGasTank.getType() != cooledCoolant.getHeatedGas()) {
                return;
            }
            long coolantCanUse = Math.min(coolantGasTank.getStored(), heatedGasCoolantGasTank.getNeeded());
            coolantCanUse = Math.min(coolantCanUse,
                    (long) Math.min((heatCapacitor.getHeat() - 30000d) / cooledCoolant.getThermalEnthalpy(),
                            Long.MAX_VALUE));
            heatCapacitor.handleHeat(-coolantCanUse * cooledCoolant.getThermalEnthalpy());
            coolantGasTank.extract(coolantCanUse, Action.EXECUTE, AutomationType.INTERNAL);
            heatedGasCoolantGasTank.insert(new GasStack(cooledCoolant.getHeatedGas(), coolantCanUse),
                    Action.EXECUTE, AutomationType.INTERNAL);
        });
    }

    public long getEfficiency() {
        return efficiency;
    }

    public double getLastTransferLoss() {
        return lastTransferLoss;
    }

    public double getLastEnvironmentLoss() {
        return lastEnvironmentLoss;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableLong.create(this::getEfficiency, value -> efficiency = value));
        container.track(SyncableDouble.create(this::getLastTransferLoss, value -> lastTransferLoss = value));
        container.track(SyncableDouble.create(this::getLastEnvironmentLoss, value -> lastEnvironmentLoss = value));
    }

    public IGasTank getFuelTank() {
        return this.fissionFuelTank;
    }

    public IGasTank getWasteTank() {
        return this.nuclearWasteTank;
    }

    public BasicFluidTank getFluidCoolantTank() {
        return this.coolantFluidTank;
    }

    public IGasTank getGasCoolantTank() {
        return this.coolantGasTank;
    }

    public IGasTank getHeatedFluidTank() {
        return this.heatedFluidCoolantGasTank;
    }

    public IGasTank getHeatedGasTank() {
        return this.heatedGasCoolantGasTank;
    }

    public List<IGasTank> getGasTanks() {
        return List.of(fissionFuelTank, nuclearWasteTank, coolantGasTank, heatedFluidCoolantGasTank,
                heatedGasCoolantGasTank);
    }

    @Override
    public void receive(int num, long value) {
        efficiency = value;
        markForSave();
    }

}
