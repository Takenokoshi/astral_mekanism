package astral_mekanism.mixin.mekanism.recipe.cache;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import astral_mekanism.generalrecipe.cachedrecipe.ICachedRecipe;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;

@Mixin(value = CachedRecipe.class, remap = false)
public abstract class CachedRecipeMixin<RECIPE extends MekanismRecipe>
        implements ICachedRecipe<RECIPE> {

    @Shadow
    @Override
    public abstract void loadSavedOperatingTicks(int operatingTicks);

    @Shadow
    @Override
    public abstract void process();

    @Shadow
    @Override
    public abstract boolean isInputValid();

    @Shadow
    @Override
    public abstract RECIPE getRecipe();
}
