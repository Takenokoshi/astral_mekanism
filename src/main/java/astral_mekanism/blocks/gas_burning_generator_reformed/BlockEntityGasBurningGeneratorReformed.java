package astral_mekanism.blocks.gas_burning_generator_reformed;

import org.jetbrains.annotations.NotNull;

import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.gas.attribute.GasAttributes.Fuel;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.EnergySlotInfo;
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo.GasSlotInfo;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityGasBurningGeneratorReformed extends TileEntityConfigurableMachine {

	public BlockEntityGasBurningGeneratorReformed(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
		super(blockProvider, pos, state);
		this.configComponent = new TileComponentConfig(this, TransmissionType.ENERGY, TransmissionType.GAS);
		ConfigInfo energyConfig = this.configComponent.getConfig(TransmissionType.ENERGY);
		if (energyConfig != null) {
			energyConfig.addSlotInfo(DataType.OUTPUT, new EnergySlotInfo(false, true, energyContainer));
		}
		ConfigInfo gasConfig = this.configComponent.getConfig(TransmissionType.GAS);
		if (gasConfig != null) {
			gasConfig.addSlotInfo(DataType.INPUT, new GasSlotInfo(true, false, gasTank));
		}
		ejectorComponent = new TileComponentEjector(this, () -> Long.MAX_VALUE, () -> 2147483647,
				() -> FloatingLong.MAX_VALUE);
		ejectorComponent.setOutputData(configComponent, TransmissionType.ENERGY);
	}

	BasicEnergyContainer energyContainer;

	@NotNull
	@Override
	protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
		EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
		builder.addContainer(energyContainer = BasicEnergyContainer.create(FloatingLong.MAX_VALUE, listener));
		return builder.build();
	}

	IGasTank gasTank;

	@NotNull
	@Override
	public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
		ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper
				.forSideGasWithConfig(this::getDirection, this::getConfig);
		builder.addTank(gasTank = ChemicalTankBuilder.GAS.create(Long.MAX_VALUE,
				gas -> {
					return gas.get(Fuel.class) != null;
				},
				this));
		return builder.build();
	}

	@Override
	protected void onUpdateServer() {
		super.onUpdateServer();
		float space = FloatingLong.MAX_VALUE.floatValue() - energyContainer.getEnergy().floatValue();
		if (!gasTank.isEmpty()) {
			GasStack gasStack = gasTank.getStack();
			// [mB]
			long gasAmount = gasStack.getAmount();
			Fuel fuel = gasStack.get(Fuel.class);
			if (fuel == null) {
				return;
			}
			// [J/mB]
			FloatingLong energyPerMilliBuckets = fuel.getEnergyPerTick().multiply(fuel.getBurnTicks());
			// [mB]
			long spacePerGasMilliBuckets = FloatingLong.create(space).divide(energyPerMilliBuckets).longValue();
			// [mB]
			long useGasAmountMilliBuckets = Math.min(gasAmount, spacePerGasMilliBuckets);
			gasTank.shrinkStack(useGasAmountMilliBuckets, Action.EXECUTE);
			energyContainer.insert(energyPerMilliBuckets.multiply(useGasAmountMilliBuckets), Action.EXECUTE,
					AutomationType.INTERNAL);
		}
	}

}
