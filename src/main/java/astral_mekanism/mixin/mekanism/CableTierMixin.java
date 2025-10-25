package astral_mekanism.mixin.mekanism;

import java.util.Arrays;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import astral_mekanism.enumexpansion.AMBaseTier;
import astral_mekanism.enumexpansion.AMCableTier;
import mekanism.api.math.FloatingLong;
import mekanism.api.tier.BaseTier;
import mekanism.common.tier.CableTier;

@Mixin(value = CableTier.class, remap = false)
public class CableTierMixin {
    @Shadow
    @Final
    @Mutable
    @SuppressWarnings("target")
    static CableTier[] $VALUES;

    @Invoker("<init>")
    private static CableTier invokeNew(String name, int num, BaseTier tier, FloatingLong capacity) {
        return null;
    }

    @Unique
    private static CableTier createNew(BaseTier tier, FloatingLong capacity) {
        int index = $VALUES.length;
        CableTier result = invokeNew(tier.name(), index, tier, capacity);
        CableTier[] newValues = Arrays.copyOf($VALUES, index + 1);
        newValues[index] = result;
        $VALUES = newValues;
        return result;
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinitInject(CallbackInfo ci) {
        AMCableTier.ASTRAL = createNew(AMBaseTier.ASTRAL, FloatingLong.MAX_VALUE);
    }
}
