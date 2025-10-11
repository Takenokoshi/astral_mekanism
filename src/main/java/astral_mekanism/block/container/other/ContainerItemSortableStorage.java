package astral_mekanism.block.container.other;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.prefab.BEAbstractStorage;
import astral_mekanism.block.blockentity.core.BlockEntityUtils;
import astral_mekanism.block.blockentity.prefab.BEAbstractItemSortble;
import astral_mekanism.block.blockentity.prefab.BEAbstractItemSortble.SlotKind;
import astral_mekanism.block.container.prefab.ContainerAbstractStorage;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.inventory.container.slot.HotBarSlot;
import mekanism.common.inventory.container.slot.InventoryContainerSlot;
import mekanism.common.inventory.container.slot.MainInventorySlot;
import mekanism.common.item.ItemEnergized;
import mekanism.common.item.block.ItemBlockChemicalTank;
import mekanism.common.item.block.machine.ItemBlockFluidTank;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ContainerItemSortableStorage<STORAGE extends BEAbstractStorage & BEAbstractItemSortble>
        extends ContainerAbstractStorage<STORAGE> {

    public ContainerItemSortableStorage(ContainerTypeRegistryObject<?> type, int id, Inventory inv,
            @NotNull STORAGE tile) {
        super(type, id, inv, tile);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slotID) {
        Slot currentSlot = this.slots.get(slotID);
        if (currentSlot == null || !currentSlot.hasItem()) {
            return ItemStack.EMPTY;
        } else {
            if (currentSlot instanceof InventoryContainerSlot inventoryContainerSlot) {
                IInventorySlot inventorySlot = inventoryContainerSlot.getInventorySlot();
                SlotKind slotKind = tile.getSlotKind(inventorySlot);
                if (slotKind.qMSOutputAble()) {
                    ItemStack left = inventorySlot.getStack().copy();
                    left = insertItem(this.hotBarSlots, left, true, null);
                    left = insertItem(this.hotBarSlots, left, false, null);
                    left = insertItem(this.mainInventorySlots, left, true, null);
                    left = insertItem(this.mainInventorySlots, left, false, null);
                    return inventorySlot.getCount() == left.getCount() ? ItemStack.EMPTY
                            : transferSuccess(currentSlot, player, inventorySlot.getStack(), left);
                } else {
                    return ItemStack.EMPTY;
                }
            } else {
                if (currentSlot instanceof MainInventorySlot || currentSlot instanceof HotBarSlot) {
                    ItemStack left = currentSlot.getItem().copy();
                    Item item = left.getItem();
                    if (item instanceof ItemEnergized) {
                        left = BlockEntityUtils.insertItem(tile.getEnergyInventorySlots(), left);
                    }
                    if (item instanceof ItemBlockFluidTank) {
                        left = BlockEntityUtils.insertItem(tile.getFluidInventorySlots(), left);
                    }
                    if (item instanceof ItemBlockChemicalTank) {
                        left = BlockEntityUtils.insertItem(tile.getChemicalInventorySlots(), left);
                    }
                    left = BlockEntityUtils.insertItem(tile.getSlots(SlotKind.INPUT), left);
                    return currentSlot.getItem().getCount() == left.getCount() ? ItemStack.EMPTY
                            : transferSuccess(currentSlot, player, currentSlot.getItem(), left);
                } else {
                    return ItemStack.EMPTY;
                }
            }
        }
    }

}
