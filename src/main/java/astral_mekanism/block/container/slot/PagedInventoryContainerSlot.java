package astral_mekanism.block.container.slot;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.slot.InventoryContainerSlot;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.inventory.warning.ISupportsWarning;
import net.minecraft.world.item.ItemStack;

public class PagedInventoryContainerSlot extends InventoryContainerSlot {

    public final int page;
    private boolean active;

    public PagedInventoryContainerSlot(BasicInventorySlot slot, int x, int y, ContainerSlotType slotType,
            @Nullable SlotOverlay slotOverlay, @Nullable Consumer<ISupportsWarning<?>> warningAdder,
            Consumer<ItemStack> uncheckedSetter, int page) {
        super(slot, x, y, slotType, slotOverlay, warningAdder, uncheckedSetter);
        this.page = page;
        this.active = false;
    }

    public void setPage(int page) {
        active = this.page == page;
    }

    @Override
    public boolean isActive() {
        return super.isActive() && active;
    }

}
