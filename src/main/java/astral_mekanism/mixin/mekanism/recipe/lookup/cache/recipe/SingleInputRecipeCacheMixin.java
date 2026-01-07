package astral_mekanism.mixin.mekanism.recipe.lookup.cache.recipe;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import astral_mekanism.generalrecipe.lookup.cache.recipe.ISingleInputRecipeCache;
import astral_mekanism.generalrecipe.lookup.cache.type.IBaseInputCache;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.common.recipe.lookup.cache.SingleInputRecipeCache;

@Mixin(value = SingleInputRecipeCache.class, remap = false)
public abstract class SingleInputRecipeCacheMixin<INPUT, INGREDIENT extends InputIngredient<INPUT>, RECIPE extends MekanismRecipe & Predicate<INPUT>, CACHE extends IBaseInputCache<INPUT, INGREDIENT, RECIPE>>
        implements ISingleInputRecipeCache<INPUT, RECIPE> {

}
