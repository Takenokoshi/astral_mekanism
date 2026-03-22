package astral_mekanism.generalrecipe.lookup.cache.recipe;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.generalrecipe.IUnifiedRecipeType;
import astral_mekanism.generalrecipe.recipe.CropSoilRecipe;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public class CropSoilInputRecipeCache extends GeneralInputRecipeCache<Container, CropSoilRecipe> {

    private List<CropSoilRecipe> allRecipes = List.of();

    public CropSoilInputRecipeCache(IUnifiedRecipeType<CropSoilRecipe, ?> recipeType) {
        super(recipeType);
    }

    @Override
    public void clear() {
        super.clear();
        allRecipes = List.of();
    }

    @Override
    public void initCacheIfNeeded(Level world) {
        createCache(world);
    }

    private void createCache(Level world) {
        if (allRecipes.isEmpty() && world != null) {
            allRecipes = CropSoilRecipe.getAllRecipes(world.getRecipeManager());
        }
    }

    @Override
    protected void initCache(List<CropSoilRecipe> recipes) {
    }

    public boolean containsRecipeCrop(Level world, ItemStack cropStack) {
        if (cropStack.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return allRecipes.stream().filter(r -> r.getCrop().test(cropStack)).findFirst().isPresent();
    }

    public boolean containsRecipeSoil(Level world, ItemStack soilStack) {
        if (soilStack.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return allRecipes.stream().filter(r -> r.getSoil().test(soilStack)).findFirst().isPresent();
    }

    public boolean containsRecipeFluid(Level world, FluidStack fluidStack) {
        if (fluidStack.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return allRecipes.stream().filter(r -> r.getWater().test(fluidStack)).findFirst().isPresent();
    }

    public boolean containsCropOther(Level world, ItemStack cropStack, ItemStack soilStack, FluidStack fluidStack) {
        if (cropStack.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return allRecipes.stream().filter(r -> r.emptyableTest(cropStack, soilStack, fluidStack)).findFirst()
                .isPresent();
    }

    public boolean containsSoilOther(Level world, ItemStack cropStack, ItemStack soilStack, FluidStack fluidStack) {
        if (soilStack.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return allRecipes.stream().filter(r -> r.emptyableTest(cropStack, soilStack, fluidStack)).findFirst()
                .isPresent();
    }

    public boolean containsFluidOther(Level world, ItemStack cropStack, ItemStack soilStack, FluidStack fluidStack) {
        if (fluidStack.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return allRecipes.stream().filter(r -> r.emptyableTest(cropStack, soilStack, fluidStack)).findFirst()
                .isPresent();
    }

    public @Nullable CropSoilRecipe findFirstRecipe(Level world, ItemStack cropStack, ItemStack soilStack,
            FluidStack fluidStack) {
        initCacheIfNeeded(world);
        return allRecipes.stream().filter(r -> r.test(cropStack, soilStack, fluidStack)).findFirst().orElse(null);
    }

}
