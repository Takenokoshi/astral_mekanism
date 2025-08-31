package astral_mekanism.blocks.universal_storage;

import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.heat.BasicHeatCapacitor;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.heat.HeatCapacitorHelper;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.integration.computer.annotation.SyntheticComputerMethod;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableDouble;
import mekanism.common.inventory.container.sync.SyncableEnum;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.inventory.slot.chemical.MergedChemicalInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.api.chemical.merged.MergedChemicalTank;
import mekanism.api.chemical.pigment.IPigmentTank;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.ISlurryTank;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.api.heat.HeatAPI.HeatTransfer;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.tile.TileEntityChemicalTank;
import mekanism.common.tile.TileEntityChemicalTank.GasMode;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.EnergySlotInfo;
import mekanism.common.tile.component.config.slot.FluidSlotInfo;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo.GasSlotInfo;
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo.InfusionSlotInfo;
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo.PigmentSlotInfo;
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo.SlurrySlotInfo;
import mekanism.common.tile.interfaces.IHasGasMode;
import mekanism.common.tile.interfaces.ISustainedData;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.util.ChemicalUtil;
import mekanism.common.util.FluidUtils;
import mekanism.common.util.NBTUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.core.BlockEntityUtils;
import astral_mekanism.extension.DataTypeEx;
import astral_mekanism.registries.AstralMekanismItems;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mekanism.api.Action;
import mekanism.api.IContentsListener;
import mekanism.api.NBTConstants;
import mekanism.api.chemical.ChemicalTankBuilder;

public class BlockEntityUniversalStorage extends TileEntityConfigurableMachine
		implements ISustainedData, IHasGasMode {

	@SyntheticComputerMethod(getter = "getDumpingMode", getterDescription = "Get the current Dumping configuration")
	public GasMode dumping0;
	public GasMode dumping1;

	BasicInventorySlot inventorySlot000;
	BasicInventorySlot inventorySlot001;
	BasicInventorySlot inventorySlot002;
	BasicInventorySlot inventorySlot003;
	BasicInventorySlot inventorySlot004;
	BasicInventorySlot inventorySlot005;
	BasicInventorySlot inventorySlot006;
	BasicInventorySlot inventorySlot007;
	BasicInventorySlot inventorySlot008;
	BasicInventorySlot inventorySlot009;
	BasicInventorySlot inventorySlot010;
	BasicInventorySlot inventorySlot011;
	BasicInventorySlot inventorySlot012;
	BasicInventorySlot inventorySlot013;
	BasicInventorySlot inventorySlot014;
	BasicInventorySlot inventorySlot015;
	BasicInventorySlot inventorySlot016;
	BasicInventorySlot inventorySlot017;
	BasicInventorySlot inventorySlot018;
	BasicInventorySlot inventorySlot019;
	BasicInventorySlot inventorySlot020;
	BasicInventorySlot inventorySlot021;
	BasicInventorySlot inventorySlot022;
	BasicInventorySlot inventorySlot023;
	BasicInventorySlot inventorySlot024;
	BasicInventorySlot inventorySlot025;
	BasicInventorySlot inventorySlot026;
	BasicInventorySlot inventorySlot027;
	BasicInventorySlot inventorySlot028;
	BasicInventorySlot inventorySlot029;
	BasicInventorySlot inventorySlot030;
	BasicInventorySlot inventorySlot031;
	BasicInventorySlot inventorySlot032;
	BasicInventorySlot inventorySlot033;
	BasicInventorySlot inventorySlot034;
	BasicInventorySlot inventorySlot035;

	BasicInventorySlot inventorySlot100;
	BasicInventorySlot inventorySlot101;
	BasicInventorySlot inventorySlot102;
	BasicInventorySlot inventorySlot103;
	BasicInventorySlot inventorySlot104;
	BasicInventorySlot inventorySlot105;
	BasicInventorySlot inventorySlot106;
	BasicInventorySlot inventorySlot107;
	BasicInventorySlot inventorySlot108;
	BasicInventorySlot inventorySlot109;
	BasicInventorySlot inventorySlot110;
	BasicInventorySlot inventorySlot111;
	BasicInventorySlot inventorySlot112;
	BasicInventorySlot inventorySlot113;
	BasicInventorySlot inventorySlot114;
	BasicInventorySlot inventorySlot115;
	BasicInventorySlot inventorySlot116;
	BasicInventorySlot inventorySlot117;
	BasicInventorySlot inventorySlot118;
	BasicInventorySlot inventorySlot119;
	BasicInventorySlot inventorySlot120;
	BasicInventorySlot inventorySlot121;
	BasicInventorySlot inventorySlot122;
	BasicInventorySlot inventorySlot123;
	BasicInventorySlot inventorySlot124;
	BasicInventorySlot inventorySlot125;
	BasicInventorySlot inventorySlot126;
	BasicInventorySlot inventorySlot127;
	BasicInventorySlot inventorySlot128;
	BasicInventorySlot inventorySlot129;
	BasicInventorySlot inventorySlot130;
	BasicInventorySlot inventorySlot131;
	BasicInventorySlot inventorySlot132;
	BasicInventorySlot inventorySlot133;
	BasicInventorySlot inventorySlot134;
	BasicInventorySlot inventorySlot135;

	List<BasicInventorySlot> listInventorySlotsA = List.of(
			inventorySlot000, inventorySlot001, inventorySlot002, inventorySlot003, inventorySlot004, inventorySlot005,
			inventorySlot006, inventorySlot007, inventorySlot008, inventorySlot009, inventorySlot010, inventorySlot011,
			inventorySlot012, inventorySlot013, inventorySlot014, inventorySlot015, inventorySlot016, inventorySlot017,
			inventorySlot018, inventorySlot019, inventorySlot020, inventorySlot021, inventorySlot022, inventorySlot023,
			inventorySlot024, inventorySlot025, inventorySlot026, inventorySlot027, inventorySlot028, inventorySlot029,
			inventorySlot030, inventorySlot031, inventorySlot032, inventorySlot033, inventorySlot034, inventorySlot035);

	List<BasicInventorySlot> listInventorySlotsB = List.of(
			inventorySlot100, inventorySlot101, inventorySlot102, inventorySlot103, inventorySlot104, inventorySlot105,
			inventorySlot106, inventorySlot107, inventorySlot108, inventorySlot109, inventorySlot110, inventorySlot111,
			inventorySlot112, inventorySlot113, inventorySlot114, inventorySlot115, inventorySlot116, inventorySlot117,
			inventorySlot118, inventorySlot119, inventorySlot120, inventorySlot121, inventorySlot122, inventorySlot123,
			inventorySlot124, inventorySlot125, inventorySlot126, inventorySlot127, inventorySlot128, inventorySlot129,
			inventorySlot130, inventorySlot131, inventorySlot132, inventorySlot133, inventorySlot134, inventorySlot135);
	List<IInventorySlot> iInventorySlotsA = listInventorySlotsA.stream()
			.map(s -> s)
			.collect(Collectors.toList());
	List<IInventorySlot> iInventorySlotsB = listInventorySlotsB.stream()
			.map(s -> s)
			.collect(Collectors.toList());

	BasicInventorySlot insertUpgradeSlotA;
	BasicInventorySlot insertUpgradeSlotB;
	BasicInventorySlot dustBoxSlotA;
	BasicInventorySlot dustBoxSlotB;

	BasicEnergyContainer energyContainer;
	BasicFluidTank fluidTank0;
	BasicFluidTank fluidTank1;
	MergedChemicalTank mergedChemicalTank0;
	MergedChemicalTank mergedChemicalTank1;

	EnergyInventorySlot energyInput;
	EnergyInventorySlot energyOutput;
	FluidInventorySlot fluidInput0;
	FluidInventorySlot fluidInput1;
	FluidInventorySlot fluidOutput0;
	FluidInventorySlot fluidOutput1;
	MergedChemicalInventorySlot<MergedChemicalTank> chemicalInput0;
	MergedChemicalInventorySlot<MergedChemicalTank> chemicalInput1;
	MergedChemicalInventorySlot<MergedChemicalTank> chemicalOutput0;
	MergedChemicalInventorySlot<MergedChemicalTank> chemicalOutput1;

	private double lastEnvironmentLoss;
	private double lastTransferLoss;
	BasicHeatCapacitor heatCapacitor;

	public BlockEntityUniversalStorage(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
		super(blockProvider, pos, state);
		this.dumping0 = GasMode.IDLE;
		this.dumping1 = GasMode.IDLE;
		this.configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.ENERGY,
				TransmissionType.FLUID, TransmissionType.GAS, TransmissionType.INFUSION,
				TransmissionType.PIGMENT,
				TransmissionType.SLURRY);
		ConfigInfo itemConfig = this.configComponent.getConfig(TransmissionType.ITEM);
		if (itemConfig != null) {
			itemConfig.addSlotInfo(DataType.OUTPUT_1, new InventorySlotInfo(false, true, iInventorySlotsA));
			itemConfig.addSlotInfo(DataType.OUTPUT_2, new InventorySlotInfo(false, true, iInventorySlotsB));
			itemConfig.addSlotInfo(DataType.INPUT_1, new InventorySlotInfo(true, false, iInventorySlotsA));
			itemConfig.addSlotInfo(DataType.INPUT_2, new InventorySlotInfo(true, false, iInventorySlotsB));
			itemConfig.addSlotInfo(DataTypeEx.INPUT1_OUTPUT1, new InventorySlotInfo(true, false, iInventorySlotsA));
			itemConfig.addSlotInfo(DataTypeEx.INPUT1_OUTPUT2, new InventorySlotInfo(true, false, iInventorySlotsA));
			itemConfig.addSlotInfo(DataTypeEx.INPUT2_OUTPUT1, new InventorySlotInfo(true, false, iInventorySlotsB));
			itemConfig.addSlotInfo(DataTypeEx.INPUT2_OUTPUT2, new InventorySlotInfo(true, false, iInventorySlotsB));
			itemConfig.setEjecting(true);
		}
		ConfigInfo energyConfig = this.configComponent.getConfig(TransmissionType.ENERGY);
		if (energyConfig != null) {
			energyConfig.addSlotInfo(DataType.OUTPUT, new EnergySlotInfo(false, true, energyContainer));
			energyConfig.addSlotInfo(DataType.INPUT, new EnergySlotInfo(true, false, energyContainer));
			energyConfig.addSlotInfo(DataType.INPUT_OUTPUT,
					new EnergySlotInfo(true, true, energyContainer));
			energyConfig.setEjecting(true);
		}
		ConfigInfo fluidConfig = this.configComponent.getConfig(TransmissionType.FLUID);
		if (fluidConfig != null) {
			fluidConfig.addSlotInfo(DataType.OUTPUT_1, new FluidSlotInfo(false, true, fluidTank0));
			fluidConfig.addSlotInfo(DataType.OUTPUT_2, new FluidSlotInfo(false, true, fluidTank1));
			fluidConfig.addSlotInfo(DataType.INPUT_1, new FluidSlotInfo(true, false, fluidTank0));
			fluidConfig.addSlotInfo(DataType.INPUT_2, new FluidSlotInfo(true, false, fluidTank1));
			fluidConfig.addSlotInfo(DataTypeEx.INPUT1_OUTPUT1, new FluidSlotInfo(true, false, fluidTank0));
			fluidConfig.addSlotInfo(DataTypeEx.INPUT1_OUTPUT2, new FluidSlotInfo(true, false, fluidTank0));
			fluidConfig.addSlotInfo(DataTypeEx.INPUT2_OUTPUT1, new FluidSlotInfo(true, false, fluidTank1));
			fluidConfig.addSlotInfo(DataTypeEx.INPUT2_OUTPUT2, new FluidSlotInfo(true, false, fluidTank1));
			fluidConfig.setEjecting(true);
		}
		ConfigInfo gasConfig = this.configComponent.getConfig(TransmissionType.GAS);
		if (gasConfig != null) {
			gasConfig.addSlotInfo(DataType.OUTPUT_1,
					new GasSlotInfo(false, true, mergedChemicalTank0.getGasTank()));
			gasConfig.addSlotInfo(DataType.OUTPUT_2,
					new GasSlotInfo(false, true, mergedChemicalTank1.getGasTank()));
			gasConfig.addSlotInfo(DataType.INPUT_1,
					new GasSlotInfo(true, false, mergedChemicalTank0.getGasTank()));
			gasConfig.addSlotInfo(DataType.INPUT_2,
					new GasSlotInfo(true, false, mergedChemicalTank1.getGasTank()));
			gasConfig.addSlotInfo(DataTypeEx.INPUT1_OUTPUT1,
					new GasSlotInfo(true, false, mergedChemicalTank0.getGasTank()));
			gasConfig.addSlotInfo(DataTypeEx.INPUT1_OUTPUT2,
					new GasSlotInfo(true, false, mergedChemicalTank0.getGasTank()));
			gasConfig.addSlotInfo(DataTypeEx.INPUT2_OUTPUT1,
					new GasSlotInfo(true, false, mergedChemicalTank1.getGasTank()));
			gasConfig.addSlotInfo(DataTypeEx.INPUT2_OUTPUT2,
					new GasSlotInfo(true, false, mergedChemicalTank1.getGasTank()));
			gasConfig.setEjecting(true);
		}
		ConfigInfo infusionConfig = this.configComponent.getConfig(TransmissionType.INFUSION);
		if (infusionConfig != null) {
			infusionConfig.addSlotInfo(DataType.OUTPUT_1,
					new InfusionSlotInfo(false, true, mergedChemicalTank0.getInfusionTank()));
			infusionConfig.addSlotInfo(DataType.OUTPUT_2,
					new InfusionSlotInfo(false, true, mergedChemicalTank1.getInfusionTank()));
			infusionConfig.addSlotInfo(DataType.INPUT_1,
					new InfusionSlotInfo(true, false, mergedChemicalTank0.getInfusionTank()));
			infusionConfig.addSlotInfo(DataType.INPUT_2,
					new InfusionSlotInfo(true, false, mergedChemicalTank1.getInfusionTank()));
			infusionConfig.addSlotInfo(DataTypeEx.INPUT1_OUTPUT1,
					new InfusionSlotInfo(true, false, mergedChemicalTank0.getInfusionTank()));
			infusionConfig.addSlotInfo(DataTypeEx.INPUT1_OUTPUT2,
					new InfusionSlotInfo(true, false, mergedChemicalTank0.getInfusionTank()));
			infusionConfig.addSlotInfo(DataTypeEx.INPUT2_OUTPUT1,
					new InfusionSlotInfo(true, false, mergedChemicalTank1.getInfusionTank()));
			infusionConfig.addSlotInfo(DataTypeEx.INPUT2_OUTPUT2,
					new InfusionSlotInfo(true, false, mergedChemicalTank1.getInfusionTank()));
			infusionConfig.setEjecting(true);
		}
		ConfigInfo pigmentConfig = this.configComponent.getConfig(TransmissionType.PIGMENT);
		if (pigmentConfig != null) {
			pigmentConfig.addSlotInfo(DataType.OUTPUT_1,
					new PigmentSlotInfo(false, true, mergedChemicalTank0.getPigmentTank()));
			pigmentConfig.addSlotInfo(DataType.OUTPUT_2,
					new PigmentSlotInfo(false, true, mergedChemicalTank1.getPigmentTank()));
			pigmentConfig.addSlotInfo(DataType.INPUT_1,
					new PigmentSlotInfo(true, false, mergedChemicalTank0.getPigmentTank()));
			pigmentConfig.addSlotInfo(DataType.INPUT_2,
					new PigmentSlotInfo(true, false, mergedChemicalTank1.getPigmentTank()));
			pigmentConfig.addSlotInfo(DataTypeEx.INPUT1_OUTPUT1,
					new PigmentSlotInfo(true, false, mergedChemicalTank0.getPigmentTank()));
			pigmentConfig.addSlotInfo(DataTypeEx.INPUT1_OUTPUT2,
					new PigmentSlotInfo(true, false, mergedChemicalTank0.getPigmentTank()));
			pigmentConfig.addSlotInfo(DataTypeEx.INPUT2_OUTPUT1,
					new PigmentSlotInfo(true, false, mergedChemicalTank1.getPigmentTank()));
			pigmentConfig.addSlotInfo(DataTypeEx.INPUT2_OUTPUT2,
					new PigmentSlotInfo(true, false, mergedChemicalTank1.getPigmentTank()));
			pigmentConfig.setEjecting(true);
		}
		ConfigInfo slurryConfig = this.configComponent.getConfig(TransmissionType.SLURRY);
		if (slurryConfig != null) {
			slurryConfig.addSlotInfo(DataType.OUTPUT_1,
					new SlurrySlotInfo(false, true, mergedChemicalTank0.getSlurryTank()));
			slurryConfig.addSlotInfo(DataType.OUTPUT_2,
					new SlurrySlotInfo(false, true, mergedChemicalTank1.getSlurryTank()));
			slurryConfig.addSlotInfo(DataType.INPUT_1,
					new SlurrySlotInfo(true, false, mergedChemicalTank0.getSlurryTank()));
			slurryConfig.addSlotInfo(DataType.INPUT_2,
					new SlurrySlotInfo(true, false, mergedChemicalTank1.getSlurryTank()));
			slurryConfig.addSlotInfo(DataTypeEx.INPUT1_OUTPUT1,
					new SlurrySlotInfo(true, false, mergedChemicalTank0.getSlurryTank()));
			slurryConfig.addSlotInfo(DataTypeEx.INPUT1_OUTPUT2,
					new SlurrySlotInfo(true, false, mergedChemicalTank0.getSlurryTank()));
			slurryConfig.addSlotInfo(DataTypeEx.INPUT2_OUTPUT1,
					new SlurrySlotInfo(true, false, mergedChemicalTank1.getSlurryTank()));
			slurryConfig.addSlotInfo(DataTypeEx.INPUT2_OUTPUT2,
					new SlurrySlotInfo(true, false, mergedChemicalTank1.getSlurryTank()));
			slurryConfig.setEjecting(true);
		}
		ejectorComponent = new TileComponentEjector(this, () -> Long.MAX_VALUE, () -> 200000,
				() -> FloatingLong.MAX_VALUE);
		ejectorComponent.setOutputData(this.configComponent, TransmissionType.ITEM, TransmissionType.ENERGY,
				TransmissionType.FLUID, TransmissionType.GAS, TransmissionType.INFUSION,
				TransmissionType.PIGMENT,
				TransmissionType.SLURRY)
				.setCanTankEject((tank) -> {
					if (tank == this.mergedChemicalTank0.getGasTank()) {
						return this.dumping0 != GasMode.DUMPING;
					} else if (tank == this.mergedChemicalTank1.getGasTank()) {
						return this.dumping1 != GasMode.DUMPING;
					} else {
						return true;
					}
				});
	}

	@NotNull
	@Override
	protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
		InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection,
				this::getConfig);
		builder.addSlot(inventorySlot000 = BasicInventorySlot.at(this, 8, 18));
		builder.addSlot(inventorySlot001 = BasicInventorySlot.at(this, 26, 18));
		builder.addSlot(inventorySlot002 = BasicInventorySlot.at(this, 44, 18));
		builder.addSlot(inventorySlot003 = BasicInventorySlot.at(this, 62, 18));
		builder.addSlot(inventorySlot004 = BasicInventorySlot.at(this, 80, 18));
		builder.addSlot(inventorySlot005 = BasicInventorySlot.at(this, 98, 18));
		builder.addSlot(inventorySlot006 = BasicInventorySlot.at(this, 116, 18));
		builder.addSlot(inventorySlot007 = BasicInventorySlot.at(this, 134, 18));
		builder.addSlot(inventorySlot008 = BasicInventorySlot.at(this, 152, 18));
		builder.addSlot(inventorySlot009 = BasicInventorySlot.at(this, 8, 36));
		builder.addSlot(inventorySlot010 = BasicInventorySlot.at(this, 26, 36));
		builder.addSlot(inventorySlot011 = BasicInventorySlot.at(this, 44, 36));
		builder.addSlot(inventorySlot012 = BasicInventorySlot.at(this, 62, 36));
		builder.addSlot(inventorySlot013 = BasicInventorySlot.at(this, 80, 36));
		builder.addSlot(inventorySlot014 = BasicInventorySlot.at(this, 98, 36));
		builder.addSlot(inventorySlot015 = BasicInventorySlot.at(this, 116, 36));
		builder.addSlot(inventorySlot016 = BasicInventorySlot.at(this, 134, 36));
		builder.addSlot(inventorySlot017 = BasicInventorySlot.at(this, 152, 36));
		builder.addSlot(inventorySlot018 = BasicInventorySlot.at(this, 8, 54));
		builder.addSlot(inventorySlot019 = BasicInventorySlot.at(this, 26, 54));
		builder.addSlot(inventorySlot020 = BasicInventorySlot.at(this, 44, 54));
		builder.addSlot(inventorySlot021 = BasicInventorySlot.at(this, 62, 54));
		builder.addSlot(inventorySlot022 = BasicInventorySlot.at(this, 80, 54));
		builder.addSlot(inventorySlot023 = BasicInventorySlot.at(this, 98, 54));
		builder.addSlot(inventorySlot024 = BasicInventorySlot.at(this, 116, 54));
		builder.addSlot(inventorySlot025 = BasicInventorySlot.at(this, 134, 54));
		builder.addSlot(inventorySlot026 = BasicInventorySlot.at(this, 152, 54));
		builder.addSlot(inventorySlot027 = BasicInventorySlot.at(this, 8, 72));
		builder.addSlot(inventorySlot028 = BasicInventorySlot.at(this, 26, 72));
		builder.addSlot(inventorySlot029 = BasicInventorySlot.at(this, 44, 72));
		builder.addSlot(inventorySlot030 = BasicInventorySlot.at(this, 62, 72));
		builder.addSlot(inventorySlot031 = BasicInventorySlot.at(this, 80, 72));
		builder.addSlot(inventorySlot032 = BasicInventorySlot.at(this, 98, 72));
		builder.addSlot(inventorySlot033 = BasicInventorySlot.at(this, 116, 72));
		builder.addSlot(inventorySlot034 = BasicInventorySlot.at(this, 134, 72));
		builder.addSlot(inventorySlot035 = BasicInventorySlot.at(this, 152, 72));
		builder.addSlot(inventorySlot100 = BasicInventorySlot.at(this, 8, 108));
		builder.addSlot(inventorySlot101 = BasicInventorySlot.at(this, 26, 108));
		builder.addSlot(inventorySlot102 = BasicInventorySlot.at(this, 44, 108));
		builder.addSlot(inventorySlot103 = BasicInventorySlot.at(this, 62, 108));
		builder.addSlot(inventorySlot104 = BasicInventorySlot.at(this, 80, 108));
		builder.addSlot(inventorySlot105 = BasicInventorySlot.at(this, 98, 108));
		builder.addSlot(inventorySlot106 = BasicInventorySlot.at(this, 116, 108));
		builder.addSlot(inventorySlot107 = BasicInventorySlot.at(this, 134, 108));
		builder.addSlot(inventorySlot108 = BasicInventorySlot.at(this, 152, 108));
		builder.addSlot(inventorySlot109 = BasicInventorySlot.at(this, 8, 126));
		builder.addSlot(inventorySlot110 = BasicInventorySlot.at(this, 26, 126));
		builder.addSlot(inventorySlot111 = BasicInventorySlot.at(this, 44, 126));
		builder.addSlot(inventorySlot112 = BasicInventorySlot.at(this, 62, 126));
		builder.addSlot(inventorySlot113 = BasicInventorySlot.at(this, 80, 126));
		builder.addSlot(inventorySlot114 = BasicInventorySlot.at(this, 98, 126));
		builder.addSlot(inventorySlot115 = BasicInventorySlot.at(this, 116, 126));
		builder.addSlot(inventorySlot116 = BasicInventorySlot.at(this, 134, 126));
		builder.addSlot(inventorySlot117 = BasicInventorySlot.at(this, 152, 126));
		builder.addSlot(inventorySlot118 = BasicInventorySlot.at(this, 8, 144));
		builder.addSlot(inventorySlot119 = BasicInventorySlot.at(this, 26, 144));
		builder.addSlot(inventorySlot120 = BasicInventorySlot.at(this, 44, 144));
		builder.addSlot(inventorySlot121 = BasicInventorySlot.at(this, 62, 144));
		builder.addSlot(inventorySlot122 = BasicInventorySlot.at(this, 80, 144));
		builder.addSlot(inventorySlot123 = BasicInventorySlot.at(this, 98, 144));
		builder.addSlot(inventorySlot124 = BasicInventorySlot.at(this, 116, 144));
		builder.addSlot(inventorySlot125 = BasicInventorySlot.at(this, 134, 144));
		builder.addSlot(inventorySlot126 = BasicInventorySlot.at(this, 152, 144));
		builder.addSlot(inventorySlot127 = BasicInventorySlot.at(this, 8, 162));
		builder.addSlot(inventorySlot128 = BasicInventorySlot.at(this, 26, 162));
		builder.addSlot(inventorySlot129 = BasicInventorySlot.at(this, 44, 162));
		builder.addSlot(inventorySlot130 = BasicInventorySlot.at(this, 62, 162));
		builder.addSlot(inventorySlot131 = BasicInventorySlot.at(this, 80, 162));
		builder.addSlot(inventorySlot132 = BasicInventorySlot.at(this, 98, 162));
		builder.addSlot(inventorySlot133 = BasicInventorySlot.at(this, 116, 162));
		builder.addSlot(inventorySlot134 = BasicInventorySlot.at(this, 134, 162));
		builder.addSlot(inventorySlot135 = BasicInventorySlot.at(this, 152, 162));
		builder.addSlot(insertUpgradeSlotA = BasicInventorySlot.at(this, 170, 54));
		builder.addSlot(insertUpgradeSlotB = BasicInventorySlot.at(this, 170, 144));
		builder.addSlot(dustBoxSlotA = BasicInventorySlot.at(this, 170, 72));
		builder.addSlot(dustBoxSlotB = BasicInventorySlot.at(this, 170, 162));
		builder.addSlot(energyInput = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel,
				listener, 206, 162));
		builder.addSlot(energyOutput = EnergyInventorySlot.drain(energyContainer, listener, 224, 162));
		builder.addSlot(fluidInput0 = FluidInventorySlot.fill(fluidTank0, listener, 224, 44));
		builder.addSlot(fluidOutput0 = FluidInventorySlot.drain(fluidTank0, listener, 224, 62));
		builder.addSlot(fluidInput1 = FluidInventorySlot.fill(fluidTank1, listener, 224, 120));
		builder.addSlot(fluidOutput1 = FluidInventorySlot.drain(fluidTank1, listener, 224, 138));
		builder.addSlot(chemicalInput0 = MergedChemicalInventorySlot.fill(mergedChemicalTank0, listener, 278,
				44));
		builder.addSlot(chemicalOutput0 = MergedChemicalInventorySlot.drain(mergedChemicalTank0, listener, 278,
				62));
		builder.addSlot(chemicalInput1 = MergedChemicalInventorySlot.fill(mergedChemicalTank1, listener, 278,
				120));
		builder.addSlot(chemicalOutput1 = MergedChemicalInventorySlot.drain(mergedChemicalTank1, listener, 278,
				138));
		energyInput.setSlotOverlay(SlotOverlay.POWER);
		energyOutput.setSlotOverlay(SlotOverlay.POWER);
		fluidInput0.setSlotOverlay(SlotOverlay.MINUS);
		fluidInput1.setSlotOverlay(SlotOverlay.MINUS);
		fluidOutput0.setSlotOverlay(SlotOverlay.PLUS);
		fluidOutput1.setSlotOverlay(SlotOverlay.PLUS);
		chemicalInput0.setSlotOverlay(SlotOverlay.MINUS);
		chemicalInput1.setSlotOverlay(SlotOverlay.MINUS);
		chemicalOutput0.setSlotOverlay(SlotOverlay.PLUS);
		chemicalOutput1.setSlotOverlay(SlotOverlay.PLUS);
		return builder.build();
	};

	@NotNull
	@Override
	protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
		EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection,
				this::getConfig);
		builder.addContainer(energyContainer = BasicEnergyContainer.create(FloatingLong.create(200000),
				listener));
		return builder.build();
	}

	@NotNull
	@Override
	protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener) {
		FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
		builder.addTank(fluidTank0 = BasicFluidTank.create(200000, fluid -> true, listener));
		builder.addTank(fluidTank1 = BasicFluidTank.create(200000, fluid -> true, listener));
		return builder.build();
	}

	@Override
	protected void presetVariables() {
		super.presetVariables();
		mergedChemicalTank0 = MergedChemicalTank.create(
				ChemicalTankBuilder.GAS.create(200000, gas -> true, this),
				ChemicalTankBuilder.INFUSION.create(200000, infusion -> true, this),
				ChemicalTankBuilder.PIGMENT.create(200000, pigment -> true, this),
				ChemicalTankBuilder.SLURRY.create(200000, slurry -> true, this));
		mergedChemicalTank1 = MergedChemicalTank.create(
				ChemicalTankBuilder.GAS.create(200000, gas -> true, this),
				ChemicalTankBuilder.INFUSION.create(200000, infusion -> true, this),
				ChemicalTankBuilder.PIGMENT.create(200000, pigment -> true, this),
				ChemicalTankBuilder.SLURRY.create(200000, slurry -> true, this));
	}

	@NotNull
	@Override
	public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
		ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper
				.forSideGasWithConfig(this::getDirection, this::getConfig);
		builder.addTank(mergedChemicalTank0.getGasTank());
		builder.addTank(mergedChemicalTank1.getGasTank());
		return builder.build();
	}

	@NotNull
	@Override
	public IChemicalTankHolder<InfuseType, InfusionStack, IInfusionTank> getInitialInfusionTanks(
			IContentsListener listener) {
		ChemicalTankHelper<InfuseType, InfusionStack, IInfusionTank> builder = ChemicalTankHelper
				.forSideInfusionWithConfig(this::getDirection, this::getConfig);
		builder.addTank(mergedChemicalTank0.getInfusionTank());
		builder.addTank(mergedChemicalTank1.getInfusionTank());
		return builder.build();
	}

	@NotNull
	@Override
	public IChemicalTankHolder<Pigment, PigmentStack, IPigmentTank> getInitialPigmentTanks(
			IContentsListener listener) {
		ChemicalTankHelper<Pigment, PigmentStack, IPigmentTank> builder = ChemicalTankHelper
				.forSidePigmentWithConfig(this::getDirection, this::getConfig);
		builder.addTank(mergedChemicalTank0.getPigmentTank());
		builder.addTank(mergedChemicalTank1.getPigmentTank());
		return builder.build();
	}

	@NotNull
	@Override
	public IChemicalTankHolder<Slurry, SlurryStack, ISlurryTank> getInitialSlurryTanks(IContentsListener listener) {
		ChemicalTankHelper<Slurry, SlurryStack, ISlurryTank> builder = ChemicalTankHelper
				.forSideSlurryWithConfig(this::getDirection, this::getConfig);
		builder.addTank(mergedChemicalTank0.getSlurryTank());
		builder.addTank(mergedChemicalTank1.getSlurryTank());
		return builder.build();
	}

	@NotNull
	@Override
	protected IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener,
			CachedAmbientTemperature ambientTemperature) {
		HeatCapacitorHelper builder = HeatCapacitorHelper.forSide(this::getDirection);
		builder.addCapacitor(heatCapacitor = BasicHeatCapacitor.create(100, 5, 10, ambientTemperature, listener));
		return builder.build();
	}

	@Override
	protected void onUpdateServer() {
		energyInput.fillContainerOrConvert();
		energyOutput.drainContainer();
		fluidInput0.fillTank();
		fluidInput1.fillTank();
		fluidOutput0.drainTank(fluidOutput0);
		fluidOutput1.drainTank(fluidOutput1);
		chemicalInput0.fillChemicalTanks();
		chemicalInput1.fillChemicalTanks();
		chemicalOutput0.drainChemicalTanks();
		chemicalOutput1.drainChemicalTanks();
		HeatTransfer loss = simulate();
		lastEnvironmentLoss = loss.environmentTransfer();
		lastTransferLoss = loss.adjacentTransfer();
		super.onUpdateServer();
		if (ejectorComponent.isEjecting(configComponent.getConfig(TransmissionType.ITEM),
				TransmissionType.ITEM)) {
			if (ItemStack.isSameItem(dustBoxSlotA.getStack(), new ItemStack(AstralMekanismItems.DUST_BOX))) {
				int p = (int) Math.min(dustBoxSlotA.getCount() * 4.5, 36);
				for (int index = 0; index < p; index++) {
					listInventorySlotsA.get(listInventorySlotsA.size() - index - 1).setEmpty();
				}
			}
			if (ItemStack.isSameItem(dustBoxSlotB.getStack(), new ItemStack(AstralMekanismItems.DUST_BOX))) {
				int p = (int) Math.min(dustBoxSlotB.getCount() * 4.5, 36);
				for (int index = 0; index < p; index++) {
					listInventorySlotsB.get(listInventorySlotsB.size() - index - 1).setEmpty();
				}
			}

		}
		if (ItemStack.isSameItem(insertUpgradeSlotA.getStack(),
				DataCacheUniversalStorage.getInsertUpgrade().getItemStack())) {
			BlockEntityUtils.itemInsert(this,
					List.of(DataType.INPUT_1, DataTypeEx.INPUT1_OUTPUT1, DataTypeEx.INPUT1_OUTPUT2));
		}
		if (ItemStack.isSameItem(insertUpgradeSlotB.getStack(),
				DataCacheUniversalStorage.getInsertUpgrade().getItemStack())) {
			BlockEntityUtils.itemInsert(this,
					List.of(DataType.INPUT_2, DataTypeEx.INPUT2_OUTPUT1, DataTypeEx.INPUT2_OUTPUT2));
		}
		BlockEntityUtils.itemEject(this, List.of(DataTypeEx.INPUT1_OUTPUT1, DataTypeEx.INPUT2_OUTPUT1),
				DataType.OUTPUT_1);
		BlockEntityUtils.itemEject(this, List.of(DataTypeEx.INPUT1_OUTPUT2, DataTypeEx.INPUT2_OUTPUT2),
				DataType.OUTPUT_2);
		if (ejectorComponent.isEjecting(configComponent.getConfig(TransmissionType.FLUID),
				TransmissionType.FLUID)) {
			FluidUtils.emit(configComponent.getConfig(TransmissionType.FLUID)
					.getSidesForData(DataTypeEx.INPUT1_OUTPUT1), fluidTank0, this, 200000);
			FluidUtils.emit(configComponent.getConfig(TransmissionType.FLUID)
					.getSidesForData(DataTypeEx.INPUT2_OUTPUT1), fluidTank0, this, 200000);
			FluidUtils.emit(configComponent.getConfig(TransmissionType.FLUID)
					.getSidesForData(DataTypeEx.INPUT1_OUTPUT2), fluidTank1, this, 200000);
			FluidUtils.emit(configComponent.getConfig(TransmissionType.FLUID)
					.getSidesForData(DataTypeEx.INPUT2_OUTPUT2), fluidTank1, this, 200000);
		}
		if (ejectorComponent.isEjecting(configComponent.getConfig(TransmissionType.GAS),
				TransmissionType.GAS)) {
			ChemicalUtil.emit(configComponent.getConfig(TransmissionType.GAS)
					.getSidesForData(DataTypeEx.INPUT1_OUTPUT1), mergedChemicalTank0.getGasTank(), this,
					200000);
			ChemicalUtil.emit(configComponent.getConfig(TransmissionType.GAS)
					.getSidesForData(DataTypeEx.INPUT2_OUTPUT1), mergedChemicalTank0.getGasTank(), this,
					200000);
			ChemicalUtil.emit(configComponent.getConfig(TransmissionType.GAS)
					.getSidesForData(DataTypeEx.INPUT1_OUTPUT2), mergedChemicalTank1.getGasTank(), this,
					200000);
			ChemicalUtil.emit(configComponent.getConfig(TransmissionType.GAS)
					.getSidesForData(DataTypeEx.INPUT2_OUTPUT2), mergedChemicalTank1.getGasTank(), this,
					200000);
		}
		if (ejectorComponent.isEjecting(configComponent.getConfig(TransmissionType.INFUSION),
				TransmissionType.INFUSION)) {
			ChemicalUtil.emit(configComponent.getConfig(TransmissionType.INFUSION)
					.getSidesForData(DataTypeEx.INPUT1_OUTPUT1), mergedChemicalTank0.getInfusionTank(), this,
					200000);
			ChemicalUtil.emit(configComponent.getConfig(TransmissionType.INFUSION)
					.getSidesForData(DataTypeEx.INPUT2_OUTPUT1), mergedChemicalTank0.getInfusionTank(), this,
					200000);
			ChemicalUtil.emit(configComponent.getConfig(TransmissionType.INFUSION)
					.getSidesForData(DataTypeEx.INPUT1_OUTPUT2), mergedChemicalTank1.getInfusionTank(), this,
					200000);
			ChemicalUtil.emit(configComponent.getConfig(TransmissionType.INFUSION)
					.getSidesForData(DataTypeEx.INPUT2_OUTPUT2), mergedChemicalTank1.getInfusionTank(), this,
					200000);
		}
		if (ejectorComponent.isEjecting(configComponent.getConfig(TransmissionType.PIGMENT),
				TransmissionType.PIGMENT)) {
			ChemicalUtil.emit(configComponent.getConfig(TransmissionType.PIGMENT)
					.getSidesForData(DataTypeEx.INPUT1_OUTPUT1), mergedChemicalTank0.getPigmentTank(), this,
					200000);
			ChemicalUtil.emit(configComponent.getConfig(TransmissionType.PIGMENT)
					.getSidesForData(DataTypeEx.INPUT2_OUTPUT1), mergedChemicalTank0.getPigmentTank(), this,
					200000);
			ChemicalUtil.emit(configComponent.getConfig(TransmissionType.PIGMENT)
					.getSidesForData(DataTypeEx.INPUT1_OUTPUT2), mergedChemicalTank1.getPigmentTank(), this,
					200000);
			ChemicalUtil.emit(configComponent.getConfig(TransmissionType.PIGMENT)
					.getSidesForData(DataTypeEx.INPUT2_OUTPUT2), mergedChemicalTank1.getPigmentTank(), this,
					200000);
		}
		if (ejectorComponent.isEjecting(configComponent.getConfig(TransmissionType.SLURRY),
				TransmissionType.SLURRY)) {
			ChemicalUtil.emit(configComponent.getConfig(TransmissionType.SLURRY)
					.getSidesForData(DataTypeEx.INPUT1_OUTPUT1), mergedChemicalTank0.getSlurryTank(), this,
					200000);
			ChemicalUtil.emit(configComponent.getConfig(TransmissionType.SLURRY)
					.getSidesForData(DataTypeEx.INPUT2_OUTPUT1), mergedChemicalTank0.getSlurryTank(), this,
					200000);
			ChemicalUtil.emit(configComponent.getConfig(TransmissionType.SLURRY)
					.getSidesForData(DataTypeEx.INPUT1_OUTPUT2), mergedChemicalTank1.getSlurryTank(), this,
					200000);
			ChemicalUtil.emit(configComponent.getConfig(TransmissionType.SLURRY)
					.getSidesForData(DataTypeEx.INPUT2_OUTPUT2), mergedChemicalTank1.getSlurryTank(), this,
					200000);
		}
		this.handleTank(this.mergedChemicalTank0.getGasTank(), dumping0);
		this.handleTank(this.mergedChemicalTank1.getGasTank(), dumping1);
	}

	private void handleTank(IGasTank tank, TileEntityChemicalTank.GasMode mode) {
		long dump_level = (long) Math.floor(tank.getCapacity() * 0.96);
		if (!tank.isEmpty()) {
			if (mode == GasMode.DUMPING) {
				tank.shrinkStack(tank.getStored(), Action.EXECUTE);
			} else if (mode == GasMode.DUMPING_EXCESS && tank.getStored() > dump_level) {
				tank.shrinkStack((long) (tank.getStored() - dump_level), Action.EXECUTE);
			}
		}
	}

	public void tickServer() {
	}

	public void tickClient() {
	}

	@Override
	public void nextMode(int tank) {
		if (tank == 0) {
			dumping0 = dumping0.getNext();
			markForSave();
		} else if (tank == 1) {
			dumping1 = dumping1.getNext();
			markForSave();
		}
	}

	@Override
	public Map<String, String> getTileDataRemap() {
		Map<String, String> remap = new Object2ObjectOpenHashMap<>();
		remap.put(NBTConstants.DUMP_LEFT, NBTConstants.DUMP_LEFT);
		remap.put(NBTConstants.DUMP_RIGHT, NBTConstants.DUMP_RIGHT);
		return remap;
	}

	@Override
	public void readSustainedData(CompoundTag dataMap) {
		NBTUtils.setEnumIfPresent(dataMap, NBTConstants.DUMP_LEFT, GasMode::byIndexStatic,
				mode -> dumping0 = mode);
		NBTUtils.setEnumIfPresent(dataMap, NBTConstants.DUMP_RIGHT, GasMode::byIndexStatic,
				mode -> dumping1 = mode);
	}

	@Override
	public void writeSustainedData(CompoundTag dataMap) {
		NBTUtils.writeEnum(dataMap, NBTConstants.DUMP_LEFT, dumping0);
		NBTUtils.writeEnum(dataMap, NBTConstants.DUMP_RIGHT, dumping1);
	}

	public double getLastTransferLoss() {
		return lastTransferLoss;
	}

	public double getLastEnvironmentLoss() {
		return lastEnvironmentLoss;
	}

	@Override
	public void addContainerTrackers(MekanismContainer container) {
		super.addContainerTrackers(container);
		container.track(
				SyncableEnum.create(GasMode::byIndexStatic, GasMode.IDLE, () -> dumping0, value -> dumping0 = value));
		container.track(
				SyncableEnum.create(GasMode::byIndexStatic, GasMode.IDLE, () -> dumping1, value -> dumping1 = value));
		container.track(SyncableDouble.create(this::getLastTransferLoss, value -> lastTransferLoss = value));
		container.track(SyncableDouble.create(this::getLastEnvironmentLoss, value -> lastEnvironmentLoss = value));
	}

}
