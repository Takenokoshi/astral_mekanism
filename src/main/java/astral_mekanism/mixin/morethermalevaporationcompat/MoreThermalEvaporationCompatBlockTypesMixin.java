package astral_mekanism.mixin.morethermalevaporationcompat;

import java.util.EnumSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import astral_mekanism.enums.AMEUpgrade;
import io.github.masyumero.morethermalevaporationcompat.common.registries.MoreThermalEvaporationCompatBlockTypes;
import io.github.masyumero.morethermalevaporationcompat.common.tier.TETier;
import mekanism.api.Upgrade;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.content.blocktype.BlockTypeTile;

@Mixin(value = { MoreThermalEvaporationCompatBlockTypes.class }, remap = false)
public class MoreThermalEvaporationCompatBlockTypesMixin {

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void astral_mekanism$clinitInject(CallbackInfo ci) {
        for (TETier tier : TETier.values()) {
            astral_mekanism$addSupportedUpgrade(MoreThermalEvaporationCompatBlockTypes.getCompactBlockType(tier),
                    AMEUpgrade.WATER_SUPPLY.getValue());
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
