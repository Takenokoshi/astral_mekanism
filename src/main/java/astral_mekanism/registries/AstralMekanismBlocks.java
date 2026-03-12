package astral_mekanism.registries;

import java.util.EnumMap;
import java.util.function.Supplier;

import appeng.block.networking.EnergyCellBlock;
import appeng.block.networking.EnergyCellBlockItem;
import astral_mekanism.AstralMekanismID;
import astral_mekanism.AstralMekanismLang;
import astral_mekanism.AstralMekanismTier;
import astral_mekanism.block.block.BlockAstralUniversalcable;
import astral_mekanism.block.block.BlockMekanicalLight;
import astral_mekanism.block.block.BlockSimpleDiscription;
import astral_mekanism.block.block.TieredEnergyCellBlock;
import astral_mekanism.enumexpansion.AMCableTier;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.item.block.transmitter.ItemBlockUniversalCable;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class AstralMekanismBlocks {
    private AstralMekanismBlocks() {
    }

    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(AstralMekanismID.MODID);

    public static final BlockRegistryObject<BlockSimpleDiscription, ItemBlockTooltip<BlockSimpleDiscription>> ASTRAL_DIAMOND_BLOCK = BLOCKS
            .register(
                    "astral_diamond_block", () -> new BlockSimpleDiscription(
                            BlockBehaviour.Properties.of()
                                    .strength(1.5f, 18000000.0f)
                                    .sound(SoundType.STONE)
                                    .mapColor(MapColor.STONE),
                            AstralMekanismLang.DESCRIPTION_ASTRAL_DIAMOND_BLOCK),
                    ItemBlockTooltip::new);
    public static final BlockRegistryObject<Block, BlockItem> Utility_block = BLOCKS.register("utility_block",
            BlockBehaviour.Properties.of()
                    .strength(1.5f, 6.0f)
                    .sound(SoundType.STONE)
                    .mapColor(MapColor.STONE));

    public static final EnumMap<AstralMekanismTier, BlockRegistryObject<EnergyCellBlock, EnergyCellBlockItem>> ENERGY_CELLS = ((Supplier<EnumMap<AstralMekanismTier, BlockRegistryObject<EnergyCellBlock, EnergyCellBlockItem>>>) () -> {
        EnumMap<AstralMekanismTier, BlockRegistryObject<EnergyCellBlock, EnergyCellBlockItem>> result = new EnumMap<>(
                AstralMekanismTier.class);
        for (AstralMekanismTier tier : AstralMekanismTier.values()) {
            result.put(tier, BLOCKS.register(
                    tier.nameForAE + "_energy_cell",
                    () -> new TieredEnergyCellBlock(tier),
                    block -> new EnergyCellBlockItem(block, new Item.Properties())));
        }
        return result;
    }).get();

    public static final BlockRegistryObject<BlockMekanicalLight, BlockItem> MEKANICAL_LIGHT = BLOCKS
            .registerDefaultProperties("mekanical_light", BlockMekanicalLight::create, BlockItem::new);

    public static final BlockRegistryObject<BlockAstralUniversalcable, ItemBlockUniversalCable> ASTRAL_UNIVERSAL_CABLE = BLOCKS
            .register("astral_universal_cable", () -> new BlockAstralUniversalcable(AMCableTier.ASTRAL),
                    ItemBlockUniversalCable::new);
}
