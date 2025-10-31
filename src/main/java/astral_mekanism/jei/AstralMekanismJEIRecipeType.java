package astral_mekanism.jei;

import astral_mekanism.recipes.recipe.ExpandedCrafterRecipe;
import astral_mekanism.recipes.recipe.FluidFluidToFluidRecipe;
import astral_mekanism.recipes.recipe.GreenHouseRecipe;
import astral_mekanism.registries.AstralMekanismMachines;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.client.jei.MekanismJEIRecipeType;

public class AstralMekanismJEIRecipeType {
    public static final MekanismJEIRecipeType<GreenHouseRecipe> Greenhouse_recipe = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.GREENHOUSE, GreenHouseRecipe.class);
    public static final MekanismJEIRecipeType<ItemStackToFluidRecipe> Melter_recipe = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.MELTER, ItemStackToFluidRecipe.class);
    public static final MekanismJEIRecipeType<FluidFluidToFluidRecipe> FLUID_INFUSER_RECIPE = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.FLUID_INFUSER, FluidFluidToFluidRecipe.class);
    public static final MekanismJEIRecipeType<ItemStackToItemStackRecipe> MEKANICAL_CHARGER_RECIPE = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.MEKANICAL_CHARGER, ItemStackToItemStackRecipe.class);
    public static final MekanismJEIRecipeType<GasToGasRecipe> SPS_RECIPE = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.COMPACT_SPS, GasToGasRecipe.class);
    public static final MekanismJEIRecipeType<ExpandedCrafterRecipe> EXPANDED_CRAFTER_RECIPE = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.EXPANDED_CRAFTER, ExpandedCrafterRecipe.class);
}
