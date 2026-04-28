package astral_mekanism.mixin.mekmm;

import java.util.EnumSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.jerry.mekmm.common.content.blocktype.MoreMachineFactoryType;
import com.jerry.mekmm.common.registries.MoreMachineBlockTypes;
import com.jerry.mekmm.common.util.MoreMachineEnumUtils;

import astral_mekanism.enums.AMEUpgrade;
import mekanism.api.Upgrade;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;

@Mixin(value = MoreMachineBlockTypes.class, remap = false)
public class MoreMachineBlockTypesMixin {

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void astral_mekanism$clinitInject(CallbackInfo ci) {
        astral_mekanism$addSupportedUpgrade(MoreMachineBlockTypes.RECYCLER, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        astral_mekanism$addSupportedUpgrade(MoreMachineBlockTypes.PLANTING_STATION,
                AMEUpgrade.COBBLESTONE_SUPPLY.getValue(), AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                            AMEUpgrade.AIR_INTAKE.getValue());
        astral_mekanism$addSupportedUpgrade(MoreMachineBlockTypes.CNC_STAMPER, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        astral_mekanism$addSupportedUpgrade(MoreMachineBlockTypes.CNC_LATHE, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        astral_mekanism$addSupportedUpgrade(MoreMachineBlockTypes.CNC_ROLLING_MILL, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        astral_mekanism$addSupportedUpgrade(MoreMachineBlockTypes.REPLICATOR,
                AMEUpgrade.COBBLESTONE_SUPPLY.getValue(), AMEUpgrade.RADIOACTIVE_SEALING.getValue());
        astral_mekanism$addSupportedUpgrade(MoreMachineBlockTypes.FLUID_REPLICATOR,
                AMEUpgrade.WATER_SUPPLY.getValue(), AMEUpgrade.RADIOACTIVE_SEALING.getValue());
        for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
            for (MoreMachineFactoryType type : MoreMachineEnumUtils.MM_FACTORY_TYPES) {
                if (type == MoreMachineFactoryType.PLANTING || type == MoreMachineFactoryType.REPLICATING) {
                    astral_mekanism$addSupportedUpgrade(MoreMachineBlockTypes.getMoreMachineFactory(tier, type),
                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(), AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                            AMEUpgrade.AIR_INTAKE.getValue());
                }
                astral_mekanism$addSupportedUpgrade(MoreMachineBlockTypes.getMoreMachineFactory(tier, type),
                        AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
            }
        }
    }

    @Unique
    private static void astral_mekanism$addSupportedUpgrade(BlockTypeTile<?> tile, Upgrade... additionalUpgrades) {
        if (tile == null) {
            return;
        }
        boolean has = tile.has(AttributeUpgradeSupport.class);
        Set<Upgrade> upgrades = has
                ? EnumSet.copyOf(tile.get(AttributeUpgradeSupport.class).supportedUpgrades())
                : EnumSet.noneOf(Upgrade.class);
        for (Upgrade upgrade : additionalUpgrades) {
            upgrades.add(upgrade);
        }
        if (has) {
            tile.remove(AttributeUpgradeSupport.class);
        }
        tile.add(new AttributeUpgradeSupport(upgrades));
    }
}
