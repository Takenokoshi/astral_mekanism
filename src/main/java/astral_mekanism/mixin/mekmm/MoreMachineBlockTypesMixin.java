package astral_mekanism.mixin.mekmm;

import java.util.EnumSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.jerry.mekmm.common.content.blocktype.MoreMachineFactoryType;
import com.jerry.mekmm.common.registries.MoreMachineBlockTypes;
import com.jerry.mekmm.common.util.MoreMachineEnumUtils;

import astral_mekanism.enumexpansion.AMEUpgrade;
import mekanism.api.Upgrade;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;

@Mixin(value = MoreMachineBlockTypes.class, remap = false)
public class MoreMachineBlockTypesMixin {

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinitInject(CallbackInfo ci) {
        addSupportedUpgrade(MoreMachineBlockTypes.RECYCLER, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        addSupportedUpgrade(MoreMachineBlockTypes.PLANTING_STATION, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        addSupportedUpgrade(MoreMachineBlockTypes.CNC_STAMPER, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        addSupportedUpgrade(MoreMachineBlockTypes.CNC_LATHE, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        addSupportedUpgrade(MoreMachineBlockTypes.CNC_ROLLING_MILL, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        addSupportedUpgrade(MoreMachineBlockTypes.REPLICATOR, AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
        addSupportedUpgrade(MoreMachineBlockTypes.FLUID_REPLICATOR, AMEUpgrade.WATER_SUPPLY.getValue());
        for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
            for (MoreMachineFactoryType type : MoreMachineEnumUtils.MM_FACTORY_TYPES) {
                addSupportedUpgrade(MoreMachineBlockTypes.getMoreMachineFactory(tier, type),
                        AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
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
