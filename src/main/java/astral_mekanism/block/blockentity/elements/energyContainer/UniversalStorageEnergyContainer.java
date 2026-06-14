package astral_mekanism.block.blockentity.elements.energyContainer;

import org.jetbrains.annotations.Nullable;

import mekanism.api.IContentsListener;
import mekanism.api.math.FloatingLong;
import mekanism.common.capabilities.energy.BasicEnergyContainer;

public class UniversalStorageEnergyContainer extends BasicEnergyContainer {

    private FloatingLong currentMaxEnergy;

    public UniversalStorageEnergyContainer(FloatingLong baseMaxEnergy, @Nullable IContentsListener listener) {
        super(baseMaxEnergy, alwaysTrue, alwaysTrue, listener);
        this.currentMaxEnergy = getBaseMaxEnergy();
    }

    @Override
    public FloatingLong getMaxEnergy() {
        return currentMaxEnergy;
    }

    public FloatingLong getBaseMaxEnergy() {
        return super.getMaxEnergy();
    }

    public void recaluculateEnergyUpgrade(int energyUpgrades) {
        currentMaxEnergy = getBaseMaxEnergy().multiply(2l << 3 * energyUpgrades);
    }

}
