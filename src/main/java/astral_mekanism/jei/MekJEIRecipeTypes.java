package astral_mekanism.jei;

import astral_mekanism.recipes.recipe.FluidFluidToFluidRecipe;
import astral_mekanism.recipes.recipe.GreenHouseRecipe;
import astral_mekanism.registries.AstralMekanismBlocks;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.client.jei.MekanismJEIRecipeType;

public class MekJEIRecipeTypes {
	public static final MekanismJEIRecipeType<GreenHouseRecipe> Greenhouse_recipe = new MekanismJEIRecipeType<>(
			AstralMekanismBlocks.Greenhouse, GreenHouseRecipe.class);
	public static final MekanismJEIRecipeType<ItemStackToFluidRecipe> Melter_recipe = new MekanismJEIRecipeType<>(
			AstralMekanismBlocks.Melter, ItemStackToFluidRecipe.class);
	public static final MekanismJEIRecipeType<FluidFluidToFluidRecipe> FLUID_INFUSER_RECIPE = new MekanismJEIRecipeType<>(
			AstralMekanismBlocks.FLUID_INFUSER, FluidFluidToFluidRecipe.class);
}
