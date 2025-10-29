package astral_mekanism.recipes.inputRecipeCache;

import java.util.function.BiPredicate;
import java.util.function.Function;

import astral_mekanism.recipes.ingredient.ArrayItemStackIngredient;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.DoubleInputRecipeCache;
import mekanism.common.recipe.lookup.cache.TripleInputRecipeCache;
import mekanism.common.recipe.lookup.cache.type.ChemicalInputCache;
import mekanism.common.recipe.lookup.cache.type.FluidInputCache;
import mekanism.common.recipe.lookup.cache.type.ItemInputCache;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.fluids.FluidStack;

public class AMInputRecipeCache {
    public static class ItemFluid<RECIPE extends MekanismRecipe & BiPredicate<ItemStack, FluidStack>> extends
            DoubleInputRecipeCache<ItemStack, ItemStackIngredient, FluidStack, FluidStackIngredient, RECIPE, ItemInputCache<RECIPE>, FluidInputCache<RECIPE>> {

        public ItemFluid(MekanismRecipeType<RECIPE, ?> recipeType,
                Function<RECIPE, ItemStackIngredient> inputAExtractor,
                Function<RECIPE, FluidStackIngredient> inputBExtractor) {
            super(recipeType, inputAExtractor, new ItemInputCache<>(), inputBExtractor,
                    new FluidInputCache<>());
        }
    }

    public static class FluidFluid<RECIPE extends MekanismRecipe & BiPredicate<FluidStack, FluidStack>> extends
            DoubleInputRecipeCache<FluidStack, FluidStackIngredient, FluidStack, FluidStackIngredient, RECIPE, FluidInputCache<RECIPE>, FluidInputCache<RECIPE>> {

        public FluidFluid(MekanismRecipeType<RECIPE, ?> recipeType,
                Function<RECIPE, FluidStackIngredient> inputAExtractor,
                Function<RECIPE, FluidStackIngredient> inputBExtractor) {
            super(recipeType, inputAExtractor, new FluidInputCache<>(), inputBExtractor,
                    new FluidInputCache<>());
        }
    }

    public static class ArrayItemFluidGas<RECIPE extends MekanismRecipe & TriPredicate<ItemStack[], FluidStack, GasStack>>
            extends
            TripleInputRecipeCache<ItemStack[], ArrayItemStackIngredient, FluidStack, FluidStackIngredient, GasStack, ChemicalStackIngredient<Gas, GasStack>, RECIPE, ArrayItemInputCache<RECIPE>, FluidInputCache<RECIPE>, ChemicalInputCache<Gas, GasStack, RECIPE>> {

        public ArrayItemFluidGas(MekanismRecipeType<RECIPE, ?> recipeType,
                Function<RECIPE, ArrayItemStackIngredient> inputAExtractor,
                Function<RECIPE, FluidStackIngredient> inputBExtractor,
                Function<RECIPE, ChemicalStackIngredient<Gas, GasStack>> inputCExtractor) {
            super(recipeType, inputAExtractor, new ArrayItemInputCache<>(), inputBExtractor, new FluidInputCache<>(), inputCExtractor, new ChemicalInputCache<>());
        }

    }
}
