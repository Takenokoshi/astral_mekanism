package astral_mekanism.mixin.ae2;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import appeng.util.inv.AppEngInternalInventory;

import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = AppEngInternalInventory.class, remap = false)
public abstract class AppEngInternalInventoryMixin {
    @ModifyArg(// 0
            method = "<init>(I)V", // 1
            at = @At(// 2
                    value = "INVOKE", // 3
                    target = "Lappeng/util/inv/AppEngInternalInventory;<init>(Lappeng/util/inv/InternalInventoryHost;IILappeng/api/util/IAEItemFilter;)V", // 4
                    ordinal = 0// 5
            ), // 6
            index = 2 // 7
    )
    private static int redirectInit1(int original) {
        return Integer.MAX_VALUE / 2;
    }

    @ModifyArg(// 0
            method = "<init>(Lappeng/util/inv/InternalInventoryHost;I)V", // 1
            at = @At(// 2
                    value = "INVOKE", // 3
                    target = "Lappeng/util/inv/AppEngInternalInventory;<init>(Lappeng/util/inv/InternalInventoryHost;IILappeng/api/util/IAEItemFilter;)V", // 4
                    ordinal = 0// 5
            ), // 6
            index = 2// 7
    )
    private static int redirectInit2(int original) {
        return Integer.MAX_VALUE / 2;
    }
}
