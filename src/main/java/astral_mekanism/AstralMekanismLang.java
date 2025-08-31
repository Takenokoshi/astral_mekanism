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
	SIDE_DATA_INPUT1_OUTPUT1("side_data", "input1_output1"),
	SIDE_DATA_INPUT1_OUTPUT2("side_data", "input1_output2"),
	SIDE_DATA_INPUT2_OUTPUT1("side_data", "input2_output1"),
	SIDE_DATA_INPUT2_OUTPUT2("side_data", "input2_output2"),
	SIDE_DATA_INPUT1_OUTPUT("side_data", "input1_output"),
	SIDE_DATA_INPUT2_OUTPUT("side_data", "input2_output"),
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
