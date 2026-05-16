package astral_mekanism.recipes.output;

import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.recipes.PressurizedReactionRecipe.PressurizedReactionRecipeOutput;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;

public class IncomparableReactionOutputHandler implements IOutputHandler<PressurizedReactionRecipeOutput> {
    private final IInventorySlot slot;
    private final RecipeError slotNotEnoughSpaceError;
    private final IGasTank tank;
    private final RecipeError tankNotEnoughSpaceError;

    public IncomparableReactionOutputHandler(IInventorySlot slot, RecipeError slotNotEnoughSpaceError, IGasTank tank,
            RecipeError tankNotEnoughSpaceError) {
        this.slot = slot;
        this.slotNotEnoughSpaceError = slotNotEnoughSpaceError;
        this.tank = tank;
        this.tankNotEnoughSpaceError = tankNotEnoughSpaceError;
    }

    @Override
    public void handleOutput(PressurizedReactionRecipeOutput toOutput, int operations) {
        if (!toOutput.gas().isEmpty()) {
            GasStack gas = toOutput.gas().copy();
            gas.setAmount(gas.getAmount() * operations);
            tank.insert(gas, Action.EXECUTE, AutomationType.INTERNAL);
        }
        if (!toOutput.item().isEmpty()) {
            ItemStack stack = toOutput.item().copyWithCount(toOutput.item().getCount() * operations);
            slot.insertItem(stack, Action.EXECUTE, AutomationType.INTERNAL);
        }
    }

    @Override
    public void calculateOperationsCanSupport(OperationTracker tracker, PressurizedReactionRecipeOutput toOutput) {
        if (!toOutput.item().isEmpty()) {
            ItemStack stack = toOutput.item().copyWithCount(slot.getLimit(toOutput.item()));
            ItemStack remainder = slot.insertItem(stack, Action.SIMULATE, AutomationType.INTERNAL);
            int amountUsed = stack.getCount() - remainder.getCount();
            int operations = amountUsed / toOutput.item().getCount();
            tracker.updateOperations(operations);
            if (operations == 0) {
                if (amountUsed == 0 && slot.getLimit(slot.getStack()) - slot.getCount() > 0) {
                    tracker.addError(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);
                } else {
                    tracker.addError(slotNotEnoughSpaceError);
                }
            }
        }
        if (!toOutput.gas().isEmpty()) {
            AMOutputHelper.calculateOperationsCanSupport(tracker, tankNotEnoughSpaceError, tank, toOutput.gas());
        }
    }
}
