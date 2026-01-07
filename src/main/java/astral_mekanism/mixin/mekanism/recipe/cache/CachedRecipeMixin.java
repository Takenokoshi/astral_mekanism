package astral_mekanism.mixin.mekanism.recipe.cache;

import org.spongepowered.asm.mixin.Mixin;
import astral_mekanism.generalrecipe.cachedrecipe.ICachedRecipe;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;

@Mixin(value = CachedRecipe.class, remap = false)
public abstract class CachedRecipeMixin<RECIPE extends MekanismRecipe>
        implements ICachedRecipe<RECIPE> {
}
