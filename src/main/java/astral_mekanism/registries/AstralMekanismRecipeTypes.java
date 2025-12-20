package astral_mekanism.registries;

import java.util.function.Function;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.mixin.mekanism.MekanismRecipeTypeMixin;
import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache;
import astral_mekanism.recipes.inputRecipeCache.AstralCraftingRecipeCache;
import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.FluidFluid;
import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.ItemItemFluid;
import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.QuadItem;
import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.TripleItem;
import astral_mekanism.recipes.recipe.AstralCraftingRecipe;
import astral_mekanism.recipes.recipe.FluidFluidToFluidRecipe;
import astral_mekanism.recipes.recipe.GreenhouseRecipe;
import astral_mekanism.recipes.recipe.TripleItemToItemRecipe;
import astral_mekanism.recipes.recipe.ItemToItemItemRecipe;
import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ChemicalDissolutionRecipe;
import mekanism.api.recipes.CombinerRecipe;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.DoubleItem;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.ItemChemical;
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

    public static final RecipeTypeRegistryObject<ItemToItemItemRecipe, SingleItem<ItemToItemItemRecipe>> FORMULIZED_SAWING_RECIPE = register(
            "sawing", rT -> new InputRecipeCache.SingleItem<>(rT, ItemToItemItemRecipe::getInput));

    public static final RecipeTypeRegistryObject<GreenhouseRecipe, ItemItemFluid<GreenhouseRecipe>> GREENHOUSE_RECIPE = register(
            "greenhouse", rt -> new ItemItemFluid<>(rt,
                    GreenhouseRecipe::getInputSeed,
                    GreenhouseRecipe::getFarmland,
                    GreenhouseRecipe::getInputFluid));

    public static final RecipeTypeRegistryObject<ItemStackToFluidRecipe, SingleItem<ItemStackToFluidRecipe>> Melter_recipe = register(
            "melter",
            recipeType -> new InputRecipeCache.SingleItem<>(recipeType, ItemStackToFluidRecipe::getInput));

    public static final RecipeTypeRegistryObject<FluidFluidToFluidRecipe, FluidFluid<FluidFluidToFluidRecipe>> FLUID_INFUSER_RECIPE = register(
            "fluid_infuser",
            recipeType -> new AMInputRecipeCache.FluidFluid<>(recipeType, FluidFluidToFluidRecipe::getInputA,
                    FluidFluidToFluidRecipe::getInputB));

    public static final RecipeTypeRegistryObject<ItemStackToItemStackRecipe, SingleItem<ItemStackToItemStackRecipe>> MEKANICAL_CHARGER_RECIPE = register(
            "mekanical_charger",
            recipeType -> new InputRecipeCache.SingleItem<>(recipeType, ItemStackToItemStackRecipe::getInput));

    public static final RecipeTypeRegistryObject<GasToGasRecipe, SingleChemical<Gas, GasStack, GasToGasRecipe>> SPS_RECIPE = register(
            "sps", recipeType -> new InputRecipeCache.SingleChemical<Gas, GasStack, GasToGasRecipe>(recipeType,
                    GasToGasRecipe::getInput));

    public static final RecipeTypeRegistryObject<ItemStackGasToItemStackRecipe, ItemChemical<Gas, GasStack, ItemStackGasToItemStackRecipe>> AM_INJECTING = register(
            "am_injecting",
            rt -> new InputRecipeCache.ItemChemical<>(rt, ItemStackGasToItemStackRecipe::getItemInput,
                    ItemStackGasToItemStackRecipe::getChemicalInput));

    public static final RecipeTypeRegistryObject<ItemStackGasToItemStackRecipe, ItemChemical<Gas, GasStack, ItemStackGasToItemStackRecipe>> AM_PURIFYING = register(
            "am_purifying",
            rt -> new InputRecipeCache.ItemChemical<>(rt, ItemStackGasToItemStackRecipe::getItemInput,
                    ItemStackGasToItemStackRecipe::getChemicalInput));

    public static final RecipeTypeRegistryObject<ItemStackGasToItemStackRecipe, ItemChemical<Gas, GasStack, ItemStackGasToItemStackRecipe>> AM_COMPRESSING = register(
            "am_compressing",
            rt -> new InputRecipeCache.ItemChemical<>(rt, ItemStackGasToItemStackRecipe::getItemInput,
                    ItemStackGasToItemStackRecipe::getChemicalInput));

    public static final RecipeTypeRegistryObject<AstralCraftingRecipe, AstralCraftingRecipeCache> ASTRAL_CRAFTING = register(
            "astral_crafting", AstralCraftingRecipeCache::new);

    public static final RecipeTypeRegistryObject<TripleItemToItemRecipe, TripleItem<TripleItemToItemRecipe>> MEKANICAL_PRESSER_RECIPE = register(
            "mekanical_presser",
            rt -> new AMInputRecipeCache.TripleItem<>(rt,
                    TripleItemToItemRecipe::getInputItemA,
                    TripleItemToItemRecipe::getInputItemB,
                    TripleItemToItemRecipe::getInputItemC));

    public static final RecipeTypeRegistryObject<CombinerRecipe, DoubleItem<CombinerRecipe>> MEKANICAL_INSCRIBER_RECIPE = register(
            "mekanical_inscriber",
            rt -> new DoubleItem<>(rt, CombinerRecipe::getMainInput, CombinerRecipe::getExtraInput));

    public static final RecipeTypeRegistryObject<ChemicalDissolutionRecipe, ItemChemical<Gas, GasStack, ChemicalDissolutionRecipe>> AM_DISSOLUTION = register(
            "am_dissolution", rt -> new ItemChemical<>(rt, ChemicalDissolutionRecipe::getItemInput,
                    ChemicalDissolutionRecipe::getGasInput));

    public static final RecipeTypeRegistryObject<MekanicalTransformRecipe, QuadItem<MekanicalTransformRecipe>> TRANSITION = register(
            "mekanical_transform", rt -> new QuadItem<>(rt,
                    MekanicalTransformRecipe::getInputItemA,
                    MekanicalTransformRecipe::getInputItemB,
                    MekanicalTransformRecipe::getInputItemC,
                    MekanicalTransformRecipe::getInputItemD));
}