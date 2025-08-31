package astral_mekanism.mixin;

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
import astral_mekanism.extension.DataTypeEx;
import mekanism.api.text.EnumColor;
import mekanism.api.text.ILangEntry;
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
		DataTypeEx.INPUT1_OUTPUT1 = createNew("INPUT1_OUTPUT1", AstralMekanismLang.SIDE_DATA_INPUT1_OUTPUT1,
				EnumColor.BRIGHT_GREEN);
		DataTypeEx.INPUT1_OUTPUT2 = createNew("INPUT1_OUTPUT2", AstralMekanismLang.SIDE_DATA_INPUT1_OUTPUT2,
				EnumColor.DARK_GREEN);
		DataTypeEx.INPUT2_OUTPUT1 = createNew("INPUT2_OUTPUT1", AstralMekanismLang.SIDE_DATA_INPUT2_OUTPUT1,
				EnumColor.BROWN);
		DataTypeEx.INPUT2_OUTPUT2 = createNew("INPUT2_OUTPUT2", AstralMekanismLang.SIDE_DATA_INPUT2_OUTPUT2,
				EnumColor.YELLOW);

		DataTypeEx.INPUT1_OUTPUT = createNew("INPUT1_OUTPUT", AstralMekanismLang.SIDE_DATA_INPUT1_OUTPUT,
				EnumColor.BRIGHT_GREEN);
		DataTypeEx.INPUT2_OUTPUT = createNew("INPUT2_OUTPUT", AstralMekanismLang.SIDE_DATA_INPUT2_OUTPUT,
				EnumColor.BROWN);
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
