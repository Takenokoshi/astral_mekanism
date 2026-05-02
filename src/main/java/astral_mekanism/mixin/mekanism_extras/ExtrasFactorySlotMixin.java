package astral_mekanism.mixin.mekanism_extras;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.jerry.mekanism_extras.common.inventory.slot.AdvancedFactoryInputInventorySlot;
import com.jerry.mekanism_extras.common.inventory.slot.AdvancedFactoryOutputInventorySlot;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;

@Mixin(value = { AdvancedFactoryInputInventorySlot.class, AdvancedFactoryOutputInventorySlot.class }, remap = false)
public class ExtrasFactorySlotMixin {
    @ModifyReturnValue(method = "getLimit", at = @At("RETURN"))
    private int astral_mekanism$getLimitModify(int original) {
        return 0x3fffffff;
    }
}
