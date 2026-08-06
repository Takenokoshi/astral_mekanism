package astral_mekanism.upgrade;

import mekanism.api.chemical.gas.IGasTank;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.heat.BasicHeatCapacitor;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.inventory.slot.chemical.GasInventorySlot;
import mekanism.common.tile.component.ITileComponent;
import mekanism.common.tile.interfaces.IRedstoneControl;
import mekanism.common.upgrade.MachineUpgradeData;

import java.util.List;

public class FissionUpgradeData extends MachineUpgradeData {

    public final long efficiency;
    public final BasicHeatCapacitor heatCapacitor;
    public final IGasTank fissionFuelTank;
    public final IGasTank nuclearWasteTank;
    public final BasicFluidTank coolantFluidTank;
    public final IGasTank coolantGasTank;
    public final IGasTank heatedFluidCoolantGasTank;
    public final IGasTank heatedGasCoolantGasTank;
    public final GasInventorySlot fissionFuelSlot;
    public final GasInventorySlot nuclearWasteSlot;
    public final FluidInventorySlot fluidCoolantSlot;
    public final GasInventorySlot gasCoolantSlot;
    public final GasInventorySlot heatedFluidSlot;
    public final GasInventorySlot heatedGasSlot;

    public FissionUpgradeData(boolean redstone, IRedstoneControl.RedstoneControl controlType, long efficiency, BasicHeatCapacitor heatCapacitor, IGasTank fissionFuelTank, IGasTank nuclearWasteTank, BasicFluidTank coolantFluidTank, IGasTank coolantGasTank, IGasTank heatedFluidCoolantGasTank, IGasTank heatedGasCoolantGasTank, GasInventorySlot fissionFuelSlot, GasInventorySlot nuclearWasteSlot, FluidInventorySlot fluidCoolantSlot, GasInventorySlot gasCoolantSlot, GasInventorySlot heatedFluidSlot, GasInventorySlot heatedGasSlot, List<ITileComponent> components) {
        super(redstone, controlType, null, 0, null, null, null, components);
        this.efficiency = efficiency;
        this.heatCapacitor = heatCapacitor;
        this.fissionFuelTank = fissionFuelTank;
        this.nuclearWasteTank = nuclearWasteTank;
        this.coolantFluidTank = coolantFluidTank;
        this.coolantGasTank = coolantGasTank;
        this.heatedFluidCoolantGasTank = heatedFluidCoolantGasTank;
        this.heatedGasCoolantGasTank = heatedGasCoolantGasTank;
        this.fissionFuelSlot = fissionFuelSlot;
        this.nuclearWasteSlot = nuclearWasteSlot;
        this.fluidCoolantSlot = fluidCoolantSlot;
        this.gasCoolantSlot = gasCoolantSlot;
        this.heatedFluidSlot = heatedFluidSlot;
        this.heatedGasSlot = heatedGasSlot;
    }
}
