package astral_mekanism.registries;

import java.util.EnumMap;

import astral_mekanism.AMEConstants;
import astral_mekanism.AMELang;
import astral_mekanism.block.block.BlockAstralUniversalcable;
import astral_mekanism.block.block.BlockMekanicalLight;
import astral_mekanism.block.block.BlockSimpleDiscription;
import astral_mekanism.block.blockitem.GlintBlockItem;
import astral_mekanism.enumexpansion.AMCableTier;
import astral_mekanism.registryenum.AMEProcessableMaterialType;
import mekanism.api.text.EnumColor;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.item.block.transmitter.ItemBlockUniversalCable;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class AMEBlocks {
    private AMEBlocks() {
    }

    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(AMEConstants.MODID);

    public static final BlockRegistryObject<BlockSimpleDiscription, ItemBlockTooltip<BlockSimpleDiscription>> ASTRAL_DIAMOND_BLOCK = BLOCKS
            .register(
                    "astral_diamond_block", () -> new BlockSimpleDiscription(
                            BlockBehaviour.Properties.of()
                                    .strength(1.5f, 18000000.0f)
                                    .sound(SoundType.STONE)
                                    .mapColor(MapColor.STONE),
                            AMELang.DESCRIPTION_ASTRAL_DIAMOND_BLOCK),
                    ItemBlockTooltip::new);
    public static final BlockRegistryObject<Block, BlockItem> UTILITY_BLOCK = BLOCKS.register("utility_block",
            BlockBehaviour.Properties.of()
                    .strength(1.5f, 6.0f)
                    .sound(SoundType.STONE)
                    .mapColor(MapColor.STONE));

    public static final BlockRegistryObject<BlockSimpleDiscription, ItemBlockTooltip<BlockSimpleDiscription>> AMETHYST_ORE = BLOCKS
            .register("amethyst_ore",
                    () -> new BlockSimpleDiscription(
                            BlockBehaviour.Properties.of()
                                    .strength(1.5f, 18000000.0f)
                                    .sound(SoundType.AMETHYST)
                                    .mapColor(MapColor.STONE),
                            AMELang.DESCRIPTION_AMETHYST_ORE),
                    ItemBlockTooltip::new);

    public static final BlockRegistryObject<BlockSimpleDiscription, ItemBlockTooltip<BlockSimpleDiscription>> CERTUS_QUARTS_ORE = BLOCKS
            .register("certus_quartz_ore",
                    () -> new BlockSimpleDiscription(
                            BlockBehaviour.Properties.of()
                                    .strength(1.5f, 18000000.0f)
                                    .sound(SoundType.STONE)
                                    .mapColor(MapColor.STONE),
                            AMELang.DESCRIPTION_CERTUS_QUARTZ_ORE),
                    ItemBlockTooltip::new);

    public static final BlockRegistryObject<BlockSimpleDiscription, ItemBlockTooltip<BlockSimpleDiscription>> NETHERITE_ORE = BLOCKS
            .register("netherite_ore",
                    () -> new BlockSimpleDiscription(
                            BlockBehaviour.Properties.of()
                                    .strength(1.5f, 18000000.0f)
                                    .sound(SoundType.ANCIENT_DEBRIS)
                                    .mapColor(MapColor.NETHER),
                            AMELang.DESCRIPTION_NETHERITE_ORE),
                    ItemBlockTooltip::new);

    public static final BlockRegistryObject<BlockMekanicalLight, BlockItem> MEKANICAL_LIGHT = BLOCKS
            .registerDefaultProperties("mekanical_light", BlockMekanicalLight::create, BlockItem::new);

    public static final BlockRegistryObject<BlockAstralUniversalcable, ItemBlockUniversalCable> ASTRAL_UNIVERSAL_CABLE = BLOCKS
            .register("astral_universal_cable", () -> new BlockAstralUniversalcable(AMCableTier.ASTRAL),
                    ItemBlockUniversalCable::new);

    private static EnumMap<AMEProcessableMaterialType, BlockRegistryObject<?, ?>> createOres(String additionalName,
            EnumColor color) {
        EnumMap<AMEProcessableMaterialType, BlockRegistryObject<?, ?>> result = new EnumMap<>(
                AMEProcessableMaterialType.class);
        for (AMEProcessableMaterialType type : AMEProcessableMaterialType.values()) {
            result.put(type,
                    BLOCKS.register(additionalName + "_" + type.name + "_ore",
                            () -> new Block(BlockBehaviour.Properties.of()),
                            GlintBlockItem.factory(color)));
        }
        return result;
    }

    public static final EnumMap<AMEProcessableMaterialType, BlockRegistryObject<?, ?>> RECONSTRUCTED_ORE = createOres(
            "reconstructed", EnumColor.PURPLE);
    public static final EnumMap<AMEProcessableMaterialType, BlockRegistryObject<?, ?>> ENRICHED_ORE = createOres(
            "enriched", EnumColor.ORANGE);
    public static final EnumMap<AMEProcessableMaterialType, BlockRegistryObject<?, ?>> SPARKLING_ORE = createOres(
            "sparkling", EnumColor.BRIGHT_GREEN);
    public static final EnumMap<AMEProcessableMaterialType, BlockRegistryObject<?, ?>> COMPRESSED_ORE = createOres(
            "compressed", EnumColor.WHITE);
}
