package astral_mekanism.mixin.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    @Inject(
        method = "checkCompatibility",
        at = @At("HEAD"),
        cancellable = true
    )
    private void allowDiggerWeaponCombination(
            Enchantment other,
            CallbackInfoReturnable<Boolean> cir
    ) {
        Enchantment self = (Enchantment) (Object) this;

        EnchantmentCategory c1 = self.category;
        EnchantmentCategory c2 = other.category;

        boolean diggerWeapon =
                (c1 == EnchantmentCategory.DIGGER && c2 == EnchantmentCategory.WEAPON)
             || (c1 == EnchantmentCategory.WEAPON && c2 == EnchantmentCategory.DIGGER);

        if (diggerWeapon) {
            cir.setReturnValue(true);
        }
    }
}
