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
    private static APILang astral_mekanism$invokeInit(String name, int i, String arg) {
        return null;
    }

    @Unique
    private static APILang astral_mekanism$createNew(String type, String path) {
        int index = $VALUES.length;
        String name = Util.makeDescriptionId(type, AMEConstants.rl(path));
        String internal = type.toUpperCase();
        String[] paths = path.split("\\.");
        for (String string : paths) {
            internal += "_";
            internal += string.toUpperCase();
        }
        APILang result = astral_mekanism$invokeInit(internal, index, name);
        APILang[] newVALUES = Arrays.copyOf($VALUES, index + 1);
        newVALUES[index] = result;
        $VALUES = newVALUES;
        return result;
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void astral_mekanism$clinitInject(CallbackInfo ci) {
        AMEAPILang.UPGRADE_COBBLESTONE_SUPPLY = astral_mekanism$createNew("upgrade", "cobblestone_supply");
        AMEAPILang.UPGRADE_COBBLESTONE_SUPPLY_DESCRIPTION = astral_mekanism$createNew("upgrade", "cobblestone_supply.description");
        AMEAPILang.UPGRADE_WATER_SUPPLY = astral_mekanism$createNew("upgrade", "water_supply");
        AMEAPILang.UPGRADE_WATER_SUPPLY_DESCRIPTION = astral_mekanism$createNew("upgrade", "water_supply.description");
        AMEAPILang.UPGRADE_XP = astral_mekanism$createNew("upgrade", "xp");
        AMEAPILang.UPGRADE_XP_DESCRIPTION = astral_mekanism$createNew("upgrade", "xp.description");
        AMEAPILang.UPGRADE_RADIOACTIVE_SEALING = astral_mekanism$createNew("upgrade", "radioactive_sealing");
        AMEAPILang.UPGRADE_RADIOACTIVE_SEALING_DESCRIPTION = astral_mekanism$createNew("upgrade", "radioactive_sealing.description");
        AMEAPILang.UPGRADE_AIR_INTAKE = astral_mekanism$createNew("upgrade", "air_intake");
        AMEAPILang.UPGRADE_AIR_INTAKE_DESCRIPTION = astral_mekanism$createNew("upgrade", "air_intake.description");
        AMEAPILang.UPGRADE_HYPER_SPEED = astral_mekanism$createNew("upgrade", "hyper_speed");
        AMEAPILang.UPGRADE_HYPER_SPEED_DESCRIPTION = astral_mekanism$createNew("upgrade", "hyper_speed.description");
    }
}
