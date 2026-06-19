package astral_mekanism.block.blockentity.elements.slot.paged;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.container.slot.PagedInventoryContainerSlot;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.IChemicalHandler;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.chemical.infuse.IInfusionHandler;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.inventory.slot.chemical.ChemicalInventorySlot;
import mekanism.common.inventory.warning.ISupportsWarning;
import mekanism.common.recipe.MekanismRecipeType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PagedInfusionInventorySlot extends ChemicalInventorySlot<InfuseType, InfusionStack> implements IPagedSlot {

    private final int page;
    private @Nullable Consumer<ISupportsWarning<?>> warningAdder;
    private final int x;
    private final int y;

    @Nullable
    public static IInfusionHandler getCapability(ItemStack stack) {
        return getCapability(stack, Capabilities.INFUSION_HANDLER);
    }

    protected static InfusionStack getPotentialConversion(@Nullable Level world, ItemStack itemStack) {
        return getPotentialConversion(MekanismRecipeType.INFUSION_CONVERSION, world, itemStack, InfusionStack.EMPTY);
    }

    protected PagedInfusionInventorySlot(IChemicalTank<InfuseType, InfusionStack> chemicalTank,
            Supplier<Level> worldSupplier,
            Predicate<@NotNull ItemStack> canExtract,
            Predicate<@NotNull ItemStack> canInsert,
            Predicate<@NotNull ItemStack> validator,
            @Nullable IContentsListener listener, int x, int y, int page) {
        super(chemicalTank, worldSupplier, canExtract, canInsert, validator, listener, x, y);
        this.page = page;
        this.x = x;
        this.y = y;
    }

    protected PagedInfusionInventorySlot(IChemicalTank<InfuseType, InfusionStack> chemicalTank,
            Predicate<@NotNull ItemStack> canExtract,
            Predicate<@NotNull ItemStack> canInsert,
            Predicate<@NotNull ItemStack> validator,
            @Nullable IContentsListener listener, int x, int y, int page) {
        this(chemicalTank, () -> null, canExtract, canInsert, validator, listener, x, y, page);
    }

    public static PagedInfusionInventorySlot fillOrConvert(IInfusionTank infusionTank, Supplier<Level> worldSupplier,
            @Nullable IContentsListener listener, int x, int y, int page) {
        Function<ItemStack, InfusionStack> potentialConversionSupplier = stack -> getPotentialConversion(
                worldSupplier.get(), stack);
        return new PagedInfusionInventorySlot(infusionTank, worldSupplier,
                getFillOrConvertExtractPredicate(infusionTank, PagedInfusionInventorySlot::getCapability,
                        potentialConversionSupplier),
                getFillOrConvertInsertPredicate(infusionTank, PagedInfusionInventorySlot::getCapability,
                        potentialConversionSupplier),
                stack -> {
                    if (stack.getCapability(Capabilities.INFUSION_HANDLER).isPresent()) {
                        return true;
                    }
                    InfusionStack infusionConversion = getPotentialConversion(worldSupplier.get(), stack);
                    return !infusionConversion.isEmpty() && infusionTank.isValid(infusionConversion);
                },
                listener, x, y, page);
    }

    public static PagedInfusionInventorySlot fill(IInfusionTank infusionTank,
            @Nullable IContentsListener listener, int x, int y, int page) {
        return new PagedInfusionInventorySlot(infusionTank,
                getFillExtractPredicate(infusionTank, PagedInfusionInventorySlot::getCapability),
                stack -> fillInsertCheck(infusionTank, getCapability(stack)),
                stack -> stack.getCapability(Capabilities.INFUSION_HANDLER).isPresent(),
                listener, x, y, page);
    }

    public static PagedInfusionInventorySlot drain(IInfusionTank infusionTank, @Nullable IContentsListener listener,
            int x, int y, int page) {
        Predicate<@NotNull ItemStack> insertPredicate = getDrainInsertPredicate(infusionTank,
                PagedInfusionInventorySlot::getCapability);
        return new PagedInfusionInventorySlot(infusionTank, insertPredicate.negate(), insertPredicate,
                stack -> stack.getCapability(Capabilities.INFUSION_HANDLER).isPresent(), listener, x, y, page);
    }

    @Nullable
    @Override
    protected IChemicalHandler<InfuseType, InfusionStack> getCapability() {
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
