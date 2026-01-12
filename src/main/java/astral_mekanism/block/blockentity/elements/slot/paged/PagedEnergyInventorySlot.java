package astral_mekanism.block.blockentity.elements.slot.paged;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.container.slot.PagedInventoryContainerSlot;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.energy.IStrictEnergyHandler;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.ItemStackToEnergyRecipe;
import mekanism.common.integration.energy.EnergyCompatUtils;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.util.MekanismUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PagedEnergyInventorySlot extends BasicInventorySlot implements IPagedSlot {

    private final int x;
    private final int y;
    private final int page;

    private static FloatingLong getPotentialConversion(@Nullable Level world, ItemStack itemStack) {
        ItemStackToEnergyRecipe foundRecipe = MekanismRecipeType.ENERGY_CONVERSION.getInputCache()
                .findTypeBasedRecipe(world, itemStack);
        return foundRecipe == null ? FloatingLong.ZERO : foundRecipe.getOutput(itemStack);
    }

    /**
     * Fills the container from this item OR converts the given item to energy
     */
    public static PagedEnergyInventorySlot fillOrConvert(IEnergyContainer energyContainer,
            Supplier<Level> worldSupplier, @Nullable IContentsListener listener, int x, int y,int page) {
        Objects.requireNonNull(energyContainer, "Energy container cannot be null");
        Objects.requireNonNull(worldSupplier, "World supplier cannot be null");
        return new PagedEnergyInventorySlot(energyContainer, worldSupplier, stack -> {
            return !fillInsertCheck(stack) && getPotentialConversion(worldSupplier.get(), stack).isZero();
        }, stack -> {
            if (fillInsertCheck(stack)) {
                return true;
            }
            return !getPotentialConversion(worldSupplier.get(), stack).isZero();
        }, stack -> {
            return EnergyCompatUtils.hasStrictEnergyHandler(stack)
                    || !getPotentialConversion(worldSupplier.get(), stack).isZero();
        }, listener, x, y,page);
    }

    public static PagedEnergyInventorySlot fill(IEnergyContainer energyContainer, @Nullable IContentsListener listener,
            int x, int y,int page) {
        Objects.requireNonNull(energyContainer, "Energy container cannot be null");
        return new PagedEnergyInventorySlot(energyContainer, stack -> !fillInsertCheck(stack),
                PagedEnergyInventorySlot::fillInsertCheck,
                EnergyCompatUtils::hasStrictEnergyHandler, listener, x, y,page);
    }

    public static PagedEnergyInventorySlot drain(IEnergyContainer energyContainer, @Nullable IContentsListener listener,
            int x, int y,int page) {
        Objects.requireNonNull(energyContainer, "Energy container cannot be null");
        Predicate<@NotNull ItemStack> insertPredicate = stack -> {
            IStrictEnergyHandler itemEnergyHandler = EnergyCompatUtils.getStrictEnergyHandler(stack);
            if (itemEnergyHandler == null) {
                return false;
            }
            FloatingLong storedEnergy = energyContainer.getEnergy();
            if (storedEnergy.isZero()) {
                // If the energy container is empty, accept the energy item as long as it is not
                // full
                for (int container = 0; container < itemEnergyHandler.getEnergyContainerCount(); container++) {
                    if (!itemEnergyHandler.getNeededEnergy(container).isZero()) {
                        // True if we have any space in this container
                        return true;
                    }
                }
                return false;
            }
            return itemEnergyHandler.insertEnergy(storedEnergy, Action.SIMULATE).smallerThan(storedEnergy);
        };
        return new PagedEnergyInventorySlot(energyContainer, insertPredicate.negate(), insertPredicate,
                EnergyCompatUtils::hasStrictEnergyHandler, listener, x, y,page);
    }

    private static boolean fillInsertCheck(ItemStack stack) {
        IStrictEnergyHandler itemEnergyHandler = EnergyCompatUtils.getStrictEnergyHandler(stack);
        return itemEnergyHandler != null
                && !itemEnergyHandler.extractEnergy(FloatingLong.MAX_VALUE, Action.SIMULATE).isZero();
    }

    private final Supplier<Level> worldSupplier;
    private final IEnergyContainer energyContainer;

    private PagedEnergyInventorySlot(IEnergyContainer energyContainer, Predicate<@NotNull ItemStack> canExtract,
            Predicate<@NotNull ItemStack> canInsert,
            Predicate<@NotNull ItemStack> validator, @Nullable IContentsListener listener, int x, int y,int page) {
        this(energyContainer, () -> null, canExtract, canInsert, validator, listener, x, y,page);
    }

    private PagedEnergyInventorySlot(IEnergyContainer energyContainer, Supplier<Level> worldSupplier,
            Predicate<@NotNull ItemStack> canExtract,
            Predicate<@NotNull ItemStack> canInsert, Predicate<@NotNull ItemStack> validator,
            @Nullable IContentsListener listener, int x, int y, int page) {
        super(canExtract, canInsert, validator, listener, x, y);
        this.x = x;
        this.y = y;
        this.page = page;
        this.energyContainer = energyContainer;
        this.worldSupplier = worldSupplier;
        setSlotType(ContainerSlotType.POWER);
        setSlotOverlay(SlotOverlay.POWER);
    }

    public void fillContainerOrConvert() {
        if (!isEmpty() && !energyContainer.getNeeded().isZero()) {
            if (!fillContainerFromItem()) {
                ItemStackToEnergyRecipe foundRecipe = MekanismRecipeType.ENERGY_CONVERSION.getInputCache()
                        .findFirstRecipe(worldSupplier.get(), current);
                if (foundRecipe != null) {
                    ItemStack itemInput = foundRecipe.getInput().getMatchingInstance(current);
                    if (!itemInput.isEmpty()) {
                        FloatingLong output = foundRecipe.getOutput(itemInput);
                        if (!output.isZero()
                                && energyContainer.insert(output, Action.SIMULATE, AutomationType.MANUAL).isZero()) {
                            MekanismUtils.logExpectedZero(
                                    energyContainer.insert(output, Action.EXECUTE, AutomationType.MANUAL));
                            int amountUsed = itemInput.getCount();
                            MekanismUtils.logMismatchedStackSize(shrinkStack(amountUsed, Action.EXECUTE), amountUsed);
                        }
                    }
                }
            }
        }
    }

    public void fillContainer() {
        if (!isEmpty() && !energyContainer.getNeeded().isZero()) {
            fillContainerFromItem();
        }
    }

    private boolean fillContainerFromItem() {
        IStrictEnergyHandler itemEnergyHandler = EnergyCompatUtils.getStrictEnergyHandler(current);
        if (itemEnergyHandler != null) {
            boolean didTransfer = false;
            for (int container = 0; container < itemEnergyHandler.getEnergyContainerCount(); container++) {
                FloatingLong energyInItem = itemEnergyHandler.getEnergy(container);
                if (!energyInItem.isZero()) {
                    FloatingLong simulatedRemainder = energyContainer.insert(energyInItem, Action.SIMULATE,
                            AutomationType.INTERNAL);
                    if (simulatedRemainder.smallerThan(energyInItem)) {
                        FloatingLong extractedEnergy = itemEnergyHandler.extractEnergy(container,
                                energyInItem.subtract(simulatedRemainder), Action.EXECUTE);
                        if (!extractedEnergy.isZero()) {
                            MekanismUtils.logExpectedZero(
                                    energyContainer.insert(extractedEnergy, Action.EXECUTE, AutomationType.INTERNAL));
                            didTransfer = true;
                            if (energyContainer.getNeeded().isZero()) {
                                break;
                            }
                        }
                    }
                }
            }
            if (didTransfer) {
                onContentsChanged();
                return true;
            }
        }
        return false;
    }

    public void drainContainer() {
        if (!isEmpty() && !energyContainer.isEmpty()) {
            IStrictEnergyHandler itemEnergyHandler = EnergyCompatUtils.getStrictEnergyHandler(current);
            if (itemEnergyHandler != null) {
                FloatingLong storedEnergy = energyContainer.getEnergy();
                FloatingLong simulatedRemainder = itemEnergyHandler.insertEnergy(storedEnergy, Action.SIMULATE);
                if (simulatedRemainder.smallerThan(storedEnergy)) {
                    FloatingLong extractedEnergy = energyContainer.extract(storedEnergy.subtract(simulatedRemainder),
                            Action.EXECUTE, AutomationType.INTERNAL);
                    if (!extractedEnergy.isZero()) {
                        MekanismUtils.logExpectedZero(itemEnergyHandler.insertEnergy(extractedEnergy, Action.EXECUTE));
                        onContentsChanged();
                    }
                }
            }
        }
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public @Nullable PagedInventoryContainerSlot createContainerSlot() {
        return new PagedInventoryContainerSlot(this, x, y, getSlotType(), getSlotOverlay(), null,
                this::setStackUnchecked, page);
    }

}
