package astral_mekanism.generalrecipe.lookup.cache.recipe;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.generalrecipe.GeneralRecipeType;
import astral_mekanism.generalrecipe.lookup.cache.type.IGeneralInputCache;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

public abstract class GeneralInputRecipeCache<C extends Container,RECIPE extends Recipe<C>> implements IInputRecipeCache {

    protected final GeneralRecipeType<C,RECIPE,?> recipeType;
    protected boolean initialized;

    protected GeneralInputRecipeCache(GeneralRecipeType<C,RECIPE,?> recipeType) {
        this.recipeType = recipeType;
    }

    @Override
    public void clear() {
        initialized = false;
    }

    protected void initCacheIfNeeded(@Nullable Level world) {
        if (!initialized) {
            initialized = true;
            initCache(recipeType.getRecipes(world));
        }
    }

    protected abstract void initCache(List<RECIPE> recipes);

    @Nullable
    protected RECIPE findFirstRecipe(@Nullable Collection<RECIPE> recipes, Predicate<RECIPE> matchCriteria) {
        return recipes == null ? null : recipes.stream().filter(matchCriteria).findFirst().orElse(null);
    }

    protected <INPUT, INGREDIENT extends InputIngredient<INPUT>, CACHE extends IGeneralInputCache<INPUT, INGREDIENT, RECIPE>> boolean containsInput(
            @Nullable Level world, INPUT input, Function<RECIPE, INGREDIENT> inputExtractor, CACHE cache,
            Set<RECIPE> complexRecipes) {
        if (cache.isEmpty(input)) {
            // Don't allow empty inputs
            return false;
        }
        initCacheIfNeeded(world);
        return cache.contains(input)
                || complexRecipes.stream().anyMatch(recipe -> inputExtractor.apply(recipe).testType(input));
    }

    protected <INPUT_1, INGREDIENT_1 extends InputIngredient<INPUT_1>, CACHE_1 extends IGeneralInputCache<INPUT_1, INGREDIENT_1, RECIPE>, INPUT_2, INGREDIENT_2 extends InputIngredient<INPUT_2>, CACHE_2 extends IGeneralInputCache<INPUT_2, INGREDIENT_2, RECIPE>> boolean containsPairing(
            @Nullable Level world,
            INPUT_1 input1, Function<RECIPE, INGREDIENT_1> input1Extractor, CACHE_1 cache1,
            Set<RECIPE> complexIngredients1, INPUT_2 input2,
            Function<RECIPE, INGREDIENT_2> input2Extractor, CACHE_2 cache2, Set<RECIPE> complexIngredients2) {
        if (cache1.isEmpty(input1)) {
            // Note: We don't bother checking if 2 is empty here as it will be verified in
            // containsInput
            return containsInput(world, input2, input2Extractor, cache2, complexIngredients2);
        } else if (cache2.isEmpty(input2)) {
            return true;
        }
        initCacheIfNeeded(world);
        // Note: If cache 1 contains input 1 then we only need to test the type of input
        // 2 as we already know input 1 matches
        if (cache1.contains(input1, recipe -> input2Extractor.apply(recipe).testType(input2))) {
            return true;
        }
        // Our quick lookup 1 cache does not contain it, check any recipes where the 1
        // ingredient was complex
        return complexIngredients1.stream().anyMatch(recipe -> input1Extractor.apply(recipe).testType(input1)
                && input2Extractor.apply(recipe).testType(input2));
    }

}
