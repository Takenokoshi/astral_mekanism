package astral_mekanism.block.blockentity.supplydevice;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.AstralMekanismConfig;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableFloatingLong;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

public class BECobblestoneSupplyDevice extends TileEntityConfigurableMachine {

    private BasicInventorySlot supplySlot;
    private BasicInventorySlot speedSlot;
    private BasicEnergyContainer energyContainer;
    private EnergyInventorySlot energySlot;

    public static final FloatingLong energyPerCobble = FloatingLong.create(4000).multiply(AstralMekanismConfig.energyRate);
    private static final ItemStack cobblestone = new ItemStack(Items.COBBLESTONE);
    private FloatingLong lastEnergyUsed = FloatingLong.ZERO;

    public BECobblestoneSupplyDevice(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        lastEnergyUsed = FloatingLong.ZERO;
        configComponent = new TileComponentConfig(this, TransmissionType.ENERGY,TransmissionType.ITEM);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        configComponent.setupOutputConfig(TransmissionType.ITEM, supplySlot, RelativeSide.values());
        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(
                energyContainer = MachineEnergyContainer.create(energyPerCobble.multiply(Integer.MAX_VALUE), listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection,
                this::getConfig);
        builder.addSlot(speedSlot = BasicInventorySlot.at(listener, 17, 35));
        builder.addSlot(
                supplySlot = BasicInventorySlot.at(item -> ItemStack.isSameItem(item, cobblestone), listener, 44, 36));
        builder.addSlot(energySlot = EnergyInventorySlot.fill(energyContainer, listener, 143, 35));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        energySlot.fillContainerOrConvert();
        super.onUpdateServer();
        supply();
    }

    private void supply() {
        if (supplySlot.isEmpty() || ItemStack.isSameItem(supplySlot.getStack(), cobblestone)) {
            int supplyCount = Math.min(cobblestone.getMaxStackSize() - supplySlot.getCount(), speedSlot.getCount());
            supplyCount = Math.min(supplyCount, energyContainer.getEnergy().divideToInt(energyPerCobble));
            supplySlot.insertItem(cobblestone.copyWithCount(supplyCount), Action.EXECUTE, AutomationType.INTERNAL);
            energyContainer.extract(lastEnergyUsed = energyPerCobble.multiply(supplyCount), Action.EXECUTE, AutomationType.INTERNAL);
        } else {
            lastEnergyUsed = FloatingLong.ZERO;
        }
        return;
    }

    public BasicEnergyContainer getEnergyContainer() {
        return energyContainer;
    }
    public FloatingLong getEnergyUsed(){
        return lastEnergyUsed;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableFloatingLong.create(this::getEnergyUsed, v -> lastEnergyUsed = v));
    }
}
