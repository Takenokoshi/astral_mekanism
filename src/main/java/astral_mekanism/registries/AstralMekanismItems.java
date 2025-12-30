package astral_mekanism.registries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.items.GlintItem;
import astral_mekanism.items.GlintItemNameColored;
import astral_mekanism.items.upgrade.ItemSingularityUpgrade;
import mekanism.api.text.EnumColor;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

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
            GlintItem::new);
    public static final ItemRegistryObject<Item> ELASTIC_ALLOY = ITEMS.register("alloy_elastic", Rarity.UNCOMMON);
    public static final ItemRegistryObject<Item> CONVERGENT_ALLOY = ITEMS.register("alloy_convergent", Rarity.RARE);
    public static final ItemRegistryObject<GlintItem> STARDUST_ALLOY = ITEMS.register("alloy_stardust",
            p -> new GlintItem(p.rarity(Rarity.EPIC)));
    public static final ItemRegistryObject<Item> VIBRATION_CONTROL_CIRCUIT = ITEMS
            .register("vibration_control_circuit", Rarity.UNCOMMON);
    public static final ItemRegistryObject<Item> RESONANCE_CONTROL_CIRCUIT = ITEMS
            .register("resonance_control_circuit", Rarity.RARE);
    public static final ItemRegistryObject<GlintItem> ILLUSION_CONTROL_CIRCUIT = ITEMS.register(
            "illusion_control_circuit",
            p -> new GlintItem(p.rarity(Rarity.EPIC)));
    public static final ItemRegistryObject<GlintItem> SPACETIME_MODULATION_CORE = ITEMS.register(
            "spacetime_modulation_core",
            p -> new GlintItem(p.rarity(Rarity.UNCOMMON)));
    public static final ItemRegistryObject<GlintItemNameColored> CRYSTAL_ANTIMATTER = ITEMS.register(
            "crystal_antimatter",
            GlintItemNameColored.getSup(EnumColor.PURPLE));
    public static final ItemRegistryObject<GlintItemNameColored> CRYSTAL_ANTIMATTER_CHARGED = ITEMS.register(
            "crystal_antimatter_charged",
            GlintItemNameColored.getSup(EnumColor.PURPLE));
    public static final ItemRegistryObject<Item> INSERT_UPGRADE = ITEMS.register("insert_upgrade");
    public static final ItemRegistryObject<ItemSingularityUpgrade> SINGULARITY_UPGRADE = ITEMS
            .register("singularity_upgrade", ItemSingularityUpgrade::new);
    public static final ItemRegistryObject<Item> SUMMARIZED_THERMAL_EVAPORTION_BLOCK = ITEMS
            .register("summarized_thermal_evaporation_block");
    public static final ItemRegistryObject<Item> SUMMARIZED_THERMAL_EVAPORTION_VALVE = ITEMS
            .register("summarized_thermal_evaporation_valve");
    public static final ItemRegistryObject<Item> SUMMARIZED_SPS_CASING = ITEMS
            .register("summarized_sps_casing", Rarity.EPIC);
    public static final ItemRegistryObject<Item> SUMMARIZED_SPS_PORT = ITEMS
            .register("summarized_sps_port", Rarity.EPIC);
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
            .register("summarized_fir_logic_adapter");

    public static final Map<OreType, ItemRegistryObject<GlintItem>> STARLIGHTS = ((Supplier<Map<OreType, ItemRegistryObject<GlintItem>>>) (() -> {
        Map<OreType, ItemRegistryObject<GlintItem>> result = new HashMap<>();
        for (OreType oreType : OreType.values()) {
            result.put(oreType, ITEMS.register("starlight_" + oreType.type, GlintItem::new));
        }
        return result;
    })).get();

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

    public static enum IntermediateState {
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
