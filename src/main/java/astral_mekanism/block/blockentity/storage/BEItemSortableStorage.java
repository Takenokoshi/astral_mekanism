package astral_mekanism.block.blockentity.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import astral_mekanism.block.blockentity.elements.slot.paged.PagedBasicInventorySlot;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedInputInventorySlot;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedOutputInventorySlot;
import astral_mekanism.enumexpansion.AMEDataType;
import astral_mekanism.item.SortableStorageFilterCardItem;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BEItemSortableStorage extends BEAbstractStorage {

    private PagedInputInventorySlot[] inputSlots;
    private PagedBasicInventorySlot[][] filterSlots;
    private PagedOutputInventorySlot[][] outputSlots;
    private PagedOutputInventorySlot[] leftItemOutputSlots;

    private List<ItemStack>[] filterCache;

    public BEItemSortableStorage(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void presetVariables() {
        super.presetVariables();
        filterCache = new List[2];
        for (int i = 0; i < 2; i++) {
            filterCache[i] = new ArrayList<>();
        }
    }

    @Override
    protected void setUpItemConfig(ConfigInfo itemConfig) {
        itemConfig.addSlotInfo(DataType.INPUT, new InventorySlotInfo(true, false, inputSlots));
        itemConfig.addSlotInfo(DataType.OUTPUT_1, new InventorySlotInfo(false, true, outputSlots[0]));
        itemConfig.addSlotInfo(DataType.OUTPUT_2, new InventorySlotInfo(false, true, outputSlots[1]));
        itemConfig.addSlotInfo(AMEDataType.OUTPUTleft, new InventorySlotInfo(false, true, leftItemOutputSlots));
        itemConfig.addSlotInfo(AMEDataType.INPUT_OUTPUT1,
                new InventorySlotInfo(true, true, Stream.of(inputSlots, outputSlots[0])
                        .flatMap(Arrays::stream)
                        .toArray(IInventorySlot[]::new)));
        itemConfig.addSlotInfo(AMEDataType.INPUT_OUTPUT2,
                new InventorySlotInfo(true, true, Stream.of(inputSlots, outputSlots[1])
                        .flatMap(Arrays::stream)
                        .toArray(IInventorySlot[]::new)));
        itemConfig.addSlotInfo(AMEDataType.INPUT_OUTPUTleft,
                new InventorySlotInfo(true, true, Stream.of(inputSlots, leftItemOutputSlots)
                        .flatMap(Arrays::stream)
                        .toArray(IInventorySlot[]::new)));
        itemConfig.setCanEject(true);
    }

    @Override
    protected FloatingLong getInitialBaseEnergyCapacity() {
        return FloatingLong.create(1l << 16);
    }

    @Override
    protected void addSlots(InventorySlotHelper builder, IContentsListener listener) {
        inputSlots = new PagedInputInventorySlot[27];
        for (int i = 0; i < 27; i++) {
            builder.addSlot(inputSlots[i] = PagedInputInventorySlot.at(listener, 8 + i % 9 * 18, 18 + i / 9 * 18, 0))
                    .setSlotOverlay(SlotOverlay.INPUT);
        }
        filterSlots = new PagedBasicInventorySlot[2][9];
        outputSlots = new PagedOutputInventorySlot[2][18];
        for (int index = 0; index < 2; index++) {
            int value = index;
            for (int filterIndex = 0; filterIndex < 9; filterIndex++) {
                builder.addSlot(filterSlots[index][filterIndex] = PagedBasicInventorySlot.at(
                        stack -> stack.getItem() instanceof SortableStorageFilterCardItem,
                        () -> {
                            listener.onContentsChanged();
                            createFilterCache(value);
                        },
                        8 + filterIndex * 18, 18, index + 1)).setSlotOverlay(SlotOverlay.CHECK);
            }
            for (int outputIndex = 0; outputIndex < 18; outputIndex++) {
                builder.addSlot(outputSlots[index][outputIndex] = PagedOutputInventorySlot.at(
                        listener, 8 + outputIndex % 9 * 18, 36 + outputIndex / 9 * 18, index + 1))
                        .setSlotOverlay(SlotOverlay.OUTPUT);
            }
        }
        leftItemOutputSlots = new PagedOutputInventorySlot[27];
        for (int i = 0; i < 27; i++) {
            builder.addSlot(leftItemOutputSlots[i] = PagedOutputInventorySlot.at(
                    listener, 8 + i % 9 * 18, 18 + i / 9 * 18, 3))
                    .setSlotOverlay(SlotOverlay.OUTPUT);
        }
    }

    private void createFilterCache(int filterIndex) {
        filterCache[filterIndex].clear();
        for (int index = 0; index < 9; index++) {
            if (filterSlots[filterIndex][index].isEmpty() || !(filterSlots[filterIndex][index].getStack()
                    .getItem() instanceof SortableStorageFilterCardItem filterCardItem)) {
                continue;
            }
            filterCache[filterIndex].addAll(filterCardItem.getFilterItems(filterSlots[filterIndex][index].getStack()));
        }
    }

    @Override
    public int getPagesForItem() {
        return 4;
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        for (int sortingIndex = 0; sortingIndex < 27; sortingIndex++) {
            ItemStack sorting = inputSlots[sortingIndex].getStack().copy();
            if (sorting.isEmpty()) {
                continue;
            }
            boolean sortCompleted = false;
            for (int filterIndex = 0; filterIndex < 2; filterIndex++) {
                if (sortCompleted) {
                    break;
                }
                for (ItemStack filter : filterCache[filterIndex]) {
                    if (sortCompleted) {
                        break;
                    }
                    if (ItemStack.isSameItemSameTags(filter, sorting)) {
                        for (PagedOutputInventorySlot outputSlot : outputSlots[filterIndex]) {
                            sorting = outputSlot.insertItem(sorting, Action.EXECUTE, AutomationType.INTERNAL);
                            if (sorting.isEmpty()) {
                                sortCompleted = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (!sortCompleted) {
                for (PagedOutputInventorySlot outputSlot : leftItemOutputSlots) {
                    sorting = outputSlot.insertItem(sorting, Action.EXECUTE, AutomationType.INTERNAL);
                    if (sorting.isEmpty()) {
                        sortCompleted = true;
                        break;
                    }
                }
            }
            if (sortCompleted) {
                inputSlots[sortingIndex].setEmpty();
            } else {
                inputSlots[sortingIndex].setStack(sorting);
            }
        }
    }

}
