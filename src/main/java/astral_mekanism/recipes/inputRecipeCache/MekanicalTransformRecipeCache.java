package astral_mekanism.recipes.inputRecipeCache;

import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.AbstractInputRecipeCache;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public class MekanicalTransformRecipeCache
        extends AbstractInputRecipeCache<MekanicalTransformRecipe> {
    private List<MekanicalTransformRecipe> allRecipesCache = List.of();

    public MekanicalTransformRecipeCache(MekanismRecipeType<MekanicalTransformRecipe, ?> recipeType) {
        super(recipeType);
    }

    private void createCache(Level world) {
        if (allRecipesCache.isEmpty()) {
            allRecipesCache = recipeType.getRecipes(world);
        }
        if (allRecipesCache.isEmpty()) {
            allRecipesCache = world.getRecipeManager().getAllRecipesFor(recipeType);
        }
    }

    public void clear() {
        super.clear();
        allRecipesCache = List.of();
    }

    public boolean containsInputIA(Level world, ItemStack input) {
        createCache(world);
        return allRecipesCache.stream().anyMatch(r -> r.getInputItemA().test(input));
    }

    public boolean containsInputIB(Level world, ItemStack input) {
        createCache(world);
        return allRecipesCache.stream().anyMatch(r -> r.getInputItemB().test(input));
    }

    public boolean containsInputIC(Level world, ItemStack input) {
        createCache(world);
        return allRecipesCache.stream().anyMatch(r -> r.getInputItemC().test(input));
    }

    public boolean containsInputFA(Level world, FluidStack input) {
        createCache(world);
        return allRecipesCache.stream().anyMatch(r -> r.getInputFluidA().test(input));
    }

    public boolean containsInputFB(Level world, FluidStack input) {
        createCache(world);
        return allRecipesCache.stream().anyMatch(r -> r.getInputFluidB().test(input));
    }

    public boolean containsInputIAOther(Level world, ItemStack inputIA, ItemStack inputIB, ItemStack inputIC,
            FluidStack inputFA, FluidStack inputFB) {
        if (inputIA.isEmpty()) {
            return false;
        }
        createCache(world);
        return allRecipesCache.stream().anyMatch(r -> r.anotherTest(inputIA, inputIB, inputIC, inputFA, inputFB));
    }

    public boolean containsInputIBOther(Level world, ItemStack inputIA, ItemStack inputIB, ItemStack inputIC,
            FluidStack inputFA, FluidStack inputFB) {
        if (inputIB.isEmpty()) {
            return false;
        }
        createCache(world);
        return allRecipesCache.stream().anyMatch(r -> r.anotherTest(inputIA, inputIB, inputIC, inputFA, inputFB));
    }

    public boolean containsInputICOther(Level world, ItemStack inputIA, ItemStack inputIB, ItemStack inputIC,
            FluidStack inputFA, FluidStack inputFB) {
        if (inputIC.isEmpty()) {
            return false;
        }
        createCache(world);
        return allRecipesCache.stream().anyMatch(r -> r.anotherTest(inputIA, inputIB, inputIC, inputFA, inputFB));
    }

    public boolean containsInputFAOther(Level world, ItemStack inputIA, ItemStack inputIB, ItemStack inputIC,
            FluidStack inputFA, FluidStack inputFB) {
        if (inputFA.isEmpty()) {
            return false;
        }
        createCache(world);
        return allRecipesCache.stream().anyMatch(r -> r.anotherTest(inputIA, inputIB, inputIC, inputFA, inputFB));
    }

    public boolean containsInputFBOther(Level world, ItemStack inputIA, ItemStack inputIB, ItemStack inputIC,
            FluidStack inputFA, FluidStack inputFB) {
        if (inputFB.isEmpty()) {
            return false;
        }
        createCache(world);
        return allRecipesCache.stream().anyMatch(r -> r.anotherTest(inputIA, inputIB, inputIC, inputFA, inputFB));
    }

    @Override
    protected void initCache(List<MekanicalTransformRecipe> recipes) {
    }

    public @Nullable MekanicalTransformRecipe findFirstRecipe(@Nullable Level world, ItemStack inputIA,
            ItemStack inputIB, ItemStack inputIC, FluidStack inputFA,
            FluidStack inputFB) {
        if (inputIA.isEmpty() || inputIB.isEmpty() || inputIC.isEmpty() || inputFA.isEmpty()) {
            return null;
        }
        createCache(world);
        return allRecipesCache.stream().filter(r -> r.test(inputIA, inputIB, inputIC, inputFA, inputFB)).findFirst()
                .orElse(null);
    }

}
