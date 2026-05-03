package astral_mekanism.block.blockentity.other;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import mekanism.api.IContentsListener;
import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.util.UpgradeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BEUpgradeExtractor extends TileEntityConfigurableMachine {

    private BasicInventorySlot inputSlot;
    private OutputInventorySlot machineOutputSlot;
    private OutputInventorySlot upgradeOutputSlot;

    public BEUpgradeExtractor(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM);
        ConfigInfo itemInfo = configComponent.getConfig(TransmissionType.ITEM);
        itemInfo.addSlotInfo(DataType.INPUT, new InventorySlotInfo(true, false, inputSlot));
        itemInfo.addSlotInfo(DataType.OUTPUT_1, new InventorySlotInfo(false, true, machineOutputSlot));
        itemInfo.addSlotInfo(DataType.OUTPUT_2, new InventorySlotInfo(false, true, upgradeOutputSlot));
        ejectorComponent = new TileComponentEjector(this).setOutputData(configComponent, TransmissionType.ITEM);
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(inputSlot = InputInventorySlot.at(stack -> true, listener, 64, 35));
        builder.addSlot(machineOutputSlot = OutputInventorySlot.at(listener, 116, 17));
        builder.addSlot(upgradeOutputSlot = OutputInventorySlot.at(listener, 116, 53));
        return builder.build();
    }

    protected void onUpdateServer() {
        super.onUpdateServer();
        if (inputSlot.isEmpty()) {
            return;
        }
        ItemStack machine = inputSlot.getStack();
        if (upgradeOutputSlot.isEmpty()) {
            CompoundTag tag = machine.getTag();
            if (tag != null && tag.contains("mekData", Tag.TAG_COMPOUND)) {
                CompoundTag mekData = tag.getCompound("mekData");
                if (mekData.contains("componentUpgrade", Tag.TAG_COMPOUND)) {
                    Map<Upgrade, Integer> map = new HashMap<>(Upgrade.buildMap(mekData.getCompound("upgrades")));
                    if (map.isEmpty()) {
                        if (machineOutputSlot.isEmpty()) {
                            machineOutputSlot.setStack(machine);
                            inputSlot.setEmpty();
                            return;
                        }
                    } else {
                        for (Upgrade upgrade : Upgrade.values()) {
                            if (map.containsKey(upgrade)) {
                                int amount = map.get(upgrade);
                                map.remove(upgrade);
                                upgradeOutputSlot.setStack(UpgradeUtils.getStack(upgrade, amount));
                                CompoundTag newTag = new CompoundTag();
                                Upgrade.saveMap(map, tag);
                                mekData.put("componentUpgrade", newTag);
                                tag.put("mekData", mekData);
                                ItemStack newStack = machine.copy();
                                newStack.setTag(tag);
                                inputSlot.setStack(newStack);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

}
