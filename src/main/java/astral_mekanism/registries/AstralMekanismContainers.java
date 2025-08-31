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
import astral_mekanism.blocks.universal_storage.ContainerUniversalStorage;
import astral_mekanism.blocks.water_supply_device.BlockEntityWaterSupplyDevice;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;

public class AstralMekanismContainers {
	public AstralMekanismContainers() {
	};

	public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(
			AstralMekanismID.MODID);

	public static final ContainerTypeRegistryObject<ContainerUniversalStorage> Universal_storage = CONTAINER_TYPES
			.register(AstralMekanismBlocks.Universal_storage, BlockEntityUniversalStorage.class,
					(windowId, inv, tile) -> new ContainerUniversalStorage(windowId, inv, tile));
	public static final ContainerTypeRegistryObject<MekanismTileContainer<BlockEntityWaterSupplyDevice>> Water_supply_device = CONTAINER_TYPES
			.register(AstralMekanismBlocks.Water_supply_device, BlockEntityWaterSupplyDevice.class);
	public static final ContainerTypeRegistryObject<MekanismTileContainer<BlockEntityCobblestoneSupplyDevice>> Cobblestone_supply_device = CONTAINER_TYPES
			.register(AstralMekanismBlocks.Cobblestone_supply_device, BlockEntityCobblestoneSupplyDevice.class);
	public static final ContainerTypeRegistryObject<MekanismTileContainer<BlockEntityGasBurningGeneratorReformed>> Gas_BGR = CONTAINER_TYPES
			.register(AstralMekanismBlocks.Gas_BGR, BlockEntityGasBurningGeneratorReformed.class);
	public static final ContainerTypeRegistryObject<MekanismTileContainer<BlockEntityGlowstoneNeutronActivator>> Glowstone_neutron_activator = CONTAINER_TYPES
			.register(AstralMekanismBlocks.Glowstone_neutron_activator,
					BlockEntityGlowstoneNeutronActivator.class);
	public static final ContainerTypeRegistryObject<MekanismTileContainer<BlockEntityGreenHouse>> Greenhouse = CONTAINER_TYPES
			.register(AstralMekanismBlocks.Greenhouse, BlockEntityGreenHouse.class);
	public static final ContainerTypeRegistryObject<MekanismTileContainer<BlockEntityMelter>> Melter = CONTAINER_TYPES
			.register(AstralMekanismBlocks.Melter, BlockEntityMelter.class);
	public static final ContainerTypeRegistryObject<MekanismTileContainer<BlockEntityCompactTEP>> CompactTEP = CONTAINER_TYPES
			.register(AstralMekanismBlocks.CompactTEP, BlockEntityCompactTEP.class);
	public static final ContainerTypeRegistryObject<MekanismTileContainer<BlockEntityFluidInfuser>> FLUID_INFUSER = CONTAINER_TYPES
			.register(AstralMekanismBlocks.FLUID_INFUSER, BlockEntityFluidInfuser.class);
}
