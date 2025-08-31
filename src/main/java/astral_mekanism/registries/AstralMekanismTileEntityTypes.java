package astral_mekanism.registries;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.blocks.cobblestone_supply_device.BlockEntityCobblestoneSupplyDevice;
import astral_mekanism.blocks.compact_tep.BlockEntityCompactTEP;
import astral_mekanism.blocks.fluid_infuser.BlockEntityFluidInfuser;
import astral_mekanism.blocks.gas_burning_generator_reformed.BlockEntityGasBurningGeneratorReformed;
import astral_mekanism.blocks.glowstone_neutron_activator.BlockEntityGlowstoneNeutronActivator;
import astral_mekanism.blocks.green_house.BlockEntityGreenHouse;
import astral_mekanism.blocks.melter.BlockEntityMelter;
import astral_mekanism.blocks.universal_storage.BlockEntityUniversalStorage;
import astral_mekanism.blocks.water_supply_device.BlockEntityWaterSupplyDevice;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;

public class AstralMekanismTileEntityTypes {
	private AstralMekanismTileEntityTypes() {
	}

	public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(
			AstralMekanismID.MODID);

	public static final TileEntityTypeRegistryObject<BlockEntityUniversalStorage> Universal_storage = TILE_ENTITY_TYPES
			.register(AstralMekanismBlocks.Universal_storage,
					(pos, state) -> new BlockEntityUniversalStorage(
							AstralMekanismBlocks.Universal_storage,
							pos,
							state),
					TileEntityConfigurableMachine::tickServer,
					TileEntityConfigurableMachine::tickClient);

	public static final TileEntityTypeRegistryObject<BlockEntityWaterSupplyDevice> Water_supply_device = TILE_ENTITY_TYPES
			.register(
					AstralMekanismBlocks.Water_supply_device,
					(pos, state) -> new BlockEntityWaterSupplyDevice(
							AstralMekanismBlocks.Water_supply_device, pos, state),
					BlockEntityWaterSupplyDevice::tickServer,
					BlockEntityWaterSupplyDevice::tickClient);

	public static final TileEntityTypeRegistryObject<BlockEntityCobblestoneSupplyDevice> Cobblestone_supply_device = TILE_ENTITY_TYPES
			.register(
					AstralMekanismBlocks.Cobblestone_supply_device,
					(pos, state) -> new BlockEntityCobblestoneSupplyDevice(
							AstralMekanismBlocks.Cobblestone_supply_device,
							pos, state),
					BlockEntityCobblestoneSupplyDevice::tickServer,
					BlockEntityCobblestoneSupplyDevice::tickClient);

	public static final TileEntityTypeRegistryObject<BlockEntityGasBurningGeneratorReformed> Gas_BGR = TILE_ENTITY_TYPES
			.register(
					AstralMekanismBlocks.Gas_BGR,
					(pos, state) -> new BlockEntityGasBurningGeneratorReformed(
							AstralMekanismBlocks.Gas_BGR, pos, state),
					BlockEntityGasBurningGeneratorReformed::tickServer,
					BlockEntityGasBurningGeneratorReformed::tickClient);

	public static final TileEntityTypeRegistryObject<BlockEntityGlowstoneNeutronActivator> Glowstone_neutron_activator = TILE_ENTITY_TYPES
			.register(AstralMekanismBlocks.Glowstone_neutron_activator,
					(pos, state) -> new BlockEntityGlowstoneNeutronActivator(
							AstralMekanismBlocks.Glowstone_neutron_activator, pos,
							state),
					BlockEntityGlowstoneNeutronActivator::tickServer,
					BlockEntityGlowstoneNeutronActivator::tickClient);

	public static final TileEntityTypeRegistryObject<BlockEntityGreenHouse> Greenhouse = TILE_ENTITY_TYPES
			.register(AstralMekanismBlocks.Greenhouse,
					(pos, state) -> new BlockEntityGreenHouse(AstralMekanismBlocks.Greenhouse, pos,
							state),
					BlockEntityGreenHouse::tickServer, BlockEntityGreenHouse::tickClient);

	public static final TileEntityTypeRegistryObject<BlockEntityMelter> Melter = TILE_ENTITY_TYPES
			.register(AstralMekanismBlocks.Melter,
					(pos, state) -> new BlockEntityMelter(AstralMekanismBlocks.Melter, pos, state),
					BlockEntityMelter::tickServer, BlockEntityMelter::tickClient);

	public static final TileEntityTypeRegistryObject<BlockEntityCompactTEP> CompactTEP = TILE_ENTITY_TYPES
			.register(AstralMekanismBlocks.CompactTEP,
					(pos, state) -> new BlockEntityCompactTEP(AstralMekanismBlocks.CompactTEP, pos,
							state),
					BlockEntityCompactTEP::tickServer, BlockEntityCompactTEP::tickClient);

	public static final TileEntityTypeRegistryObject<BlockEntityFluidInfuser> FLUID_INFUSER = TILE_ENTITY_TYPES
			.register(AstralMekanismBlocks.FLUID_INFUSER,
					(pos, state) -> new BlockEntityFluidInfuser(AstralMekanismBlocks.FLUID_INFUSER, pos,
							state),
					BlockEntityFluidInfuser::tickServer, BlockEntityFluidInfuser::tickClient);

}
