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

import astral_mekanism.AMELang;
import astral_mekanism.enumexpansion.AMEDataType;
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
        AMEDataType.INPUT_OUTPUT_astral = astral_mekanism$createNew("IOastral",
                MekanismLang.SIDE_DATA_INPUT_OUTPUT,
                EnumColor.PURPLE);

        AMEDataType.INPUT3 = astral_mekanism$createNew("INPUT3", AMELang.SIDE_DATA_INPUT3,
                EnumColor.BRIGHT_PINK);

        AMEDataType.OUTPUTleft = astral_mekanism$createNew("OUTPUTleft", AMELang.SIDE_DATA_OUTPUTleft,
                EnumColor.DARK_GRAY);

        AMEDataType.INPUT1_OUTPUT1 = astral_mekanism$createNew("INPUT1_OUTPUT1",
                AMELang.SIDE_DATA_INPUT1_OUTPUT1,
                EnumColor.BRIGHT_GREEN);
        AMEDataType.INPUT1_OUTPUT2 = astral_mekanism$createNew("INPUT1_OUTPUT2",
                AMELang.SIDE_DATA_INPUT1_OUTPUT2,
                EnumColor.DARK_GREEN);
        AMEDataType.INPUT2_OUTPUT1 = astral_mekanism$createNew("INPUT2_OUTPUT1",
                AMELang.SIDE_DATA_INPUT2_OUTPUT1,
                EnumColor.BROWN);
        AMEDataType.INPUT2_OUTPUT2 = astral_mekanism$createNew("INPUT2_OUTPUT2",
                AMELang.SIDE_DATA_INPUT2_OUTPUT2,
                EnumColor.YELLOW);

        AMEDataType.INPUT1_OUTPUT = astral_mekanism$createNew("INPUT1_OUTPUT",
                AMELang.SIDE_DATA_INPUT1_OUTPUT,
                EnumColor.BRIGHT_GREEN);
        AMEDataType.INPUT2_OUTPUT = astral_mekanism$createNew("INPUT2_OUTPUT",
                AMELang.SIDE_DATA_INPUT2_OUTPUT,
                EnumColor.BROWN);
        AMEDataType.INPUT3_OUTPUT = astral_mekanism$createNew("INPUT3_OUTPUT",
                AMELang.SIDE_DATA_INPUT3_OUTPUT,
                EnumColor.BRIGHT_PINK);

        AMEDataType.INPUT_OUTPUT1 = astral_mekanism$createNew("INPUT_OUTPUT1",
                AMELang.SIDE_DATA_INPUT_OUTPUT1,
                EnumColor.BRIGHT_GREEN);
        AMEDataType.INPUT_OUTPUT2 = astral_mekanism$createNew("INPUT_OUTPUT1",
                AMELang.SIDE_DATA_INPUT_OUTPUT2,
                EnumColor.DARK_GREEN);
        AMEDataType.INPUT_OUTPUTleft = astral_mekanism$createNew("INPUT_OUTPUT1",
                AMELang.SIDE_DATA_INPUT_OUTPUTleft,
                EnumColor.INDIGO);

        AMEDataType.OUTPUT1low = astral_mekanism$createNew("OUTPUT1low", AMELang.SIDE_DATA_OUTPUT1low,
                EnumColor.DARK_BLUE);
        AMEDataType.OUTPUT2low = astral_mekanism$createNew("OUTPUT2low", AMELang.SIDE_DATA_OUTPUT2low,
                EnumColor.DARK_AQUA);
        AMEDataType.OUTPUTleftlow = astral_mekanism$createNew("OUTPUTleftlow",
                AMELang.SIDE_DATA_OUTPUTleftlow,
                EnumColor.DARK_GRAY);

        AMEDataType.FISSION_FUEL = astral_mekanism$createNew("fission_fuel",
                AMELang.SIDE_DATA_FISSION_FUEL,
                EnumColor.DARK_GREEN);
        AMEDataType.NUCLEAR_WASTE = astral_mekanism$createNew("nuclear_waste",
                AMELang.SIDE_DATA_NUCLEAR_WASTE,
                EnumColor.YELLOW);
        AMEDataType.FLUID_COOLANT = astral_mekanism$createNew("fluid_coolant",
                AMELang.SIDE_DATA_FLUID_COOLANT,
                EnumColor.DARK_BLUE);
        AMEDataType.GAS_COOLANT = astral_mekanism$createNew("gas_coolant",
                AMELang.SIDE_DATA_GAS_COOLANT,
                EnumColor.INDIGO);
        AMEDataType.HEATED_FLUID_COOLANT = astral_mekanism$createNew("heated_fluid_coolant",
                AMELang.SIDE_DATA_HEATED_FLUID_COOLANT, EnumColor.DARK_RED);
        AMEDataType.HEATED_GAS_COOLANT = astral_mekanism$createNew("heated_gas_coolant",
                AMELang.SIDE_DATA_HEATED_GAS_COOLANT, EnumColor.RED);
        AMEDataType.DOUBLE_GAS_COOLANT = astral_mekanism$createNew("double_gas_coolant",
                AMELang.SIDE_DATA_DOUBLE_GAS_COOLANT, EnumColor.PURPLE);

        AMEDataType.MIXED_FUEL = astral_mekanism$createNew("mixed_fuel", AMELang.SIDE_DATA_MIXED_FUEL,
                EnumColor.PURPLE);
        AMEDataType.LEFT_FUEL = astral_mekanism$createNew("left_fuel", AMELang.SIDE_DATA_LEFT_FUEL,
                EnumColor.RED);
        AMEDataType.RIGHT_FUEL = astral_mekanism$createNew("right_fuel", AMELang.SIDE_DATA_RIGHT_FUEL,
                EnumColor.BRIGHT_GREEN);
        AMEDataType.STEAM = astral_mekanism$createNew("steam", AMELang.SIDE_DATA_STEAM,
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
