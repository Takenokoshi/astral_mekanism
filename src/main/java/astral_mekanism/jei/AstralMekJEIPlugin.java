package astral_mekanism.jei;

import appeng.integration.modules.jei.ChargerCategory;
import astral_mekanism.AstralMekanismID;
import astral_mekanism.jei.recipeCategory.AstralCraftingRecipeCategory;
import astral_mekanism.jei.recipeCategory.FluidInfuserRecipeCategory;
import astral_mekanism.jei.recipeCategory.GreenHouseRecipeCategory;
import astral_mekanism.jei.recipeCategory.MekanicalInscriberRecipeCategory;
import astral_mekanism.jei.recipeCategory.MelterRecipeCategory;
import astral_mekanism.jei.recipeCategory.TripleItemToItemRecipeCategory;
import astral_mekanism.registries.AstralMekanismMachines;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.client.jei.CatalystRegistryHelper;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.client.jei.RecipeRegistryHelper;
import mekanism.client.jei.machine.GasToGasRecipeCategory;
import mekanism.client.jei.machine.ItemStackToItemStackRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin

public class AstralMekJEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return AstralMekanismID.rl("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(
                new IRecipeCategory[] {
                        new GreenHouseRecipeCategory(guiHelper, AstralMekanismJEIRecipeType.GREENHOUSE_RECIPE),
                        new MelterRecipeCategory(guiHelper, AstralMekanismJEIRecipeType.MELTER_RECIPE),
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
                });
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        RecipeRegistryHelper.register(registry, AstralMekanismJEIRecipeType.GREENHOUSE_RECIPE,
                AstralMekanismRecipeTypes.Greenhouse_recipe);
        RecipeRegistryHelper.register(registry, AstralMekanismJEIRecipeType.MELTER_RECIPE,
                AstralMekanismRecipeTypes.Melter_recipe);
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
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(AstralMekanismMachines.MEKANICAL_CHARGER,
                new RecipeType[] { ChargerCategory.RECIPE_TYPE });
        registry.addRecipeCatalyst(AstralMekanismMachines.ASTRAL_MEKANICAL_CHARGER,
                new RecipeType[] { ChargerCategory.RECIPE_TYPE });
        CatalystRegistryHelper.register(registry, AstralMekanismJEIRecipeType.GREENHOUSE_RECIPE,
                AstralMekanismMachines.GREENHOUSE);
        CatalystRegistryHelper.register(registry, AstralMekanismJEIRecipeType.MELTER_RECIPE,
                AstralMekanismMachines.MELTER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.ACTIVATING,
                AstralMekanismMachines.GLOWSTONE_NEUTRON_ACTIVATOR);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.EVAPORATING,
                AstralMekanismMachines.COMPACT_TEP);
        CatalystRegistryHelper.register(registry, AstralMekanismJEIRecipeType.FLUID_INFUSER_RECIPE,
                AstralMekanismMachines.FLUID_INFUSER);
        CatalystRegistryHelper.register(registry, AstralMekanismJEIRecipeType.MEKANICAL_CHARGER_RECIPE,
                AstralMekanismMachines.MEKANICAL_CHARGER);
        CatalystRegistryHelper.register(registry, AstralMekanismJEIRecipeType.ASTRAL_CRAFTING,
                AstralMekanismMachines.ASTRAL_CRAFTER);
        CatalystRegistryHelper.register(registry, AstralMekanismJEIRecipeType.MEKANICAL_INSCRIBER_RECIPE,
                AstralMekanismMachines.MEKANICAL_INSCRIBER);
        CatalystRegistryHelper.register(registry, AstralMekanismJEIRecipeType.MEKANICAL_PRESSER_RECIPE,
                AstralMekanismMachines.MEKANICAL_PRESSER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.CRUSHING,
                AstralMekanismMachines.ASTRAL_CRUSHER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.SMELTING,
                AstralMekanismMachines.ASTRAL_ENERGIZED_SMELTER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.ENRICHING,
                AstralMekanismMachines.ASTRAL_ENRICHMENT_CHAMBER);
        CatalystRegistryHelper.register(registry, AstralMekanismJEIRecipeType.MEKANICAL_CHARGER_RECIPE,
                AstralMekanismMachines.ASTRAL_MEKANICAL_CHARGER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.INJECTING,
                AstralMekanismMachines.ASTRAL_CHEMICAL_INJECTION_CHAMBER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.COMPRESSING,
                AstralMekanismMachines.ASTRAL_OSMIUM_COMPRESSOR);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.PURIFYING,
                AstralMekanismMachines.ASTRAL_PURIFICATION_CHAMBER);
    }
}
