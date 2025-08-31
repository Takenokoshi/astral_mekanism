package astral_mekanism.blocks.cobblestone_supply_device;

import org.jetbrains.annotations.NotNull;

import mekanism.api.IContentsListener;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.EnergySlotInfo;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityCobblestoneSupplyDevice extends TileEntityConfigurableMachine {

    public BlockEntityCobblestoneSupplyDevice(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        this.configComponent = new TileComponentConfig(this, TransmissionType.ENERGY, TransmissionType.ITEM);
        ConfigInfo energyConfig = this.configComponent.getConfig(TransmissionType.ENERGY);
        if (energyConfig != null) {
            energyConfig.addSlotInfo(DataType.INPUT, new EnergySlotInfo(true, false, energyContainer));
        }
        ConfigInfo itemConfig = this.configComponent.getConfig(TransmissionType.ITEM);
        if (itemConfig != null) {
            itemConfig.addSlotInfo(DataType.OUTPUT, new InventorySlotInfo(false, true, inventorySlot));
        }
        this.ejectorComponent = new TileComponentEjector(this);
        this.ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);
    }

    BasicEnergyContainer energyContainer;

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(energyContainer = BasicEnergyContainer.input(FloatingLong.create(1000000), listener));
        return builder.build();
    }

    BasicInventorySlot inventorySlot;

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(inventorySlot = BasicInventorySlot.at(this, 79, 54));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        if (MekanismUtils.canFunction(this) && (2560 <= energyContainer.getEnergy().floatValue())) {
            float rate = inventorySlot.getCount() / 64;
            energyContainer.setEnergy(FloatingLong.create(energyContainer.getEnergy().floatValue() - 2560 * (1 - rate)));
            inventorySlot.setStack(new ItemStack(Items.COBBLESTONE, 64));
        }
    }
}
