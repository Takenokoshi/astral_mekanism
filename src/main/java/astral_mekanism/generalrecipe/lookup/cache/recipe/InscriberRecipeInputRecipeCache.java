package astral_mekanism.generalrecipe.lookup.cache.recipe;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

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
        return contains(world, topInput, topInputExtractor, middleInput, middleInputExtractor, bottomInput,
                bottomInputExtractor);
    }

    public boolean containsMTB(Level world, ItemStack topInput, ItemStack middleInput, ItemStack bottomInput) {
        return contains(world, middleInput, middleInputExtractor, topInput, topInputExtractor, bottomInput,
                bottomInputExtractor);
    }

    public boolean containsBTM(Level world, ItemStack topInput, ItemStack middleInput, ItemStack bottomInput) {
        return contains(world, bottomInput, bottomInputExtractor, topInput, topInputExtractor, middleInput,
                middleInputExtractor);
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

    private boolean contains(Level world,
            ItemStack input, Function<InscriberRecipe, ItemStackIngredient> inputExtractor,
            ItemStack other1, Function<InscriberRecipe, ItemStackIngredient> other1Extractor,
            ItemStack other2, Function<InscriberRecipe, ItemStackIngredient> other2Extractor) {
        initCacheIfNeeded(world);
        return complexRecipes.stream().anyMatch(recipe -> {
            if (input.isEmpty() || inputExtractor.apply(recipe) == null) {
                return false;
            }
            boolean result = true;
            result &= (other1.isEmpty() || other1Extractor.apply(recipe) == null
                    || other1Extractor.apply(recipe).testType(other1));
            result &= (other2.isEmpty() || other2Extractor.apply(recipe) == null
                    || other2Extractor.apply(recipe).testType(other2));
            return result;
        });
    }
}
