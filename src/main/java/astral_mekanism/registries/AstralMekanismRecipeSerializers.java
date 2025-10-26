package astral_mekanism.registries;

import java.util.function.Function;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.recipes.Irecipe.FluidInfuserIRecipe;
import astral_mekanism.recipes.Irecipe.FormulizedSawingIRecipe;
import astral_mekanism.recipes.Irecipe.GreenHouseIRecipe;
import astral_mekanism.recipes.Irecipe.MekanicalChagerIRecipe;
import astral_mekanism.recipes.Irecipe.MelterIRecipe;
import astral_mekanism.recipes.Irecipe.SPSIRecipe;
import astral_mekanism.recipes.Irecipe.gasformulized.AMInjectionIrecipe;
import astral_mekanism.recipes.Irecipe.gasformulized.AMPurifyingIrecipe;
import astral_mekanism.recipes.recipe.FluidFluidToFluidRecipe;
import astral_mekanism.recipes.recipe.GreenHouseRecipe;
import astral_mekanism.recipes.recipe.ItemToItemItemRecipe;
import astral_mekanism.recipes.serializer.FluidFluidToFluidRecipeSerializer;
import astral_mekanism.recipes.serializer.GreenHouseRecipeSerializer;
import astral_mekanism.recipes.serializer.ItemStackToFluidRecipeSerializer;
import astral_mekanism.recipes.serializer.ItemToItemItemRecipeSerializer;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.common.recipe.serializer.GasToGasRecipeSerializer;
import mekanism.common.recipe.serializer.ItemStackChemicalToItemStackRecipeSerializer.IFactory;
import mekanism.common.recipe.serializer.ItemStackGasToItemStackRecipeSerializer;
import mekanism.common.recipe.serializer.ItemStackToItemStackRecipeSerializer;
import mekanism.common.registration.impl.RecipeSerializerDeferredRegister;
import mekanism.common.registration.impl.RecipeSerializerRegistryObject;

public class AstralMekanismRecipeSerializers {

    private AstralMekanismRecipeSerializers() {
    }

    public static final RecipeSerializerDeferredRegister RECIPE_SERIALIZERS = new RecipeSerializerDeferredRegister(
            AstralMekanismID.MODID);

    private static RecipeSerializerRegistryObject<ItemStackGasToItemStackRecipe>[] gasFReg(
            String name,
            Function<Integer, IFactory<Gas, GasStack, GasStackIngredient, ItemStackGasToItemStackRecipe>> factories) {
        @SuppressWarnings("unchecked")
        RecipeSerializerRegistryObject<ItemStackGasToItemStackRecipe>[] result = new RecipeSerializerRegistryObject[9];
        for (int i : AstralMekanismID.ZERO_EIGHT) {
            result[i] = RECIPE_SERIALIZERS.register(name + i,
                    () -> new ItemStackGasToItemStackRecipeSerializer<>(factories.apply(i)));
        }
        return result;
    }

    public static final RecipeSerializerRegistryObject<ItemToItemItemRecipe> FORMULIZED_SAWING_RECIPE = RECIPE_SERIALIZERS
            .register("sawing", () -> new ItemToItemItemRecipeSerializer<>(FormulizedSawingIRecipe::new));

    public static final RecipeSerializerRegistryObject<GreenHouseRecipe> Greenhouse_recipe = RECIPE_SERIALIZERS
            .register("greenhouse", () -> new GreenHouseRecipeSerializer<>(GreenHouseIRecipe::new));

    public static final RecipeSerializerRegistryObject<ItemStackToFluidRecipe> Melter_recipe = RECIPE_SERIALIZERS
            .register("melter", () -> new ItemStackToFluidRecipeSerializer<>(MelterIRecipe::new));

    public static final RecipeSerializerRegistryObject<FluidFluidToFluidRecipe> FLUID_INFUSER_RECIPE = RECIPE_SERIALIZERS
            .register("fluid_infuser",
                    () -> new FluidFluidToFluidRecipeSerializer<>(FluidInfuserIRecipe::new));

    public static final RecipeSerializerRegistryObject<ItemStackToItemStackRecipe> MEKANICAL_CHARGER_RECIPE = RECIPE_SERIALIZERS
            .register("mekanical_charger",
                    () -> new ItemStackToItemStackRecipeSerializer<>(MekanicalChagerIRecipe::new));

    public static final RecipeSerializerRegistryObject<GasToGasRecipe> SPS_RECIPE = RECIPE_SERIALIZERS
            .register("sps", () -> new GasToGasRecipeSerializer<>(SPSIRecipe::new));

    public static final RecipeSerializerRegistryObject<ItemStackGasToItemStackRecipe>[] AM_INJECTING = gasFReg(
            "injecting", AMInjectionIrecipe::getFac);
    public static final RecipeSerializerRegistryObject<ItemStackGasToItemStackRecipe>[] AM_PURIFYING = gasFReg(
            "purifying", AMPurifyingIrecipe::getFac);
}
