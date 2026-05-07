package astral_mekanism.mixin.morethermalevaporationcompat;

import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import astral_mekanism.enums.AMEUpgrade;
import mekanism.api.Upgrade;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.content.blocktype.BlockTypeTile;

@Pseudo
@Mixin(targets = {
        "io.github.masyumero.morethermalevaporationcompat.common.registries.MoreThermalEvaporationCompatBlockTypes"
}, remap = false)
public class MoreThermalEvaporationCompatBlockTypesMixin {

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void astral_mekanism$clinitInject(CallbackInfo ci) {
        try {
            ClassLoader loader = MoreThermalEvaporationCompatBlockTypesMixin.class.getClassLoader();

            Class<?> tierClass = Class.forName(
                    "io.github.masyumero.morethermalevaporationcompat.common.tier.TETier",
                    false,
                    loader);

            Class<?> registryClass = Class.forName(
                    "io.github.masyumero.morethermalevaporationcompat.common.registries.MoreThermalEvaporationCompatBlockTypes",
                    false,
                    loader);

            Method getCompactBlockType = registryClass.getDeclaredMethod(
                    "getCompactBlockType",
                    tierClass);

            Object[] tiers = tierClass.getEnumConstants();

            for (Object tier : tiers) {
                BlockTypeTile<?> tile = (BlockTypeTile<?>) getCompactBlockType.invoke(null, tier);

                astral_mekanism$addSupportedUpgrade(
                        tile,
                        AMEUpgrade.WATER_SUPPLY.getValue());
            }

        } catch (Throwable t) {
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
