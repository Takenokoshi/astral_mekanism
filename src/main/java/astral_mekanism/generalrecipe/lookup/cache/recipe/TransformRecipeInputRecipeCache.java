package astral_mekanism.generalrecipe.lookup.cache.recipe;

import java.util.List;
import org.jetbrains.annotations.Nullable;

import appeng.recipes.AERecipeTypes;
import appeng.recipes.transform.TransformRecipe;
import astral_mekanism.AstralMekanismID;
import astral_mekanism.generalrecipe.IUnifiedRecipeType;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public class TransformRecipeInputRecipeCache extends GeneralInputRecipeCache<Container, TransformRecipe> {

    private List<TransformRecipe> allRecipesCache = List.of();

    public TransformRecipeInputRecipeCache(IUnifiedRecipeType<TransformRecipe, ?> recipeType) {
        super(recipeType);
    }

    @Override
    public void initCacheIfNeeded(Level world) {
        createCache(world);
    }

    private void createCache(Level world) {
        if (allRecipesCache.isEmpty()) {
            allRecipesCache = recipeType.getRecipes(world);
        }
        if (allRecipesCache.isEmpty() && world != null) {
            allRecipesCache = world.getRecipeManager().getAllRecipesFor(AERecipeTypes.TRANSFORM);
        }
    }

    private static boolean testItem(TransformRecipe recipe, ItemStack stack, int index) {
        return recipe.ingredients.size() > index && recipe.ingredients.get(index).test(stack);
    }

    private static boolean testRecipe(TransformRecipe recipe, FluidStack fluid,
            ItemStack first, ItemStack second, ItemStack third) {
        int size = recipe.ingredients.size();
        boolean result = size < 4;
        result &= fluid.isEmpty() || AstralMekanismID.transformFluidPredicate.test(recipe, fluid);
        result &= first.isEmpty() || recipe.ingredients.get(0).test(first);
        result &= second.isEmpty() || (size > 1 && recipe.ingredients.get(1).test(second));
        result &= third.isEmpty() || (size > 2 && recipe.ingredients.get(2).test(third));
        return result;
    }

    private static boolean testInputs(TransformRecipe recipe, FluidStack fluid,
            ItemStack first, ItemStack second, ItemStack third) {
        int size = recipe.ingredients.size();
        boolean result = !fluid.isEmpty() && AstralMekanismID.transformFluidPredicate.test(recipe, fluid)
                && !first.isEmpty() && recipe.ingredients.get(0).test(first);
        result &= size > 1 ? (!second.isEmpty() && recipe.ingredients.get(1).test(second)) : second.isEmpty();
        result &= size > 2 ? (!third.isEmpty() && recipe.ingredients.get(2).test(third)) : third.isEmpty();
        return result;
    }

    public boolean containsInputFluid(Level world, FluidStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        createCache(world);
        return allRecipesCache.stream()
                .anyMatch(r -> r.ingredients.size() < 4 && AstralMekanismID.transformFluidPredicate.test(r, stack));
    }

    public boolean containsInputFirst(Level world, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        createCache(world);
        return allRecipesCache.stream()
                .anyMatch(r -> r.ingredients.size() < 4 && testItem(r, stack, 0));
    }

    public boolean containsInputSecond(Level world, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        createCache(world);
        return allRecipesCache.stream()
                .anyMatch(r -> r.ingredients.size() < 4 && testItem(r, stack, 1));
    }

    public boolean containsInputThird(Level world, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        createCache(world);
        return allRecipesCache.stream()
                .anyMatch(r -> r.ingredients.size() < 4 && testItem(r, stack, 2));
    }

    public boolean containsFluidOther(Level world, FluidStack fluid, ItemStack first, ItemStack second,
            ItemStack third) {
        if (fluid.isEmpty()) {
            return false;
        }
        createCache(world);
        return allRecipesCache.stream().anyMatch(r -> testRecipe(r, fluid, first, second, third));
    }

    public boolean containsFirstOther(Level world, FluidStack fluid, ItemStack first, ItemStack second,
            ItemStack third) {
        if (first.isEmpty()) {
            return false;
        }
        createCache(world);
        return allRecipesCache.stream().anyMatch(r -> testRecipe(r, fluid, first, second, third));
    }

    public boolean containsSecondOther(Level world, FluidStack fluid, ItemStack first, ItemStack second,
            ItemStack third) {
        if (second.isEmpty()) {
            return false;
        }
        createCache(world);
        return allRecipesCache.stream().anyMatch(r -> testRecipe(r, fluid, first, second, third));
    }

    public boolean containsThirdOther(Level world, FluidStack fluid, ItemStack first, ItemStack second,
            ItemStack third) {
        if (third.isEmpty()) {
            return false;
        }
        createCache(world);
        return allRecipesCache.stream().anyMatch(r -> testRecipe(r, fluid, first, second, third));
    }

    @Override
    public void clear() {
        super.clear();
        allRecipesCache = List.of();
    }

    public @Nullable TransformRecipe findFirstRecipe(@Nullable Level world, FluidStack fluid,
            ItemStack first, ItemStack second, ItemStack third) {
        if (fluid.isEmpty() || first.isEmpty()) {
            return null;
        }
        createCache(world);
        return allRecipesCache.stream().filter(r -> testInputs(r, fluid, first, second, third)).findFirst()
                .orElse(null);
    }

    @Override
    protected void initCache(List<TransformRecipe> recipes) {
    }
}
