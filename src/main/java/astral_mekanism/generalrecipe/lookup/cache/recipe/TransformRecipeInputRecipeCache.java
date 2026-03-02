package astral_mekanism.generalrecipe.lookup.cache.recipe;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import appeng.recipes.transform.TransformRecipe;
import astral_mekanism.generalrecipe.IUnifiedRecipeType;
import astral_mekanism.generalrecipe.cachedrecipe.TransformCachedRecipe;
import astral_mekanism.generalrecipe.lookup.cache.type.FluidGeneralInputCache;
import astral_mekanism.generalrecipe.lookup.cache.type.ItemGeneralInputCache;
import mekanism.api.recipes.ingredients.creator.IFluidStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public class TransformRecipeInputRecipeCache extends GeneralInputRecipeCache<Container, TransformRecipe> {

    public static final Function<TransformRecipe, FluidStack> fluidExtractor = TransformCachedRecipe.fluidExtractor;

    private final Set<TransformRecipe> complexRecipes;
    private final Set<TransformRecipe> complexRecipesFluid;
    private final Set<TransformRecipe> complexRecipesFirst;
    private final Set<TransformRecipe> complexRecipesSecond;
    private final Set<TransformRecipe> complexRecipesThird;
    private final FluidGeneralInputCache<TransformRecipe> cacheFluid;
    private final ItemGeneralInputCache<TransformRecipe> cacheFirst;
    private final ItemGeneralInputCache<TransformRecipe> cacheSecond;
    private final ItemGeneralInputCache<TransformRecipe> cacheThird;

    public TransformRecipeInputRecipeCache(IUnifiedRecipeType<TransformRecipe, ?> recipeType) {
        super(recipeType);
        this.complexRecipes = new HashSet<>();
        this.complexRecipesFluid = new HashSet<>();
        this.complexRecipesFirst = new HashSet<>();
        this.complexRecipesSecond = new HashSet<>();
        this.complexRecipesThird = new HashSet<>();
        this.cacheFluid = new FluidGeneralInputCache<>();
        this.cacheFirst = new ItemGeneralInputCache<>();
        this.cacheSecond = new ItemGeneralInputCache<>();
        this.cacheThird = new ItemGeneralInputCache<>();
    }

    public boolean containsInputFluid(Level world, FluidStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return cacheFluid.contains(stack)
                || complexRecipesFluid.stream().anyMatch(
                        recipe -> recipe.ingredients.size() < 4 && stack.isFluidEqual(fluidExtractor.apply(recipe)));
    }

    public boolean containsInputFirst(Level world, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return cacheFirst.contains(stack) || complexRecipesFirst.stream().anyMatch(recipe -> {
            int size = recipe.ingredients.size();
            if (size > 0 && size < 4) {
                return recipe.ingredients.get(0).test(stack);
            }
            return false;
        });
    }

    public boolean containsInputSecond(Level world, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return cacheSecond.contains(stack) || complexRecipesSecond.stream().anyMatch(recipe -> {
            int size = recipe.ingredients.size();
            if (size > 1 && size < 4) {
                return recipe.ingredients.get(1).test(stack);
            }
            return false;
        });
    }

    public boolean containsInputThird(Level world, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return cacheThird.contains(stack) || complexRecipesThird.stream().anyMatch(recipe -> {
            int size = recipe.ingredients.size();
            if (size > 2 && size < 4) {
                return recipe.ingredients.get(2).test(stack);
            }
            return false;
        });
    }

    public boolean containsFluidOther(Level world, FluidStack fluid, ItemStack first, ItemStack second,
            ItemStack third) {
        if (fluid.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return complexRecipesFluid.stream().anyMatch(recipe -> {
            int size = recipe.ingredients.size();
            if (size > 3 || size < 1) {
                return false;
            } else if (fluidExtractor.apply(recipe).isFluidEqual(fluid)
                    && (first.isEmpty() || recipe.ingredients.get(0).test(first))) {
                if (size == 1) {
                    return second.isEmpty() && third.isEmpty();
                } else if (size == 2) {
                    return third.isEmpty()
                            && (second.isEmpty() || recipe.ingredients.get(1).test(second));
                } else {
                    return (second.isEmpty() || recipe.ingredients.get(1).test(second))
                            && (third.isEmpty() || recipe.ingredients.get(2).test(third));
                }
            } else {
                return false;
            }
        });
    }

    public boolean containsFirstOther(Level world, FluidStack fluid, ItemStack first, ItemStack second,
            ItemStack third) {
        if (first.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return complexRecipesFirst.stream().anyMatch(recipe -> {
            int size = recipe.ingredients.size();
            if (size > 3 || size < 1) {
                return false;
            } else if ((fluid.isEmpty() || fluidExtractor.apply(recipe).isFluidEqual(fluid))
                    && recipe.ingredients.get(0).test(first)) {
                if (size == 1) {
                    return second.isEmpty() && third.isEmpty();
                } else if (size == 2) {
                    return third.isEmpty()
                            && (second.isEmpty() || recipe.ingredients.get(1).test(second));
                } else {
                    return (second.isEmpty() || recipe.ingredients.get(1).test(second))
                            && (third.isEmpty() || recipe.ingredients.get(2).test(third));
                }
            } else {
                return false;
            }
        });
    }

    public boolean containsSecondOther(Level world, FluidStack fluid, ItemStack first, ItemStack second,
            ItemStack third) {
        if (second.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return complexRecipesSecond.stream().anyMatch(recipe -> {
            int size = recipe.ingredients.size();
            if (size > 3 || size < 2) {
                return false;
            } else if ((fluid.isEmpty() || fluidExtractor.apply(recipe).isFluidEqual(fluid))
                    && (first.isEmpty() || recipe.ingredients.get(0).test(first))
                    && recipe.ingredients.get(1).test(second)) {
                if (size == 2) {
                    return third.isEmpty();
                } else {
                    return (third.isEmpty() || recipe.ingredients.get(2).test(third));
                }
            } else {
                return false;
            }
        });
    }

    public boolean containsThirdOther(Level world, FluidStack fluid, ItemStack first, ItemStack second,
            ItemStack third) {
        if (third.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return complexRecipesThird.stream()
                .anyMatch(recipe -> recipe.ingredients.size() == 3
                        && (fluid.isEmpty() || fluidExtractor.apply(recipe).isFluidEqual(fluid))
                        && (first.isEmpty() || recipe.ingredients.get(0).test(first))
                        && (second.isEmpty() || recipe.ingredients.get(1).test(second))
                        && recipe.ingredients.get(2).test(third));
    }

    @Override
    public void clear() {
        super.clear();
        complexRecipes.clear();
        complexRecipesFluid.clear();
        complexRecipesFirst.clear();
        complexRecipesSecond.clear();
        complexRecipesThird.clear();
        cacheFluid.clear();
        cacheFirst.clear();
        cacheSecond.clear();
        cacheThird.clear();
    }

    public @Nullable TransformRecipe findFirstRecipe(@Nullable Level world, FluidStack fluid,
            ItemStack first, ItemStack second, ItemStack third) {
        if (fluid.isEmpty() || first.isEmpty()) {
            return null;
        }
        initCacheIfNeeded(world);
        Predicate<TransformRecipe> matchPredicate = r -> {
            int size = r.ingredients.size();
            boolean result = 0 < size && size < 4 && fluidExtractor.apply(r).isFluidEqual(fluid);
            result &= r.ingredients.get(0).test(first);
            result &= 1 < size ? r.ingredients.get(1).test(second) : second.isEmpty();
            result &= 2 < size ? r.ingredients.get(2).test(third) : third.isEmpty();
            return result;
        };
        TransformRecipe recipe = cacheFluid.findFirstRecipe(fluid, matchPredicate);
        return recipe == null ? findFirstRecipe(complexRecipes, matchPredicate) : recipe;
    }

    @Override
    protected void initCache(List<TransformRecipe> recipes) {
        IItemStackIngredientCreator creatorI = IngredientCreatorAccess.item();
        IFluidStackIngredientCreator creatorF = IngredientCreatorAccess.fluid();
        for (TransformRecipe recipe : recipes) {
            int size = recipe.ingredients.size();
            if (size > 3) {
                continue;
            }
            boolean complexFluid = cacheFluid.mapInputs(recipe, creatorF.from(fluidExtractor.apply(recipe)));
            boolean complexFirst = size > 0 ? cacheFirst.mapInputs(recipe, creatorI.from(recipe.ingredients.get(0)))
                    : false;
            boolean complexSecond = size > 1 ? cacheSecond.mapInputs(recipe, creatorI.from(recipe.ingredients.get(1)))
                    : false;
            boolean complexThird = size > 2 ? cacheThird.mapInputs(recipe, creatorI.from(recipe.ingredients.get(2)))
                    : false;
            if (complexFluid) {
                complexRecipesFluid.add(recipe);
            }
            if (complexFirst) {
                complexRecipesFirst.add(recipe);
            }
            if (complexSecond) {
                complexRecipesSecond.add(recipe);
            }
            if (complexThird) {
                complexRecipesThird.add(recipe);
            }
            if (complexFluid || complexFirst || complexSecond || complexThird) {
                complexRecipes.add(recipe);
            }
        }
    }
}
