package astral_mekanism.recipes.inputRecipeCache;

import java.util.function.BiPredicate;
import java.util.function.Function;

import astral_mekanism.util.AMEInterface.QuadPredicate;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.DoubleInputRecipeCache;
import mekanism.common.recipe.lookup.cache.EitherSideInputRecipeCache;
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
            EitherSideInputRecipeCache<FluidStack, FluidStackIngredient, RECIPE, FluidInputCache<RECIPE>> {

        public FluidFluid(MekanismRecipeType<RECIPE, ?> recipeType,
                Function<RECIPE, FluidStackIngredient> inputAExtractor,
                Function<RECIPE, FluidStackIngredient> inputBExtractor) {
            super(recipeType, inputAExtractor, inputBExtractor, new FluidInputCache<>());
        }

    }

    public static class TripleItem<RECIPE extends MekanismRecipe & TriPredicate<ItemStack, ItemStack, ItemStack>>
            extends
            TripleInputRecipeCache<ItemStack, ItemStackIngredient, ItemStack, ItemStackIngredient, ItemStack, ItemStackIngredient, RECIPE, ItemInputCache<RECIPE>, ItemInputCache<RECIPE>, ItemInputCache<RECIPE>> {

        public TripleItem(MekanismRecipeType<RECIPE, ?> recipeType,
                Function<RECIPE, ItemStackIngredient> inputAExtractor,
                Function<RECIPE, ItemStackIngredient> inputBExtractor,
                Function<RECIPE, ItemStackIngredient> inputCExtractor) {
            super(recipeType,
                    inputAExtractor, new ItemInputCache<>(),
                    inputBExtractor, new ItemInputCache<>(),
                    inputCExtractor, new ItemInputCache<>());
        }

    }

    public static class ItemItemFluid<RECIPE extends MekanismRecipe & TriPredicate<ItemStack, ItemStack, FluidStack>>
            extends
            TripleInputRecipeCache<ItemStack, ItemStackIngredient, ItemStack, ItemStackIngredient, FluidStack, FluidStackIngredient, RECIPE, ItemInputCache<RECIPE>, ItemInputCache<RECIPE>, FluidInputCache<RECIPE>> {

        public ItemItemFluid(MekanismRecipeType<RECIPE, ?> recipeType,
                Function<RECIPE, ItemStackIngredient> inputAExtractor,
                Function<RECIPE, ItemStackIngredient> inputBExtractor,
                Function<RECIPE, FluidStackIngredient> inputCExtractor) {
            super(recipeType,
                    inputAExtractor, new ItemInputCache<>(),
                    inputBExtractor, new ItemInputCache<>(),
                    inputCExtractor, new FluidInputCache<>());
        }

    }

    public static class QuadItem<RECIPE extends MekanismRecipe & QuadPredicate<ItemStack, ItemStack, ItemStack, ItemStack>>
            extends
            QuadrupleInputRecipeCache<ItemStack, ItemStackIngredient, ItemStack, ItemStackIngredient, ItemStack, ItemStackIngredient, ItemStack, ItemStackIngredient, RECIPE, ItemInputCache<RECIPE>, ItemInputCache<RECIPE>, ItemInputCache<RECIPE>, ItemInputCache<RECIPE>> {

        public QuadItem(MekanismRecipeType<RECIPE, ?> recipeType,
                Function<RECIPE, ItemStackIngredient> inputAExtractor,
                Function<RECIPE, ItemStackIngredient> inputBExtractor,
                Function<RECIPE, ItemStackIngredient> inputCExtractor,
                Function<RECIPE, ItemStackIngredient> inputDExtractor) {
            super(recipeType, inputAExtractor, inputBExtractor, inputCExtractor, inputDExtractor,
                    new ItemInputCache<>(), new ItemInputCache<>(), new ItemInputCache<>(), new ItemInputCache<>());
        }

    }

    public static class GasInfusion<RECIPE extends MekanismRecipe & BiPredicate<GasStack, InfusionStack>>
            extends
            DoubleInputRecipeCache<GasStack, ChemicalStackIngredient<Gas, GasStack>, InfusionStack, ChemicalStackIngredient<InfuseType, InfusionStack>, RECIPE, ChemicalInputCache<Gas, GasStack, RECIPE>, ChemicalInputCache<InfuseType, InfusionStack, RECIPE>> {

        public GasInfusion(MekanismRecipeType<RECIPE, ?> recipeType,
                Function<RECIPE, ChemicalStackIngredient<Gas, GasStack>> inputAExtractor,
                Function<RECIPE, ChemicalStackIngredient<InfuseType, InfusionStack>> inputBExtractor) {
            super(recipeType, inputAExtractor, new ChemicalInputCache<>(), inputBExtractor, new ChemicalInputCache<>());
        }
    }
}
