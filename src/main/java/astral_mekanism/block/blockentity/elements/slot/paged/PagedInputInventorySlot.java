package astral_mekanism.block.blockentity.elements.slot.paged;

import java.util.Objects;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.container.core.AMWindowType;
import mekanism.api.IContentsListener;
import mekanism.common.inventory.container.SelectedWindowData;
import mekanism.common.inventory.container.slot.InventoryContainerSlot;
import mekanism.common.inventory.container.slot.VirtualInventoryContainerSlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import net.minecraft.world.item.ItemStack;

public class PagedInputInventorySlot extends InputInventorySlot implements IPagedSlot {

    private final int page;

    protected PagedInputInventorySlot(Predicate<@NotNull ItemStack> insertPredicate,
            Predicate<@NotNull ItemStack> isItemValid, @Nullable IContentsListener listener, int x, int y, int page) {
        super(insertPredicate, isItemValid, listener, x, y);
        this.page = page;
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
    public @Nullable InventoryContainerSlot createContainerSlot() {
        return new VirtualInventoryContainerSlot(this,
                new SelectedWindowData(AMWindowType.PAGED, page > 127 ? (byte) 0 : (byte) page), getSlotOverlay(),
                this::setStackUnchecked);
    }

}
