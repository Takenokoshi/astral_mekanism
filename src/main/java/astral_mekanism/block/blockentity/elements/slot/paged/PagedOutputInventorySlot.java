package astral_mekanism.block.blockentity.elements.slot.paged;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.container.core.AMWindowType;
import mekanism.api.IContentsListener;
import mekanism.common.inventory.container.SelectedWindowData;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.slot.InventoryContainerSlot;
import mekanism.common.inventory.container.slot.VirtualInventoryContainerSlot;
import mekanism.common.inventory.slot.BasicInventorySlot;

public class PagedOutputInventorySlot extends BasicInventorySlot implements IPagedSlot {

    private final int page;

    private PagedOutputInventorySlot(@Nullable IContentsListener listener, int x, int y, int page) {
        super(alwaysTrueBi, internalOnly, alwaysTrue, listener, x, y);
        setSlotType(ContainerSlotType.OUTPUT);
        this.page = page;
    }

    public static PagedOutputInventorySlot at(@Nullable IContentsListener listener, int x, int y, int page) {
        return new PagedOutputInventorySlot(listener, x, y, page);
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public @Nullable InventoryContainerSlot createContainerSlot() {
        return new VirtualInventoryContainerSlot(this,
                new SelectedWindowData(AMWindowType.PAGED, page > 127 ? (byte) 0 : (byte) page), getSlotOverlay(),
                this::setStackUnchecked);
    }
}
