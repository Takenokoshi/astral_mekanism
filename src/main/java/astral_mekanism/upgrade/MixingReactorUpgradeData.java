package astral_mekanism.upgrade;

import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.heat.IHeatCapacitor;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.tile.component.ITileComponent;
import mekanism.common.tile.interfaces.IRedstoneControl;
import mekanism.common.upgrade.MachineUpgradeData;

import java.util.List;

public class MixingReactorUpgradeData extends MachineUpgradeData {

    public final long injectionRate;
    public final IHeatCapacitor heatCapacitor;
    public final IGasTank leftFuelTank;
    public final IGasTank mixedFuelTank;
    public final IGasTank rightFuelTank;
    public final BasicFluidTank waterTank;
    public final IGasTank steamTank;

    public MixingReactorUpgradeData(boolean redstone, IRedstoneControl.RedstoneControl controlType, IEnergyContainer energyContainer, long injectionRate, IHeatCapacitor heatCapacitor, IGasTank leftFuelTank, IGasTank mixedFuelTank, IGasTank rightFuelTank, BasicFluidTank waterTank, IGasTank steamTank, List<ITileComponent> components) {
        super(redstone, controlType, energyContainer, 0, null, null, null, components);
        this.injectionRate = injectionRate;
        this.heatCapacitor = heatCapacitor;
        this.leftFuelTank = leftFuelTank;
        this.mixedFuelTank = mixedFuelTank;
        this.rightFuelTank = rightFuelTank;
        this.waterTank = waterTank;
        this.steamTank = steamTank;
    }
}
