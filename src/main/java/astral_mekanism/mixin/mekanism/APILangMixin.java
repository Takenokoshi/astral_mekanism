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

import astral_mekanism.AMEConstants;
import astral_mekanism.enumexpansion.AMEAPILang;
import mekanism.api.text.APILang;
import net.minecraft.Util;

@Mixin(value = APILang.class, remap = false)
public class APILangMixin {

    @Shadow
    @Final
    @Mutable
    @SuppressWarnings("target")
    static APILang[] $VALUES;

    @Invoker("<init>")
    private static APILang invokeInit(String name, int i, String arg) {
        return null;
    }

    @Unique
    private static APILang createNew(String type, String path) {
        int index = $VALUES.length;
        String name = Util.makeDescriptionId(type, AMEConstants.rl(path));
        String internal = type.toUpperCase();
        String[] paths = path.split("\\.");
        for (String string : paths) {
            internal += "_";
            internal += string.toUpperCase();
        }
        APILang result = invokeInit(internal, index, name);
        APILang[] newVALUES = Arrays.copyOf($VALUES, index + 1);
        newVALUES[index] = result;
        $VALUES = newVALUES;
        return result;
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinitInject(CallbackInfo ci) {
        AMEAPILang.UPGRADE_COBBLESTONE_SUPPLY = createNew("upgrade", "cobblestone_supply");
        AMEAPILang.UPGRADE_COBBLESTONE_SUPPLY_DESCRIPTION = createNew("upgrade", "cobblestone_supply.description");
        AMEAPILang.UPGRADE_WATER_SUPPLY = createNew("upgrade", "water_supply");
        AMEAPILang.UPGRADE_WATER_SUPPLY_DESCRIPTION = createNew("upgrade", "water_supply.description");
        AMEAPILang.UPGRADE_XP = createNew("upgrade", "xp");
        AMEAPILang.UPGRADE_XP_DESCRIPTION = createNew("upgrade", "xp.description");
        AMEAPILang.UPGRADE_RADIOACTIVE_SEALING = createNew("upgrade", "radioactive_sealing");
        AMEAPILang.UPGRADE_RADIOACTIVE_SEALING_DESCRIPTION = createNew("upgrade", "radioactive_sealing.description");
    }
}
