package astral_mekanism.recipes.inputRecipeCache;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.checkerframework.checker.nullness.qual.Nullable;

import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.AbstractInputRecipeCache;
import mekanism.common.recipe.lookup.cache.type.FluidInputCache;
import mekanism.common.recipe.lookup.cache.type.ItemInputCache;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public class MekanicalTransformRecipeCache
        extends AbstractInputRecipeCache<MekanicalTransformRecipe> {
    private final Set<MekanicalTransformRecipe> complexIngredientIA = new HashSet<>();
    private final Set<MekanicalTransformRecipe> complexIngredientIB = new HashSet<>();
    private final Set<MekanicalTransformRecipe> complexIngredientIC = new HashSet<>();
    private final Set<MekanicalTransformRecipe> complexIngredientFA = new HashSet<>();
    private final Set<MekanicalTransformRecipe> complexIngredientFB = new HashSet<>();
    private final Set<MekanicalTransformRecipe> complexRecipes = new HashSet<>();
    private final ItemInputCache<MekanicalTransformRecipe> cacheIA = new ItemInputCache<>();
    private final ItemInputCache<MekanicalTransformRecipe> cacheIB = new ItemInputCache<>();
    private final ItemInputCache<MekanicalTransformRecipe> cacheIC = new ItemInputCache<>();
    private final FluidInputCache<MekanicalTransformRecipe> cacheFA = new FluidInputCache<>();
    private final FluidInputCache<MekanicalTransformRecipe> cacheFB = new FluidInputCache<>();

    public MekanicalTransformRecipeCache(MekanismRecipeType<MekanicalTransformRecipe, ?> recipeType) {
        super(recipeType);
    }

    public void clear() {
        super.clear();
        complexIngredientIA.clear();
        complexIngredientIB.clear();
        complexIngredientIC.clear();
        complexIngredientFA.clear();
        complexIngredientFB.clear();
        complexRecipes.clear();
        cacheIA.clear();
        cacheIB.clear();
        cacheIC.clear();
        cacheFA.clear();
        cacheFB.clear();
    }

    public boolean containsInputIA(Level world, ItemStack input) {
        return containsInput(world, input, MekanicalTransformRecipe::getInputItemA, cacheIA, complexIngredientIA);
    }

    public boolean containsInputIB(Level world, ItemStack input) {
        return containsInput(world, input, MekanicalTransformRecipe::getInputItemB, cacheIB, complexIngredientIB);
    }

    public boolean containsInputIC(Level world, ItemStack input) {
        return containsInput(world, input, MekanicalTransformRecipe::getInputItemC, cacheIC, complexIngredientIC);
    }

    public boolean containsInputFA(Level world, FluidStack input) {
        return containsInput(world, input, MekanicalTransformRecipe::getInputFluidA, cacheFA, complexIngredientFA);
    }

    public boolean containsInputFB(Level world, FluidStack input) {
        return containsInput(world, input, MekanicalTransformRecipe::getInputFluidB, cacheFB, complexIngredientFB);
    }

    public boolean containsInputIAOther(Level world, ItemStack inputIA, ItemStack inputIB, ItemStack inputIC,
            FluidStack inputFA, FluidStack inputFB) {
        if (inputIA.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return complexIngredientIA.stream()
                .anyMatch(recipe -> recipe.anotherTest(inputIA, inputIB, inputIC, inputFA, inputFB));
    }

    public boolean containsInputIBOther(Level world, ItemStack inputIA, ItemStack inputIB, ItemStack inputIC,
            FluidStack inputFA, FluidStack inputFB) {
        if (inputIB.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return complexIngredientIB.stream()
                .anyMatch(recipe -> recipe.anotherTest(inputIA, inputIB, inputIC, inputFA, inputFB));
    }

    public boolean containsInputICOther(Level world, ItemStack inputIA, ItemStack inputIB, ItemStack inputIC,
            FluidStack inputFA, FluidStack inputFB) {
        if (inputIC.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return complexIngredientIC.stream()
                .anyMatch(recipe -> recipe.anotherTest(inputIA, inputIB, inputIC, inputFA, inputFB));
    }

    public boolean containsInputFAOther(Level world, ItemStack inputIA, ItemStack inputIB, ItemStack inputIC,
            FluidStack inputFA, FluidStack inputFB) {
        if (inputFA.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return complexIngredientFA.stream()
                .anyMatch(recipe -> recipe.anotherTest(inputIA, inputIB, inputIC, inputFA, inputFB));
    }

    public boolean containsInputFBOther(Level world, ItemStack inputIA, ItemStack inputIB, ItemStack inputIC,
            FluidStack inputFA, FluidStack inputFB) {
        if (inputFB.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return complexIngredientFB.stream()
                .anyMatch(recipe -> recipe.anotherTest(inputIA, inputIB, inputIC, inputFA, inputFB));
    }

    @Override
    protected void initCache(List<MekanicalTransformRecipe> recipes) {
        for (MekanicalTransformRecipe recipe : recipes) {
            boolean complex = false;
            if (cacheIA.mapInputs(recipe, recipe.getInputItemA())) {
                complexIngredientIA.add(recipe);
                complex |= true;
            }
            if (cacheIB.mapInputs(recipe, recipe.getInputItemB())) {
                complexIngredientIB.add(recipe);
                complex |= true;
            }
            if (cacheIC.mapInputs(recipe, recipe.getInputItemC())) {
                complexIngredientIC.add(recipe);
                complex |= true;
            }
            if (cacheFA.mapInputs(recipe, recipe.getInputFluidA())) {
                complexIngredientFA.add(recipe);
                complex |= true;
            }
            if (cacheFB.mapInputs(recipe, recipe.getInputFluidB())) {
                complexIngredientFB.add(recipe);
                complex |= true;
            }
            if (complex) {
                complexRecipes.add(recipe);
            }
        }
    }

    public @Nullable MekanicalTransformRecipe findFirstRecipe(@Nullable Level world, ItemStack inputIA,
            ItemStack inputIB, ItemStack inputIC, FluidStack inputFA,
            FluidStack inputFB) {
        if (inputIA.isEmpty() || inputIB.isEmpty() || inputIC.isEmpty() || inputFA.isEmpty()) {
            return null;
        }
        initCacheIfNeeded(world);
        Predicate<MekanicalTransformRecipe> matchPredicate = r -> r.test(inputIA, inputIB, inputIC, inputFA, inputFB);
        MekanicalTransformRecipe recipe = cacheIA.findFirstRecipe(inputIA, matchPredicate);
        return recipe == null ? findFirstRecipe(complexRecipes, matchPredicate) : recipe;
    }

}
