package astral_mekanism.generalrecipe.lookup.cache.type;

import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.common.recipe.ingredient.IMultiIngredient;
import mekanism.common.recipe.lookup.cache.type.IInputCache;

public interface IBaseInputCache<INPUT, INGREDIENT extends InputIngredient<INPUT>, RECIPE extends MekanismRecipe>
        extends IUnifiedInputCache<INPUT, INGREDIENT, RECIPE>, IInputCache<INPUT, INGREDIENT, RECIPE> {

    @Override
    default boolean mapMultiInputs(RECIPE recipe, IMultiIngredient<INPUT, ? extends INGREDIENT> multi) {
        return IUnifiedInputCache.super.mapMultiInputs(recipe, multi);
    }

}
