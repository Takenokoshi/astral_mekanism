package astral_mekanism.block.blockentity.elements.slot.paged;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.container.core.AMWindowType;
import astral_mekanism.block.container.slot.PagedInventoryContainerSlot;
import mekanism.api.IContentsListener;
import mekanism.common.inventory.container.SelectedWindowData;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.slot.InventoryContainerSlot;
import mekanism.common.inventory.slot.BasicInventorySlot;

public class PagedOutputInventorySlot extends BasicInventorySlot implements IPagedSlot {

    private final int page;
    private final int x;
    private final int y;

    private PagedOutputInventorySlot(@Nullable IContentsListener listener, int x, int y, int page) {
        super(alwaysTrueBi, internalOnly, alwaysTrue, listener, x, y);
        setSlotType(ContainerSlotType.OUTPUT);
        this.page = page;
        this.x = x;
        this.y = y;
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
        return new PagedInventoryContainerSlot(this, x, y, getSlotType(), getSlotOverlay(), null, this::setStackUnchecked,
                new SelectedWindowData(AMWindowType.PAGED, (byte) page));
    }
}
