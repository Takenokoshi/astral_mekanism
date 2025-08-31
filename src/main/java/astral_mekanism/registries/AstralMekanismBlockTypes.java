package astral_mekanism.registries;

import astral_mekanism.AstralMekanismConfig;
import astral_mekanism.AstralMekanismLang;
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
import astral_mekanism.extension.AstralMachine.AstralMachineBuilder;
import mekanism.api.math.FloatingLong;
import mekanism.common.block.attribute.AttributeStateFacing;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.BlockTypeTile.BlockTileBuilder;

public class AstralMekanismBlockTypes {
	private AstralMekanismBlockTypes() {
	}

	public static final BlockTypeTile<BlockEntityUniversalStorage> Universal_storage = BlockTileBuilder
			.createBlock(() -> AstralMekanismTileEntityTypes.Universal_storage,
					AstralMekanismLang.DESCRIPTION_UNIVERSAL_STORAGE)
			.withGui(() -> AstralMekanismContainers.Universal_storage)
			.with(new AttributeStateFacing())
			.build();

	public static final BlockTypeTile<BlockEntityWaterSupplyDevice> Water_supply_device = BlockTileBuilder
			.createBlock(() -> AstralMekanismTileEntityTypes.Water_supply_device,
					AstralMekanismLang.DESCRIPTION_WATER_SUPPLY_DEVICE)
			.withGui(() -> AstralMekanismContainers.Water_supply_device)
			.with(new AttributeStateFacing())
			.build();

	public static final BlockTypeTile<BlockEntityCobblestoneSupplyDevice> Cobblestone_supply_device = BlockTileBuilder
			.createBlock(() -> AstralMekanismTileEntityTypes.Cobblestone_supply_device,
					AstralMekanismLang.DESCRIPTION_COBBLESTONE_SUPPLY_DEVICE)
			.withGui(() -> AstralMekanismContainers.Cobblestone_supply_device)
			.with(new AttributeStateFacing())
			.build();

	public static final BlockTypeTile<BlockEntityGasBurningGeneratorReformed> Gas_BGR = BlockTileBuilder
			.createBlock(() -> AstralMekanismTileEntityTypes.Gas_BGR, AstralMekanismLang.DESCRIPTION_GAS_BGR)
			.withGui(() -> AstralMekanismContainers.Gas_BGR)
			.with(new AttributeStateFacing())
			.build();

	public static final BlockTypeTile<BlockEntityGlowstoneNeutronActivator> Glowstone_neutron_activator = BlockTileBuilder
			.createBlock(() -> AstralMekanismTileEntityTypes.Glowstone_neutron_activator,
					AstralMekanismLang.DESCRIPTION_GLOWSTONE_NEUTRON_ACTIVATOR)
			.withGui(() -> AstralMekanismContainers.Glowstone_neutron_activator)
			.with(new AttributeStateFacing())
			.build();

	public static final AstralMachine<BlockEntityGreenHouse> Greenhouse = AstralMachineBuilder
			.createMachine(() -> AstralMekanismTileEntityTypes.Greenhouse, AstralMekanismLang.DESCRIPTION_GREENHOUSE)
			.withEnergyConfig(() -> FloatingLong.create(1000 * AstralMekanismConfig.energyRate),
					() -> FloatingLong.create(10000000))
			.withGui(() -> AstralMekanismContainers.Greenhouse)
			.build();

	public static final BlockTypeTile<BlockEntityMelter> Melter = BlockTileBuilder
			.createBlock(() -> AstralMekanismTileEntityTypes.Melter, AstralMekanismLang.DESCRIPTION_MELTER)
			.withGui(() -> AstralMekanismContainers.Melter)
			.with(new AttributeStateFacing())
			.build();

	public static final BlockTypeTile<BlockEntityCompactTEP> CompactTEP = BlockTileBuilder
			.createBlock(() -> AstralMekanismTileEntityTypes.CompactTEP, AstralMekanismLang.DESCRIPTION_COMPACT_TEP)
			.withGui(() -> AstralMekanismContainers.CompactTEP)
			.with(new AttributeStateFacing())
			.build();

	public static final AstralMachine<BlockEntityFluidInfuser> FLUID_INFUSER = AstralMachineBuilder
			.createMachine(() -> AstralMekanismTileEntityTypes.FLUID_INFUSER,
					AstralMekanismLang.DESCRIPTION_FLUID_INFUSER)
			.withEnergyConfig(() -> FloatingLong.create(1000 * AstralMekanismConfig.energyRate),
					() -> FloatingLong.create(10000000))
			.withGui(() -> AstralMekanismContainers.FLUID_INFUSER)
			.build();
}
