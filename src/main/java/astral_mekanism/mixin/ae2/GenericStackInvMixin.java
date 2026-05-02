package astral_mekanism.mixin.ae2;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import appeng.helpers.externalstorage.GenericStackInv;

@Mixin(value = GenericStackInv.class, remap = false)
public class GenericStackInvMixin {
    @ModifyReturnValue(at = { @At("RETURN") }, method = { "getMaxAmount" })
    protected long astral_mekanism$getMaxAmountModify(long original) {
        return original;
    }
}
