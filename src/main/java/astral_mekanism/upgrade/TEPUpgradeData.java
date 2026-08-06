package astral_mekanism.upgrade;

import astral_mekanism.block.blockentity.elements.slot.paged.PagedEnergyInventorySlot;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedOutputInventorySlot;
import mekanism.api.energy.IEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.heat.VariableHeatCapacitor;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.tile.component.ITileComponent;
import mekanism.common.tile.interfaces.IRedstoneControl;
import mekanism.common.upgrade.MachineUpgradeData;

import java.util.List;

public class TEPUpgradeData extends MachineUpgradeData {

    public final VariableHeatCapacitor heatCapacitor;
    public final BasicFluidTank inputTank;
    public final BasicFluidTank outputTank;
    public final FluidInventorySlot inputInputSlot;
    public final FluidInventorySlot outputInputSlot;
    public final PagedOutputInventorySlot inputOutputSlot;
    public final PagedOutputInventorySlot outputOutputSlot;
    public final PagedEnergyInventorySlot energySlot;

    public TEPUpgradeData(boolean redstone, IRedstoneControl.RedstoneControl controlType, IEnergyContainer energyContainer, VariableHeatCapacitor heatCapacitor, BasicFluidTank inputTank, BasicFluidTank outputTank, FluidInventorySlot inputInputSlot, FluidInventorySlot outputInputSlot, PagedEnergyInventorySlot energySlot, PagedOutputInventorySlot inputOutputSlot, PagedOutputInventorySlot outputOutputSlot, List<ITileComponent> components) {
        super(redstone, controlType, energyContainer,0, null, null, null, components);
        this.heatCapacitor = heatCapacitor;
        this.inputTank = inputTank;
        this.outputTank = outputTank;
        this.inputInputSlot = inputInputSlot;
        this.outputInputSlot = outputInputSlot;
        this.inputOutputSlot = inputOutputSlot;
        this.outputOutputSlot = outputOutputSlot;
        this.energySlot = energySlot;
    }
}
