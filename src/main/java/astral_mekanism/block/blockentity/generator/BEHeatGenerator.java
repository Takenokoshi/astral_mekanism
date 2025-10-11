package astral_mekanism.block.blockentity.generator;

import java.util.Arrays;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.heat.HeatAPI.HeatTransfer;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.heat.BasicHeatCapacitor;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.heat.HeatCapacitorHelper;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableDouble;
import mekanism.common.inventory.container.sync.SyncableFloatingLong;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.util.EnumUtils;
import mekanism.common.util.WorldUtils;
import mekanism.generators.common.config.MekanismGeneratorsConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class BEHeatGenerator extends TileEntityConfigurableMachine {

    private static final double THERMAL_EFFICIENCY = 0.5;

    BasicHeatCapacitor heatCapacitor;
    BasicEnergyContainer energyContainer;
    EnergyInventorySlot energySlot;
    private FloatingLong lastProduced = FloatingLong.ZERO;
    private double lastTransferLoss;
    private double lastEnvironmentLoss;

    public BEHeatGenerator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        configComponent = new TileComponentConfig(this, TransmissionType.ENERGY, TransmissionType.HEAT);
        configComponent.setupOutputConfig(TransmissionType.ENERGY, energyContainer, RelativeSide.values());
        configComponent.setupInputConfig(TransmissionType.HEAT, heatCapacitor);
        ejectorComponent = new TileComponentEjector(this, () -> getTier().energyCapacity);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ENERGY);
        lastProduced = FloatingLong.ZERO;
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(energyContainer = MachineEnergyContainer.create(getTier().energyCapacity, listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener,
            CachedAmbientTemperature ambientTemperature) {
        HeatCapacitorHelper builder = HeatCapacitorHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addCapacitor(heatCapacitor = BasicHeatCapacitor.create(10, 5, 100, ambientTemperature, listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection,
                this::getConfig);
        builder.addSlot(energySlot = EnergyInventorySlot.drain(energyContainer, listener, 143, 35));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        energySlot.drainContainer();
        super.onUpdateServer();
        boost();
        HeatTransfer loss = simulate();
        lastTransferLoss = loss.adjacentTransfer();
        lastEnvironmentLoss = loss.environmentTransfer();
    }

    @NotNull
    @Override
    public HeatTransfer simulate() {
        double ambientTemp = ambientTemperature.getAsDouble();
        double temp = getTotalTemperature();
        double carnotEfficiency = 1 - Math.min(ambientTemp, temp) / Math.max(ambientTemp, temp);
        double heatLost = THERMAL_EFFICIENCY * (temp - ambientTemp);
        heatCapacitor.handleHeat(-heatLost);
        FloatingLong energyFromHeat = FloatingLong.create(Math.abs(heatLost) * carnotEfficiency);
        energyContainer.insert(lastProduced = energyFromHeat.min(energyContainer.getNeeded()), Action.EXECUTE,
                AutomationType.INTERNAL);
        return super.simulate();
    }

    private void boost() {
        heatCapacitor.handleHeat(getBoost().doubleValue());
    }

    private FloatingLong getBoost() {
        if (level == null) {
            return FloatingLong.ZERO;
        }
        FloatingLong boost;
        FloatingLong passiveLavaAmount = MekanismGeneratorsConfig.generators.heatGenerationLava.get();
        if (passiveLavaAmount.isZero()) {
            boost = FloatingLong.ZERO;
        } else {
            long lavaSides = Arrays.stream(EnumUtils.DIRECTIONS).filter(side -> {
                Optional<FluidState> fluidState = WorldUtils.getFluidState(level, worldPosition.relative(side));
                return fluidState.isPresent() && fluidState.get().is(FluidTags.LAVA);
            }).count();
            if (getBlockState().getFluidState().is(FluidTags.LAVA)) {
                lavaSides++;
            }
            boost = passiveLavaAmount.multiply(lavaSides);
        }
        if (level.dimensionType().ultraWarm()) {
            boost = boost.plusEqual(MekanismGeneratorsConfig.generators.heatGenerationNether.get());
        }
        return boost;
    }

    public double getLastTransferLoss() {
        return lastTransferLoss;
    }

    public double getLastEnvironmentLoss() {
        return lastEnvironmentLoss;
    }

    public FloatingLong getProduced() {
        return lastProduced;
    }

    public BasicEnergyContainer getEnergyContainer() {
        return energyContainer;
    }

    public BasicHeatCapacitor getHeatCapacitor(){
        return heatCapacitor;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableFloatingLong.create(this::getProduced, v -> lastProduced = v));
        container.track(SyncableDouble.create(this::getLastTransferLoss, value -> lastTransferLoss = value));
        container.track(SyncableDouble.create(this::getLastEnvironmentLoss, value -> lastEnvironmentLoss = value));
    }

    public AstralMekGeneratorTier getTier() {
        return Attribute.getTier(getBlockType(), AstralMekGeneratorTier.class);
    }

}
