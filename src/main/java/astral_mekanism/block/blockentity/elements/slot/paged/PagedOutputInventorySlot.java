package astral_mekanism.block.blockentity.elements.slot.paged;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.container.slot.PagedInventoryContainerSlot;
import mekanism.api.IContentsListener;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.inventory.warning.ISupportsWarning;

public class PagedOutputInventorySlot extends BasicInventorySlot implements IPagedSlot {

    private final int page;
    private final int x;
    private final int y;
    private @Nullable Consumer<ISupportsWarning<?>> warningAdder;

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

    public void tracksWarnings(@Nullable Consumer<ISupportsWarning<?>> warningAdder) {
        super.tracksWarnings(warningAdder);
        this.warningAdder = warningAdder;
    }

    @Override
    public @Nullable PagedInventoryContainerSlot createContainerSlot() {
        return new PagedInventoryContainerSlot(this, x, y, getSlotType(), getSlotOverlay(), warningAdder,
                this::setStackUnchecked, page);
    }
}
