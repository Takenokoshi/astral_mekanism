package astral_mekanism.jei;

import appeng.api.config.CondenserOutput;
import appeng.core.AppEng;
import appeng.integration.modules.jei.ChargerCategory;
import appeng.recipes.handlers.ChargerRecipe;
import appeng.recipes.handlers.InscriberRecipe;
import appeng.recipes.transform.TransformRecipe;
import astral_mekanism.AMEConstants;
import astral_mekanism.AMETier;
import astral_mekanism.generalrecipe.recipe.CropSoilRecipe;
import astral_mekanism.jei.jeirecipe.GasBurningJEIRecipe;
import astral_mekanism.jei.jeirecipe.MekanicalComposterJEIRecipe;
import astral_mekanism.jei.jeirecipe.MixingReactorJEIrecipe;
import astral_mekanism.recipes.recipe.AstralCraftingRecipe;
import astral_mekanism.recipes.recipe.FluidFluidToFluidRecipe;
import astral_mekanism.recipes.recipe.GasInfusionToFluidRecipe;
import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
import astral_mekanism.recipes.recipe.ReconstructionRecipe;
import astral_mekanism.registries.AMEMachines;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.generators.common.registries.GeneratorsBlocks;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.pedroksl.advanced_ae.recipes.ReactionChamberRecipe;
import net.pedroksl.advanced_ae.xmod.jei.ReactionChamberCategory;

public final class AMEJEIRecipeType {
    public static final MekanismJEIRecipeType<FluidFluidToFluidRecipe> FLUID_INFUSER_RECIPE = new MekanismJEIRecipeType<>(
            AMEMachines.FLUID_INFUSER, FluidFluidToFluidRecipe.class);
    public static final MekanismJEIRecipeType<GasToGasRecipe> SPS_RECIPE = new MekanismJEIRecipeType<>(
            AMEMachines.COMPACT_SPS, GasToGasRecipe.class);
    public static final MekanismJEIRecipeType<AstralCraftingRecipe> ASTRAL_CRAFTING = new MekanismJEIRecipeType<>(
            AMEMachines.ASTRAL_CRAFTER, AstralCraftingRecipe.class);
    public static final MekanismJEIRecipeType<ItemStackToItemStackRecipe> ITEM_COMPRESSING = new MekanismJEIRecipeType<>(
            AMEMachines.ITEM_COMPRESSOR, ItemStackToItemStackRecipe.class);
    public static final MekanismJEIRecipeType<ItemStackToItemStackRecipe> ITEM_UNZIPPING = new MekanismJEIRecipeType<>(
            AMEMachines.ITEM_UNZIPPER, ItemStackToItemStackRecipe.class);
    public static final MekanismJEIRecipeType<MekanicalTransformRecipe> MEKANICAL_TRANSFORM = new MekanismJEIRecipeType<>(
            AMEMachines.TRANSFORMER, MekanicalTransformRecipe.class);
    public static final MekanismJEIRecipeType<MixingReactorJEIrecipe> FUSION_REACTOR = new MekanismJEIRecipeType<>(
            AMEMachines.COMPACT_FUSION_REACTOR.get(AMETier.ASTRAL), MixingReactorJEIrecipe.class);
    public static final MekanismJEIRecipeType<MixingReactorJEIrecipe> NAQUADAH_REACTOR = new MekanismJEIRecipeType<>(
            AMEMachines.COMPACT_NAQUADAH_REACTOR.get(AMETier.ASTRAL), MixingReactorJEIrecipe.class);
    public static final MekanismJEIRecipeType<MekanicalComposterJEIRecipe> MEKANICAL_COMPOSTER = new MekanismJEIRecipeType<>(
            AMEMachines.MEKANICAL_COMPOSTER, MekanicalComposterJEIRecipe.class);
    public static final MekanismJEIRecipeType<ReactionChamberRecipe> AAE_REACTION = new MekanismJEIRecipeType<>(
            ReactionChamberCategory.RECIPE_TYPE.getUid(), ReactionChamberRecipe.class);
    public static final MekanismJEIRecipeType<SmeltingRecipe> ESSENTIAL_SMELTING = new MekanismJEIRecipeType<>(
            AMEConstants.rl("essential_smelter"), SmeltingRecipe.class);
    public static final MekanismJEIRecipeType<ChargerRecipe> AE_CHARGER = new MekanismJEIRecipeType<>(
            ChargerCategory.RECIPE_TYPE.getUid(), ChargerRecipe.class);
    public static final MekanismJEIRecipeType<InscriberRecipe> INSCRIBE = new MekanismJEIRecipeType<>(
            AppEng.makeId("inscriber"), InscriberRecipe.class);
    public static final MekanismJEIRecipeType<TransformRecipe> TRANSFORM = new MekanismJEIRecipeType<>(
            AMEConstants.rl("ae2_transform"), TransformRecipe.class);
    public static final MekanismJEIRecipeType<CropSoilRecipe> CROP_SOIL = new MekanismJEIRecipeType<>(
            AMEConstants.rl("crop_soil_fluid"), CropSoilRecipe.class);
    public static final MekanismJEIRecipeType<CondenserOutput> MATTER_CONDENSER = new MekanismJEIRecipeType<>(
            AppEng.makeId("condenser"), CondenserOutput.class);

    public static final RecipeType<CropSoilRecipe> CROP_SOIL_FAKE = RecipeType.create(AMEConstants.MODID,
            "crop_soil_fluid", CropSoilRecipe.class);
    public static final MekanismJEIRecipeType<GasBurningJEIRecipe> GAS_BURNING = new MekanismJEIRecipeType<>(
            GeneratorsBlocks.GAS_BURNING_GENERATOR, GasBurningJEIRecipe.class);
    public static final MekanismJEIRecipeType<GasInfusionToFluidRecipe> INFUSING_CONDENSE = new MekanismJEIRecipeType<>(
            AMEMachines.INFUSING_CONDENSENTRATOR, GasInfusionToFluidRecipe.class);
    public static final MekanismJEIRecipeType<GasToGasRecipe> GAS_CONVERSION = new MekanismJEIRecipeType<>(
            AMEMachines.GAS_CONVERTER, GasToGasRecipe.class);
    public static final MekanismJEIRecipeType<ReconstructionRecipe> RECONSTRUCTION = new MekanismJEIRecipeType<>(
            AMEMachines.INTERSTELLAR_POSITRONIC_MATTER_RECONSTRUCTION_APPARATUS, ReconstructionRecipe.class);
}
