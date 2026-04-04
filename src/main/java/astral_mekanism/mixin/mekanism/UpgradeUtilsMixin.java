package astral_mekanism.mixin.mekanism;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import astral_mekanism.enumexpansion.AMEUpgrade;
import astral_mekanism.registries.AstralMekanismItems;
import mekanism.api.Upgrade;
import mekanism.common.util.UpgradeUtils;
import net.minecraft.world.item.ItemStack;

@Mixin(value = UpgradeUtils.class, remap = false)
public class UpgradeUtilsMixin {

    @Inject(method = "getStack(Lmekanism/api/Upgrade;I)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"),cancellable = true)
    private static void astral_mekanism$getStackInject(Upgrade upgrade, int count, CallbackInfoReturnable<ItemStack> cir) {
        if (upgrade==AMEUpgrade.COBBLESTONE_SUPPLY.getValue()) {
            cir.setReturnValue(AstralMekanismItems.COBBLESTONE_SUPPLY_UPGRADE.getItemStack(count));
            cir.cancel();
            return;
        }
        if (upgrade==AMEUpgrade.WATER_SUPPLY.getValue()) {
            cir.setReturnValue(AstralMekanismItems.WATER_SUPPLY_UPGRADE.getItemStack(count));
            cir.cancel();
            return;
        }
        if (upgrade==AMEUpgrade.XP.getValue()) {
            cir.setReturnValue(AstralMekanismItems.XP_UPGRADE.getItemStack(count));
            cir.cancel();
            return;
        }
        if (upgrade==AMEUpgrade.RADIOACTIVE_SEALING.getValue()) {
            cir.setReturnValue(AstralMekanismItems.RADIOACTIVE_SEALING_UPGRADE.getItemStack(count));
            cir.cancel();
            return;
        }
        if (upgrade==AMEUpgrade.AIR_INTAKE.getValue()) {
            cir.setReturnValue(AstralMekanismItems.AIR_INTAKE_UPGRADE.getItemStack(count));
            cir.cancel();
            return;
        }
    }
}
