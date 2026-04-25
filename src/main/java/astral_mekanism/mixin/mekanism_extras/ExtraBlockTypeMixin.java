package astral_mekanism.mixin.mekanism_extras;

import java.util.EnumSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.jerry.mekanism_extras.common.registry.ExtraBlockType;
import com.jerry.mekanism_extras.common.tier.AdvancedFactoryTier;
import com.jerry.mekanism_extras.common.util.ExtraEnumUtils;

import astral_mekanism.enums.AMEUpgrade;
import mekanism.api.Upgrade;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.FactoryType;
import mekanism.common.util.EnumUtils;

@Mixin(value = ExtraBlockType.class, remap = false)
public class ExtraBlockTypeMixin {

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void astral_mekanism$clinitInject(CallbackInfo ci) {
        astral_mekanism$addSupportedUpgrade(ExtraBlockType.ENRICHMENT_CHAMBER,
                AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        astral_mekanism$addSupportedUpgrade(ExtraBlockType.CRUSHER, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        astral_mekanism$addSupportedUpgrade(ExtraBlockType.ENERGIZED_SMELTER, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        astral_mekanism$addSupportedUpgrade(ExtraBlockType.PRECISION_SAWMILL, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        astral_mekanism$addSupportedUpgrade(ExtraBlockType.OSMIUM_COMPRESSOR,
                AMEUpgrade.COBBLESTONE_SUPPLY.getValue(), AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                            AMEUpgrade.AIR_INTAKE.getValue());
        astral_mekanism$addSupportedUpgrade(ExtraBlockType.COMBINER, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        astral_mekanism$addSupportedUpgrade(ExtraBlockType.METALLURGIC_INFUSER,
                AMEUpgrade.COBBLESTONE_SUPPLY.getValue(), AMEUpgrade.RADIOACTIVE_SEALING.getValue());
        astral_mekanism$addSupportedUpgrade(ExtraBlockType.PURIFICATION_CHAMBER,
                AMEUpgrade.COBBLESTONE_SUPPLY.getValue(), AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                            AMEUpgrade.AIR_INTAKE.getValue());
        astral_mekanism$addSupportedUpgrade(ExtraBlockType.CHEMICAL_INJECTION_CHAMBER,
                AMEUpgrade.COBBLESTONE_SUPPLY.getValue(), AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                            AMEUpgrade.AIR_INTAKE.getValue());
        for (AdvancedFactoryTier tier : ExtraEnumUtils.ADVANCED_FACTORY_TIERS) {
            for (FactoryType type : EnumUtils.FACTORY_TYPES) {
                if (type == FactoryType.COMPRESSING
                        || type == FactoryType.INFUSING
                        || type == FactoryType.INJECTING
                        || type == FactoryType.PURIFYING) {
                    astral_mekanism$addSupportedUpgrade(ExtraBlockType.getAdvancedFactory(tier, type),
                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(), AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                            AMEUpgrade.AIR_INTAKE.getValue());
                } else {
                    astral_mekanism$addSupportedUpgrade(ExtraBlockType.getAdvancedFactory(tier, type),
                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
                }
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
