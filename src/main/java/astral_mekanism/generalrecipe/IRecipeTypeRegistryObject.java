package astral_mekanism.generalrecipe;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.mixin.mekanism.recipe.MekanismRecipeTypeMixin2;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public interface IRecipeTypeRegistryObject<RECIPE extends MekanismRecipe, INPUT_CACHE extends IInputRecipeCache>
        extends IUnifiedRecipeTypeProvider<RECIPE, INPUT_CACHE>, IMekanismRecipeTypeProvider<RECIPE, INPUT_CACHE> {

    @SuppressWarnings("unchecked")
    @Override
    public default MekanismRecipeTypeMixin2<RECIPE, INPUT_CACHE> getUnifiedRecipeType() {
        return (MekanismRecipeTypeMixin2<RECIPE, INPUT_CACHE>) (Object) ((RecipeTypeRegistryObject<RECIPE, INPUT_CACHE>) (Object) this)
                .getRecipeType();
    }

    @Override
    default INPUT_CACHE getInputCache() {
        return getRecipeType().getInputCache();
    }

    @Override
    default boolean contains(@Nullable Level world, Predicate<RECIPE> matchCriteria) {
        return stream(world).anyMatch(matchCriteria);
    }

    @Override
    default ResourceLocation getRegistryName() {
        return getRecipeType().getRegistryName();
    }

    @Override
    default @NotNull List<RECIPE> getRecipes(@Nullable Level world) {
        return getRecipeType().getRecipes(world);
    }

    @Override
    default Stream<RECIPE> stream(@Nullable Level world) {
        return getRecipes(world).stream();
    }

}
