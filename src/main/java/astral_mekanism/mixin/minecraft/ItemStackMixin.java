package astral_mekanism.mixin.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mekanism.common.item.gear.ItemAtomicDisassembler;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "isEnchantable", at = @At("HEAD"), cancellable = true)
    private void forceEnchantable(CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof ItemAtomicDisassembler) {
            cir.setReturnValue(true);
        }
    }
}
