package astral_mekanism.recipes.input;

import astral_mekanism.util.ArrayItemStackUtils;
import mekanism.api.Action;
import mekanism.api.MekanismAPI;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.api.recipes.inputs.IInputHandler;
import net.minecraft.world.item.ItemStack;

public class AMInputHelper {
    public static IInputHandler<ItemStack[]> getInputHandler(IInventorySlot[] slots, RecipeError notEnoughError) {
        return new IInputHandler<>() {

            @Override
            public void calculateOperationsCanSupport(OperationTracker tracker, ItemStack[] stacks,
                    int usageMultiplier) {
                if (usageMultiplier > 0 && ArrayItemStackUtils.includeNullOrEmpty(stacks)) {
                    ItemStack[] iui = ArrayItemStackUtils.copyWithLength(stacks, slots.length);
                    for (int i = 0; i < slots.length; i++) {
                        calculateOperationsCanSupport(tracker, iui[i], usageMultiplier, i);
                    }
                }
            }

            private void calculateOperationsCanSupport(OperationTracker tracker, ItemStack recipeInput,
                    int usageMultiplier, int index) {
                if (usageMultiplier > 0) {
                    if (!recipeInput.isEmpty()) {
                        int operations = getInput()[index].getCount() / (recipeInput.getCount() * usageMultiplier);
                        if (operations > 0) {
                            tracker.updateOperations(operations);
                            return;
                        }
                    }
                    tracker.resetProgress(notEnoughError);
                }
            }

            @Override
            public ItemStack[] getInput() {
                ItemStack[] result = new ItemStack[slots.length];
                for (int i = 0; i < slots.length; i++) {
                    result[i] = slots[i].getStack();
                }
                return result;
            }

            @Override
            public ItemStack[] getRecipeInput(InputIngredient<ItemStack[]> recipeIngredient) {
                ItemStack[] input = getInput();
                if (ArrayItemStackUtils.includeNullOrEmpty(input)) {
                    return ArrayItemStackUtils.createArrayFilledEmpty(slots.length);
                }
                return recipeIngredient.getMatchingInstance(input);
            }

            @Override
            public void use(ItemStack[] recipeInputsaaa, int operations) {
                ItemStack[] recipeInputs = ArrayItemStackUtils.copyWithLength(recipeInputsaaa, slots.length);
                if (operations > 0 && !ArrayItemStackUtils.includeNullOrEmpty(recipeInputs)) {
                    for (int i = 0; i < slots.length; i++) {
                        int amount = recipeInputs[i].getCount() * operations;
                        logMismatchedStackSize(slots[i].shrinkStack(amount, Action.EXECUTE), amount);
                    }
                }
            }

        };
    }

    private static void logMismatchedStackSize(long actual, long expected) {
        if (expected != actual) {
            MekanismAPI.logger.error("Stack size changed by a different amount ({}) than requested ({}).", actual,
                    expected, new Exception());
        }
    }
}
