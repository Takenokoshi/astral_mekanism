package astral_mekanism.mixin.evolvedmekanism;

import java.util.EnumSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import astral_mekanism.enumexpansion.AMEUpgrade;
import fr.iglee42.evolvedmekanism.registries.EMBlockTypes;
import mekanism.api.Upgrade;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.content.blocktype.BlockTypeTile;

@Mixin(value = EMBlockTypes.class, remap = false)
public class EMBlockTypesMixin {

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinitInject(CallbackInfo ci) {
        addSupportedUpgrade(EMBlockTypes.ALLOYER, AMEUpgrade.COBBLESTONE_SUPPLY);
        addSupportedUpgrade(EMBlockTypes.CHEMIXER, AMEUpgrade.COBBLESTONE_SUPPLY);
        addSupportedUpgrade(EMBlockTypes.MELTER, AMEUpgrade.COBBLESTONE_SUPPLY);
    }

    @Unique
    private static void addSupportedUpgrade(BlockTypeTile<?> tile, Upgrade... additionalUpgrades) {
        if (tile==null) {
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
