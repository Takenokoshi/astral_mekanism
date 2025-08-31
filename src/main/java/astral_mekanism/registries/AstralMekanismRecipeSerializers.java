package astral_mekanism.registries;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.recipes.Irecipe.FluidInfuserIRecipe;
import astral_mekanism.recipes.Irecipe.GreenHouseIRecipe;
import astral_mekanism.recipes.Irecipe.MelterIRecipe;
import astral_mekanism.recipes.recipe.FluidFluidToFluidRecipe;
import astral_mekanism.recipes.recipe.GreenHouseRecipe;
import astral_mekanism.recipes.serializer.FluidFluidToFluidRecipeSerializer;
import astral_mekanism.recipes.serializer.GreenHouseRecipeSerializer;
import astral_mekanism.recipes.serializer.ItemStackToFluidRecipeSerializer;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.common.registration.impl.RecipeSerializerDeferredRegister;
import mekanism.common.registration.impl.RecipeSerializerRegistryObject;

public class AstralMekanismRecipeSerializers {

	private AstralMekanismRecipeSerializers() {
	}

	public static final RecipeSerializerDeferredRegister RECIPE_SERIALIZERS = new RecipeSerializerDeferredRegister(
			AstralMekanismID.MODID);

	public static final RecipeSerializerRegistryObject<GreenHouseRecipe> Greenhouse_recipe = RECIPE_SERIALIZERS
			.register("greenhouse", () -> new GreenHouseRecipeSerializer<>(GreenHouseIRecipe::new));

	public static final RecipeSerializerRegistryObject<ItemStackToFluidRecipe> Melter_recipe = RECIPE_SERIALIZERS
			.register("melter", () -> new ItemStackToFluidRecipeSerializer<>(MelterIRecipe::new));

	public static final RecipeSerializerRegistryObject<FluidFluidToFluidRecipe> FLUID_INFUSER_RECIPE = RECIPE_SERIALIZERS
			.register("fluid_infuser",
					() -> new FluidFluidToFluidRecipeSerializer<>(FluidInfuserIRecipe::new));

}
