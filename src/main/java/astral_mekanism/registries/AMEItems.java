package astral_mekanism.registries;

import java.util.EnumMap;
import java.util.function.Supplier;

import astral_mekanism.AMEConstants;
import astral_mekanism.enumexpansion.AMEUpgrade;
import astral_mekanism.items.GlintItem;
import astral_mekanism.items.GlintItemNameColored;
import astral_mekanism.registryenum.AMEProcessableMaterialType;
import astral_mekanism.registryenum.AMEProcessingItemStates;
import mekanism.api.Upgrade;
import mekanism.api.text.EnumColor;
import mekanism.common.item.ItemUpgrade;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import mekanism.common.registries.MekanismItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class AMEItems {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(AMEConstants.MODID);

    public static final ItemRegistryObject<GlintItem> SPARKLING_NOVA = ITEMS
            .register("sparkling_nova", p -> new GlintItem(p.rarity(Rarity.EPIC).fireResistant()));
    public static final ItemRegistryObject<Item> ASTRAL_DIAMOND = ITEMS
            .register("astral_diamond", Rarity.RARE);
    public static final ItemRegistryObject<Item> ASTRAL_GLOWSTONE_INGOT = ITEMS
            .register("astral_glowstone_ingot", Rarity.RARE);
    public static final ItemRegistryObject<Item> RIFINED_EMERALD_INGOT = ITEMS
            .register("refined_emerald_ingot");
    public static final ItemRegistryObject<Item> COMPOSITE_ALLOY_INGOT = ITEMS
            .register("composite_alloy_ingot", Rarity.RARE);
    public static final ItemRegistryObject<Item> FIRMAMENT_ALLOY_INGOT = ITEMS
            .register("firmament_alloy_ingot", Rarity.RARE);
    public static final ItemRegistryObject<Item> PRINTED_PHOTON_PROCESSOR = ITEMS
            .register("printed_photon_processor", Rarity.RARE);
    public static final ItemRegistryObject<Item> PRINTED_COMPOSITE_PROCESSOR = ITEMS
            .register("printed_composite_processor");
    public static final ItemRegistryObject<Item> PRINTED_ORIGIN_PROCESSOR = ITEMS
            .register("printed_origin_processor");
    public static final ItemRegistryObject<Item> PRINTED_AUTONOMY_PROCESSOR = ITEMS.register(
            "printed_autonomy_processor",
            p -> new Item(p.fireResistant()));
    public static final ItemRegistryObject<Item> PRINTED_FIRMAMENT_PROCESSOR = ITEMS
            .register("printed_firmament_processor");
    public static final ItemRegistryObject<Item> PHOTON_PROCESSOR = ITEMS
            .register("photon_processor");
    public static final ItemRegistryObject<Item> COMPOSITE_PROCESSOR = ITEMS
            .register("composite_processor");
    public static final ItemRegistryObject<Item> ORIGIN_PROCESSOR = ITEMS
            .register("origin_processor");
    public static final ItemRegistryObject<Item> AUTONOMY_PROCESSOR = ITEMS
            .register("autonomy_processor");
    public static final ItemRegistryObject<Item> FIRMAMENT_PROCESSOR = ITEMS
            .register("firmament_processor");
    public static final ItemRegistryObject<Item> PHOTON_PROCESSOR_PRESS = ITEMS
            .register("photon_processor_press");
    public static final ItemRegistryObject<Item> FIRMAMENT_PROCESSOR_PRESS = ITEMS
            .register("firmament_processor_press");
    public static final ItemRegistryObject<Item> UTILITY_DUST = ITEMS.register("utility_dust");
    public static final ItemRegistryObject<Item> POLONIUM_CONTAINING_UTILITY_DUST = ITEMS
            .register("polonium_containing_utility_dust");
    public static final ItemRegistryObject<Item> RIFINED_EMERALD_DUST = ITEMS.register("refined_emerald_dust");
    public static final ItemRegistryObject<Item> GOLDEN_REDSTONE = ITEMS.register("golden_redstone");
    public static final ItemRegistryObject<Item> AMETHYST_DUST = ITEMS.register("amethyst_dust");
    public static final ItemRegistryObject<Item> SODIUM_HYDROXIDE_DUST = ITEMS.register("sodium_hydroxide_dust");
    public static final ItemRegistryObject<GlintItemNameColored> CRYSTAL_ANTIMATTER = ITEMS.register(
            "crystal_antimatter",
            GlintItemNameColored.getSup(EnumColor.PURPLE));
    public static final ItemRegistryObject<GlintItemNameColored> CRYSTAL_ANTIMATTER_CHARGED = ITEMS.register(
            "crystal_antimatter_charged",
            GlintItemNameColored.getSup(EnumColor.PURPLE));
    public static final ItemRegistryObject<Item> NETHERITE_CLUSTER = ITEMS.register("netherite_cluster");
    public static final ItemRegistryObject<Item> BIOMASS_PASTE = ITEMS.register("biomass_paste");
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
    public static final ItemRegistryObject<Item> UPGRADE_BASE = ITEMS.register("ame_upgrade_base");
    public static final ItemRegistryObject<Item> HYPER_UPGRADE_BASE = ITEMS.register("hyper_upgrade_base");
    public static final ItemRegistryObject<ItemUpgrade> COBBLESTONE_SUPPLY_UPGRADE = registerUpgrade(
            AMEUpgrade.COBBLESTONE_SUPPLY.getValue());
    public static final ItemRegistryObject<ItemUpgrade> WATER_SUPPLY_UPGRADE = registerUpgrade(
            AMEUpgrade.WATER_SUPPLY.getValue());
    public static final ItemRegistryObject<ItemUpgrade> XP_UPGRADE = registerUpgrade(AMEUpgrade.XP.getValue());
    public static final ItemRegistryObject<ItemUpgrade> RADIOACTIVE_SEALING_UPGRADE = registerUpgrade(
            AMEUpgrade.RADIOACTIVE_SEALING.getValue());
    public static final ItemRegistryObject<ItemUpgrade> AIR_INTAKE_UPGRADE = registerUpgrade(
            AMEUpgrade.AIR_INTAKE.getValue());
    public static final ItemRegistryObject<ItemUpgrade> HYPER_SPEED_UPGRADE = registerUpgrade(
            AMEUpgrade.HYPER_SPEED.getValue());
    public static final ItemRegistryObject<Item> INSERT_UPGRADE = ITEMS.register("insert_upgrade");

    public static final EnumMap<AMEProcessableMaterialType, EnumMap<AMEProcessingItemStates, ItemRegistryObject<?>>> AME_MATERIAL_PROCESSING_ITEMS = createMaterialProcessItemMap();

    private static EnumMap<AMEProcessableMaterialType, EnumMap<AMEProcessingItemStates, ItemRegistryObject<?>>> createMaterialProcessItemMap() {
        EnumMap<AMEProcessableMaterialType, EnumMap<AMEProcessingItemStates, ItemRegistryObject<?>>> result = new EnumMap<>(
                AMEProcessableMaterialType.class);
        for (AMEProcessableMaterialType type : AMEProcessableMaterialType.values()) {
            EnumMap<AMEProcessingItemStates, ItemRegistryObject<?>> map = new EnumMap<>(AMEProcessingItemStates.class);
            map.put(AMEProcessingItemStates.SHINING_CRYSTAL,
                    ITEMS.register("shining_" + type.name + "_crystal", GlintItem::new));
            map.put(AMEProcessingItemStates.SHINING_SHARD,
                    ITEMS.register("shining_" + type.name + "_shard", GlintItem::new));
            map.put(AMEProcessingItemStates.SHINING_DUST, ITEMS.register(type == AMEProcessableMaterialType.REDSTONE
                    ? "shining_redstone"
                    : "shining_" + type.name + "_dust",
                    GlintItem::new));
            map.put(AMEProcessingItemStates.SHINING_CLUMP_GEM,
                    ITEMS.register(type.isMetal || type == AMEProcessableMaterialType.REDSTONE
                            ? "shining_" + type.name + "_clump"
                            : "shining_" + type.name,
                            GlintItem::new));
            result.put(type, map);
        }
        return result;
    }

    public static final EnumMap<AMEProcessableMaterialType, ItemRegistryObject<GlintItem>> STARLIGHTS = ((Supplier<EnumMap<AMEProcessableMaterialType, ItemRegistryObject<GlintItem>>>) (() -> {
        EnumMap<AMEProcessableMaterialType, ItemRegistryObject<GlintItem>> result = new EnumMap<>(
                AMEProcessableMaterialType.class);
        for (AMEProcessableMaterialType type : AMEProcessableMaterialType.values()) {
            result.put(type, ITEMS.register(type.name + "_starlight", GlintItem::new));
        }
        return result;
    })).get();

    public static final EnumMap<OreType, ItemRegistryObject<GlintItem>> OLD_STARLIGHTS = ((Supplier<EnumMap<OreType, ItemRegistryObject<GlintItem>>>) (() -> {
        EnumMap<OreType, ItemRegistryObject<GlintItem>> result = new EnumMap<>(OreType.class);
        for (OreType oreType : OreType.values()) {
            if (oreType == OreType.NETHERITE) {
                continue;
            }
            result.put(oreType, ITEMS.register("starlight_" + oreType.type, GlintItem::new));
        }
        return result;
    })).get();

    public static final EnumMap<OreType, EnumMap<IntermediateState, ItemRegistryObject<Item>>> GEM_INTERMEDIATE_ITEMS = ((Supplier<EnumMap<OreType, EnumMap<IntermediateState, ItemRegistryObject<Item>>>>) (() -> {
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
    })).get();

    public static final EnumMap<OreType, EnumMap<IntermediateState, ItemRegistryObject<Item>>> COMPRESSED_PROCESSING_ITEMS = ((Supplier<EnumMap<OreType, EnumMap<IntermediateState, ItemRegistryObject<Item>>>>) () -> {
        EnumMap<OreType, EnumMap<IntermediateState, ItemRegistryObject<Item>>> result = new EnumMap<>(OreType.class);
        for (OreType type : OreType.values()) {
            EnumMap<IntermediateState, ItemRegistryObject<Item>> processings = new EnumMap<>(IntermediateState.class);
            for (IntermediateState state : IntermediateState.values()) {
                String process = type.hasMekprocessing
                        ? state == IntermediateState.RAW
                                ? "dust"
                                : state == IntermediateState.CRUSHED
                                        ? "dirty_dust"
                                        : state.state
                        : state.state;
                processings.put(state, ITEMS.register(process + "_compressed_" + type.type));
            }
            result.put(type, processings);
        }
        return result;
    }).get();

    public static final EnumMap<OreType, ItemRegistryObject<?>[]> COMPRESSED_INGOTS_GEMS = ((Supplier<EnumMap<OreType, ItemRegistryObject<?>[]>>) () -> {
        EnumMap<OreType, ItemRegistryObject<?>[]> result = new EnumMap<>(OreType.class);
        for (OreType type : OreType.values()) {
            ItemRegistryObject<?>[] items = new ItemRegistryObject[20];
            for (int i = 0; i < 20; i++) {
                items[i] = ITEMS.register((i + 1) + "x_compressed_" + type.type);
            }
            result.put(type, items);
        }
        return result;
    }).get();

    private static ItemRegistryObject<ItemUpgrade> registerUpgrade(Upgrade type) {
        return ITEMS.register(type.getRawName() + "_upgrade", properties -> new ItemUpgrade(type, properties));
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
}
