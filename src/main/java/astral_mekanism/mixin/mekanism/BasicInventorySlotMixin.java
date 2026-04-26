package astral_mekanism.mixin.mekanism;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import mekanism.common.inventory.slot.BasicInventorySlot;

@Mixin(value = BasicInventorySlot.class, remap = false)
public class BasicInventorySlotMixin {
    @ModifyReturnValue(method = "getLimit", at = @At("RETURN"))
    private int astral_mekanism$getLimitModify(int original) {
        return Math.max(original, 0x3fffffff);
    }
}
