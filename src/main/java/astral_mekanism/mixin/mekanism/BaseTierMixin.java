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
import mekanism.api.tier.BaseTier;
import net.minecraft.world.level.material.MapColor;

@Mixin(value = BaseTier.class, remap = false)
public class BaseTierMixin {

    @Shadow
    @Final
    @Mutable
    @SuppressWarnings("target")
    static BaseTier[] $VALUES;

    @Invoker("<init>")
    private static BaseTier involeNew(String name, int num, String name2, int[] rgbCode, MapColor mapColor) {
        return null;
    }

    @Unique
    private static BaseTier createNew(String name, int[] rgbCode, MapColor mapColor) {
        int index = $VALUES.length;
        BaseTier result = involeNew(name, index, name, rgbCode, mapColor);
        BaseTier[] newValues = Arrays.copyOf($VALUES, index + 1);
        newValues[index] = result;
        $VALUES = newValues;
        return result;
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinitInject(CallbackInfo ci) {
        AMBaseTier.ASTRAL = createNew("astral", new int[] { 0xD4, 0xA1, 0xFF }, MapColor.TERRACOTTA_MAGENTA);
    }
}
