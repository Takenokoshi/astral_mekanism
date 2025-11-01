package astral_mekanism.recipes.inputRecipeCache;

import astral_mekanism.recipes.ingredient.ArrayItemStackIngredient;
import astral_mekanism.util.ArrayItemStackUtils;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.lookup.cache.type.BaseInputCache;
import net.minecraft.world.item.ItemStack;

public class ArrayItemInputCache<RECIPE extends MekanismRecipe>
        extends BaseInputCache<ItemStackArrayKey, ItemStack[], ArrayItemStackIngredient, RECIPE> {

    @Override
    public boolean isEmpty(ItemStack[] input) {
        return ArrayItemStackUtils.includeNullOrEmpty(input);
    }

    @Override
    public boolean mapInputs(RECIPE recipe, ArrayItemStackIngredient inputIngredient) {
        ItemStack[] stacks = new ItemStack[inputIngredient.ingredients.length];
        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = inputIngredient.ingredients[i].getRepresentations().get(0);
        }
        addInputCache(createKey(stacks), recipe);
        return false;
    }

    @Override
    protected ItemStackArrayKey createKey(ItemStack[] input) {
        return new ItemStackArrayKey(input);
    }
}