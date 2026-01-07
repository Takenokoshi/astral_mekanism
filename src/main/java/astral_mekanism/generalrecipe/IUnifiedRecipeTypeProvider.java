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

    IUnifiedRecipeType<RECIPE, INPUT_CACHE> getUnifiedRecipeType();

    default ResourceLocation getRegistryName() {
        return getUnifiedRecipeType().getRegistryName();
    }

    default INPUT_CACHE getInputCache() {
        return getUnifiedRecipeType().getInputCache();
    }

    default List<RECIPE> getRecipes(@Nullable Level level) {
        return getUnifiedRecipeType().getRecipes(level);
    }

    default Stream<RECIPE> stream(@Nullable Level world) {
        return getRecipes(world).stream();
    }
    default boolean contains(@Nullable Level world, Predicate<RECIPE> matchCriteria) {
        return stream(world).anyMatch(matchCriteria);
    }
}
