package astral_mekanism.mixin.emextras;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import io.github.masyumero.emextras.common.inventory.slot.EMExtraFactoryInputInventorySlot;
import io.github.masyumero.emextras.common.inventory.slot.EMExtraFactoryOutputInventorySlot;

@Mixin(value = { EMExtraFactoryInputInventorySlot.class, EMExtraFactoryOutputInventorySlot.class }, remap = false)
public class EMExtrasFactorySlotMixin {
    @ModifyReturnValue(method = "getLimit", at = @At("RETURN"))
    private int astral_mekanism$getLimitModify(int original) {
        return 0x3fffffff;
    }
}
