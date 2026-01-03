package astral_mekanism.generalrecipe;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

public interface IGeneralRecipeTypeProvider<C extends Container, RECIPE extends Recipe<C>, INPUT_CACHE extends IInputRecipeCache> {
    public ResourceLocation getRegistryName();

    public GeneralRecipeType<C, RECIPE, INPUT_CACHE> getRecipeType();

    public INPUT_CACHE getInputCache();

    @NotNull
    default List<RECIPE> getRecipes(@Nullable Level world) {
        return getRecipeType().getRecipes(world);
    }

    default Stream<RECIPE> stream(@Nullable Level world) {
        return getRecipes(world).stream();
    }

    @Nullable
    default RECIPE findFirst(@Nullable Level world, Predicate<RECIPE> matchCriteria) {
        return stream(world).filter(matchCriteria).findFirst().orElse(null);
    }

    default boolean contains(@Nullable Level world, Predicate<RECIPE> matchCriteria) {
        return stream(world).anyMatch(matchCriteria);
    }
}
