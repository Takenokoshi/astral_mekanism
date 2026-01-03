package astral_mekanism.generalrecipe.lookup.cache.type;

import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.common.recipe.ingredient.creator.ItemStackIngredientCreator.MultiItemStackIngredient;
import mekanism.common.recipe.ingredient.creator.ItemStackIngredientCreator.SingleItemStackIngredient;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.common.crafting.CompoundIngredient;

public class ItemGeneralInputCache<RECIPE extends Recipe<?>>
        extends BaseGeneralInputCache<Item, ItemStack, ItemStackIngredient, RECIPE> {

    @Override
    public boolean mapInputs(RECIPE recipe, ItemStackIngredient inputIngredient) {
        if (inputIngredient instanceof SingleItemStackIngredient single) {
            return mapIngredient(recipe, single.getInputRaw());
        } else if (inputIngredient instanceof MultiItemStackIngredient multi) {
            return mapMultiInputs(recipe, multi);
        }
        return true;
    }

    private boolean mapIngredient(RECIPE recipe, Ingredient input) {
        if (input.isVanilla() || input.isSimple()) {
            for (ItemStack item : input.getItems()) {
                if (!item.isEmpty()) {
                    addInputCache(item.getItem(), recipe);
                }
            }
        } else if (input instanceof CompoundIngredient compoundIngredient) {
            boolean result = false;
            for (Ingredient child : compoundIngredient.getChildren()) {
                result |= mapIngredient(recipe, child);
            }
            return result;
        } else {
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty(ItemStack input) {
        return input.isEmpty();
    }

    @Override
    protected Item createKey(ItemStack input) {
        return input.getItem();
    }

}
