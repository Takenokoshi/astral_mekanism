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

import astral_mekanism.enumexpansion.AstralMekanismBaseTier;
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
    private static BaseTier invokeNew(String name, int num, String name2, int[] rgbCode, MapColor mapColor) {
        return null;
    }

    @Unique
    private static BaseTier createNew(String name, int[] rgbCode, MapColor mapColor) {
        int index = $VALUES.length;
        BaseTier result = invokeNew(name, index, name, rgbCode, mapColor);
        BaseTier[] newValues = Arrays.copyOf($VALUES, index + 1);
        newValues[index] = result;
        $VALUES = newValues;
        return result;
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinitInject(CallbackInfo ci) {
        AstralMekanismBaseTier.ESSENTIAL = createNew("essential", new int[] { 0xff, 0x22, 0xF9 },
                MapColor.TERRACOTTA_MAGENTA);
        AstralMekanismBaseTier.ABSOLUTE = createNew("absolute_overclocked", new int[] { 95, 255, 184 },
                MapColor.COLOR_LIGHT_GREEN);
        AstralMekanismBaseTier.SUPREME = createNew("supreme_quantum", new int[] { 255, 128, 106 },
                MapColor.TERRACOTTA_PINK);
        AstralMekanismBaseTier.COSMIC = createNew("cosmic_dense", new int[] { 75, 248, 255 }, MapColor.DIAMOND);
        AstralMekanismBaseTier.INFINITE = createNew("infinite_multiversal", new int[] { 247, 135, 255 },
                MapColor.COLOR_MAGENTA);
        AstralMekanismBaseTier.ASTRAL = createNew("astral", new int[] { 0xD4, 0xA1, 0xFF }, MapColor.SAND);
    }
}
