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

import astral_mekanism.AstralMekanismLang;
import astral_mekanism.block.blockentity.elements.AstralMekDataType;
import mekanism.api.text.EnumColor;
import mekanism.api.text.ILangEntry;
import mekanism.common.MekanismLang;
import mekanism.common.tile.component.config.DataType;

@Mixin(value = DataType.class, remap = false)
public class DataTypeMixin {

    @Shadow
    @Final
    @Mutable
    @SuppressWarnings("target")
    static DataType[] $VALUES;

    @Shadow
    @Final
    @Mutable
    static DataType[] TYPES;

    @Invoker("<init>")
    private static DataType astral_mekanism$invokeNew(String name, int num, ILangEntry langEntry, EnumColor enumColor) {
        return null;
    };

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void astral_mekanism$clinitInject(CallbackInfo ci) {
        AstralMekDataType.INPUT_OUTPUT_astral = astral_mekanism$createNew("IOastral",
                MekanismLang.SIDE_DATA_INPUT_OUTPUT,
                EnumColor.PURPLE);

        AstralMekDataType.INPUT3 = astral_mekanism$createNew("INPUT3", AstralMekanismLang.SIDE_DATA_INPUT3,
                EnumColor.BRIGHT_PINK);

        AstralMekDataType.OUTPUTleft = astral_mekanism$createNew("OUTPUTleft", AstralMekanismLang.SIDE_DATA_OUTPUTleft,
                EnumColor.DARK_GRAY);

        AstralMekDataType.INPUT1_OUTPUT1 = astral_mekanism$createNew("INPUT1_OUTPUT1",
                AstralMekanismLang.SIDE_DATA_INPUT1_OUTPUT1,
                EnumColor.BRIGHT_GREEN);
        AstralMekDataType.INPUT1_OUTPUT2 = astral_mekanism$createNew("INPUT1_OUTPUT2",
                AstralMekanismLang.SIDE_DATA_INPUT1_OUTPUT2,
                EnumColor.DARK_GREEN);
        AstralMekDataType.INPUT2_OUTPUT1 = astral_mekanism$createNew("INPUT2_OUTPUT1",
                AstralMekanismLang.SIDE_DATA_INPUT2_OUTPUT1,
                EnumColor.BROWN);
        AstralMekDataType.INPUT2_OUTPUT2 = astral_mekanism$createNew("INPUT2_OUTPUT2",
                AstralMekanismLang.SIDE_DATA_INPUT2_OUTPUT2,
                EnumColor.YELLOW);

        AstralMekDataType.INPUT1_OUTPUT = astral_mekanism$createNew("INPUT1_OUTPUT",
                AstralMekanismLang.SIDE_DATA_INPUT1_OUTPUT,
                EnumColor.BRIGHT_GREEN);
        AstralMekDataType.INPUT2_OUTPUT = astral_mekanism$createNew("INPUT2_OUTPUT",
                AstralMekanismLang.SIDE_DATA_INPUT2_OUTPUT,
                EnumColor.BROWN);
        AstralMekDataType.INPUT3_OUTPUT = astral_mekanism$createNew("INPUT3_OUTPUT",
                AstralMekanismLang.SIDE_DATA_INPUT3_OUTPUT,
                EnumColor.BRIGHT_PINK);

        AstralMekDataType.INPUT_OUTPUT1 = astral_mekanism$createNew("INPUT_OUTPUT1",
                AstralMekanismLang.SIDE_DATA_INPUT_OUTPUT1,
                EnumColor.BRIGHT_GREEN);
        AstralMekDataType.INPUT_OUTPUT2 = astral_mekanism$createNew("INPUT_OUTPUT1",
                AstralMekanismLang.SIDE_DATA_INPUT_OUTPUT2,
                EnumColor.DARK_GREEN);
        AstralMekDataType.INPUT_OUTPUTleft = astral_mekanism$createNew("INPUT_OUTPUT1",
                AstralMekanismLang.SIDE_DATA_INPUT_OUTPUTleft,
                EnumColor.INDIGO);

        AstralMekDataType.OUTPUT1low = astral_mekanism$createNew("OUTPUT1low", AstralMekanismLang.SIDE_DATA_OUTPUT1low,
                EnumColor.DARK_BLUE);
        AstralMekDataType.OUTPUT2low = astral_mekanism$createNew("OUTPUT2low", AstralMekanismLang.SIDE_DATA_OUTPUT2low,
                EnumColor.DARK_AQUA);
        AstralMekDataType.OUTPUTleftlow = astral_mekanism$createNew("OUTPUTleftlow",
                AstralMekanismLang.SIDE_DATA_OUTPUTleftlow,
                EnumColor.DARK_GRAY);

        AstralMekDataType.FISSION_FUEL = astral_mekanism$createNew("fission_fuel",
                AstralMekanismLang.SIDE_DATA_FISSION_FUEL,
                EnumColor.DARK_GREEN);
        AstralMekDataType.NUCLEAR_WASTE = astral_mekanism$createNew("nuclear_waste",
                AstralMekanismLang.SIDE_DATA_NUCLEAR_WASTE,
                EnumColor.YELLOW);
        AstralMekDataType.FLUID_COOLANT = astral_mekanism$createNew("fluid_coolant",
                AstralMekanismLang.SIDE_DATA_FLUID_COOLANT,
                EnumColor.DARK_BLUE);
        AstralMekDataType.GAS_COOLANT = astral_mekanism$createNew("gas_coolant",
                AstralMekanismLang.SIDE_DATA_GAS_COOLANT,
                EnumColor.INDIGO);
        AstralMekDataType.HEATED_FLUID_COOLANT = astral_mekanism$createNew("heated_fluid_coolant",
                AstralMekanismLang.SIDE_DATA_HEATED_FLUID_COOLANT, EnumColor.DARK_RED);
        AstralMekDataType.HEATED_GAS_COOLANT = astral_mekanism$createNew("heated_gas_coolant",
                AstralMekanismLang.SIDE_DATA_HEATED_GAS_COOLANT, EnumColor.RED);
        AstralMekDataType.DOUBLE_GAS_COOLANT = astral_mekanism$createNew("double_gas_coolant",
                AstralMekanismLang.SIDE_DATA_DOUBLE_GAS_COOLANT, EnumColor.PURPLE);

        AstralMekDataType.MIXED_FUEL = astral_mekanism$createNew("mixed_fuel", AstralMekanismLang.SIDE_DATA_MIXED_FUEL,
                EnumColor.PURPLE);
        AstralMekDataType.LEFT_FUEL = astral_mekanism$createNew("left_fuel", AstralMekanismLang.SIDE_DATA_LEFT_FUEL,
                EnumColor.RED);
        AstralMekDataType.RIGHT_FUEL = astral_mekanism$createNew("right_fuel", AstralMekanismLang.SIDE_DATA_RIGHT_FUEL,
                EnumColor.BRIGHT_GREEN);
        AstralMekDataType.STEAM = astral_mekanism$createNew("steam", AstralMekanismLang.SIDE_DATA_STEAM,
                EnumColor.BLACK);
    }

    @Unique
    private static DataType astral_mekanism$createNew(String name, ILangEntry langEntry, EnumColor enumColor) {
        int index = $VALUES.length;
        DataType result = astral_mekanism$invokeNew(name, index, langEntry, enumColor);
        DataType[] newValues = Arrays.copyOf($VALUES, index + 1);
        newValues[index] = result;
        $VALUES = newValues;
        TYPES = DataType.values();
        return result;
    }

}
