package astral_mekanism.recipes;

import java.util.function.BiPredicate;

import javax.annotation.Nullable;

import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.FluidFluid;
import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.ItemFluid;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.common.recipe.lookup.IDoubleRecipeLookupHandler;
import mekanism.common.recipe.lookup.IRecipeLookupHandler.IRecipeTypedLookupHandler;
import mekanism.common.recipe.lookup.cache.DoubleInputRecipeCache;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface AMIDoubleRecipeLookUpHandler<INPUT_A, INPUT_B, RECIPE extends MekanismRecipe & BiPredicate<INPUT_A, INPUT_B>,
      INPUT_CACHE extends DoubleInputRecipeCache<INPUT_A, ?, INPUT_B, ?, RECIPE, ?, ?>> extends IRecipeTypedLookupHandler<RECIPE, INPUT_CACHE> {

    default boolean containsRecipeAB(INPUT_A inputA, INPUT_B inputB) {
        return getRecipeType().getInputCache().containsInputAB(getHandlerWorld(), inputA, inputB);
    }
    default boolean containsRecipeBA(INPUT_A inputA, INPUT_B inputB) {
        return getRecipeType().getInputCache().containsInputBA(getHandlerWorld(), inputA, inputB);
    }
    default boolean containsRecipeA(INPUT_A input) {
        return getRecipeType().getInputCache().containsInputA(getHandlerWorld(), input);
    }
    default boolean containsRecipeB(INPUT_B input) {
        return getRecipeType().getInputCache().containsInputB(getHandlerWorld(), input);
    }
    @Nullable
    default RECIPE findFirstRecipe(INPUT_A inputA, INPUT_B inputB) {
        return getRecipeType().getInputCache().findFirstRecipe(getHandlerWorld(), inputA, inputB);
    }
    @Nullable
    default RECIPE findFirstRecipe(IInputHandler<INPUT_A> inputAHandler, IInputHandler<INPUT_B> inputBHandler) {
        return findFirstRecipe(inputAHandler.getInput(), inputBHandler.getInput());
    }
    interface ItemFluidRecipeLookupHandler<RECIPE extends MekanismRecipe & BiPredicate<ItemStack, FluidStack>> extends
          IDoubleRecipeLookupHandler<ItemStack, FluidStack, RECIPE, ItemFluid<RECIPE>> {
    }
    
    interface FluidFluidRecipeLookupHandler<RECIPE extends MekanismRecipe & BiPredicate<FluidStack, FluidStack>> extends
          IDoubleRecipeLookupHandler<FluidStack, FluidStack, RECIPE, FluidFluid<RECIPE>> {
    }
    
}