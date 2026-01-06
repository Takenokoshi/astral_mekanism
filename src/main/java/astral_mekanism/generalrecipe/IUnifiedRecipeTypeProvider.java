package astral_mekanism.generalrecipe;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

public interface IUnifiedRecipeTypeProvider<RECIPE extends Recipe<?>, INPUT_CACHE extends IInputRecipeCache> {

    IUnifiedRecipeType<RECIPE, INPUT_CACHE> getRecipeType();

    default ResourceLocation getRegistryName() {
        return getRecipeType().getRegistryName();
    }

    default INPUT_CACHE getInputCache() {
        return getRecipeType().getInputCache();
    }

    default List<RECIPE> getRecipes(@Nullable Level level) {
        return getRecipeType().getRecipes(level);
    }

    default Stream<RECIPE> stream(@Nullable Level world) {
        return getRecipes(world).stream();
    }
    default boolean contains(@Nullable Level world, Predicate<RECIPE> matchCriteria) {
        return stream(world).anyMatch(matchCriteria);
    }
}
