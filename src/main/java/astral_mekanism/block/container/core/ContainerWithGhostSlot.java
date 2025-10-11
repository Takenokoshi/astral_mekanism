package astral_mekanism.block.container.core;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.elements.slot.GhostInventorySlot;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.inventory.container.SelectedWindowData;
import mekanism.common.inventory.container.slot.ArmorSlot;
import mekanism.common.inventory.container.slot.HotBarSlot;
import mekanism.common.inventory.container.slot.InventoryContainerSlot;
import mekanism.common.inventory.container.slot.MainInventorySlot;
import mekanism.common.inventory.container.slot.OffhandSlot;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class ContainerWithGhostSlot<TILE extends TileEntityMekanism> extends MekanismTileContainer<TILE> {

    public ContainerWithGhostSlot(ContainerTypeRegistryObject<?> type, int id, Inventory inv, @NotNull TILE tile) {
        super(type, id, inv, tile);

    }
    private List<InventoryContainerSlot> getNonGhostSlot() {
        List<InventoryContainerSlot> result = new ArrayList<InventoryContainerSlot>();
        for (InventoryContainerSlot slot : inventoryContainerSlots) {
            IInventorySlot inventorySlot = slot.getInventorySlot();
            if (!(inventorySlot instanceof GhostInventorySlot)) {
                result.add(slot);
            }
        }
        return result;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slotID) {
        Slot slot = this.slots.get(slotID);
        if (slot instanceof InventoryContainerSlot containerSlot) {
            if (containerSlot.getInventorySlot() instanceof GhostInventorySlot) {
                return ItemStack.EMPTY;
            }
        } else if (slot instanceof MainInventorySlot || slot instanceof HotBarSlot || slot instanceof ArmorSlot
                || slot instanceof OffhandSlot) {
            SelectedWindowData selectedWindow = player.level().isClientSide ? this.getSelectedWindow()
                    : this.getSelectedWindow(player.getUUID());
            ItemStack slotStack = slot.getItem().copy();
            ItemStack stackToInsert = insertItem(getNonGhostSlot(), slotStack, true, selectedWindow);
            return stackToInsert.getCount() == slotStack.getCount() ? ItemStack.EMPTY
                    : this.transferSuccess(slot, player, slotStack, stackToInsert);
        }
        return super.quickMoveStack(player, slotID);
    }

}
