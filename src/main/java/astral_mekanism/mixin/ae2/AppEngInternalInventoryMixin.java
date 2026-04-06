package astral_mekanism.mixin.ae2;

import java.util.Arrays;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import appeng.util.inv.AppEngInternalInventory;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(value = AppEngInternalInventory.class, remap = false)
public abstract class AppEngInternalInventoryMixin {
    @Shadow
    private int[] maxStack;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void astral_mekanism$modifyMaxStack(CallbackInfo ci) {
        Arrays.fill(this.maxStack, 0x7fffffff);
    }
}