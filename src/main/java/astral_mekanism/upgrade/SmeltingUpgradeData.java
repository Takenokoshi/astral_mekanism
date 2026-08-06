package astral_mekanism.upgrade;

import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.tile.TileEntityChemicalTank.GasMode;
import mekanism.common.tile.component.ITileComponent;
import mekanism.common.tile.interfaces.IRedstoneControl;
import mekanism.common.upgrade.MachineUpgradeData;

import java.util.List;

public class SmeltingUpgradeData extends MachineUpgradeData {

    public final IInfusionTank stored;
    public final GasMode gasMode;

    public SmeltingUpgradeData(boolean redstone, IRedstoneControl.RedstoneControl controlType, IEnergyContainer energyContainer, int operatingTicks, GasMode gasMode, EnergyInventorySlot energySlot, IInfusionTank stored, InputInventorySlot inputSlot, OutputInventorySlot outputSlot, List<ITileComponent> components) {
        super(redstone, controlType, energyContainer, operatingTicks, energySlot, inputSlot, outputSlot, components);
        this.stored = stored;
        this.gasMode = gasMode;
    }

    public SmeltingUpgradeData(boolean redstone, IRedstoneControl.RedstoneControl controlType, IEnergyContainer energyContainer, int[] progress, GasMode gasMode, EnergyInventorySlot energySlot, IInfusionTank stored, List<IInventorySlot> inputSlots, List<IInventorySlot> outputSlots, boolean sorting, List<ITileComponent> components) {
        super(redstone, controlType, energyContainer, progress, energySlot, inputSlots, outputSlots, sorting, components);
        this.stored = stored;
        this.gasMode = gasMode;
    }
}
