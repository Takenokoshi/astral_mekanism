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
    private static DataType invokeNew(String name, int num, ILangEntry langEntry, EnumColor enumColor) {
        return null;
    };

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinitInject(CallbackInfo ci) {
        AstralMekDataType.INPUT_OUTPUT_astral = createNew("IOastral", MekanismLang.SIDE_DATA_INPUT_OUTPUT,
                EnumColor.PURPLE);

        AstralMekDataType.INPUT3 = createNew("INPUT3", AstralMekanismLang.SIDE_DATA_INPUT3, EnumColor.BRIGHT_PINK);

        AstralMekDataType.OUTPUTleft = createNew("OUTPUTleft", AstralMekanismLang.SIDE_DATA_OUTPUTleft,
                EnumColor.DARK_GRAY);

        AstralMekDataType.INPUT1_OUTPUT1 = createNew("INPUT1_OUTPUT1", AstralMekanismLang.SIDE_DATA_INPUT1_OUTPUT1,
                EnumColor.BRIGHT_GREEN);
        AstralMekDataType.INPUT1_OUTPUT2 = createNew("INPUT1_OUTPUT2", AstralMekanismLang.SIDE_DATA_INPUT1_OUTPUT2,
                EnumColor.DARK_GREEN);
        AstralMekDataType.INPUT2_OUTPUT1 = createNew("INPUT2_OUTPUT1", AstralMekanismLang.SIDE_DATA_INPUT2_OUTPUT1,
                EnumColor.BROWN);
        AstralMekDataType.INPUT2_OUTPUT2 = createNew("INPUT2_OUTPUT2", AstralMekanismLang.SIDE_DATA_INPUT2_OUTPUT2,
                EnumColor.YELLOW);

        AstralMekDataType.INPUT1_OUTPUT = createNew("INPUT1_OUTPUT", AstralMekanismLang.SIDE_DATA_INPUT1_OUTPUT,
                EnumColor.BRIGHT_GREEN);
        AstralMekDataType.INPUT2_OUTPUT = createNew("INPUT2_OUTPUT", AstralMekanismLang.SIDE_DATA_INPUT2_OUTPUT,
                EnumColor.BROWN);
        AstralMekDataType.INPUT3_OUTPUT = createNew("INPUT3_OUTPUT", AstralMekanismLang.SIDE_DATA_INPUT3,
                EnumColor.BRIGHT_PINK);

        AstralMekDataType.INPUT_OUTPUT1 = createNew("INPUT_OUTPUT1", AstralMekanismLang.SIDE_DATA_INPUT_OUTPUT1,
                EnumColor.BRIGHT_GREEN);
        AstralMekDataType.INPUT_OUTPUT2 = createNew("INPUT_OUTPUT1", AstralMekanismLang.SIDE_DATA_INPUT_OUTPUT2,
                EnumColor.DARK_GREEN);
        AstralMekDataType.INPUT_OUTPUTleft = createNew("INPUT_OUTPUT1", AstralMekanismLang.SIDE_DATA_INPUT_OUTPUTleft,
                EnumColor.INDIGO);

        AstralMekDataType.OUTPUT1low = createNew("OUTPUT1low", AstralMekanismLang.SIDE_DATA_OUTPUT1low,
                EnumColor.DARK_BLUE);
        AstralMekDataType.OUTPUT2low = createNew("OUTPUT2low", AstralMekanismLang.SIDE_DATA_OUTPUT2low,
                EnumColor.DARK_AQUA);
        AstralMekDataType.OUTPUTleftlow = createNew("OUTPUTleftlow", AstralMekanismLang.SIDE_DATA_OUTPUTleftlow,
                EnumColor.DARK_GRAY);

        AstralMekDataType.FISSION_FUEL = createNew("fission_fuel", AstralMekanismLang.SIDE_DATA_FISSION_FUEL,
                EnumColor.DARK_GREEN);
        AstralMekDataType.NUCLEAR_WASTE = createNew("nuclear_waste", AstralMekanismLang.SIDE_DATA_NUCLEAR_WASTE,
                EnumColor.YELLOW);
        AstralMekDataType.FLUID_COOLANT = createNew("fluid_coolant", AstralMekanismLang.SIDE_DATA_FLUID_COOLANT,
                EnumColor.DARK_BLUE);
        AstralMekDataType.GAS_COOLANT = createNew("gas_coolant", AstralMekanismLang.SIDE_DATA_GAS_COOLANT,
                EnumColor.INDIGO);
        AstralMekDataType.HEATED_FLUID_COOLANT = createNew("heated_fluid_coolant",
                AstralMekanismLang.SIDE_DATA_HEATED_FLUID_COOLANT, EnumColor.DARK_RED);
        AstralMekDataType.HEATED_GAS_COOLANT = createNew("heated_gas_coolant",
                AstralMekanismLang.SIDE_DATA_HEATED_GAS_COOLANT, EnumColor.RED);
        AstralMekDataType.DOUBLE_GAS_COOLANT = createNew("double_gas_coolant",
                AstralMekanismLang.SIDE_DATA_DOUBLE_GAS_COOLANT, EnumColor.PURPLE);
    }

    @Unique
    private static DataType createNew(String name, ILangEntry langEntry, EnumColor enumColor) {
        int index = $VALUES.length;
        DataType result = invokeNew(name, index, langEntry, enumColor);
        DataType[] newValues = Arrays.copyOf($VALUES, index + 1);
        newValues[index] = result;
        $VALUES = newValues;
        TYPES = DataType.values();
        return result;
    }

}
