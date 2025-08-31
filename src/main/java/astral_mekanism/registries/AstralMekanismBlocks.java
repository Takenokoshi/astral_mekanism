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
import astral_mekanism.extension.AstralMachine;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class AstralMekanismBlocks {
	private AstralMekanismBlocks() {
	}

	public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(AstralMekanismID.MODID);
	public static final BlockRegistryObject<Block, BlockItem> Utility_block = BLOCKS.register("utility_block",
			BlockBehaviour.Properties.of()
					.strength(1.5f, 6.0f)
					.sound(SoundType.STONE)
					.mapColor(MapColor.STONE));
	public static final BlockRegistryObject<Block, BlockItem> ASTRAL_DIAMOND_BLOCK = BLOCKS.register("astral_diamond_block",
			BlockBehaviour.Properties.of()
					.strength(1.5f, 18000000.0f)
					.sound(SoundType.STONE)
					.mapColor(MapColor.STONE));

	public static final BlockRegistryObject<BlockTileModel<BlockEntityUniversalStorage, BlockTypeTile<BlockEntityUniversalStorage>>, ItemBlockMachine> Universal_storage = BLOCKS
			.register("universal_storage", () -> new BlockTileModel<>(AstralMekanismBlockTypes.Universal_storage,
					properties -> properties.mapColor(MapColor.COLOR_GRAY)), ItemBlockMachine::new);

	public static final BlockRegistryObject<BlockTileModel<BlockEntityWaterSupplyDevice, BlockTypeTile<BlockEntityWaterSupplyDevice>>, ItemBlockMachine> Water_supply_device = BLOCKS
			.register("water_supply_device", () -> new BlockTileModel<>(AstralMekanismBlockTypes.Water_supply_device,
					properties -> properties.mapColor(MapColor.COLOR_GRAY)), ItemBlockMachine::new);

	public static final BlockRegistryObject<BlockTileModel<BlockEntityCobblestoneSupplyDevice, BlockTypeTile<BlockEntityCobblestoneSupplyDevice>>, ItemBlockMachine> Cobblestone_supply_device = BLOCKS
			.register("cobblestone_supply_device",
					() -> new BlockTileModel<>(AstralMekanismBlockTypes.Cobblestone_supply_device,
							properties -> properties.mapColor(MapColor.COLOR_GRAY)),
					ItemBlockMachine::new);

	public static final BlockRegistryObject<BlockTileModel<BlockEntityGasBurningGeneratorReformed, BlockTypeTile<BlockEntityGasBurningGeneratorReformed>>, ItemBlockMachine> Gas_BGR = BLOCKS
			.register("gas_burning_generator_reformed", () -> new BlockTileModel<>(AstralMekanismBlockTypes.Gas_BGR,
					properties -> properties.mapColor(MapColor.COLOR_GRAY)), ItemBlockMachine::new);

	public static final BlockRegistryObject<BlockTileModel<BlockEntityGlowstoneNeutronActivator, BlockTypeTile<BlockEntityGlowstoneNeutronActivator>>, ItemBlockMachine> Glowstone_neutron_activator = BLOCKS
			.register("glowstone_neutron_activator",
					() -> new BlockTileModel<>(AstralMekanismBlockTypes.Glowstone_neutron_activator,
							properties -> properties.mapColor(MapColor.COLOR_GRAY)),
					ItemBlockMachine::new);

	public static final BlockRegistryObject<BlockTileModel<BlockEntityGreenHouse, AstralMachine<BlockEntityGreenHouse>>, ItemBlockMachine> Greenhouse = BLOCKS
			.register("greenhouse",
					() -> new BlockTileModel<>(AstralMekanismBlockTypes.Greenhouse,
							properties -> properties.mapColor(MapColor.COLOR_GRAY)),
					ItemBlockMachine::new);

	public static final BlockRegistryObject<BlockTileModel<BlockEntityMelter, BlockTypeTile<BlockEntityMelter>>, ItemBlockMachine> Melter = BLOCKS
			.register("melter",
					() -> new BlockTileModel<>(AstralMekanismBlockTypes.Melter,
							properties -> properties.mapColor(MapColor.COLOR_GRAY)),
					ItemBlockMachine::new);

	public static final BlockRegistryObject<BlockTileModel<BlockEntityCompactTEP, BlockTypeTile<BlockEntityCompactTEP>>, ItemBlockMachine> CompactTEP = BLOCKS
			.register("compact_tep",
					() -> new BlockTileModel<>(AstralMekanismBlockTypes.CompactTEP,
							properties -> properties.mapColor(MapColor.COLOR_GRAY)),
					ItemBlockMachine::new);

	public static final BlockRegistryObject<BlockTileModel<BlockEntityFluidInfuser, AstralMachine<BlockEntityFluidInfuser>>, ItemBlockMachine> FLUID_INFUSER = BLOCKS
			.register("fluid_infuser",
					() -> new BlockTileModel<>(AstralMekanismBlockTypes.FLUID_INFUSER,
							properties -> properties.mapColor(MapColor.COLOR_GRAY)),
					ItemBlockMachine::new);

}
