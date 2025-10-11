package astral_mekanism.block.blockentity.supplydevice;

import org.jetbrains.annotations.NotNull;

import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.Upgrade;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableFloatingLong;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.registries.MekanismFluids;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

public class BEWaterSupplyDevice extends TileEntityConfigurableMachine {

    BasicFluidTank fluidTank;
    FluidInventorySlot fluidSlot;
    BasicEnergyContainer energyContainer;
    EnergyInventorySlot energySlot;
    private static final FloatingLong energyPer1mB = FloatingLong.create(0, (short) 1);
    private static final FloatingLong energyPerHeavy = FloatingLong.create(200);
    FloatingLong lastEnergyUsed = FloatingLong.ZERO;

    public BEWaterSupplyDevice(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        lastEnergyUsed = FloatingLong.ZERO;
        configComponent = new TileComponentConfig(this, TransmissionType.ENERGY, TransmissionType.FLUID);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        configComponent.setupOutputConfig(TransmissionType.FLUID, fluidTank, RelativeSide.values());
        ejectorComponent = new TileComponentEjector(this, () -> 0, () -> Integer.MAX_VALUE);
        ejectorComponent.setOutputData(configComponent, TransmissionType.FLUID);
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(
                energyContainer = MachineEnergyContainer
                        .create(energyPerHeavy.multiply(Integer.MAX_VALUE).multiply(100), listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(fluidSlot = FluidInventorySlot.drain(fluidTank, listener, 17, 35));
        builder.addSlot(energySlot = EnergyInventorySlot.fill(energyContainer, listener, 143, 35));
        fluidSlot.setSlotOverlay(SlotOverlay.PLUS);
        return builder.build();
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(fluidTank = BasicFluidTank.create(Integer.MAX_VALUE, listener));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        fluidSlot.drainTank(fluidSlot);
        energySlot.fillContainerOrConvert();
        super.onUpdateServer();
        supply();
    }

    private void supply() {
        boolean hasFilter = upgradeComponent.isUpgradeInstalled(Upgrade.FILTER);
        if (hasFilter) {
            if (fluidTank.isEmpty() || fluidTank.isFluidEqual(MekanismFluids.HEAVY_WATER.getFluidStack(1))) {
                int amount = Math.min(energyContainer.getEnergy().divideToInt(energyPerHeavy), fluidTank.getNeeded());
                fluidTank.insert(MekanismFluids.HEAVY_WATER.getFluidStack(amount), Action.EXECUTE,
                        AutomationType.INTERNAL);
                energyContainer.extract(lastEnergyUsed = energyPerHeavy.multiply(amount), Action.EXECUTE,
                        AutomationType.INTERNAL);
                return;
            }
        } else if (fluidTank.isEmpty() || fluidTank.getFluid().getFluid() == Fluids.WATER) {
            int amount = Math.min(energyContainer.getEnergy().divideToInt(energyPer1mB), fluidTank.getNeeded());
            fluidTank.insert(new FluidStack(Fluids.WATER, amount), Action.EXECUTE, AutomationType.INTERNAL);
            lastEnergyUsed = energyContainer.extract(energyPer1mB.multiply(amount), Action.EXECUTE,
                    AutomationType.INTERNAL);
            return;
        }
        lastEnergyUsed = FloatingLong.ZERO;
        return;
    }

    public BasicEnergyContainer getEnergyContainer() {
        return energyContainer;
    }

    public BasicFluidTank getFluidTank() {
        return fluidTank;
    }

    public FloatingLong getEnergyUsed() {
        return lastEnergyUsed;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableFloatingLong.create(this::getEnergyUsed, v -> lastEnergyUsed = v));
    }
}
