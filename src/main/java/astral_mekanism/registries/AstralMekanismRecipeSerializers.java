package astral_mekanism.registries;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.recipes.irecipe.AstralCraftingIRecipe;
import astral_mekanism.recipes.irecipe.CompressingAMIRecipe;
import astral_mekanism.recipes.irecipe.DissolutionAMIrecipe;
import astral_mekanism.recipes.irecipe.FluidInfuserIRecipe;
import astral_mekanism.recipes.irecipe.FormulizedSawingIRecipe;
import astral_mekanism.recipes.irecipe.GreenhouseIRecipe;
import astral_mekanism.recipes.irecipe.InjectingAMIRecipe;
import astral_mekanism.recipes.irecipe.MekanicalChagerIRecipe;
import astral_mekanism.recipes.irecipe.MekanicalInscriberIRecipe;
import astral_mekanism.recipes.irecipe.MekanicalPresserIRecipe;
import astral_mekanism.recipes.irecipe.MelterIRecipe;
import astral_mekanism.recipes.irecipe.PurifyingAMIRecipe;
import astral_mekanism.recipes.irecipe.SPSIRecipe;
import astral_mekanism.recipes.recipe.AstralCraftingRecipe;
import astral_mekanism.recipes.recipe.FluidFluidToFluidRecipe;
import astral_mekanism.recipes.recipe.GreenhouseRecipe;
import astral_mekanism.recipes.recipe.TripleItemToItemRecipe;
import astral_mekanism.recipes.recipe.ItemToItemItemRecipe;
import astral_mekanism.recipes.serializer.AstralCraftingRecipeSerializer;
import astral_mekanism.recipes.serializer.FluidFluidToFluidRecipeSerializer;
import astral_mekanism.recipes.serializer.GreenHouseRecipeSerializer;
import astral_mekanism.recipes.serializer.TripleItemToItemRecipeSerializer;
import astral_mekanism.recipes.serializer.ItemStackToFluidRecipeSerializer;
import astral_mekanism.recipes.serializer.ItemToItemItemRecipeSerializer;
import mekanism.api.recipes.ChemicalDissolutionRecipe;
import mekanism.api.recipes.CombinerRecipe;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.common.recipe.serializer.ChemicalDissolutionRecipeSerializer;
import mekanism.common.recipe.serializer.CombinerRecipeSerializer;
import mekanism.common.recipe.serializer.GasToGasRecipeSerializer;
import mekanism.common.recipe.serializer.ItemStackGasToItemStackRecipeSerializer;
import mekanism.common.recipe.serializer.ItemStackToItemStackRecipeSerializer;
import mekanism.common.registration.impl.RecipeSerializerDeferredRegister;
import mekanism.common.registration.impl.RecipeSerializerRegistryObject;

public class AstralMekanismRecipeSerializers {

    private AstralMekanismRecipeSerializers() {
    }

    public static final RecipeSerializerDeferredRegister RECIPE_SERIALIZERS = new RecipeSerializerDeferredRegister(
            AstralMekanismID.MODID);

    public static final RecipeSerializerRegistryObject<ItemToItemItemRecipe> FORMULIZED_SAWING_RECIPE = RECIPE_SERIALIZERS
            .register("sawing", () -> new ItemToItemItemRecipeSerializer<>(FormulizedSawingIRecipe::new));

    public static final RecipeSerializerRegistryObject<GreenhouseRecipe> GREENHOUSE_RECIPE = RECIPE_SERIALIZERS
            .register("greenhouse", () -> new GreenHouseRecipeSerializer<>(GreenhouseIRecipe::new));

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

    public static final RecipeSerializerRegistryObject<ItemStackGasToItemStackRecipe> AM_INJECTING = RECIPE_SERIALIZERS
            .register("am_injecting", () -> new ItemStackGasToItemStackRecipeSerializer<>(InjectingAMIRecipe::new));

    public static final RecipeSerializerRegistryObject<ItemStackGasToItemStackRecipe> AM_PURIFYING = RECIPE_SERIALIZERS
            .register("am_purifying", () -> new ItemStackGasToItemStackRecipeSerializer<>(PurifyingAMIRecipe::new));

    public static final RecipeSerializerRegistryObject<ItemStackGasToItemStackRecipe> AM_COMPRESSING = RECIPE_SERIALIZERS
            .register("am_compressing", () -> new ItemStackGasToItemStackRecipeSerializer<>(CompressingAMIRecipe::new));

    public static final RecipeSerializerRegistryObject<AstralCraftingRecipe> ASTRAL_CRAFTING = RECIPE_SERIALIZERS
            .register("astral_crafting", () -> new AstralCraftingRecipeSerializer<>(AstralCraftingIRecipe::new));

    public static final RecipeSerializerRegistryObject<TripleItemToItemRecipe> MEKANICAL_PRESSER = RECIPE_SERIALIZERS
            .register("mekanical_presser",
                    () -> new TripleItemToItemRecipeSerializer<>(MekanicalPresserIRecipe::new));

    public static final RecipeSerializerRegistryObject<CombinerRecipe> MEKANICAL_INSCRIBER = RECIPE_SERIALIZERS
            .register("mekanical_inscriber",
                    () -> new CombinerRecipeSerializer<>(MekanicalInscriberIRecipe::new));

    public static final RecipeSerializerRegistryObject<ChemicalDissolutionRecipe> AM_DISSOLUTION = RECIPE_SERIALIZERS
            .register("am_dissolution", () -> new ChemicalDissolutionRecipeSerializer<>(DissolutionAMIrecipe::new));
}
