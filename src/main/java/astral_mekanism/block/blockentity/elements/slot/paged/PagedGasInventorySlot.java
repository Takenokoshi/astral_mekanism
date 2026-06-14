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
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.recipes.ItemStackToGasRecipe;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.inventory.slot.chemical.ChemicalInventorySlot;
import mekanism.common.inventory.warning.ISupportsWarning;
import mekanism.common.recipe.MekanismRecipeType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PagedGasInventorySlot extends ChemicalInventorySlot<Gas, GasStack> implements IPagedSlot {

    @Nullable
    public static IGasHandler getCapability(ItemStack stack) {
        return getCapability(stack, Capabilities.GAS_HANDLER);
    }

    private static GasStack getPotentialConversion(@Nullable Level world, ItemStack itemStack) {
        return getPotentialConversion(MekanismRecipeType.GAS_CONVERSION, world, itemStack, GasStack.EMPTY);
    }

    private final int page;
    private @Nullable Consumer<ISupportsWarning<?>> warningAdder;
    private final int x;
    private final int y;

    private PagedGasInventorySlot(IChemicalTank<Gas, GasStack> chemicalTank, Supplier<Level> worldSupplier,
            Predicate<@NotNull ItemStack> canExtract, Predicate<@NotNull ItemStack> canInsert,
            Predicate<@NotNull ItemStack> validator, @Nullable IContentsListener listener, int x, int y, int page) {
        super(chemicalTank, worldSupplier, canExtract, canInsert, validator, listener, x, y);
        this.page = page;
        this.x = x;
        this.y = y;
    }

    private PagedGasInventorySlot(IChemicalTank<Gas, GasStack> chemicalTank,
            Predicate<@NotNull ItemStack> canExtract, Predicate<@NotNull ItemStack> canInsert,
            Predicate<@NotNull ItemStack> validator, @Nullable IContentsListener listener, int x, int y, int page) {
        this(chemicalTank, () -> null, canExtract, canInsert, validator, listener, x, y, page);
    }

    public static PagedGasInventorySlot fillOrConvert(IGasTank gasTank, Supplier<Level> worldSupplier,
            @Nullable IContentsListener listener, int x, int y, int page) {
        Function<ItemStack, GasStack> potentialConversionSupplier = stack -> getPotentialConversion(worldSupplier.get(),
                stack);
        return new PagedGasInventorySlot(gasTank, worldSupplier,
                getFillOrConvertExtractPredicate(gasTank, PagedGasInventorySlot::getCapability, potentialConversionSupplier),
                getFillOrConvertInsertPredicate(gasTank, PagedGasInventorySlot::getCapability, potentialConversionSupplier),
                stack -> {
                    if (stack.getCapability(Capabilities.GAS_HANDLER).isPresent()) {
                        return true;
                    }
                    GasStack gasConversion = getPotentialConversion(worldSupplier.get(), stack);
                    return !gasConversion.isEmpty() && gasTank.isValid(gasConversion);
                },
                listener, x, y, page);
    }

    public static PagedGasInventorySlot fill(IGasTank gasTank, @Nullable IContentsListener listener, int x, int y,
            int page) {
        return new PagedGasInventorySlot(gasTank,
                getFillExtractPredicate(gasTank, PagedGasInventorySlot::getCapability),
                stack -> fillInsertCheck(gasTank, getCapability(stack)),
                stack -> stack.getCapability(Capabilities.GAS_HANDLER).isPresent(),
                listener, x, y, page);
    }

    public static PagedGasInventorySlot drain(IGasTank gasTank, @Nullable IContentsListener listener, int x, int y,
            int page) {
        Predicate<@NotNull ItemStack> insertPredicate = getDrainInsertPredicate(gasTank,
                PagedGasInventorySlot::getCapability);
        return new PagedGasInventorySlot(gasTank, insertPredicate.negate(), insertPredicate,
                stack -> stack.getCapability(Capabilities.GAS_HANDLER).isPresent(), listener, x, y, page);
    }

    @Override
    public int getPage() {
        return page;
    }

    @Nullable
    @Override
    protected IChemicalHandler<Gas, GasStack> getCapability() {
        return getCapability(current);
    }

    @Nullable
    @Override
    protected ItemStackToGasRecipe getConversionRecipe(@Nullable Level world, ItemStack stack) {
        return MekanismRecipeType.GAS_CONVERSION.getInputCache().findFirstRecipe(world, stack);
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
