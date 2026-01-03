package astral_mekanism.block.blockentity.interf;

import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.tile.base.TileEntityMekanism;

public interface IEnergizedMachine<BE extends TileEntityMekanism & IEnergizedMachine<BE>> {

    public MachineEnergyContainer<BE> getEnergyContainer();

    public double getProgressScaled();
}
