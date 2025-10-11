package astral_mekanism.registries;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.block.blockentity.astralmachine.advanced.BEAstralChemicalInjectionChamber;
import astral_mekanism.block.blockentity.astralmachine.advanced.BEAstralOsmiumCompressor;
import astral_mekanism.block.blockentity.astralmachine.advanced.BEAstralPurificationChamber;
import astral_mekanism.block.blockentity.astralmachine.electric.BEAstralCrusher;
import astral_mekanism.block.blockentity.astralmachine.electric.BEAstralEnergizedSmelter;
import astral_mekanism.block.blockentity.astralmachine.electric.BEAstralEnrichmentChamber;
import astral_mekanism.block.blockentity.astralmachine.electric.BEAstralMekanicalCharger;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;

public class AstralMekanismContainers {
	public AstralMekanismContainers() {
	};

	public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(
			AstralMekanismID.MODID);

	public static final ContainerTypeRegistryObject<MekanismTileContainer<BEAstralCrusher>> ASTRAL_CRUSHER = CONTAINER_TYPES
			.register(AstralMekanismBlocks.ASTRAL_CRUSHER, BEAstralCrusher.class);
	public static final ContainerTypeRegistryObject<MekanismTileContainer<BEAstralEnergizedSmelter>> ASTRAL_ENERGIZED_SMELTER = CONTAINER_TYPES
			.register(AstralMekanismBlocks.ASTRAL_ENERGIZED_SMELTER, BEAstralEnergizedSmelter.class);
	public static final ContainerTypeRegistryObject<MekanismTileContainer<BEAstralEnrichmentChamber>> ASTRAL_ENRICHMENT_CHAMBER = CONTAINER_TYPES
			.register(AstralMekanismBlocks.ASTRAL_ENRICHMENT_CHAMBER, BEAstralEnrichmentChamber.class);
	public static final ContainerTypeRegistryObject<MekanismTileContainer<BEAstralMekanicalCharger>> ASTRAL_MEKANICAL_CHARGER = CONTAINER_TYPES
			.register(AstralMekanismBlocks.ASTRAL_MEKANICAL_CHARGER, BEAstralMekanicalCharger.class);
	public static final ContainerTypeRegistryObject<MekanismTileContainer<BEAstralChemicalInjectionChamber>> ASTRAL_CHEMICAL_INJECTION_CHAMBER = CONTAINER_TYPES
			.register(AstralMekanismBlocks.ASTRAL_CHEMICAL_INJECTION_CHAMBER,
					BEAstralChemicalInjectionChamber.class);
	public static final ContainerTypeRegistryObject<MekanismTileContainer<BEAstralOsmiumCompressor>> ASTRAL_OSMIUM_COMPRESSOR = CONTAINER_TYPES
			.register(AstralMekanismBlocks.ASTRAL_OSMIUM_COMPRESSOR, BEAstralOsmiumCompressor.class);
	public static final ContainerTypeRegistryObject<MekanismTileContainer<BEAstralPurificationChamber>> ASTRAL_PURIFICATION_CHAMBER = CONTAINER_TYPES
			.register(AstralMekanismBlocks.ASTRAL_PURIFICATION_CHAMBER, BEAstralPurificationChamber.class);
}
