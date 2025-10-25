package astral_mekanism.registries;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.block.blockentity.astralmachine.advanced.BEAstralChemicalInjectionChamber;
import astral_mekanism.block.blockentity.astralmachine.advanced.BEAstralOsmiumCompressor;
import astral_mekanism.block.blockentity.astralmachine.advanced.BEAstralPurificationChamber;
import astral_mekanism.block.blockentity.astralmachine.electric.BEAstralCrusher;
import astral_mekanism.block.blockentity.astralmachine.electric.BEAstralEnergizedSmelter;
import astral_mekanism.block.blockentity.astralmachine.electric.BEAstralEnrichmentChamber;
import astral_mekanism.block.blockentity.astralmachine.electric.BEAstralMekanicalCharger;
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

    public static final TileEntityTypeRegistryObject<BEAstralCrusher> ASTRAL_CRUSHER = reg(
            AstralMekanismBlocks.ASTRAL_CRUSHER, BEAstralCrusher::new);

    public static final TileEntityTypeRegistryObject<BEAstralEnrichmentChamber> ASTRAL_ENRICHMENT_CHAMBER = reg(
            AstralMekanismBlocks.ASTRAL_ENRICHMENT_CHAMBER, BEAstralEnrichmentChamber::new);

    public static final TileEntityTypeRegistryObject<BEAstralEnergizedSmelter> ASTRAL_ENERGIZED_SMELTER = reg(
            AstralMekanismBlocks.ASTRAL_ENERGIZED_SMELTER, BEAstralEnergizedSmelter::new);

    public static final TileEntityTypeRegistryObject<BEAstralMekanicalCharger> ASTRAL_MEKANICAL_CHARGER = reg(
            AstralMekanismBlocks.ASTRAL_MEKANICAL_CHARGER, BEAstralMekanicalCharger::new);

    public static final TileEntityTypeRegistryObject<BEAstralChemicalInjectionChamber> ASTRAL_CHEMICAL_INJECTION_CHAMBER = reg(
            AstralMekanismBlocks.ASTRAL_CHEMICAL_INJECTION_CHAMBER, BEAstralChemicalInjectionChamber::new);

    public static final TileEntityTypeRegistryObject<BEAstralOsmiumCompressor> ASTRAL_OSMIUM_COMPRESSOR = reg(
            AstralMekanismBlocks.ASTRAL_OSMIUM_COMPRESSOR, BEAstralOsmiumCompressor::new);

    public static final TileEntityTypeRegistryObject<BEAstralPurificationChamber> ASTRAL_PURIFICATION_CHAMBER = reg(
            AstralMekanismBlocks.ASTRAL_PURIFICATION_CHAMBER, BEAstralPurificationChamber::new);

}
