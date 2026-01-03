package astral_mekanism.generalrecipe.lookup.cache.recipe;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.generalrecipe.lookup.cache.type.IGeneralInputCache;
import astral_mekanism.generalrecipe.lookup.cache.type.ItemGeneralInputCache;
import mekanism.api.functions.ConstantPredicates;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public abstract class SingleInputGeneralRecipeCache<INPUT, INGREDIENT extends InputIngredient<INPUT>, C extends Container, RECIPE extends Recipe<C>, CACHE extends IGeneralInputCache<INPUT, INGREDIENT, RECIPE>>
        extends GeneralInputRecipeCache<C, RECIPE> {

    private final Set<RECIPE> complexRecipes = new HashSet<>();
    private final Function<RECIPE, INGREDIENT> inputExtractor;
    private final CACHE cache;
    private final BiPredicate<INPUT, RECIPE> biPredicate;

    protected SingleInputGeneralRecipeCache(RecipeType<RECIPE> recipeType, CACHE cache,
            Function<RECIPE, INGREDIENT> inputExtractor, BiPredicate<INPUT, RECIPE> biPredicate) {
        super(recipeType);
        this.inputExtractor = inputExtractor;
        this.cache = cache;
        this.biPredicate = biPredicate;
    }

    @Override
    public void clear() {
        super.clear();
        cache.clear();
        complexRecipes.clear();
    }

    public boolean containsInput(@Nullable Level world, INPUT input) {
        return containsInput(world, input, inputExtractor, cache, complexRecipes);
    }

    @Nullable
    public RECIPE findFirstRecipe(@Nullable Level world, INPUT input) {
        if (cache.isEmpty(input)) {
            return null;
        }
        initCacheIfNeeded(world);
        Predicate<RECIPE> matchPredicate = recipe -> biPredicate.test(input, recipe);
        RECIPE recipe = cache.findFirstRecipe(input, matchPredicate);
        return recipe == null ? findFirstRecipe(complexRecipes, matchPredicate) : recipe;
    }

    @Nullable
    public RECIPE findTypeBasedRecipe(@Nullable Level world, INPUT input) {
        return findTypeBasedRecipe(world, input, ConstantPredicates.alwaysTrue());
    }

    @Nullable
    public RECIPE findTypeBasedRecipe(@Nullable Level world, INPUT input, Predicate<RECIPE> matchCriteria) {
        if (cache.isEmpty(input)) {
            return null;
        }
        initCacheIfNeeded(world);
        RECIPE recipe = cache.findFirstRecipe(input, matchCriteria);
        return recipe == null
                ? findFirstRecipe(complexRecipes, r -> inputExtractor.apply(r).testType(input) && matchCriteria.test(r))
                : recipe;
    }

    @Override
    protected void initCache(List<RECIPE> recipes) {
        for (RECIPE recipe : recipes) {
            if (cache.mapInputs(recipe, inputExtractor.apply(recipe))) {
                complexRecipes.add(recipe);
            }
        }
    }

    public static class GeneralSingleItem<C extends Container, RECIPE extends Recipe<C>> extends
            SingleInputGeneralRecipeCache<ItemStack, ItemStackIngredient, C, RECIPE, ItemGeneralInputCache<RECIPE>> {

        public GeneralSingleItem(RecipeType<RECIPE> recipeType,
                Function<RECIPE, ItemStackIngredient> inputExtractor, BiPredicate<ItemStack, RECIPE> biPredicate) {
            super(recipeType, new ItemGeneralInputCache<>(), inputExtractor, biPredicate);
        }

    }

}
