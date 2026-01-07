package astral_mekanism.mixin.mekanism.recipe.lookup.cache.type;

import org.spongepowered.asm.mixin.Mixin;

import astral_mekanism.generalrecipe.lookup.cache.type.IBaseInputCache;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.common.recipe.lookup.cache.type.BaseInputCache;

@Mixin(value = BaseInputCache.class, remap = false)
public abstract class BaseInputCacheMixin<KEY, INPUT, INGREDIENT extends InputIngredient<INPUT>, RECIPE extends MekanismRecipe>
        implements IBaseInputCache<INPUT, INGREDIENT, RECIPE> {

}
