package astral_mekanism.block.blockentity.elements.slot.paged;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.container.slot.PagedInventoryContainerSlot;
import mekanism.api.IContentsListener;
import mekanism.common.inventory.container.slot.InventoryContainerSlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.warning.ISupportsWarning;
import net.minecraft.world.item.ItemStack;

public class PagedInputInventorySlot extends InputInventorySlot implements IPagedSlot {

    private final int page;
    private final int x;
    private final int y;
    private @Nullable Consumer<ISupportsWarning<?>> warningAdder;

    protected PagedInputInventorySlot(Predicate<@NotNull ItemStack> insertPredicate,
            Predicate<@NotNull ItemStack> isItemValid, @Nullable IContentsListener listener, int x, int y, int page) {
        super(insertPredicate, isItemValid, listener, x, y);
        this.page = page;
        this.x = x;
        this.y = y;
    }

    public static PagedInputInventorySlot at(Predicate<@NotNull ItemStack> insertPredicate,
            Predicate<@NotNull ItemStack> isItemValid, @Nullable IContentsListener listener,
            int x, int y, int page) {
        Objects.requireNonNull(insertPredicate, "Insertion check cannot be null");
        Objects.requireNonNull(isItemValid, "Item validity check cannot be null");
        return new PagedInputInventorySlot(insertPredicate, isItemValid, listener, x, y, page);
    }

    public static PagedInputInventorySlot at(Predicate<@NotNull ItemStack> isItemValid,
            @Nullable IContentsListener listener, int x, int y, int page) {
        return at(alwaysTrue, isItemValid, listener, x, y, page);
    }

    public static PagedInputInventorySlot at(@Nullable IContentsListener listener, int x, int y, int page) {
        return at(alwaysTrue, listener, x, y, page);
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
    public @Nullable InventoryContainerSlot createContainerSlot() {
        return new PagedInventoryContainerSlot(this, x, y, getSlotType(), getSlotOverlay(), warningAdder,
                this::setStackUnchecked, page);
    }

}
