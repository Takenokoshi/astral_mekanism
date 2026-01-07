package astral_mekanism.mixin.mekanism.recipe;

import org.spongepowered.asm.mixin.Mixin;

import astral_mekanism.generalrecipe.IMekanismRecipeType;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;

@Mixin(value = MekanismRecipeType.class, remap = false)
public abstract class MekanismRecipeTypeMixin2<RECIPE extends MekanismRecipe, INPUT_CACHE extends IInputRecipeCache>
        implements IMekanismRecipeType<RECIPE, INPUT_CACHE> {
}
