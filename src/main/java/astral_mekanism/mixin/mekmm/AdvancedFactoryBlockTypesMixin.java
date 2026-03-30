package astral_mekanism.mixin.mekmm;

import java.util.EnumSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.jerry.mekaf.common.content.blocktype.AdvancedFactoryType;
import com.jerry.mekaf.common.registries.AdvancedFactoryBlockTypes;

import astral_mekanism.enumexpansion.AMEUpgrade;
import mekanism.api.Upgrade;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;

@Mixin(value = AdvancedFactoryBlockTypes.class, remap = false)
public class AdvancedFactoryBlockTypesMixin {

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinitInject(CallbackInfo ci) {
        addSupportedUpgrade(AdvancedFactoryBlockTypes.CHEMICAL_DISSOLUTION_CHAMBER, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        addSupportedUpgrade(AdvancedFactoryBlockTypes.CHEMICAL_OXIDIZER, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        addSupportedUpgrade(AdvancedFactoryBlockTypes.CHEMICAL_WASHER, AMEUpgrade.WATER_SUPPLY.getValue());
        addSupportedUpgrade(AdvancedFactoryBlockTypes.NUTRITIONAL_LIQUIFIER, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        addSupportedUpgrade(AdvancedFactoryBlockTypes.PRESSURIZED_REACTION_CHAMBER, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                AMEUpgrade.WATER_SUPPLY.getValue());
        for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
            addSupportedUpgrade(AdvancedFactoryBlockTypes.getAdvancedFactory(tier, AdvancedFactoryType.DISSOLVING),
                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
            addSupportedUpgrade(AdvancedFactoryBlockTypes.getAdvancedFactory(tier, AdvancedFactoryType.OXIDIZING),
                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
            addSupportedUpgrade(AdvancedFactoryBlockTypes.getAdvancedFactory(tier, AdvancedFactoryType.WASHING),
                    AMEUpgrade.WATER_SUPPLY.getValue());
            addSupportedUpgrade(AdvancedFactoryBlockTypes.getAdvancedFactory(tier, AdvancedFactoryType.LIQUIFYING),
                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
            addSupportedUpgrade(
                    AdvancedFactoryBlockTypes.getAdvancedFactory(tier, AdvancedFactoryType.PRESSURISED_REACTING),
                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue(), AMEUpgrade.WATER_SUPPLY.getValue());
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
