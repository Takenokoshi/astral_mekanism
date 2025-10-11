package astral_mekanism.block.blockentity.generator;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.gas.attribute.GasAttributes.Fuel;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableFloatingLong;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.chemical.GasInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEGasBurningGenerator extends TileEntityConfigurableMachine {

    private BasicEnergyContainer energyContainer;
    private IGasTank fuelTank;
    private static final Predicate<Gas> isFuel = gas -> gas.get(Fuel.class) != null;

    private EnergyInventorySlot energySlot;
    private GasInventorySlot gasSlot;
    private FloatingLong lastProduced = FloatingLong.ZERO;
    private long gasBurnRate;

    public BEGasBurningGenerator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        configComponent = new TileComponentConfig(this, TransmissionType.ENERGY, TransmissionType.GAS);
        configComponent.setupOutputConfig(TransmissionType.ENERGY, energyContainer, RelativeSide.values());
        configComponent.setupInputConfig(TransmissionType.GAS, fuelTank);
        ejectorComponent = new TileComponentEjector(this,
                () -> Attribute.getTier(getBlockType(), AstralMekGeneratorTier.class).energyCapacity);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ENERGY);
        lastProduced = FloatingLong.ZERO;
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper
                .forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(fuelTank = ChemicalTankBuilder.GAS.create(
                Attribute.getTier(getBlockType(), AstralMekGeneratorTier.class).getGastankCapacity(),
                isFuel, listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(energyContainer = MachineEnergyContainer.create(
                Attribute.getTier(getBlockType(), AstralMekGeneratorTier.class).energyCapacity,
                listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection,
                this::getConfig);
        builder.addSlot(energySlot = EnergyInventorySlot.drain(energyContainer, listener, 143, 35));
        builder.addSlot(gasSlot = GasInventorySlot.fill(fuelTank, listener, 17, 35));
        gasSlot.setSlotOverlay(SlotOverlay.MINUS);
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        energySlot.drainContainer();
        gasSlot.fillTankOrConvert();
        super.onUpdateServer();
        burning();
    }

    private void burning() {
        GasStack gas = fuelTank.getStack();
        if (gas.isEmpty()) {
            lastProduced = FloatingLong.ZERO;
            gasBurnRate = 0;
            return;
        }
        Fuel fuel = gas.get(Fuel.class);
        if (fuel == null) {
            lastProduced = FloatingLong.ZERO;
            gasBurnRate = 0;
            return;
        }
        FloatingLong energyPer1mB = fuel.getEnergyPerTick().multiply(fuel.getBurnTicks());
        long energyLimit = energyContainer.getNeeded().divideToLong(energyPer1mB);
        long burnGasAmount = Math.min(energyLimit, fuelTank.getStored());
        energyContainer.insert(lastProduced = energyPer1mB.multiply(burnGasAmount), Action.EXECUTE,
                AutomationType.INTERNAL);
        fuelTank.extract(gasBurnRate = burnGasAmount, Action.EXECUTE, AutomationType.INTERNAL).getAmount();
        return;
    }

    public EnergyInventorySlot getEnergySlot() {
        return energySlot;
    }

    public GasInventorySlot getGasSlot() {
        return gasSlot;
    }

    public BasicEnergyContainer getEnergyContainer() {
        return energyContainer;
    }

    public IGasTank getFuelTank() {
        return fuelTank;
    }

    public FloatingLong getProduced() {
        return lastProduced;
    }

    public long getBurnRate() {
        return gasBurnRate;
    }

    public AstralMekGeneratorTier getTier() {
        return Attribute.getTier(getBlockType(), AstralMekGeneratorTier.class);
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableFloatingLong.create(this::getProduced, v -> lastProduced = v));
        container.track(SyncableLong.create(this::getBurnRate, v -> gasBurnRate = v));
    }

}
