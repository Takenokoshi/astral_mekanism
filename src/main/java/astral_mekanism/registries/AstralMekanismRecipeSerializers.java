package astral_mekanism.registries;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.recipes.irecipe.AstralCraftingIRecipe;
import astral_mekanism.recipes.irecipe.FluidInfuserIRecipe;
import astral_mekanism.recipes.irecipe.GreenhouseIRecipe;
import astral_mekanism.recipes.irecipe.IonExchangeIRecipe;
import astral_mekanism.recipes.irecipe.SPSIRecipe;
import astral_mekanism.recipes.irecipe.MekanicalTransformIRecipe;
import astral_mekanism.recipes.recipe.AstralCraftingRecipe;
import astral_mekanism.recipes.recipe.FluidFluidToFluidRecipe;
import astral_mekanism.recipes.recipe.GreenhouseRecipe;
import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
import astral_mekanism.recipes.serializer.AstralCraftingRecipeSerializer;
import astral_mekanism.recipes.serializer.FluidFluidToFluidRecipeSerializer;
import astral_mekanism.recipes.serializer.GreenHouseRecipeSerializer;
import astral_mekanism.recipes.serializer.MekanicalTransformRecipeSerializer;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.common.recipe.serializer.GasToGasRecipeSerializer;
import mekanism.common.registration.impl.RecipeSerializerDeferredRegister;
import mekanism.common.registration.impl.RecipeSerializerRegistryObject;

public class AstralMekanismRecipeSerializers {

    private AstralMekanismRecipeSerializers() {
    }

    public static final RecipeSerializerDeferredRegister RECIPE_SERIALIZERS = new RecipeSerializerDeferredRegister(
            AstralMekanismID.MODID);

    public static final RecipeSerializerRegistryObject<GreenhouseRecipe> GREENHOUSE_RECIPE = RECIPE_SERIALIZERS
            .register("greenhouse", () -> new GreenHouseRecipeSerializer<>(GreenhouseIRecipe::new));

    public static final RecipeSerializerRegistryObject<FluidFluidToFluidRecipe> FLUID_INFUSER_RECIPE = RECIPE_SERIALIZERS
            .register("fluid_infuser",
                    () -> new FluidFluidToFluidRecipeSerializer<>(FluidInfuserIRecipe::new));

    public static final RecipeSerializerRegistryObject<GasToGasRecipe> SPS_RECIPE = RECIPE_SERIALIZERS
            .register("sps", () -> new GasToGasRecipeSerializer<>(SPSIRecipe::new));

    public static final RecipeSerializerRegistryObject<AstralCraftingRecipe> ASTRAL_CRAFTING = RECIPE_SERIALIZERS
            .register("astral_crafting", () -> new AstralCraftingRecipeSerializer<>(AstralCraftingIRecipe::new));

    public static final RecipeSerializerRegistryObject<MekanicalTransformRecipe> MEKANICAL_TRANSFORM = RECIPE_SERIALIZERS
            .register("mekanical_transform",
                    () -> new MekanicalTransformRecipeSerializer<>(MekanicalTransformIRecipe::new));

    public static final RecipeSerializerRegistryObject<GasToGasRecipe> ION_EXCHANGE = RECIPE_SERIALIZERS
            .register("ion_exchange", () -> new GasToGasRecipeSerializer<>(IonExchangeIRecipe::new));
}
