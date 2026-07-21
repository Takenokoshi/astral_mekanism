package astral_mekanism.mixin.mekanism_extras.mekmm;

import java.util.EnumSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.jerry.mekanism_extras.common.integration.mekmm.registries.ExtraMoreMachineBlockTypes;
import com.jerry.mekanism_extras.common.tier.ExtraFactoryTier;
import com.jerry.mekanism_extras.common.util.ExtraEnumUtils;
import com.jerry.mekmm.common.content.blocktype.MoreMachineFactoryType;
import astral_mekanism.enums.AMEUpgrade;
import mekanism.api.Upgrade;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.content.blocktype.BlockTypeTile;

@Mixin(value = { ExtraMoreMachineBlockTypes.class }, remap = false)
public class ExtraMoreMachineBlockTypesMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void astral_mekanism$clinitInject(CallbackInfo ci) {
        for (ExtraFactoryTier tier : ExtraEnumUtils.EXTRA_FACTORY_TIERS) {
            astral_mekanism$addSupportedUpgrade(
                    ExtraMoreMachineBlockTypes.getExtraMoreMachineFactory(tier, MoreMachineFactoryType.CNC_LATHING),
                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
            astral_mekanism$addSupportedUpgrade(
                    ExtraMoreMachineBlockTypes.getExtraMoreMachineFactory(tier,
                            MoreMachineFactoryType.CNC_ROLLING_MILL),
                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
            astral_mekanism$addSupportedUpgrade(
                    ExtraMoreMachineBlockTypes.getExtraMoreMachineFactory(tier, MoreMachineFactoryType.CNC_STAMPING),
                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
            astral_mekanism$addSupportedUpgrade(
                    ExtraMoreMachineBlockTypes.getExtraMoreMachineFactory(tier, MoreMachineFactoryType.PLANTING),
                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                    AMEUpgrade.AIR_INTAKE.getValue(), AMEUpgrade.RADIOACTIVE_SEALING.getValue());
            astral_mekanism$addSupportedUpgrade(
                    ExtraMoreMachineBlockTypes.getExtraMoreMachineFactory(tier, MoreMachineFactoryType.RECYCLING),
                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
            astral_mekanism$addSupportedUpgrade(
                    ExtraMoreMachineBlockTypes.getExtraMoreMachineFactory(tier, MoreMachineFactoryType.REPLICATING),
                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                    AMEUpgrade.AIR_INTAKE.getValue(), AMEUpgrade.RADIOACTIVE_SEALING.getValue());
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
