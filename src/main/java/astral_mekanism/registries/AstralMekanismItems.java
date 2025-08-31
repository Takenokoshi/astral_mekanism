package astral_mekanism.registries;

import java.util.HashMap;
import java.util.Map;

import astral_mekanism.AstralMekanismID;
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
	public static final ItemRegistryObject<Item> ELASTIC_ALLOY = ITEMS.register("alloy_elastic");
	public static final ItemRegistryObject<Item> ELASTIC_CONTROL_CIRCUIT = ITEMS.register("elastic_control_circuit");
	public static final ItemRegistryObject<Item> ITEM_INSERT_UPGRADE = ITEMS.register("item_insert_upgrade");
	public static final ItemRegistryObject<Item> DUST_BOX = ITEMS.register("dust_box");
	public static final ItemRegistryObject<Item> SUMMARIZED_THERMAL_EVAPORTION_BLOCK = ITEMS
			.register("summarized_thermal_evaporation_block");
	public static final ItemRegistryObject<Item> SUMMARIZED_THERMAL_EVAPORTION_VALVE = ITEMS
			.register("summarized_thermal_evaporation_valve");

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
