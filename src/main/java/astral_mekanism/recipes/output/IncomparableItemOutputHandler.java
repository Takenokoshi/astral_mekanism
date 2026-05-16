package astral_mekanism.recipes.output;

import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;

public class IncomparableItemOutputHandler implements IOutputHandler<ItemStack> {

    private final IInventorySlot slot;
    private final RecipeError notEnoughSpaceError;

    public IncomparableItemOutputHandler(IInventorySlot slot, RecipeError notEnoughSpaceError) {
        this.slot = slot;
        this.notEnoughSpaceError = notEnoughSpaceError;
    }

    @Override
    public void handleOutput(ItemStack toOutput, int operations) {
        if (operations == 0 || toOutput.isEmpty()) {
            return;
        }
        ItemStack output = toOutput.copy();
        if (operations > 1) {
            output.setCount(output.getCount() * operations);
        }
        slot.insertItem(output, Action.EXECUTE, AutomationType.INTERNAL);
    }

    @Override
    public void calculateOperationsCanSupport(OperationTracker tracker, ItemStack toOutput) {
        if (!toOutput.isEmpty()) {
            ItemStack stack = toOutput.copyWithCount(slot.getLimit(toOutput));
            ItemStack remainder = slot.insertItem(stack, Action.SIMULATE, AutomationType.INTERNAL);
            int amountUsed = stack.getCount() - remainder.getCount();
            int operations = amountUsed / toOutput.getCount();
            tracker.updateOperations(operations);
            if (operations == 0) {
                if (amountUsed == 0 && slot.getLimit(slot.getStack()) - slot.getCount() > 0) {
                    tracker.addError(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);
                } else {
                    tracker.addError(notEnoughSpaceError);
                }
            }
        }
    }
}
