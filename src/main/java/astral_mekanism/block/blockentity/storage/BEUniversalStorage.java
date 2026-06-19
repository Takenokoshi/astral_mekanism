package astral_mekanism.block.blockentity.storage;

import astral_mekanism.block.blockentity.elements.slot.paged.PagedBasicInventorySlot;
import mekanism.api.IContentsListener;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEUniversalStorage extends BEAbstractStorage {

    private BasicInventorySlot[] inventorySlots;

    public BEUniversalStorage(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected void setUpItemConfig(ConfigInfo itemConfig) {
        itemConfig.addSlotInfo(DataType.INPUT, new InventorySlotInfo(true, false, inventorySlots));
        itemConfig.addSlotInfo(DataType.OUTPUT, new InventorySlotInfo(false, true, inventorySlots));
        itemConfig.addSlotInfo(DataType.INPUT_OUTPUT, new InventorySlotInfo(true, true, inventorySlots));
        itemConfig.setCanEject(true);
    }

    @Override
    protected FloatingLong getInitialBaseEnergyCapacity() {
        return FloatingLong.create(1l << 16);
    }

    @Override
    protected void addSlots(InventorySlotHelper builder, IContentsListener listener) {
        inventorySlots = new BasicInventorySlot[81];
        for (int page = 0; page < 3; page++) {
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 9; x++) {
                    builder.addSlot(inventorySlots[x + 9 * y + 27 * page] = PagedBasicInventorySlot
                            .at(stack -> true, listener, x * 18 + 8, y * 18 + 18, page));
                }
            }
        }
    }

    @Override
    public int getPagesForItem() {
        return 3;
    }

}
