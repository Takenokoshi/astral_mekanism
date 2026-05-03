package astral_mekanism.block.blockentity.elements;

import mekanism.api.heat.IHeatCapacitor;
import net.minecraft.nbt.CompoundTag;

public class MekanicalMagmaBlockHeatCapacitor implements IHeatCapacitor {

    private final double temp;

    public MekanicalMagmaBlockHeatCapacitor(double temp) {
        this.temp = temp;
    }

    public static MekanicalMagmaBlockHeatCapacitor createTiered(int t) {
        return new MekanicalMagmaBlockHeatCapacitor(300d + 100 * Math.pow(8, t) * t);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
    }

    @Override
    public void onContentsChanged() {
    }

    @Override
    public double getTemperature() {
        return temp;
    }

    @Override
    public double getInverseConduction() {
        return 1;
    }

    @Override
    public double getInverseInsulation() {
        return 1;
    }

    @Override
    public double getHeatCapacity() {
        return 100;
    }

    @Override
    public double getHeat() {
        return temp * 100;
    }

    @Override
    public void setHeat(double heat) {
    }

    @Override
    public void handleHeat(double transfer) {
    }
}