package astral_mekanism.jei;

import appeng.recipes.handlers.InscriberRecipe;
import astral_mekanism.AstralMekanismID;
import astral_mekanism.recipes.recipe.AstralCraftingRecipe;
import astral_mekanism.recipes.recipe.FluidFluidToFluidRecipe;
import astral_mekanism.recipes.recipe.GreenhouseRecipe;
import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
import astral_mekanism.registries.AstralMekanismMachines;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.client.jei.MekanismJEIRecipeType;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;

public final class AstralMekanismJEIRecipeType {
    public static final MekanismJEIRecipeType<FluidFluidToFluidRecipe> FLUID_INFUSER_RECIPE = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.FLUID_INFUSER, FluidFluidToFluidRecipe.class);
    public static final MekanismJEIRecipeType<GasToGasRecipe> SPS_RECIPE = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.COMPACT_SPS, GasToGasRecipe.class);
    public static final MekanismJEIRecipeType<AstralCraftingRecipe> ASTRAL_CRAFTING = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.ASTRAL_CRAFTER, AstralCraftingRecipe.class);
    public static final MekanismJEIRecipeType<MekanicalTransformRecipe> MEKANICAL_TRANSFORM = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.MEKANICAL_TRANSFORMER, MekanicalTransformRecipe.class);
    public static final MekanismJEIRecipeType<GreenhouseRecipe> GREENHOUSE_RECIPE = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.GREENHOUSE, GreenhouseRecipe.class);
    public static final MekanismJEIRecipeType<ItemStackToItemStackRecipe> ITEM_COMPRESSING = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.ITEM_COMPRESSOR, ItemStackToItemStackRecipe.class);
    public static final MekanismJEIRecipeType<ItemStackToItemStackRecipe> ITEM_UNZIPPING = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.ITEM_UNZIPPER, ItemStackToItemStackRecipe.class);

    public static final RecipeType<SmeltingRecipe> ESSENTIAL_SMELTING = RecipeType.create(AstralMekanismID.MODID,
            "essential_smelter", SmeltingRecipe.class);
    public static final RecipeType<InscriberRecipe> MEKANICAL_INSCRIBING = RecipeType.create(AstralMekanismID.MODID,
            "mekanical_inscriber", InscriberRecipe.class);
}
