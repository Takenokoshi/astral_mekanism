package astral_mekanism.blocks.water_supply_device;

import java.util.Set;

import org.jetbrains.annotations.NotNull;

import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.util.FluidUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.api.math.FloatingLong;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

public class BlockEntityWaterSupplyDevice extends TileEntityConfigurableMachine {

	public BlockEntityWaterSupplyDevice(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
		super(blockProvider, pos, state);
		this.configComponent = new TileComponentConfig(this, TransmissionType.ENERGY, TransmissionType.FLUID);
		configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
		configComponent.setupOutputConfig(TransmissionType.FLUID, fluidTank, RelativeSide.RIGHT);
		this.ejectorComponent = new TileComponentEjector(this, () -> Long.MAX_VALUE, () -> 2147483647,
				() -> FloatingLong.MAX_VALUE);
		this.ejectorComponent.setOutputData(configComponent, TransmissionType.FLUID);
	}

	BasicEnergyContainer energyContainer;

	@NotNull
	@Override
	protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
		EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection,
				this::getConfig);
		builder.addContainer(
				energyContainer = BasicEnergyContainer.input(FloatingLong.create(1000000), listener));
		return builder.build();
	}

	BasicFluidTank fluidTank;

	@NotNull
	@Override
	protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener) {
		FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
		builder.addTank(fluidTank = BasicFluidTank.output(2147483647, listener));
		return builder.build();
	}

	@Override
	protected void onUpdateServer() {
		if (MekanismUtils.canFunction(this)
				&& (256 <= energyContainer.getEnergy().floatValue())) {
			float rate = fluidTank.getFluidAmount() / 2147483647;
			energyContainer.setEnergy(FloatingLong.create(
					energyContainer.getEnergy().floatValue() - 256 * (1 - rate)));
			fluidTank.setStack(new FluidStack(Fluids.WATER, 2147483647));
		}
		Set<Direction> directions = configComponent.getConfig(TransmissionType.FLUID)
				.getSidesForData(DataType.OUTPUT);
		FluidUtils.emit(directions, fluidTank, this, 2147483647);
		super.onUpdateServer();
	};

	@Override
	public Component getDisplayName() {
		return getName();
	}

	public Component getName() {
		return Component.translatable("container.water_supply_device");
	}

}
