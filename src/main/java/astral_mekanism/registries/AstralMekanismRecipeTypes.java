package astral_mekanism.registries;

import java.util.function.Function;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.mixin.mekanism.MekanismRecipeTypeMixin;
import astral_mekanism.recipes.InputRecipeCache2;
import astral_mekanism.recipes.InputRecipeCache2.FluidFluid;
import astral_mekanism.recipes.InputRecipeCache2.ItemFluid;
import astral_mekanism.recipes.recipe.FluidFluidToFluidRecipe;
import astral_mekanism.recipes.recipe.GreenHouseRecipe;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleChemical;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleItem;
import mekanism.common.registration.impl.RecipeTypeDeferredRegister;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;

public class AstralMekanismRecipeTypes {

	public static final RecipeTypeDeferredRegister RECIPE_TYPES = new RecipeTypeDeferredRegister(
			AstralMekanismID.MODID);

	private static <MR extends MekanismRecipe, IIRC extends IInputRecipeCache> RecipeTypeRegistryObject<MR, IIRC> register(
			String name, Function<MekanismRecipeType<MR, IIRC>, IIRC> inputCacheCreator) {
		return RECIPE_TYPES.register(name, () -> {
			MekanismRecipeType<MR, IIRC> recipeType = MekanismRecipeTypeMixin.invokeNew(name,
					(t) -> null);
			if (recipeType instanceof MekanismRecipeTypeMixin rtMixin) {
				rtMixin.setRegistryName(AstralMekanismID.rl(name));
				rtMixin.setInputCache(inputCacheCreator.apply(recipeType));
			}

			return recipeType;
		});
	}

	public static final RecipeTypeRegistryObject<GreenHouseRecipe, ItemFluid<GreenHouseRecipe>> Greenhouse_recipe = register(
			"greenhouse",
			recipeType -> new InputRecipeCache2.ItemFluid<>(recipeType, GreenHouseRecipe::getInputItem,
					GreenHouseRecipe::getInputFluid));

	public static final RecipeTypeRegistryObject<ItemStackToFluidRecipe, SingleItem<ItemStackToFluidRecipe>> Melter_recipe = register(
			"melter",
			recipeType -> new InputRecipeCache.SingleItem<>(recipeType, ItemStackToFluidRecipe::getInput));

	public static final RecipeTypeRegistryObject<FluidFluidToFluidRecipe, FluidFluid<FluidFluidToFluidRecipe>> FLUID_INFUSER_RECIPE = register(
			"fluid_infuser",
			recipeType -> new InputRecipeCache2.FluidFluid<>(recipeType, FluidFluidToFluidRecipe::getInputA,
					FluidFluidToFluidRecipe::getInputB));

	public static final RecipeTypeRegistryObject<ItemStackToItemStackRecipe, SingleItem<ItemStackToItemStackRecipe>> MEKANICAL_CHARGER_RECIPE = register(
			"mekanical_charger",
			recipeType -> new InputRecipeCache.SingleItem<>(recipeType, ItemStackToItemStackRecipe::getInput));

	public static final RecipeTypeRegistryObject<GasToGasRecipe, SingleChemical<Gas, GasStack, GasToGasRecipe>> SPS_RECIPE = register(
			"sps", recipeType -> new InputRecipeCache.SingleChemical<Gas, GasStack, GasToGasRecipe>(recipeType, GasToGasRecipe::getInput));
}