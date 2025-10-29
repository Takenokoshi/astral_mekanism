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
    public static IOutputHandler<DoubleItemStackOutput> getOutputHandler(IInventorySlot slotA,
            CachedRecipe.OperationTracker.RecipeError errorA, IInventorySlot slotB,
            CachedRecipe.OperationTracker.RecipeError errorB) {
        Objects.requireNonNull(slotA);
        Objects.requireNonNull(slotB);
        Objects.requireNonNull(errorA);
        Objects.requireNonNull(errorB);
        return new IOutputHandler<DoubleItemStackOutput>() {
            @Override
            public void handleOutput(DoubleItemStackOutput output, int operations) {
                AMOutputHelper.handleOutput(slotA, output.itemA(), operations);
                AMOutputHelper.handleOutput(slotB, output.itemB(), operations);
            };

            @Override
            public void calculateOperationsCanSupport(OperationTracker arg0, DoubleItemStackOutput arg1) {
                AMOutputHelper.calculateOperationsCanSupport(arg0, errorA, slotA, arg1.itemA());
                AMOutputHelper.calculateOperationsCanSupport(arg0, errorB, slotB, arg1.itemB());
            };
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
            CachedRecipe.OperationTracker.RecipeError notEnoughSpace, IInventorySlot slot, ItemStack toOutput) {
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
