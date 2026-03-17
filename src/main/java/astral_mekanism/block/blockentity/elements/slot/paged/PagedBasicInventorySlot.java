package astral_mekanism.block.blockentity.elements.slot.paged;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.container.slot.PagedInventoryContainerSlot;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.inventory.warning.ISupportsWarning;
import net.minecraft.world.item.ItemStack;

public class PagedBasicInventorySlot extends BasicInventorySlot implements IPagedSlot {

    private final int page;
    private @Nullable Consumer<ISupportsWarning<?>> warningAdder;
    private final int x;
    private final int y;

    protected PagedBasicInventorySlot(BiPredicate<@NotNull ItemStack, @NotNull AutomationType> canExtract,
            BiPredicate<@NotNull ItemStack, @NotNull AutomationType> canInsert, Predicate<@NotNull ItemStack> validator,
            @Nullable IContentsListener listener, int x, int y, int page) {
        super(canExtract, canInsert, validator, listener, x, y);
        this.page = page;
        this.x = x;
        this.y = y;
    }

    public static PagedBasicInventorySlot at(Predicate<@NotNull ItemStack> validator,
            @Nullable IContentsListener listener, int x, int y, int page) {
        return new PagedBasicInventorySlot(alwaysTrueBi, alwaysTrueBi, validator, listener, x, y, page);
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
