package astral_mekanism.registries;

import java.util.HashMap;
import java.util.Map;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.items.GlintItem;
import astral_mekanism.items.upgrade.ItemSingularityUpgrade;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import net.minecraft.world.item.Item;

public class AstralMekanismItems {
	public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(AstralMekanismID.MODID);

	public static final ItemRegistryObject<Item> ASTRAL_DIAMOND = ITEMS
			.register("astral_diamond");
	public static final ItemRegistryObject<Item> ASTRAL_GLOWSTONE_INGOT = ITEMS
			.register("astral_glowstone_ingot");
	public static final ItemRegistryObject<Item> UTILITY_DUST = ITEMS.register("utility_dust");
	public static final ItemRegistryObject<Item> ENRICHED_UTILITY = ITEMS.register("enriched_utility");
	public static final ItemRegistryObject<Item> ENRICHED_SINGULARITY = ITEMS.register("enriched_singularity");
	public static final ItemRegistryObject<GlintItem> ENRICHED_NETHER_STAR = ITEMS.register("enriched_nether_star",
			(p) -> new GlintItem(p));
	public static final ItemRegistryObject<Item> ELASTIC_ALLOY = ITEMS.register("alloy_elastic");
	public static final ItemRegistryObject<Item> CONVERGENT_ALLOY = ITEMS.register("alloy_convergent");
	public static final ItemRegistryObject<GlintItem> STARDUST_ALLOY = ITEMS.register("alloy_stardust",
			(p) -> new GlintItem(p));
	public static final ItemRegistryObject<Item> VIBRATION_CONTROL_CIRCUIT = ITEMS
			.register("vibration_control_circuit");
	public static final ItemRegistryObject<Item> RESONANCE_CONTROL_CIRCUIT = ITEMS
			.register("resonance_control_circuit");
	public static final ItemRegistryObject<GlintItem> ILLUSION_CONTROL_CIRCUIT = ITEMS.register(
			"illusion_control_circuit",
			(p) -> new GlintItem(p));
	public static final ItemRegistryObject<GlintItem> SPACETIME_MODULATION_CORE = ITEMS.register(
			"spacetime_modulation_core",
			(p) -> new GlintItem(p));
	public static final ItemRegistryObject<GlintItem> CRYSTAL_ANTIMATTER = ITEMS.register("crystal_antimatter",
			(p) -> new GlintItem(p));
	public static final ItemRegistryObject<Item> ITEM_INSERT_UPGRADE = ITEMS.register("item_insert_upgrade");
	public static final ItemRegistryObject<Item> INSERT_UPGRADE = ITEMS.register("insert_upgrade");
	public static final ItemRegistryObject<ItemSingularityUpgrade> SINGULARITY_UPGRADE = ITEMS
			.register("singularity_upgrade", ItemSingularityUpgrade::new);
	public static final ItemRegistryObject<Item> DUST_BOX = ITEMS.register("dust_box");
	public static final ItemRegistryObject<Item> SUMMARIZED_THERMAL_EVAPORTION_BLOCK = ITEMS
			.register("summarized_thermal_evaporation_block");
	public static final ItemRegistryObject<Item> SUMMARIZED_THERMAL_EVAPORTION_VALVE = ITEMS
			.register("summarized_thermal_evaporation_valve");
	public static final ItemRegistryObject<Item> SUMMARIZED_SPS_CASING = ITEMS
			.register("summarized_sps_casing");
	public static final ItemRegistryObject<Item> SUMMARIZED_SPS_VALVE = ITEMS
			.register("summarized_sps_valve");
	public static final ItemRegistryObject<Item> SUMMARIZED_FIR_CASING = ITEMS
			.register("summarized_fir_casing");
	public static final ItemRegistryObject<Item> SUPERSUMMARIZED_FIR_CASING = ITEMS
			.register("supersummarized_fir_casing");
	public static final ItemRegistryObject<Item> SUMMARIZED_FIR_FUEL_ASSEMBLY = ITEMS
			.register("summarized_fir_fuel_assembly");
	public static final ItemRegistryObject<Item> SUPERSUMMARIZED_FIR_FUEL_ASSEMBLY = ITEMS
			.register("supersummarized_fir_fuel_assembly");
	public static final ItemRegistryObject<Item> SUMMARIZED_FIR_CONTROL_ROD_ASSEMBLY = ITEMS
			.register("summarized_fir_control_rod_assembly");
	public static final ItemRegistryObject<Item> SUPERSUMMARIZED_FIR_CONTROL_ROD_ASSEMBLY = ITEMS
			.register("supersummarized_fir_control_rod_assembly");
	public static final ItemRegistryObject<Item> SUMMARIZED_FIR_PORT = ITEMS
			.register("summarized_fir_port");
	public static final ItemRegistryObject<Item> SUMMARIZED_FIR_LOGIC_ADAPTOR = ITEMS
			.register("summarized_fir_logic_adaptor");

	static Map<OreType, Map<IntermediateState, ItemRegistryObject<Item>>> GII() {
		Map<OreType, Map<IntermediateState, ItemRegistryObject<Item>>> result = new HashMap<>();
		for (OreType type : OreType.values()) {
			if (!type.hasMekprocessing) {
				Map<IntermediateState, ItemRegistryObject<Item>> stateMap = new HashMap<>();
				for (IntermediateState state : IntermediateState.values()) {
					stateMap.put(state, ITEMS.register(state.state + "_" + type.type));
				}
				result.put(type, stateMap);
			}
		}
		return result;
	}

	public enum IntermediateState {
		CLUMP("clump"),
		CRUSHED("crushed"),
		CRYSTAL("crystal"),
		RAW("raw"),
		SHARD("shard"),
		;

		private IntermediateState(String state) {
			this.state = state;
		}

		public final String state;
	}

	public static final Map<OreType, Map<IntermediateState, ItemRegistryObject<Item>>> GEM_INTERMEDIATE_ITEMS = GII();
}
