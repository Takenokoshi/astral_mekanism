package astral_mekanism.generalrecipe;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public interface IMekanismRecipeType<RECIPE extends MekanismRecipe, INPUT_CACHE extends IInputRecipeCache>
        extends IUnifiedRecipeType<RECIPE, INPUT_CACHE>,
        IUnifiedRecipeTypeProvider<RECIPE, INPUT_CACHE>,
        IMekanismRecipeTypeProvider<RECIPE, INPUT_CACHE> {

    @Override
    INPUT_CACHE getInputCache();

    @Override
    ResourceLocation getRegistryName();

    @NotNull
    List<RECIPE> getRecipes(@Nullable Level world);

    @Override
    boolean contains(@Nullable Level world, Predicate<RECIPE> matchCriteria);

    @Override
    Stream<RECIPE> stream(@Nullable Level world);

    @SuppressWarnings("unchecked")
    @Override
    default IUnifiedRecipeType<RECIPE, INPUT_CACHE> getUnifiedRecipeType() {
        return (IUnifiedRecipeType<RECIPE, INPUT_CACHE>) (Object) getRecipeType();
    }

}
