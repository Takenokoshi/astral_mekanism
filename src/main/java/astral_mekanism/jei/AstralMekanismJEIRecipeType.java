package astral_mekanism.jei;

import astral_mekanism.recipes.recipe.AstralCraftingRecipe;
import astral_mekanism.recipes.recipe.FluidFluidToFluidRecipe;
import astral_mekanism.recipes.recipe.GreenHouseRecipe;
import astral_mekanism.recipes.recipe.TripleItemToItemRecipe;
import astral_mekanism.registries.AstralMekanismMachines;
import mekanism.api.recipes.CombinerRecipe;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.client.jei.MekanismJEIRecipeType;

public class AstralMekanismJEIRecipeType {
    public static final MekanismJEIRecipeType<GreenHouseRecipe> GREENHOUSE_RECIPE = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.GREENHOUSE, GreenHouseRecipe.class);
    public static final MekanismJEIRecipeType<ItemStackToFluidRecipe> MELTER_RECIPE = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.MELTER, ItemStackToFluidRecipe.class);
    public static final MekanismJEIRecipeType<FluidFluidToFluidRecipe> FLUID_INFUSER_RECIPE = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.FLUID_INFUSER, FluidFluidToFluidRecipe.class);
    public static final MekanismJEIRecipeType<ItemStackToItemStackRecipe> MEKANICAL_CHARGER_RECIPE = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.MEKANICAL_CHARGER, ItemStackToItemStackRecipe.class);
    public static final MekanismJEIRecipeType<GasToGasRecipe> SPS_RECIPE = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.COMPACT_SPS, GasToGasRecipe.class);
    public static final MekanismJEIRecipeType<AstralCraftingRecipe> ASTRAL_CRAFTING = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.ASTRAL_CRAFTER, AstralCraftingRecipe.class);
    public static final MekanismJEIRecipeType<CombinerRecipe> MEKANICAL_INSCRIBER_RECIPE = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.MEKANICAL_INSCRIBER, CombinerRecipe.class);
    public static final MekanismJEIRecipeType<TripleItemToItemRecipe> MEKANICAL_PRESSER_RECIPE = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.MEKANICAL_PRESSER, TripleItemToItemRecipe.class);
}
