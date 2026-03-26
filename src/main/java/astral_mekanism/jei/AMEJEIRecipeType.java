package astral_mekanism.jei;

import appeng.recipes.handlers.InscriberRecipe;
import appeng.recipes.transform.TransformRecipe;
import astral_mekanism.AMEConstants;
import astral_mekanism.AMETier;
import astral_mekanism.generalrecipe.recipe.CropSoilRecipe;
import astral_mekanism.recipes.recipe.AstralCraftingRecipe;
import astral_mekanism.recipes.recipe.FluidFluidToFluidRecipe;
import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
import astral_mekanism.registries.AstralMekanismMachines;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.client.jei.MekanismJEIRecipeType;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;

public final class AMEJEIRecipeType {
    public static final MekanismJEIRecipeType<FluidFluidToFluidRecipe> FLUID_INFUSER_RECIPE = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.FLUID_INFUSER, FluidFluidToFluidRecipe.class);
    public static final MekanismJEIRecipeType<GasToGasRecipe> SPS_RECIPE = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.COMPACT_SPS, GasToGasRecipe.class);
    public static final MekanismJEIRecipeType<AstralCraftingRecipe> ASTRAL_CRAFTING = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.ASTRAL_CRAFTER, AstralCraftingRecipe.class);
    public static final MekanismJEIRecipeType<ItemStackToItemStackRecipe> ITEM_COMPRESSING = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.ITEM_COMPRESSOR, ItemStackToItemStackRecipe.class);
    public static final MekanismJEIRecipeType<ItemStackToItemStackRecipe> ITEM_UNZIPPING = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.ITEM_UNZIPPER, ItemStackToItemStackRecipe.class);
    public static final MekanismJEIRecipeType<MekanicalTransformRecipe> MEKANICAL_TRANSFORM = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.TRANSFORMER, MekanicalTransformRecipe.class);
    public static final MekanismJEIRecipeType<MixingReactorJEIrecipe> FUSION_REACTOR = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.COMPACT_FUSION_REACTOR.get(AMETier.ASTRAL), MixingReactorJEIrecipe.class);
    public static final MekanismJEIRecipeType<MixingReactorJEIrecipe> NAQUADAH_REACTOR = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.COMPACT_NAQUADAH_REACTOR.get(AMETier.ASTRAL), MixingReactorJEIrecipe.class);
    public static final MekanismJEIRecipeType<MekanicalComposterJEIRecipe> MEKANICAL_COMPOSTER = new MekanismJEIRecipeType<>(
            AstralMekanismMachines.MEKANICAL_COMPOSTER, MekanicalComposterJEIRecipe.class);

    public static final RecipeType<SmeltingRecipe> ESSENTIAL_SMELTING = RecipeType.create(AMEConstants.MODID,
            "essential_smelter", SmeltingRecipe.class);
    public static final RecipeType<InscriberRecipe> MEKANICAL_INSCRIBING = RecipeType.create(AMEConstants.MODID,
            "mekanical_inscriber", InscriberRecipe.class);
    public static final RecipeType<TransformRecipe> TRANSFORM = RecipeType.create(AMEConstants.MODID,
            "item_transformation", TransformRecipe.class);
    public static final RecipeType<CropSoilRecipe> CROP_SOIL = RecipeType.create(AMEConstants.MODID,
            "crop_soil", CropSoilRecipe.class);
}
