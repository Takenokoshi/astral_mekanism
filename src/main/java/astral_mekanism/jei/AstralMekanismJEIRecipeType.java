package astral_mekanism.jei;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.recipes.recipe.AstralCraftingRecipe;
import astral_mekanism.recipes.recipe.FluidFluidToFluidRecipe;
import astral_mekanism.recipes.recipe.GreenhouseRecipe;
import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
import astral_mekanism.registries.AstralMekanismMachines;
import mekanism.api.recipes.CombinerRecipe;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.client.jei.MekanismJEIRecipeType;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;

public final class AstralMekanismJEIRecipeType {
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
    public static final MekanismJEIRecipeType<MekanicalTransformRecipe> MEKANICAL_TRANSFORM = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.MEKANICAL_TRANSFORMER, MekanicalTransformRecipe.class);
    public static final MekanismJEIRecipeType<GreenhouseRecipe> GREENHOUSE_RECIPE = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.GREENHOUSE, GreenhouseRecipe.class);
    public static final RecipeType<SmeltingRecipe> ESSENTIAL_SMELTING = RecipeType.create(AstralMekanismID.MODID,
            "essential_smelter", SmeltingRecipe.class);
}
