package astral_mekanism.jei;

import appeng.integration.modules.jei.ChargerCategory;
import astral_mekanism.AstralMekanismID;
import astral_mekanism.jei.recipeCategory.AstralCraftingRecipeCategory;
import astral_mekanism.jei.recipeCategory.FluidInfuserRecipeCategory;
import astral_mekanism.jei.recipeCategory.GreenhouseRecipeCategory;
import astral_mekanism.jei.recipeCategory.MekanicalInscriberRecipeCategory;
import astral_mekanism.jei.recipeCategory.MekanicalTransformRecipeCategory;
import astral_mekanism.jei.recipeCategory.TripleItemToItemRecipeCategory;
import astral_mekanism.registries.AstralMekanismMachines;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.client.jei.CatalystRegistryHelper;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.client.jei.RecipeRegistryHelper;
import mekanism.client.jei.machine.GasToGasRecipeCategory;
import mekanism.client.jei.machine.ItemStackToItemStackRecipeCategory;
import mekanism.generators.client.jei.GeneratorsJEIRecipeType;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin

public class AstralMekanismJEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return AstralMekanismID.rl("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(
                new IRecipeCategory[] {
                        new FluidInfuserRecipeCategory(guiHelper, AstralMekanismJEIRecipeType.FLUID_INFUSER_RECIPE),
                        new ItemStackToItemStackRecipeCategory(guiHelper,
                                AstralMekanismJEIRecipeType.MEKANICAL_CHARGER_RECIPE,
                                AstralMekanismMachines.MEKANICAL_CHARGER),
                        new GasToGasRecipeCategory(guiHelper, AstralMekanismJEIRecipeType.SPS_RECIPE,
                                AstralMekanismMachines.COMPACT_SPS),
                        new AstralCraftingRecipeCategory(guiHelper, AstralMekanismJEIRecipeType.ASTRAL_CRAFTING,
                                AstralMekanismMachines.ASTRAL_CRAFTER),
                        new MekanicalInscriberRecipeCategory(guiHelper,
                                AstralMekanismJEIRecipeType.MEKANICAL_INSCRIBER_RECIPE),
                        new TripleItemToItemRecipeCategory(guiHelper,
                                AstralMekanismJEIRecipeType.MEKANICAL_PRESSER_RECIPE,
                                AstralMekanismMachines.MEKANICAL_PRESSER),
                        new MekanicalTransformRecipeCategory(guiHelper,
                                AstralMekanismJEIRecipeType.MEKANICAL_TRANSFORM,
                                AstralMekanismMachines.MEKANICAL_TRANSFORMER),
                        new GreenhouseRecipeCategory(guiHelper,
                                AstralMekanismJEIRecipeType.GREENHOUSE_RECIPE,
                                AstralMekanismMachines.GREENHOUSE),
                });
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        RecipeRegistryHelper.register(registry, AstralMekanismJEIRecipeType.FLUID_INFUSER_RECIPE,
                AstralMekanismRecipeTypes.FLUID_INFUSER_RECIPE);
        RecipeRegistryHelper.register(registry, AstralMekanismJEIRecipeType.MEKANICAL_CHARGER_RECIPE,
                AstralMekanismRecipeTypes.MEKANICAL_CHARGER_RECIPE);
        RecipeRegistryHelper.register(registry, AstralMekanismJEIRecipeType.SPS_RECIPE,
                AstralMekanismRecipeTypes.SPS_RECIPE);
        RecipeRegistryHelper.register(registry, AstralMekanismJEIRecipeType.ASTRAL_CRAFTING,
                AstralMekanismRecipeTypes.ASTRAL_CRAFTING);
        RecipeRegistryHelper.register(registry, AstralMekanismJEIRecipeType.MEKANICAL_INSCRIBER_RECIPE,
                AstralMekanismRecipeTypes.MEKANICAL_INSCRIBER_RECIPE);
        RecipeRegistryHelper.register(registry, AstralMekanismJEIRecipeType.MEKANICAL_PRESSER_RECIPE,
                AstralMekanismRecipeTypes.MEKANICAL_PRESSER_RECIPE);
        RecipeRegistryHelper.register(registry, AstralMekanismJEIRecipeType.MEKANICAL_TRANSFORM,
                AstralMekanismRecipeTypes.MEKANICAL_TRANSFORM);
        RecipeRegistryHelper.register(registry, AstralMekanismJEIRecipeType.GREENHOUSE_RECIPE,
                AstralMekanismRecipeTypes.GREENHOUSE_RECIPE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        // ae2
        registry.addRecipeCatalyst(AstralMekanismMachines.MEKANICAL_CHARGER,
                new RecipeType[] { ChargerCategory.RECIPE_TYPE });
        registry.addRecipeCatalyst(AstralMekanismMachines.ASTRAL_MEKANICAL_CHARGER,
                new RecipeType[] { ChargerCategory.RECIPE_TYPE });
        // mekanism
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.INJECTING,
                AstralMekanismMachines.ASTRAL_CHEMICAL_INJECTION_CHAMBER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.COMPRESSING,
                AstralMekanismMachines.ASTRAL_OSMIUM_COMPRESSOR);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.PURIFYING,
                AstralMekanismMachines.ASTRAL_PURIFICATION_CHAMBER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.CRUSHING,
                AstralMekanismMachines.ASTRAL_CRUSHER);
        CatalystRegistryHelper.registerRecipeItem(registry, AstralMekanismMachines.ASTRAL_ENERGIZED_SMELTER,
                MekanismJEIRecipeType.SMELTING, RecipeTypes.SMELTING);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.ENRICHING,
                AstralMekanismMachines.ASTRAL_ENRICHMENT_CHAMBER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.CHEMICAL_INFUSING,
                AstralMekanismMachines.ASTRAL_CHEMICAL_INFUSER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.OXIDIZING,
                AstralMekanismMachines.ASTRAL_CHEMICAL_OXIDIZER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.WASHING,
                AstralMekanismMachines.ASTRAL_CHEMICAL_WASHER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.COMBINING,
                AstralMekanismMachines.ASTRAL_COMBINER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.CRYSTALLIZING,
                AstralMekanismMachines.ASTRAL_CRYSTALLIZER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.DISSOLUTION,
                AstralMekanismMachines.ASTRAL_DISSOLUTION_CHAMBER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.SEPARATING,
                AstralMekanismMachines.ASTRAL_ELECTROLYTIC_SEPARATOR);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.ACTIVATING,
                AstralMekanismMachines.GLOWSTONE_NEUTRON_ACTIVATOR,
                AstralMekanismMachines.ASTRAL_GNA);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.CENTRIFUGING,
                AstralMekanismMachines.ASTRAL_ISOTOPIC_CENTRIFUGE);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.METALLURGIC_INFUSING,
                AstralMekanismMachines.ASTRAL_METALLURGIC_INFUSER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.REACTION,
                AstralMekanismMachines.ASTRAL_PRC);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.SAWING,
                AstralMekanismMachines.ASTRAL_PRECISION_SAWMILL);
        CatalystRegistryHelper.registerRecipeItem(registry, AstralMekanismMachines.ASTRAL_ROTARY_CONDENSENTRATOR,
                MekanismJEIRecipeType.CONDENSENTRATING, MekanismJEIRecipeType.DECONDENSENTRATING);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.SPS,
                AstralMekanismMachines.COMPACT_SPS,
                AstralMekanismMachines.ASTRAL_SPS);
        CatalystRegistryHelper.register(registry, GeneratorsJEIRecipeType.FISSION,
                AstralMekanismMachines.COMPACT_FIR);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.EVAPORATING,
                AstralMekanismMachines.COMPACT_TEP);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.INFUSION_CONVERSION,
                AstralMekanismMachines.INFUSE_SYNTHESIZER);

        // astral_mekanism
        CatalystRegistryHelper.register(registry, AstralMekanismJEIRecipeType.ASTRAL_CRAFTING,
                AstralMekanismMachines.ASTRAL_CRAFTER);
        CatalystRegistryHelper.register(registry, AstralMekanismJEIRecipeType.FLUID_INFUSER_RECIPE,
                AstralMekanismMachines.FLUID_INFUSER);
        CatalystRegistryHelper.register(registry, AstralMekanismJEIRecipeType.MEKANICAL_CHARGER_RECIPE,
                AstralMekanismMachines.ASTRAL_MEKANICAL_CHARGER,
                AstralMekanismMachines.MEKANICAL_CHARGER);
        CatalystRegistryHelper.register(registry, AstralMekanismJEIRecipeType.MEKANICAL_INSCRIBER_RECIPE,
                AstralMekanismMachines.ASTRAL_MEKANICAL_INSCRIBER,
                AstralMekanismMachines.MEKANICAL_INSCRIBER);
        CatalystRegistryHelper.register(registry, AstralMekanismJEIRecipeType.MEKANICAL_PRESSER_RECIPE,
                AstralMekanismMachines.ASTRAL_MEKANICAL_PRESSER,
                AstralMekanismMachines.MEKANICAL_PRESSER);
        CatalystRegistryHelper.register(registry, AstralMekanismJEIRecipeType.MEKANICAL_TRANSFORM,
                AstralMekanismMachines.ASTRAL_MEKANICAL_TRANSFOMER,
                AstralMekanismMachines.MEKANICAL_TRANSFORMER);
        CatalystRegistryHelper.register(registry, AstralMekanismJEIRecipeType.GREENHOUSE_RECIPE,
                AstralMekanismMachines.GREENHOUSE,
                AstralMekanismMachines.ASTRAL_GREENHOUSE);
    }
}
