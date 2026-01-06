package astral_mekanism.mixin.mekanism;

import java.util.Arrays;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import astral_mekanism.block.container.core.AMWindowType;
import mekanism.common.inventory.container.SelectedWindowData.WindowType;

@Mixin(value = WindowType.class, remap = false)
public class WindowTypeMixin {

    @Shadow
    @Final
    @Mutable
    @SuppressWarnings("target")
    static WindowType[] $VALUES;

    @Invoker("<init>")
    private static WindowType invokeNew(String name, int num, @Nullable String saveName, byte maxData) {
        return null;
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinitInject(CallbackInfo ci) {
        AMWindowType.PAGED = createNew("paged", "paged", (byte) 127);
    }

    @Unique
    private static WindowType createNew(String name, @Nullable String saveName, byte maxData) {
        int index = $VALUES.length;
        WindowType result = invokeNew(name, index, saveName, maxData);
        WindowType[] newValues = Arrays.copyOf($VALUES, index + 1);
        newValues[index] = result;
        $VALUES = newValues;
        return result;
    }
}
