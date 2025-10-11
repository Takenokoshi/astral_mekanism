package astral_mekanism.block.blockentity.prefab;

import mekanism.api.inventory.IInventorySlot;

public interface BEAbstractItemSortble {

    public abstract SlotKind getSlotKind(IInventorySlot slot);

    public abstract IInventorySlot[] getSlots(SlotKind slotType);

    public enum SlotKind {
        INPUT, OUTPUT, FILTER, OTHER;

        public boolean qMSOutputAble() {
            return this == INPUT || this == OUTPUT|| this==OTHER;
        }
        
    }
}
