package astral_mekanism.generalrecipe.lookup.cache.recipe;

import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

public interface ISingleInputRecipeCache<INPUT, RECIPE extends Recipe<?>> extends IInputRecipeCache {

    void clear();

    boolean containsInput(@Nullable Level world, INPUT input);

    @Nullable
    RECIPE findFirstRecipe(@Nullable Level world, INPUT input);

    @Nullable
    RECIPE findTypeBasedRecipe(@Nullable Level world, INPUT input);

    @Nullable
    RECIPE findTypeBasedRecipe(
            @Nullable Level world,
            INPUT input,
            Predicate<RECIPE> matchCriteria);
}
