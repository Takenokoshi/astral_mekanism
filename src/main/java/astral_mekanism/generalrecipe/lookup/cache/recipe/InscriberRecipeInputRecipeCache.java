package astral_mekanism.generalrecipe.lookup.cache.recipe;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import appeng.recipes.handlers.InscriberRecipe;
import astral_mekanism.generalrecipe.IUnifiedRecipeType;
import astral_mekanism.generalrecipe.lookup.cache.type.ItemGeneralInputCache;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class InscriberRecipeInputRecipeCache extends GeneralInputRecipeCache<Container, InscriberRecipe> {

    private final Set<InscriberRecipe> recipes;
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
        recipes = new HashSet<>();
    }

    public boolean containsInputTop(Level world, ItemStack input) {
        return containsInput(world, input, InscriberRecipe::getTopOptional, cacheTop, complexRecipesTop);
    }

    public boolean containsInputMiddle(Level world, ItemStack input) {
        return containsInput(world, input, InscriberRecipe::getMiddleInput, cacheMiddle, complexRecipesMiddle);
    }

    public boolean containsInputBottom(Level world, ItemStack input) {
        return containsInput(world, input, InscriberRecipe::getBottomOptional, cacheBottom, complexRecipesBottom);
    }

    public boolean containsTMB(Level world, ItemStack topInput, ItemStack middleInput, ItemStack bottomInput) {
        return containsGrouping(world,
                topInput, InscriberRecipe::getTopOptional, cacheTop, complexRecipesTop,
                middleInput, InscriberRecipe::getMiddleInput, cacheMiddle, complexRecipesMiddle,
                bottomInput, InscriberRecipe::getBottomOptional, cacheBottom, complexRecipesBottom);
    }

    public boolean containsMTB(Level world, ItemStack topInput, ItemStack middleInput, ItemStack bottomInput) {
        return containsGrouping(world,
                middleInput, InscriberRecipe::getMiddleInput, cacheMiddle, complexRecipesMiddle,
                topInput, InscriberRecipe::getTopOptional, cacheTop, complexRecipesTop,
                bottomInput, InscriberRecipe::getBottomOptional, cacheBottom, complexRecipesBottom);
    }

    public boolean containsBTM(Level world, ItemStack topInput, ItemStack middleInput, ItemStack bottomInput) {
        return containsGrouping(world,
                bottomInput, InscriberRecipe::getBottomOptional, cacheBottom, complexRecipesBottom,
                topInput, InscriberRecipe::getTopOptional, cacheTop, complexRecipesTop,
                middleInput, InscriberRecipe::getMiddleInput, cacheMiddle, complexRecipesMiddle);
    }

    public InscriberRecipe findFirstRecipe(Level world, ItemStack topInput, ItemStack middleInput,
            ItemStack bottomInput) {
        initCacheIfNeeded(world);
        Predicate<InscriberRecipe> matchPredicate = r -> r.getMiddleInput().test(middleInput)
                && (r.getTopOptional().isEmpty() ? topInput.isEmpty() : r.getTopOptional().test(topInput))
                && (r.getBottomOptional().isEmpty() ? bottomInput.isEmpty() : r.getBottomOptional().test(bottomInput));
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
        IItemStackIngredientCreator creator = IngredientCreatorAccess.item();
        for (InscriberRecipe recipe : recipes) {
            boolean complexT = false;
            boolean complexM = false;
            boolean complexB = false;
            Ingredient topIngredient = recipe.getTopOptional();
            Ingredient middleIngredient = recipe.getMiddleInput();
            Ingredient bottomIngredient = recipe.getBottomOptional();
            if (!topIngredient.isEmpty()) {
                complexT = cacheTop.mapInputs(recipe, creator.from(topIngredient));
                if (complexT) {
                    complexRecipesTop.add(recipe);
                }
            }
            if (!middleIngredient.isEmpty()) {
                complexM = cacheMiddle.mapInputs(recipe, creator.from(middleIngredient));
                if (complexM) {
                    complexRecipesMiddle.add(recipe);
                }
            }
            if (!bottomIngredient.isEmpty()) {
                complexB = cacheBottom.mapInputs(recipe, creator.from(bottomIngredient));
                if (complexB) {
                    complexRecipesBottom.add(recipe);
                }
            }
            if (complexT || complexM || complexB) {
                complexRecipes.add(recipe);
            }
        }
    }

    private boolean containsGrouping(Level world,
            ItemStack input1, Function<InscriberRecipe, Ingredient> input1Extractor,
            ItemGeneralInputCache<InscriberRecipe> cache1, Set<InscriberRecipe> complexIngredients1,
            ItemStack input2, Function<InscriberRecipe, Ingredient> input2Extractor,
            ItemGeneralInputCache<InscriberRecipe> cache2, Set<InscriberRecipe> complexIngredients2,
            ItemStack input3, Function<InscriberRecipe, Ingredient> input3Extractor,
            ItemGeneralInputCache<InscriberRecipe> cache3, Set<InscriberRecipe> complexIngredients3) {
        if (cache1.isEmpty(input1)) {
            if (cache3.isEmpty(input3)) {
                return containsInput(world, input2, input2Extractor, cache2, complexIngredients2);
            }
            return containsPairing(world, input2, input2Extractor, cache2, complexIngredients2, input3, input3Extractor,
                    cache3, complexIngredients3);
        } else if (cache2.isEmpty(input2)) {
            return containsPairing(world, input1, input1Extractor, cache1, complexIngredients1, input3, input3Extractor,
                    cache3, complexIngredients3);
        } else if (cache3.isEmpty(input3)) {
            return containsPairing(world, input1, input1Extractor, cache1, complexIngredients1, input2, input2Extractor,
                    cache2, complexIngredients2);
        }
        initCacheIfNeeded(world);
        if (cache1.contains(input1, recipe -> {
            Ingredient input2Ingredient = input2Extractor.apply(recipe);
            Ingredient input3Ingredient = input3Extractor.apply(recipe);
            return (input2Ingredient.isEmpty() ? input2.isEmpty() : input2Ingredient.test(input2))
                    && (input3Ingredient.isEmpty() ? input3.isEmpty() : input3Ingredient.test(input3));
        })) {
            return true;
        }
        return complexIngredients1.stream().anyMatch(recipe -> {
            Ingredient input1Ingredient = input1Extractor.apply(recipe);
            Ingredient input2Ingredient = input2Extractor.apply(recipe);
            Ingredient input3Ingredient = input3Extractor.apply(recipe);
            return (input1Ingredient.isEmpty() ? input1.isEmpty() : input1Ingredient.test(input1))
                    && (input2Ingredient.isEmpty() ? input2.isEmpty() : input2Ingredient.test(input2))
                    && (input3Ingredient.isEmpty() ? input3.isEmpty() : input3Ingredient.test(input3));
        });
    }

    private boolean containsPairing(
            @Nullable Level world,
            ItemStack input1, Function<InscriberRecipe, Ingredient> input1Extractor,
            ItemGeneralInputCache<InscriberRecipe> cache1,
            Set<InscriberRecipe> complexIngredients1, ItemStack input2,
            Function<InscriberRecipe, Ingredient> input2Extractor,
            ItemGeneralInputCache<InscriberRecipe> cache2, Set<InscriberRecipe> complexIngredients2) {
        if (cache1.isEmpty(input1)) {
            return containsInput(world, input2, input2Extractor, cache2, complexIngredients2);
        } else if (cache2.isEmpty(input2)) {
            return true;
        }
        initCacheIfNeeded(world);
        if (cache1.contains(input1,
                recipe -> {
                    Ingredient ingredient = input2Extractor.apply(recipe);
                    return ingredient.isEmpty() ? input2.isEmpty() : ingredient.test(input2);
                })) {
            return true;
        }
        return complexIngredients1.stream().anyMatch(recipe -> {
            Ingredient input1Ingredient = input1Extractor.apply(recipe);
            Ingredient input2Ingredient = input2Extractor.apply(recipe);
            return (input1Ingredient.isEmpty() ? input1.isEmpty() : input1Ingredient.test(input1))
                    && (input2Ingredient.isEmpty() ? input2.isEmpty() : input2Ingredient.test(input2));
        });
    }

    private boolean containsInput(
            @Nullable Level world, ItemStack input, Function<InscriberRecipe, Ingredient> inputExtractor,
            ItemGeneralInputCache<InscriberRecipe> cache,
            Set<InscriberRecipe> complexRecipes) {
        if (cache.isEmpty(input)) {
            return false;
        }
        initCacheIfNeeded(world);
        return cache.contains(input) ||
                complexRecipes.stream().anyMatch(recipe -> {
                    Ingredient ingredient = inputExtractor.apply(recipe);
                    return ingredient.isEmpty() ? input.isEmpty() : ingredient.test(input);
                });
    }
}
