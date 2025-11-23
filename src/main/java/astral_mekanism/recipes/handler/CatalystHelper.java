package astral_mekanism.recipes.handler;

import mekanism.api.inventory.IInventorySlot;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.ingredients.InputIngredient;
import net.minecraft.world.item.ItemStack;

public class CatalystHelper {
    public static ICatalystHandler<ItemStack> getCatalystHandler(IInventorySlot slot, RecipeError notEnoughError) {
        return new ICatalystHandler<ItemStack>() {

            @Override
            public void calculateOperationsCanSupport(OperationTracker tracker, ItemStack stack, int www) {
                if (stack.isEmpty()) {
                    tracker.resetProgress(notEnoughError);
                }
            }

            @Override
            public ItemStack getCatalyst() {
                return slot.getStack();
            }

            @Override
            public ItemStack getRecipeCatalyst(InputIngredient<ItemStack> ingredient) {
                return getCatalyst().isEmpty() ? ingredient.getMatchingInstance(getCatalyst()) : ItemStack.EMPTY;
            }

        };
    }
}
