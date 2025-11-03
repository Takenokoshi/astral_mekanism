package astral_mekanism.registries;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.registration.RegistrationInterfaces.BlockEntityConstructor;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.transmitter.TileEntityTransmitter;
import mekanism.common.tile.transmitter.TileEntityUniversalCable;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;

public class AstralMekanismTileEntityTypes {
    private AstralMekanismTileEntityTypes() {
    }

    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(
            AstralMekanismID.MODID);

    private static <MACHINE extends TileEntityMekanism, BLOCKTYPE extends BlockTypeTile<MACHINE>, BLOCK extends BlockTileModel<MACHINE, BLOCKTYPE>> TileEntityTypeRegistryObject<MACHINE> reg(
            BlockRegistryObject<BLOCK, ? extends ItemBlockMachine> blockRegistryObject,
            BlockEntitySupplier<MACHINE> factory) {
        return TILE_ENTITY_TYPES.register(blockRegistryObject, factory, MACHINE::tickServer, MACHINE::tickClient);
    }

    private static <MACHINE extends TileEntityMekanism, BLOCKTYPE extends BlockTypeTile<MACHINE>, BLOCK extends BlockTileModel<MACHINE, BLOCKTYPE>> TileEntityTypeRegistryObject<MACHINE> reg(
            BlockRegistryObject<BLOCK, ? extends ItemBlockMachine> blockRegistryObject,
            BlockEntityConstructor<MACHINE, BLOCKTYPE, BLOCK> constructor) {
        return TILE_ENTITY_TYPES.register(blockRegistryObject,
                (pos, state) -> constructor.create(blockRegistryObject, pos, state), MACHINE::tickServer,
                MACHINE::tickClient);
    }

    public static final TileEntityTypeRegistryObject<TileEntityUniversalCable> ASTRAL_UNIVERSAL_CABLE = TILE_ENTITY_TYPES
            .builder(AstralMekanismBlocks.ASTRAL_UNIVERSAL_CABLE,
                    (p, s) -> new TileEntityUniversalCable(AstralMekanismBlocks.ASTRAL_UNIVERSAL_CABLE, p, s))
            .serverTicker(TileEntityTransmitter::tickServer).build();

}
