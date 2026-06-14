package astral_mekanism.block.blockentity.elements.slot.paged;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.container.slot.PagedInventoryContainerSlot;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.IChemicalHandler;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.chemical.slurry.ISlurryHandler;
import mekanism.api.chemical.slurry.ISlurryTank;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.inventory.slot.chemical.ChemicalInventorySlot;
import mekanism.common.inventory.slot.chemical.SlurryInventorySlot;
import mekanism.common.inventory.warning.ISupportsWarning;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PagedSlurryInventorySlot extends ChemicalInventorySlot<Slurry, SlurryStack> implements IPagedSlot {

    @Nullable
    public static ISlurryHandler getCapability(ItemStack stack) {
        return getCapability(stack, Capabilities.SLURRY_HANDLER);
    }

    private final int page;
    private @Nullable Consumer<ISupportsWarning<?>> warningAdder;
    private final int x;
    private final int y;

    private PagedSlurryInventorySlot(IChemicalTank<Slurry, SlurryStack> chemicalTank, Supplier<Level> worldSupplier,
            Predicate<@NotNull ItemStack> canExtract, Predicate<@NotNull ItemStack> canInsert,
            Predicate<@NotNull ItemStack> validator, @Nullable IContentsListener listener, int x, int y, int page) {
        super(chemicalTank, worldSupplier, canExtract, canInsert, validator, listener, x, y);
        this.page = page;
        this.x = x;
        this.y = y;
    }

    private PagedSlurryInventorySlot(IChemicalTank<Slurry, SlurryStack> chemicalTank,
            Predicate<@NotNull ItemStack> canExtract, Predicate<@NotNull ItemStack> canInsert,
            Predicate<@NotNull ItemStack> validator, @Nullable IContentsListener listener, int x, int y, int page) {
        this(chemicalTank, () -> null, canExtract, canInsert, validator, listener, x, y, page);
    }

    public static PagedSlurryInventorySlot fill(ISlurryTank slurryTank, @Nullable IContentsListener listener, int x,
            int y, int page) {
        return new PagedSlurryInventorySlot(slurryTank,
                getFillExtractPredicate(slurryTank, PagedSlurryInventorySlot::getCapability),
                stack -> fillInsertCheck(slurryTank, getCapability(stack)),
                stack -> stack.getCapability(Capabilities.SLURRY_HANDLER).isPresent(), listener, x, y, page);
    }

    public static PagedSlurryInventorySlot drain(ISlurryTank slurryTank, @Nullable IContentsListener listener, int x,
            int y, int page) {
        Predicate<@NotNull ItemStack> insertPredicate = getDrainInsertPredicate(slurryTank,
                SlurryInventorySlot::getCapability);
        return new PagedSlurryInventorySlot(slurryTank, insertPredicate.negate(), insertPredicate,
                stack -> stack.getCapability(Capabilities.SLURRY_HANDLER).isPresent(), listener, x, y, page);
    }

    @Override
    public int getPage() {
        return page;
    }

    @Nullable
    @Override
    protected IChemicalHandler<Slurry, SlurryStack> getCapability() {
        return getCapability(current);
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
