package astral_mekanism;

import mekanism.api.text.ILangEntry;
import net.minecraft.Util;

public enum AstralMekanismLang implements ILangEntry {

	DESCRIPTION_UNIVERSAL_STORAGE("description", "universal_storage"),
	DESCRIPTION_WATER_SUPPLY_DEVICE("description", "water_supply_device"),
	DESCRIPTION_COBBLESTONE_SUPPLY_DEVICE("description", "cobblestone_supply_device"),
	DESCRIPTION_GAS_BGR("description", "gas_bgr"),
	DESCRIPTION_GLOWSTONE_NEUTRON_ACTIVATOR("description", "glowstone_neutron_activator"),
	DESCRIPTION_GREENHOUSE("description", "greenhouse"),
	DESCRIPTION_MELTER("description", "melter"),
	DESCRIPTION_COMPACT_TEP("description", "compact_tep"),
	DESCRIPTION_FLUID_INFUSER("description", "fluid_infuser"),
	DESCRIPTION_MEKANICAL_CHARGER("block.ae2.charger"),
	SIDE_DATA_OUTPUTleft("side_data", "output_left"),
	SIDE_DATA_INPUT3("side_data", "input3"),
	SIDE_DATA_OUTPUT1low("side_data", "output1_low"),
	SIDE_DATA_OUTPUT2low("side_data", "output2_low"),
	SIDE_DATA_OUTPUTleftlow("side_data", "output_left_low"),
	SIDE_DATA_INPUT1_OUTPUT1("side_data", "input1_output1"),
	SIDE_DATA_INPUT1_OUTPUT2("side_data", "input1_output2"),
	SIDE_DATA_INPUT2_OUTPUT1("side_data", "input2_output1"),
	SIDE_DATA_INPUT2_OUTPUT2("side_data", "input2_output2"),
	SIDE_DATA_INPUT1_OUTPUT("side_data", "input1_output"),
	SIDE_DATA_INPUT2_OUTPUT("side_data", "input2_output"),
	SIDE_DATA_INPUT3_OUTPUT("side_data", "input3_output"),
	SIDE_DATA_INPUT_OUTPUT1("side_data", "input_output1"),
	SIDE_DATA_INPUT_OUTPUT2("side_data", "input_output2"),
	SIDE_DATA_INPUT_OUTPUTleft("side_data", "input_output_left"),
	SIDE_DATA_FISSION_FUEL("side_data", "fission_fuel"),
	SIDE_DATA_NUCLEAR_WASTE("side_data", "nuclear_waste"),
	SIDE_DATA_FLUID_COOLANT("side_data", "fluid_coolant"),
	SIDE_DATA_GAS_COOLANT("side_data", "gas_coolant"),
	SIDE_DATA_HEATED_FLUID_COOLANT("side_data", "heated_fluid_coolant"),
	SIDE_DATA_HEATED_GAS_COOLANT("side_data", "heated_gas_coolant"),
	SIDE_DATA_DOUBLE_GAS_COOLANT("side_data", "double_gas_coolant"),
	ITEM_GROUP("item_group", "modid"),;

	private final String key;

	AstralMekanismLang(String type, String path) {
		this(Util.makeDescriptionId(type, AstralMekanismID.rl(path)));
	}

	AstralMekanismLang(String key) {
		this.key = key;
	}

	@Override
	public String getTranslationKey() {
		return key;
	}

}
