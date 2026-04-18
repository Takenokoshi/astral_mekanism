package astral_mekanism.longRecipe.lookup;

import org.jetbrains.annotations.NotNull;

import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;

public interface ILongMekanismRecipeTypedLookupHandler<RECIPE extends MekanismRecipe, INPUT_CACHE extends IInputRecipeCache>
        extends ILongMekanismRecipeLookupHandler<RECIPE> {
    @NotNull
    @Override
    IMekanismRecipeTypeProvider<RECIPE, INPUT_CACHE> getRecipeType();
}
