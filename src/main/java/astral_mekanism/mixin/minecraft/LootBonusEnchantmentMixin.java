package astral_mekanism.mixin.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LootBonusEnchantment;
import net.minecraft.world.item.enchantment.UntouchingEnchantment;

@Mixin(value = LootBonusEnchantment.class, remap = true)
public class LootBonusEnchantmentMixin {
    @Inject(method = "checkCompatibility", at = @At("HEAD"), cancellable = true)
    public void checkCompatibilityInject(Enchantment enchantment, CallbackInfoReturnable<Boolean> cir) {
        if (enchantment == Enchantments.SILK_TOUCH && enchantment instanceof UntouchingEnchantment silk) {
            cir.setReturnValue(silk.checkCompatibility((LootBonusEnchantment) (Object) this));
        }
    }
}
