package astral_mekanism.recipes.lookup;

import java.util.function.BiPredicate;

import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.FluidFluid;
import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.ItemFluid;
import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.ItemItemFluid;
import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.QuadItem;
import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.TripleItem;
import astral_mekanism.util.AMInterface.QuadPredicate;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.lookup.IDoubleRecipeLookupHandler;
import mekanism.common.recipe.lookup.ITripleRecipeLookupHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.fluids.FluidStack;

public class AMIRecipeLookUpHandler {

    public static interface ItemFluidRecipeLookupHandler<RECIPE extends MekanismRecipe & BiPredicate<ItemStack, FluidStack>>
            extends IDoubleRecipeLookupHandler<ItemStack, FluidStack, RECIPE, ItemFluid<RECIPE>> {
    }

    public static interface FluidFluidRecipeLookupHandler<RECIPE extends MekanismRecipe & BiPredicate<FluidStack, FluidStack>>
            extends IDoubleRecipeLookupHandler<FluidStack, FluidStack, RECIPE, FluidFluid<RECIPE>> {
    }

    public static interface TripleItemRecipeLookUpHandler<RECIPE extends MekanismRecipe & TriPredicate<ItemStack, ItemStack, ItemStack>>
            extends ITripleRecipeLookupHandler<ItemStack, ItemStack, ItemStack, RECIPE, TripleItem<RECIPE>> {

    }

    public static interface ItemItemFluidRecipeLookUpHandler<RECIPE extends MekanismRecipe & TriPredicate<ItemStack, ItemStack, FluidStack>>
            extends ITripleRecipeLookupHandler<ItemStack, ItemStack, FluidStack, RECIPE, ItemItemFluid<RECIPE>> {
    }

    public static interface QuadrupleItemRecipeLookUpHandler<RECIPE extends MekanismRecipe & QuadPredicate<ItemStack, ItemStack, ItemStack, ItemStack>>
            extends IQuadrupleRecipeLookUpHandler<ItemStack, ItemStack, ItemStack, ItemStack, RECIPE, QuadItem<RECIPE>> {
    }

}