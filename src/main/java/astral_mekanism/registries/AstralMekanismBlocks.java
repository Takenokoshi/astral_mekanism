package astral_mekanism.registries;

import java.util.function.Function;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.block.block.BlockAstralUniversalcable;
import astral_mekanism.block.block.BlockMekanicalLight;
import astral_mekanism.block.blockentity.astralmachine.advanced.BEAstralChemicalInjectionChamber;
import astral_mekanism.block.blockentity.astralmachine.advanced.BEAstralOsmiumCompressor;
import astral_mekanism.block.blockentity.astralmachine.advanced.BEAstralPurificationChamber;
import astral_mekanism.enumexpansion.AMCableTier;
import astral_mekanism.registration.BlockTypeMachine;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.item.block.transmitter.ItemBlockUniversalCable;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class AstralMekanismBlocks {
    private AstralMekanismBlocks() {
    }

    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(AstralMekanismID.MODID);

    public static final BlockRegistryObject<Block, BlockItem> ASTRAL_DIAMOND_BLOCK = BLOCKS.register(
            "astral_diamond_block",
            BlockBehaviour.Properties.of()
                    .strength(1.5f, 18000000.0f)
                    .sound(SoundType.STONE)
                    .mapColor(MapColor.STONE));
    public static final BlockRegistryObject<Block, BlockItem> Utility_block = BLOCKS.register("utility_block",
            BlockBehaviour.Properties.of()
                    .strength(1.5f, 6.0f)
                    .sound(SoundType.STONE)
                    .mapColor(MapColor.STONE));

    public static final BlockRegistryObject<BlockMekanicalLight, BlockItem> MEKANICAL_LIGHT = BLOCKS
            .registerDefaultProperties("mekanical_light", BlockMekanicalLight::create, BlockItem::new);

    public static final BlockRegistryObject<BlockAstralUniversalcable, ItemBlockUniversalCable> ASTRAL_UNIVERSAL_CABLE = BLOCKS
            .register("astral_universal_cable", () -> new BlockAstralUniversalcable(AMCableTier.ASTRAL),
                    ItemBlockUniversalCable::new);

    private static <MACHINE extends TileEntityMekanism, BLOCKTYPE extends BlockTypeTile<MACHINE>, ITEM extends ItemBlockMachine> BlockRegistryObject<BlockTileModel<MACHINE, BLOCKTYPE>, ITEM> regMachineAndItem(
            String name, BLOCKTYPE blockType, Function<BlockTileModel<MACHINE, BLOCKTYPE>, ITEM> itemCreator) {
        return regMachineAndItem(name, blockType, bt -> new BlockTileModel<>(bt, p -> p
                .strength(1.5f, 6.0f).mapColor(MapColor.COLOR_GRAY)), itemCreator);
    }

    private static <MACHINE extends TileEntityMekanism, BLOCKTYPE extends BlockTypeTile<MACHINE>, ITEM extends ItemBlockMachine, BLOCK extends BlockTileModel<MACHINE, BLOCKTYPE>> BlockRegistryObject<BLOCK, ITEM> regMachineAndItem(
            String name, BLOCKTYPE blockType, Function<BLOCKTYPE, BLOCK> blockCreater,
            Function<BLOCK, ITEM> itemCreator) {
        return BLOCKS.register(name, () -> blockCreater.apply(blockType), itemCreator);
    }

    private static <MACHINE extends TileEntityMekanism, BLOCKTYPE extends BlockTypeTile<MACHINE>> BlockRegistryObject<BlockTileModel<MACHINE, BLOCKTYPE>, ItemBlockMachine> regMachine(
            String name, BLOCKTYPE blockType) {
        return regMachineAndItem(name, blockType, ItemBlockMachine::new);
    }

    public static final BlockRegistryObject<BlockTileModel<BEAstralChemicalInjectionChamber, BlockTypeMachine<BEAstralChemicalInjectionChamber>>, ItemBlockMachine> ASTRAL_CHEMICAL_INJECTION_CHAMBER = regMachine(
            "astral_chemical_injection_chamber", AstralMekanismBlockTypes.ASTRAL_CHEMICAL_INJECTION_CHAMBER);

    public static final BlockRegistryObject<BlockTileModel<BEAstralOsmiumCompressor, BlockTypeMachine<BEAstralOsmiumCompressor>>, ItemBlockMachine> ASTRAL_OSMIUM_COMPRESSOR = regMachine(
            "astral_osmium_compressor", AstralMekanismBlockTypes.ASTRAL_OSMIUM_COMPRESSOR);

    public static final BlockRegistryObject<BlockTileModel<BEAstralPurificationChamber, BlockTypeMachine<BEAstralPurificationChamber>>, ItemBlockMachine> ASTRAL_PURIFICATION_CHAMBER = regMachine(
            "astral_purification_chamber", AstralMekanismBlockTypes.ASTRAL_PURIFICATION_CHAMBER);

}
