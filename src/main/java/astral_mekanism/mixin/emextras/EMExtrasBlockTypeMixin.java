package astral_mekanism.mixin.emextras;

import java.util.EnumSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.jerry.mekanism_extras.common.tier.AdvancedFactoryTier;
import com.jerry.mekanism_extras.common.util.ExtraEnumUtils;

import astral_mekanism.enumexpansion.AMEUpgrade;
import fr.iglee42.evolvedmekanism.registries.EMFactoryType;
import io.github.masyumero.emextras.common.content.blocktype.EMExtraFactoryType;
import io.github.masyumero.emextras.common.registry.EMExtrasBlockType;
import io.github.masyumero.emextras.common.tier.EMExtraFactoryTier;
import io.github.masyumero.emextras.common.util.EMExtraEnumUtils;
import mekanism.api.Upgrade;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.content.blocktype.BlockTypeTile;

@Mixin(value = EMExtrasBlockType.class)
public class EMExtrasBlockTypeMixin {

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinitInject(CallbackInfo ci) {
        addSupportedUpgrade(EMExtrasBlockType.ALLOYER, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        addSupportedUpgrade(EMExtrasBlockType.ADVANCED_ALLOYER, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        addSupportedUpgrade(EMExtrasBlockType.ENERGIZED_SMELTER, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        addSupportedUpgrade(EMExtrasBlockType.ENRICHMENT_CHAMBER, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        addSupportedUpgrade(EMExtrasBlockType.CRUSHER, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        addSupportedUpgrade(EMExtrasBlockType.OSMIUM_COMPRESSOR, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        addSupportedUpgrade(EMExtrasBlockType.COMBINER, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        addSupportedUpgrade(EMExtrasBlockType.PURIFICATION_CHAMBER, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        addSupportedUpgrade(EMExtrasBlockType.CHEMICAL_INJECTION_CHAMBER, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        addSupportedUpgrade(EMExtrasBlockType.METALLURGIC_INFUSER, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        addSupportedUpgrade(EMExtrasBlockType.PRECISION_SAWMILL, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        for (EMExtraFactoryTier tier : EMExtraEnumUtils.EMEXTRA_FACTORY_TIERS) {
            for (EMExtraFactoryType type : EMExtraEnumUtils.EMEXTRA_FACTORY_TYPES) {
                if (type != EMExtraFactoryType.ADVANCED_ALLOYING) {
                    addSupportedUpgrade(EMExtrasBlockType.getEMExtraFactory(tier, type), AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
                }
            }
        }
        for (AdvancedFactoryTier tier : ExtraEnumUtils.ADVANCED_FACTORY_TIERS) {
            addSupportedUpgrade(EMExtrasBlockType.getAdvancedFactory(tier, EMFactoryType.ALLOYING),
                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        }
    }

    @Unique
    private static void addSupportedUpgrade(BlockTypeTile<?> tile, Upgrade... additionalUpgrades) {
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
