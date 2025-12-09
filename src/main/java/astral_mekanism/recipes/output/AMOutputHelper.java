package astral_mekanism.recipes.output;

import java.util.Objects;

import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;

@NothingNullByDefault
public class AMOutputHelper {
    public static IOutputHandler<DoubleItemOutput> getOutputHandler(IInventorySlot slotA,
            RecipeError errorA, IInventorySlot slotB,
            RecipeError errorB) {
        Objects.requireNonNull(slotA);
        Objects.requireNonNull(slotB);
        Objects.requireNonNull(errorA);
        Objects.requireNonNull(errorB);
        return new IOutputHandler<DoubleItemOutput>() {
            @Override
            public void handleOutput(DoubleItemOutput output, int operations) {
                AMOutputHelper.handleOutput(slotA, output.itemA(), operations);
                AMOutputHelper.handleOutput(slotB, output.itemB(), operations);
            };

            @Override
            public void calculateOperationsCanSupport(OperationTracker arg0, DoubleItemOutput arg1) {
                AMOutputHelper.calculateOperationsCanSupport(arg0, errorA, slotA, arg1.itemA());
                AMOutputHelper.calculateOperationsCanSupport(arg0, errorB, slotB, arg1.itemB());
            };
        };
    }

    public static IOutputHandler<TripleItemOutput> getOutputhandler(
            IInventorySlot slotA, RecipeError errorA,
            IInventorySlot slotB, RecipeError errorB,
            IInventorySlot slotC, RecipeError errorC) {
        return new IOutputHandler<TripleItemOutput>() {

            @Override
            public void calculateOperationsCanSupport(OperationTracker tracker, TripleItemOutput output) {
                AMOutputHelper.calculateOperationsCanSupport(tracker, errorA, slotA, output.itemA());
                AMOutputHelper.calculateOperationsCanSupport(tracker, errorB, slotB, output.itemB());
                AMOutputHelper.calculateOperationsCanSupport(tracker, errorC, slotC, output.itemC());
            }

            @Override
            public void handleOutput(TripleItemOutput output, int operations) {
                AMOutputHelper.handleOutput(slotA, output.itemA(), operations);
                AMOutputHelper.handleOutput(slotB, output.itemB(), operations);
                AMOutputHelper.handleOutput(slotC, output.itemC(), operations);
            }

        };
    }

    private static void handleOutput(IInventorySlot inventorySlot, ItemStack toOutput, int operations) {
        if (operations != 0 && !toOutput.isEmpty()) {
            ItemStack output = toOutput.copy();
            if (operations > 1) {
                output.setCount(output.getCount() * operations);
            }

            inventorySlot.insertItem(output, Action.EXECUTE, AutomationType.INTERNAL);
        }
    }

    private static void calculateOperationsCanSupport(CachedRecipe.OperationTracker tracker,
            RecipeError notEnoughSpace, IInventorySlot slot, ItemStack toOutput) {
        if (!toOutput.isEmpty()) {
            ItemStack output = toOutput.copyWithCount(toOutput.getMaxStackSize());
            ItemStack remainder = slot.insertItem(output, Action.SIMULATE, AutomationType.INTERNAL);
            int amountUsed = output.getCount() - remainder.getCount();
            int operations = amountUsed / toOutput.getCount();
            tracker.updateOperations(operations);
            if (operations == 0) {
                if (amountUsed == 0 && slot.getLimit(slot.getStack()) - slot.getCount() > 0) {
                    tracker.addError(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);
                } else {
                    tracker.addError(notEnoughSpace);
                }
            }
        }

    }

}
