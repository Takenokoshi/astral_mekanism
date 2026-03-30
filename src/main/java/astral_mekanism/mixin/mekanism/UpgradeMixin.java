package astral_mekanism.mixin.mekanism;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import astral_mekanism.enumexpansion.AMEAPILang;
import astral_mekanism.enumexpansion.AMEUpgrade;
import mekanism.api.Upgrade;
import mekanism.api.text.APILang;
import mekanism.api.text.EnumColor;
import net.minecraft.nbt.CompoundTag;

@Mixin(value = Upgrade.class, remap = false)
public class UpgradeMixin {
    @Shadow
    @Final
    @Mutable
    @SuppressWarnings("target")
    static Upgrade[] $VALUES;

    @Shadow
    @Final
    @Mutable
    static Upgrade[] UPGRADES;

    @Invoker("<init>")
    private static Upgrade astral_mekanism$invokeInit(String string, int i, String name, APILang langKey,
            APILang descLangKey,
            int maxStack, EnumColor color) {
        return null;
    }

    @Unique
    private static Upgrade astral_mekanism$createNew(String name, APILang langKey, APILang descLangKey,
            int maxStack, EnumColor color) {
        int index = $VALUES.length;
        Upgrade result = astral_mekanism$invokeInit(name.toUpperCase(), index, name, langKey, descLangKey, maxStack,
                color);
        Upgrade[] newVALUES = Arrays.copyOf($VALUES, index + 1);
        newVALUES[index] = result;
        $VALUES = newVALUES;
        UPGRADES = Upgrade.values();
        return result;
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void astral_mekanism$clinitInject(CallbackInfo ci) {
        AMEUpgrade.COBBLESTONE_SUPPLY
                .setValue(astral_mekanism$createNew("cobblestone_supply", AMEAPILang.UPGRADE_COBBLESTONE_SUPPLY,
                        AMEAPILang.UPGRADE_COBBLESTONE_SUPPLY_DESCRIPTION, 16, EnumColor.GRAY));
        AMEUpgrade.WATER_SUPPLY.setValue(astral_mekanism$createNew("water_supply", AMEAPILang.UPGRADE_WATER_SUPPLY,
                AMEAPILang.UPGRADE_WATER_SUPPLY_DESCRIPTION, 1, EnumColor.AQUA));
        AMEUpgrade.XP
                .setValue(astral_mekanism$createNew("xp", AMEAPILang.UPGRADE_XP, AMEAPILang.UPGRADE_XP_DESCRIPTION, 4,
                        EnumColor.BRIGHT_GREEN));
        AMEUpgrade.initializeMap();
    }

    @ModifyVariable(method = "buildMap", at = @At(value = "STORE", ordinal = 0), name = "upgrades")
    private static Map<Upgrade, Integer> astral_mekanism$buildMapModify(@Nullable Map<Upgrade, Integer> upgrades,
            @Nullable CompoundTag nbtTags) {
        return AMEUpgrade.buildMap(upgrades, nbtTags);
    }

    @ModifyExpressionValue(method = "saveMap", at = @At(value = "INVOKE", target = "Ljava/util/Map;entrySet()Ljava/util/Set;"))
    private static Set<Map.Entry<Upgrade, Integer>> astral_mekanism$saveMapModify(
            Set<Map.Entry<Upgrade, Integer>> original, @Local(argsOnly = true, name = "arg1") CompoundTag nbtTags) {
        return AMEUpgrade.saveMap(original, nbtTags);
    }
}
