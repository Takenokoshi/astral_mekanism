package astral_mekanism.mixin.mekanism_extras.mekmm;

import java.util.EnumSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.jerry.mekaf.common.content.blocktype.AdvancedFactoryType;
import com.jerry.mekanism_extras.common.integration.mekaf.registries.ExtraAdvancedFactoryBlockTypes;
import com.jerry.mekanism_extras.common.tier.ExtraFactoryTier;
import com.jerry.mekanism_extras.common.util.ExtraEnumUtils;

import astral_mekanism.enums.AMEUpgrade;
import mekanism.api.Upgrade;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.content.blocktype.BlockTypeTile;

@Mixin(value = { ExtraAdvancedFactoryBlockTypes.class }, remap = false)
public class ExtraAdvancedFactoryBlockTypesMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void astral_mekanism$clinitInject(CallbackInfo ci) {

        for (ExtraFactoryTier tier : ExtraEnumUtils.EXTRA_FACTORY_TIERS) {
            astral_mekanism$addSupportedUpgrade(
                    ExtraAdvancedFactoryBlockTypes.getExtraAdvancedFactory(tier, AdvancedFactoryType.CENTRIFUGING),
                    AMEUpgrade.AIR_INTAKE.getValue(), AMEUpgrade.RADIOACTIVE_SEALING.getValue());
            astral_mekanism$addSupportedUpgrade(
                    ExtraAdvancedFactoryBlockTypes.getExtraAdvancedFactory(tier, AdvancedFactoryType.CRYSTALLIZING),
                    AMEUpgrade.AIR_INTAKE.getValue(), AMEUpgrade.RADIOACTIVE_SEALING.getValue());
            astral_mekanism$addSupportedUpgrade(
                    ExtraAdvancedFactoryBlockTypes.getExtraAdvancedFactory(tier, AdvancedFactoryType.DISSOLVING),
                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue(), AMEUpgrade.RADIOACTIVE_SEALING.getValue());
            astral_mekanism$addSupportedUpgrade(
                    ExtraAdvancedFactoryBlockTypes.getExtraAdvancedFactory(tier, AdvancedFactoryType.LIQUIFYING),
                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
            astral_mekanism$addSupportedUpgrade(
                    ExtraAdvancedFactoryBlockTypes.getExtraAdvancedFactory(tier, AdvancedFactoryType.OXIDIZING),
                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue(), AMEUpgrade.RADIOACTIVE_SEALING.getValue());
            astral_mekanism$addSupportedUpgrade(
                    ExtraAdvancedFactoryBlockTypes.getExtraAdvancedFactory(tier, AdvancedFactoryType.PAINTING),
                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
            astral_mekanism$addSupportedUpgrade(
                    ExtraAdvancedFactoryBlockTypes.getExtraAdvancedFactory(tier,
                            AdvancedFactoryType.PIGMENT_EXTRACTING),
                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
            astral_mekanism$addSupportedUpgrade(
                    ExtraAdvancedFactoryBlockTypes.getExtraAdvancedFactory(tier,
                            AdvancedFactoryType.PRESSURISED_REACTING),
                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue(), AMEUpgrade.WATER_SUPPLY.getValue(),
                    AMEUpgrade.AIR_INTAKE.getValue(), AMEUpgrade.RADIOACTIVE_SEALING.getValue());
            astral_mekanism$addSupportedUpgrade(
                    ExtraAdvancedFactoryBlockTypes.getExtraAdvancedFactory(tier, AdvancedFactoryType.WASHING),
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
