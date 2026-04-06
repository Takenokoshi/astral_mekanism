package astral_mekanism.mixin.mekanismelements;

import java.util.EnumSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.fxd927.mekanismelements.common.registries.MSBlockTypes;

import astral_mekanism.enumexpansion.AMEUpgrade;
import mekanism.api.Upgrade;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.content.blocktype.BlockTypeTile;

@Mixin(value = MSBlockTypes.class, remap = false)
public class MSBlockTypesMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void astral_mekanism$clinitInject(CallbackInfo ci) {
        astral_mekanism$addSupportedUpgrade(MSBlockTypes.ADSORPTION_SEPARATOR,
                AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                AMEUpgrade.WATER_SUPPLY.getValue(),
                AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                AMEUpgrade.AIR_INTAKE.getValue());
        astral_mekanism$addSupportedUpgrade(MSBlockTypes.RADIATION_IRRADIATOR,
                AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                AMEUpgrade.AIR_INTAKE.getValue());
        astral_mekanism$addSupportedUpgrade(MSBlockTypes.AIR_COMPRESSOR,
                AMEUpgrade.RADIOACTIVE_SEALING.getValue());
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
