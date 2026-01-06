package astral_mekanism.mixin.mekanism.recipe;

import org.spongepowered.asm.mixin.Mixin;
import astral_mekanism.generalrecipe.IUnifiedRecipeTypeProvider;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;

@Mixin(value = RecipeTypeRegistryObject.class, remap = false)
public abstract class RecipeTypeRegistryObjectMixin<RECIPE extends MekanismRecipe, INPUT_CACHE extends IInputRecipeCache>
        implements IUnifiedRecipeTypeProvider<RECIPE, INPUT_CACHE> {

    @SuppressWarnings("unchecked")
    @Override
    public MekanismRecipeTypeMixin<RECIPE, INPUT_CACHE> getRecipeType() {
        return (MekanismRecipeTypeMixin<RECIPE, INPUT_CACHE>) (Object) ((RecipeTypeRegistryObject<RECIPE, INPUT_CACHE>) (Object) this)
                .getRecipeType();
    };
}
