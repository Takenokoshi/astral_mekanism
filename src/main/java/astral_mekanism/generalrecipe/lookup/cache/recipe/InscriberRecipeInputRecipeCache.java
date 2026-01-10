package astral_mekanism.generalrecipe.lookup.cache.recipe;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import appeng.recipes.handlers.InscriberRecipe;
import astral_mekanism.generalrecipe.IUnifiedRecipeType;
import astral_mekanism.generalrecipe.lookup.cache.type.IUnifiedInputCache;
import astral_mekanism.generalrecipe.lookup.cache.type.ItemGeneralInputCache;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class InscriberRecipeInputRecipeCache extends GeneralInputRecipeCache<Container, InscriberRecipe> {

    private final Set<InscriberRecipe> complexRecipes;
    private final Set<InscriberRecipe> complexRecipesTop;
    private final Set<InscriberRecipe> complexRecipesMiddle;
    private final Set<InscriberRecipe> complexRecipesBottom;
    private final Function<InscriberRecipe, ItemStackIngredient> topInputExtractor;
    private final Function<InscriberRecipe, ItemStackIngredient> middleInputExtractor;
    private final Function<InscriberRecipe, ItemStackIngredient> bottomInputExtractor;
    private final ItemGeneralInputCache<InscriberRecipe> cacheTop;
    private final ItemGeneralInputCache<InscriberRecipe> cacheMiddle;
    private final ItemGeneralInputCache<InscriberRecipe> cacheBottom;

    public InscriberRecipeInputRecipeCache(IUnifiedRecipeType<InscriberRecipe, ?> recipeType) {
        super(recipeType);
        IItemStackIngredientCreator creator = IngredientCreatorAccess.item();
        this.complexRecipes = new HashSet<>();
        this.complexRecipesTop = new HashSet<>();
        this.complexRecipesMiddle = new HashSet<>();
        this.complexRecipesBottom = new HashSet<>();
        this.topInputExtractor = recipe -> recipe.getTopOptional().isEmpty()
                ? null
                : creator.from(recipe.getTopOptional());
        this.middleInputExtractor = recipe -> recipe.getMiddleInput().isEmpty()
                ? null
                : creator.from(recipe.getMiddleInput());
        this.bottomInputExtractor = recipe -> recipe.getBottomOptional().isEmpty()
                ? null
                : creator.from(recipe.getBottomOptional());
        this.cacheTop = new ItemGeneralInputCache<>();
        this.cacheMiddle = new ItemGeneralInputCache<>();
        this.cacheBottom = new ItemGeneralInputCache<>();
    }

    public boolean containsInputTop(Level world, ItemStack input) {
        return containsInput(world, input, topInputExtractor, cacheTop, complexRecipesTop);
    }

    public boolean containsInputMiddle(Level world, ItemStack input) {
        return containsInput(world, input, middleInputExtractor, cacheMiddle, complexRecipesMiddle);
    }

    public boolean containsInputBottom(Level world, ItemStack input) {
        return containsInput(world, input, bottomInputExtractor, cacheBottom, complexRecipesBottom);
    }

    public boolean containsTMB(Level world, ItemStack topInput, ItemStack middleInput, ItemStack bottomInput) {
        return containsGrouping(world,
                topInput, topInputExtractor, cacheTop, complexRecipesTop,
                middleInput, middleInputExtractor, cacheMiddle, complexRecipesMiddle,
                bottomInput, bottomInputExtractor, cacheBottom, complexRecipesBottom);
    }

    public boolean containsMTB(Level world, ItemStack topInput, ItemStack middleInput, ItemStack bottomInput) {
        return containsGrouping(world,
                middleInput, middleInputExtractor, cacheMiddle, complexRecipesMiddle,
                topInput, topInputExtractor, cacheTop, complexRecipesTop,
                bottomInput, bottomInputExtractor, cacheBottom, complexRecipesBottom);
    }

    public boolean containsBTM(Level world, ItemStack topInput, ItemStack middleInput, ItemStack bottomInput) {
        return containsGrouping(world,
                bottomInput, bottomInputExtractor, cacheBottom, complexRecipesBottom,
                topInput, topInputExtractor, cacheTop, complexRecipesTop,
                middleInput, middleInputExtractor, cacheMiddle, complexRecipesMiddle);
    }

    public InscriberRecipe findFirstRecipe(Level world, ItemStack topInput, ItemStack middleInput,
            ItemStack bottomInput) {
        initCacheIfNeeded(world);
        Predicate<InscriberRecipe> matchPredicate = r -> r.getMiddleInput().test(middleInput)
                && (r.getTopOptional().isEmpty() || r.getTopOptional().test(topInput))
                && (r.getBottomOptional().isEmpty() || r.getBottomOptional().test(bottomInput));
        InscriberRecipe recipe = cacheMiddle.findFirstRecipe(middleInput, matchPredicate);
        return recipe == null ? findFirstRecipe(complexRecipes, matchPredicate) : recipe;
    }

    @Override
    public void clear() {
        super.clear();
        complexRecipes.clear();
        complexRecipesTop.clear();
        complexRecipesMiddle.clear();
        complexRecipesBottom.clear();
        cacheTop.clear();
        cacheMiddle.clear();
        cacheBottom.clear();
    }

    @Override
    protected void initCache(List<InscriberRecipe> recipes) {
        for (InscriberRecipe recipe : recipes) {
            boolean complexT = false;
            boolean complexM = false;
            boolean complexB = false;
            Ingredient topIngredient = recipe.getTopOptional();
            if (!topIngredient.isEmpty()) {
                complexT = cacheTop.mapInputs(recipe, topInputExtractor.apply(recipe));
            }
            if (complexT) {
                complexRecipesTop.add(recipe);
            }
            Ingredient middleIngredient = recipe.getTopOptional();
            if (!middleIngredient.isEmpty()) {
                complexM = cacheMiddle.mapInputs(recipe, middleInputExtractor.apply(recipe));
            }
            if (complexM) {
                complexRecipesMiddle.add(recipe);
            }
            Ingredient bottomIngredient = recipe.getTopOptional();
            if (!bottomIngredient.isEmpty()) {
                complexB = cacheBottom.mapInputs(recipe, bottomInputExtractor.apply(recipe));
            }
            if (complexB) {
                complexRecipesBottom.add(recipe);
            }
            if (complexT || complexM || complexB) {
                complexRecipes.add(recipe);
            }
        }
    }

    private <INPUT_1, INGREDIENT_1 extends InputIngredient<INPUT_1>, CACHE_1 extends IUnifiedInputCache<INPUT_1, INGREDIENT_1, InscriberRecipe>, INPUT_2, INGREDIENT_2 extends InputIngredient<INPUT_2>, CACHE_2 extends IUnifiedInputCache<INPUT_2, INGREDIENT_2, InscriberRecipe>, INPUT_3, INGREDIENT_3 extends InputIngredient<INPUT_3>, CACHE_3 extends IUnifiedInputCache<INPUT_3, INGREDIENT_3, InscriberRecipe>> boolean containsGrouping(
            @Nullable Level world,
            INPUT_1 input1, Function<InscriberRecipe, INGREDIENT_1> input1Extractor, CACHE_1 cache1,
            Set<InscriberRecipe> complexIngredients1,
            INPUT_2 input2, Function<InscriberRecipe, INGREDIENT_2> input2Extractor, CACHE_2 cache2,
            Set<InscriberRecipe> complexIngredients2,
            INPUT_3 input3, Function<InscriberRecipe, INGREDIENT_3> input3Extractor, CACHE_3 cache3,
            Set<InscriberRecipe> complexIngredients3) {
        if (cache1.isEmpty(input1)) {
            if (cache3.isEmpty(input3)) {
                // If 1 and 3 are empty just check 2. We have this extra check here as
                // containsPairing will always return true
                // if the secondary type is empty, but this is the special case when we don't
                // want that to actually happen
                return containsInput(world, input2, input2Extractor, cache2, complexIngredients2);
            }
            // Note: We don't bother checking if 2 is empty here as it will be verified in
            // containsPairing
            return containsPairing(world, input2, input2Extractor, cache2, complexIngredients2, input3, input3Extractor,
                    cache3, complexIngredients3);
        } else if (cache2.isEmpty(input2)) {
            // Note: We don't bother checking if 3 is empty here as it will be verified in
            // containsPairing
            return containsPairing(world, input1, input1Extractor, cache1, complexIngredients1, input3, input3Extractor,
                    cache3, complexIngredients3);
        } else if (cache3.isEmpty(input3)) {
            return containsPairing(world, input1, input1Extractor, cache1, complexIngredients1, input2, input2Extractor,
                    cache2, complexIngredients2);
        }
        initCacheIfNeeded(world);
        // Note: If cache 1 contains input 1 then we only need to test the type of input
        // 2 and 3 as we already know input 1 matches
        if (cache1.contains(input1, recipe -> input2Extractor.apply(recipe).testType(input2)
                && input3Extractor.apply(recipe).testType(input3))) {
            return true;
        }
        // Our quick lookup 1 cache does not contain it, check any recipes where the 1
        // ingredient was complex
        return complexIngredients1.stream().anyMatch(recipe -> input1Extractor.apply(recipe).testType(input1) &&
                input2Extractor.apply(recipe).testType(input2) &&
                input3Extractor.apply(recipe).testType(input3));
    }

}
