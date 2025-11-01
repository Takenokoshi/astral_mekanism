package astral_mekanism.recipes.inputRecipeCache;

import astral_mekanism.recipes.ingredient.ArrayItemStackIngredient;
import astral_mekanism.util.ArrayItemStackUtils;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.lookup.cache.type.BaseInputCache;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ArrayItemInputCache<RECIPE extends MekanismRecipe>
        extends BaseInputCache<Item[], ItemStack[], ArrayItemStackIngredient, RECIPE> {

    @Override
    public boolean isEmpty(ItemStack[] input) {
        return ArrayItemStackUtils.includeNullOrEmpty(input);
    }

    @Override
    public boolean mapInputs(RECIPE recipe, ArrayItemStackIngredient inputIngredient) {
        Item[] input = new Item[inputIngredient.ingredients.length];
        for (int i = 0; i < input.length; i++) {
            input[i] = inputIngredient.ingredients[i].getRepresentations().get(0).getItem();
        }
        addInputCache(input, recipe);
        return false;
    }

    @Override
    protected Item[] createKey(ItemStack[] input) {
        Item[] result = new Item[input.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = input[i].getItem();
        }
        return result;
    }
}