package astral_mekanism.recipes.lookup;

import java.util.function.BiPredicate;

import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.FluidFluid;
import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.ItemFluid;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.lookup.IDoubleRecipeLookupHandler;
import mekanism.common.recipe.lookup.IRecipeLookupHandler.IRecipeTypedLookupHandler;
import mekanism.common.recipe.lookup.cache.DoubleInputRecipeCache;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface AMIDoubleRecipeLookUpHandler<INPUT_A, INPUT_B, RECIPE extends MekanismRecipe & BiPredicate<INPUT_A, INPUT_B>, INPUT_CACHE extends DoubleInputRecipeCache<INPUT_A, ?, INPUT_B, ?, RECIPE, ?, ?>>
        extends IRecipeTypedLookupHandler<RECIPE, INPUT_CACHE> {
    public interface ItemFluidRecipeLookupHandler<RECIPE extends MekanismRecipe & BiPredicate<ItemStack, FluidStack>>
            extends
            IDoubleRecipeLookupHandler<ItemStack, FluidStack, RECIPE, ItemFluid<RECIPE>> {
    }

    public interface FluidFluidRecipeLookupHandler<RECIPE extends MekanismRecipe & BiPredicate<FluidStack, FluidStack>>
            extends
            IDoubleRecipeLookupHandler<FluidStack, FluidStack, RECIPE, FluidFluid<RECIPE>> {
    }

}