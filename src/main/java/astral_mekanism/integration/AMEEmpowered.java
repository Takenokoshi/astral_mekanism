package astral_mekanism.integration;

import java.util.EnumSet;
import java.util.function.IntConsumer;

import mekanism.api.Upgrade;
import mekanism.api.math.MathUtils;
import mekanism.common.config.MekanismConfig;
import mekanism.common.tile.interfaces.IUpgradeTile;
import mekanism.common.util.MekanismUtils;
import net.minecraftforge.fml.ModList;

public class AMEEmpowered {

    public static final String EMPOWERED_ID = "mekanism_empowered";
    private static final String EMP_SPEED = "EMPOWERED_SPEED";
    private static final String EMP_ENERGY = "EMPOWERED_ENERGY";

    public static boolean empoweredIsLoaded() {
        return ModList.get().isLoaded(EMPOWERED_ID);
    }

    public static EnumSet<Upgrade> getEmpoweredUpgrades(EnumSet<Upgrade> upgrades) {
        if (!empoweredIsLoaded()) {
            return upgrades;
        }
        EnumSet<Upgrade> result = upgrades.clone();
        if (upgrades.contains(Upgrade.SPEED)) {
            result.add(Upgrade.valueOf("EMPOWERED_SPEED"));
        }
        if (upgrades.contains(Upgrade.ENERGY)) {
            result.add(Upgrade.valueOf("EMPOWERED_ENERGY"));
        }
        return result;
    }

    public static int getTicks(int defalutTicks, IUpgradeTile tile) {
        if (!empoweredIsLoaded()) {
            return MekanismUtils.getTicks(tile, defalutTicks);
        }
        if (tile.getComponent().getUpgrades(Upgrade.SPEED) < 8 || !tile.supportsUpgrade(Upgrade.valueOf(EMP_SPEED))) {
            return MekanismUtils.getTicks(tile, defalutTicks);
        }
        int empower = tile.getComponent().getUpgrades(Upgrade.valueOf(EMP_SPEED));
        return MathUtils.clampToInt(
                defalutTicks * Math.pow(20, -empower / 8f) / MekanismConfig.general.maxUpgradeMultiplier.get());
    }

    public static int getAllSpeeds(IUpgradeTile tile) {
        int normal = tile.getComponent().getUpgrades(Upgrade.SPEED);
        if (!empoweredIsLoaded()) {
            return normal;
        }
        if (normal < 8 || !tile.supportsUpgrade(Upgrade.valueOf(EMP_SPEED))) {
            return normal;
        }
        return normal + tile.getComponent().getUpgrades(Upgrade.valueOf(EMP_SPEED));
    }

    public static int getEmpoweredSpeeds(IUpgradeTile tile) {
        if (!empoweredIsLoaded()) {
            return 0;
        }
        if (tile.getComponent().getUpgrades(Upgrade.SPEED) < 8 || !tile.supportsUpgrade(Upgrade.valueOf(EMP_SPEED))) {
            return 0;
        }
        return tile.getComponent().getUpgrades(Upgrade.valueOf(EMP_SPEED));
    }

    public static void recalculateUpgrades(IUpgradeTile tile, Upgrade upgrade,
            int baseTicksRequired, IntConsumer tickSetter) {
        if (!empoweredIsLoaded()) {
            return;
        }
        if (upgrade == Upgrade.SPEED || upgrade == Upgrade.valueOf(EMP_SPEED)) {
            tickSetter.accept(getTicks(baseTicksRequired, tile));
            return;
        } // MachineEnergyContainer's Energy will be set by Mekanism Empowered.
    }

    public static boolean isEmpoweredSpeed(Upgrade upgrade){
        if (!empoweredIsLoaded()) {
            return false;
        }
        return upgrade == Upgrade.valueOf(EMP_SPEED);
    }
}
