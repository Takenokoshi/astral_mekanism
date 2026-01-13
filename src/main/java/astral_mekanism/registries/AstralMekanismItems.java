package astral_mekanism.registries;

import java.util.EnumMap;
import java.util.function.Supplier;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.items.GlintItem;
import astral_mekanism.items.GlintItemNameColored;
import astral_mekanism.items.upgrade.ItemSingularityUpgrade;
import mekanism.api.text.EnumColor;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import mekanism.common.registries.MekanismItems;
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
            p -> new GlintItem(p.rarity(Rarity.UNCOMMON)));
    public static final ItemRegistryObject<Item> ELASTIC_ALLOY = ITEMS.register("alloy_elastic", Rarity.UNCOMMON);
    public static final ItemRegistryObject<Item> CONVERGENT_ALLOY = ITEMS.register("alloy_convergent", Rarity.RARE);
    public static final ItemRegistryObject<GlintItem> ENCHANTED_ALLOY = ITEMS.register("alloy_enchanted",
            p -> new GlintItem(p.rarity(Rarity.RARE)));
    public static final ItemRegistryObject<GlintItem> INFUSE_ALLOY = ITEMS.register("alloy_infuse",
            p -> new GlintItem(p.rarity(Rarity.EPIC)));
    public static final ItemRegistryObject<GlintItem> STARDUST_ALLOY = ITEMS.register("alloy_stardust",
            p -> new GlintItem(p.rarity(Rarity.EPIC)));
    public static final ItemRegistryObject<Item> VIBRATION_CONTROL_CIRCUIT = ITEMS
            .register("vibration_control_circuit", Rarity.UNCOMMON);
    public static final ItemRegistryObject<Item> RESONANCE_CONTROL_CIRCUIT = ITEMS
            .register("resonance_control_circuit", Rarity.RARE);
    public static final ItemRegistryObject<GlintItem> ENHANCED_CONTROL_CIRCUIT = ITEMS.register(
            "enhanced_control_circuit",
            p -> new GlintItem(p.rarity(Rarity.EPIC)));
    public static final ItemRegistryObject<GlintItem> INFUSE_CONTROL_CIRCUIT = ITEMS.register(
            "infuse_control_circuit",
            p -> new GlintItem(p.rarity(Rarity.EPIC)));
    public static final ItemRegistryObject<GlintItem> ILLUSION_CONTROL_CIRCUIT = ITEMS.register(
            "illusion_control_circuit",
            p -> new GlintItem(p.rarity(Rarity.EPIC)));
    public static final ItemRegistryObject<GlintItem> SPACETIME_MODULATION_CORE = ITEMS.register(
            "spacetime_modulation_core",
            p -> new GlintItem(p.rarity(Rarity.EPIC)));
    public static final ItemRegistryObject<GlintItemNameColored> CRYSTAL_ANTIMATTER = ITEMS.register(
            "crystal_antimatter",
            GlintItemNameColored.getSup(EnumColor.PURPLE));
    public static final ItemRegistryObject<GlintItemNameColored> CRYSTAL_ANTIMATTER_CHARGED = ITEMS.register(
            "crystal_antimatter_charged",
            GlintItemNameColored.getSup(EnumColor.PURPLE));
    public static final ItemRegistryObject<Item> GOLDEN_REDSTONE = ITEMS.register("golden_redstone");
    public static final ItemRegistryObject<Item> INSERT_UPGRADE = ITEMS.register("insert_upgrade");
    public static final ItemRegistryObject<ItemSingularityUpgrade> SINGULARITY_UPGRADE = ITEMS
            .register("singularity_upgrade", ItemSingularityUpgrade::new);
    public static final ItemRegistryObject<Item> SUMMARIZED_SPS_CASING = ITEMS
            .register("summarized_sps_casing", Rarity.EPIC);
    public static final ItemRegistryObject<Item> SUMMARIZED_SPS_PORT = ITEMS
            .register("summarized_sps_port", Rarity.EPIC);
    public static final EnumMap<OreType, ItemRegistryObject<GlintItem>> STARLIGHTS = ((Supplier<EnumMap<OreType, ItemRegistryObject<GlintItem>>>) (() -> {
        EnumMap<OreType, ItemRegistryObject<GlintItem>> result = new EnumMap<>(OreType.class);
        for (OreType oreType : OreType.values()) {
            if (oreType == OreType.NETHERITE) {
                continue;
            }
            result.put(oreType, ITEMS.register("starlight_" + oreType.type, GlintItem::new));
        }
        return result;
    })).get();

    static EnumMap<OreType, EnumMap<IntermediateState, ItemRegistryObject<Item>>> GII() {
        EnumMap<OreType, EnumMap<IntermediateState, ItemRegistryObject<Item>>> result = new EnumMap<>(OreType.class);
        for (OreType type : OreType.values()) {
            if (!type.hasMekprocessing) {
                EnumMap<IntermediateState, ItemRegistryObject<Item>> stateMap = new EnumMap<>(IntermediateState.class);
                for (IntermediateState state : IntermediateState.values()) {
                    stateMap.put(state, ITEMS.register(state.state + "_" + type.type));
                }
                result.put(type, stateMap);
            }
        }
        EnumMap<IntermediateState, ItemRegistryObject<Item>> netheriteMap = new EnumMap<>(IntermediateState.class);
        for (IntermediateState state : IntermediateState.values()) {
            String stateType = state == IntermediateState.RAW ? "dirty_dust" : state.state;
            netheriteMap.put(state,
                    state == IntermediateState.CRUSHED ? MekanismItems.NETHERITE_DUST
                            : ITEMS.register(stateType + "_" + OreType.NETHERITE.type));
        }
        result.put(OreType.NETHERITE, netheriteMap);
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

    public static final EnumMap<OreType, EnumMap<IntermediateState, ItemRegistryObject<Item>>> GEM_INTERMEDIATE_ITEMS = GII();
}
