package astral_mekanism.mixin.ae2;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.SoftOverride;

import appeng.helpers.patternprovider.PatternProviderReturnInventory;

@Mixin(value = PatternProviderReturnInventory.class, remap = false)
public class PatternProviderReturnInventoryMixin extends GenericStackInvMixin {
    @Override
    @SoftOverride
    protected long astral_mekanism$getMaxAmountModify(long original) {
        return Long.MAX_VALUE;
    }
}
