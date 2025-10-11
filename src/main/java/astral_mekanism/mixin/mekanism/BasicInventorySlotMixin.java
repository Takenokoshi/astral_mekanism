package astral_mekanism.mixin.mekanism;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import mekanism.common.inventory.slot.BasicInventorySlot;

@Mixin(value = BasicInventorySlot.class, remap = false)
public abstract class BasicInventorySlotMixin {
    @ModifyArg(method = "<init>(Ljava/util/function/BiPredicate;Ljava/util/function/BiPredicate;Ljava/util/function/Predicate;Lmekanism/api/IContentsListener;II)V", at = @At(value = "INVOKE", target = "Lmekanism/common/inventory/slot/BasicInventorySlot;<init>(ILjava/util/function/BiPredicate;Ljava/util/function/BiPredicate;Ljava/util/function/Predicate;Lmekanism/api/IContentsListener;II)V"), index = 0)
    private static int replaceDefaultLimit(int original) {
        return original == 64 ? (int) (Integer.MAX_VALUE / 2) : original;
    }
}
