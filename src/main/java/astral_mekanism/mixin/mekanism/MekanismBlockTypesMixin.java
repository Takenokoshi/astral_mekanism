package astral_mekanism.mixin.mekanism;

import java.util.EnumSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import astral_mekanism.enumexpansion.AMEUpgrade;
import mekanism.api.Upgrade;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.FactoryType;
import mekanism.common.registries.MekanismBlockTypes;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;

@Mixin(value = MekanismBlockTypes.class, remap = false)
public class MekanismBlockTypesMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinitInject(CallbackInfo ci) {
        addSupportedUpgrade(MekanismBlockTypes.ENRICHMENT_CHAMBER, AMEUpgrade.COBBLESTONE_SUPPLY);
        addSupportedUpgrade(MekanismBlockTypes.CRUSHER, AMEUpgrade.COBBLESTONE_SUPPLY);
        addSupportedUpgrade(MekanismBlockTypes.ENERGIZED_SMELTER, AMEUpgrade.COBBLESTONE_SUPPLY);
        addSupportedUpgrade(MekanismBlockTypes.PRECISION_SAWMILL, AMEUpgrade.COBBLESTONE_SUPPLY);
        addSupportedUpgrade(MekanismBlockTypes.OSMIUM_COMPRESSOR, AMEUpgrade.COBBLESTONE_SUPPLY);
        addSupportedUpgrade(MekanismBlockTypes.COMBINER, AMEUpgrade.COBBLESTONE_SUPPLY);
        addSupportedUpgrade(MekanismBlockTypes.METALLURGIC_INFUSER, AMEUpgrade.COBBLESTONE_SUPPLY);
        addSupportedUpgrade(MekanismBlockTypes.PURIFICATION_CHAMBER, AMEUpgrade.COBBLESTONE_SUPPLY);
        addSupportedUpgrade(MekanismBlockTypes.CHEMICAL_INJECTION_CHAMBER, AMEUpgrade.COBBLESTONE_SUPPLY);
        addSupportedUpgrade(MekanismBlockTypes.PRESSURIZED_REACTION_CHAMBER,
                AMEUpgrade.COBBLESTONE_SUPPLY, AMEUpgrade.WATER_SUPPLY);
        addSupportedUpgrade(MekanismBlockTypes.CHEMICAL_DISSOLUTION_CHAMBER, AMEUpgrade.COBBLESTONE_SUPPLY);
        addSupportedUpgrade(MekanismBlockTypes.CHEMICAL_OXIDIZER, AMEUpgrade.COBBLESTONE_SUPPLY);
        addSupportedUpgrade(MekanismBlockTypes.CHEMICAL_WASHER, AMEUpgrade.WATER_SUPPLY);
        addSupportedUpgrade(MekanismBlockTypes.ROTARY_CONDENSENTRATOR, AMEUpgrade.WATER_SUPPLY);
        addSupportedUpgrade(MekanismBlockTypes.ELECTROLYTIC_SEPARATOR, AMEUpgrade.WATER_SUPPLY);
        addSupportedUpgrade(MekanismBlockTypes.DIGITAL_MINER, AMEUpgrade.COBBLESTONE_SUPPLY);
        addSupportedUpgrade(MekanismBlockTypes.FLUIDIC_PLENISHER, AMEUpgrade.COBBLESTONE_SUPPLY);
        addSupportedUpgrade(MekanismBlockTypes.NUTRITIONAL_LIQUIFIER, AMEUpgrade.COBBLESTONE_SUPPLY);
        addSupportedUpgrade(MekanismBlockTypes.ANTIPROTONIC_NUCLEOSYNTHESIZER, AMEUpgrade.COBBLESTONE_SUPPLY);
        addSupportedUpgrade(MekanismBlockTypes.PAINTING_MACHINE, AMEUpgrade.COBBLESTONE_SUPPLY);

        for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
            for (FactoryType type : EnumUtils.FACTORY_TYPES) {
                addSupportedUpgrade(MekanismBlockTypes.getFactory(tier, type), AMEUpgrade.COBBLESTONE_SUPPLY);
            }
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
