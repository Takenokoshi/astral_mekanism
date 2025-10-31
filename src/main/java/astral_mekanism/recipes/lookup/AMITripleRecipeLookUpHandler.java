package astral_mekanism.recipes.lookup;

import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.ArrayItemFluidGas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.lookup.ITripleRecipeLookupHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.fluids.FluidStack;

public class AMITripleRecipeLookUpHandler {

    public interface ArrayItemFluidGasRecipeLookUpHandler<RECIPE extends MekanismRecipe & TriPredicate<ItemStack[], FluidStack, GasStack>>
            extends ITripleRecipeLookupHandler<ItemStack[], FluidStack, GasStack, RECIPE, ArrayItemFluidGas<RECIPE>> {

    }
}
