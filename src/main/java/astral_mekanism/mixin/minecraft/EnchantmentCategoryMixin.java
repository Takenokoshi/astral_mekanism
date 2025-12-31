package astral_mekanism.mixin.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mekanism.common.item.gear.ItemAtomicDisassembler;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

@Mixin(EnchantmentCategory.class)
public class EnchantmentCategoryMixin {

    @Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
    private void allowAtomicDisassembler(
            Item item,
            CallbackInfoReturnable<Boolean> cir) {
        if (item instanceof ItemAtomicDisassembler) {
            EnchantmentCategory self = (EnchantmentCategory) (Object) this;

            if (self == EnchantmentCategory.DIGGER
                    || self == EnchantmentCategory.WEAPON) {
                cir.setReturnValue(true);
            }
        }
    }
}
