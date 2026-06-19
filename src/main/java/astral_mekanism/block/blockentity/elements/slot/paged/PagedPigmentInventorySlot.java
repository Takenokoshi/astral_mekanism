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
import mekanism.api.chemical.pigment.IPigmentHandler;
import mekanism.api.chemical.pigment.IPigmentTank;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.inventory.slot.chemical.ChemicalInventorySlot;
import mekanism.common.inventory.warning.ISupportsWarning;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PagedPigmentInventorySlot extends ChemicalInventorySlot<Pigment, PigmentStack> implements IPagedSlot {

    @Nullable
    public static IPigmentHandler getCapability(ItemStack stack) {
        return getCapability(stack, Capabilities.PIGMENT_HANDLER);
    }

    private final int page;
    private @Nullable Consumer<ISupportsWarning<?>> warningAdder;
    private final int x;
    private final int y;

    private PagedPigmentInventorySlot(IChemicalTank<Pigment, PigmentStack> chemicalTank,
            Supplier<Level> worldSupplier, Predicate<@NotNull ItemStack> canExtract,
            Predicate<@NotNull ItemStack> canInsert, Predicate<@NotNull ItemStack> validator,
            @Nullable IContentsListener listener, int x, int y, int page) {
        super(chemicalTank, worldSupplier, canExtract, canInsert, validator, listener, x, y);
        this.page = page;
        this.x = x;
        this.y = y;
    }

    private PagedPigmentInventorySlot(IChemicalTank<Pigment, PigmentStack> chemicalTank,
            Predicate<@NotNull ItemStack> canExtract,
            Predicate<@NotNull ItemStack> canInsert, Predicate<@NotNull ItemStack> validator,
            @Nullable IContentsListener listener, int x, int y, int page) {
        this(chemicalTank, () -> null, canExtract, canInsert, validator, listener, x, y, page);
    }

    public static PagedPigmentInventorySlot fill(IPigmentTank pigmentTank, @Nullable IContentsListener listener, int x,
            int y, int page) {
        return new PagedPigmentInventorySlot(pigmentTank,
                getFillExtractPredicate(pigmentTank, PagedPigmentInventorySlot::getCapability),
                stack -> fillInsertCheck(pigmentTank, getCapability(stack)),
                stack -> stack.getCapability(Capabilities.PIGMENT_HANDLER).isPresent(),
                listener, x, y, page);
    }

    public static PagedPigmentInventorySlot drain(IPigmentTank pigmentTank, @Nullable IContentsListener listener, int x,
            int y, int page) {
        Predicate<@NotNull ItemStack> insertPredicate = getDrainInsertPredicate(pigmentTank,
                PagedPigmentInventorySlot::getCapability);
        return new PagedPigmentInventorySlot(pigmentTank, insertPredicate.negate(), insertPredicate,
                stack -> stack.getCapability(Capabilities.PIGMENT_HANDLER).isPresent(), listener, x, y, page);
    }

    @Nullable
    @Override
    protected IChemicalHandler<Pigment, PigmentStack> getCapability() {
        return getCapability(current);
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
