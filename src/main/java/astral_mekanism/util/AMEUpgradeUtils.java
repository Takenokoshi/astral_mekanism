package astral_mekanism.util;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.enums.AMEUpgrade;
import astral_mekanism.registries.AMEItems;
import mekanism.api.Upgrade;
import mekanism.common.item.ItemUpgrade;
import mekanism.common.registration.impl.ItemRegistryObject;

public class AMEUpgradeUtils {

    @Nullable
    public static ItemRegistryObject<ItemUpgrade> getUpgradeIRO(Upgrade upgrade) {
        if (AMEUpgrade.isAMEUpgrade(upgrade)) {
            AMEUpgrade ameUpgrade = AMEUpgrade.getAMEUpgrade(upgrade);
            switch (ameUpgrade) {
                case COBBLESTONE_SUPPLY:
                    return AMEItems.COBBLESTONE_SUPPLY_UPGRADE;
                case WATER_SUPPLY:
                    return AMEItems.WATER_SUPPLY_UPGRADE;
                case XP:
                    return AMEItems.XP_UPGRADE;
                case RADIOACTIVE_SEALING:
                    return AMEItems.RADIOACTIVE_SEALING_UPGRADE;
                case AIR_INTAKE:
                    return AMEItems.AIR_INTAKE_UPGRADE;
                case HYPER_SPEED:
                    return AMEItems.HYPER_SPEED_UPGRADE;
                case STARDUST_SPEED:
                    return AMEItems.STARDUST_SPEED_UPGRADE;
                default:
                    break;
            }
        }
        return null;
    }
}
