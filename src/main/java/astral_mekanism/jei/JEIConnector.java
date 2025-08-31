package astral_mekanism.jei;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.jei.recipeCategory.FluidInfuserRecipeCategory;
import astral_mekanism.jei.recipeCategory.GreenHouseRecipeCategory;
import astral_mekanism.jei.recipeCategory.MelterRecipeCategory;
import astral_mekanism.registries.AstralMekanismBlocks;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.client.jei.CatalystRegistryHelper;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.client.jei.RecipeRegistryHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin

public class JEIConnector implements IModPlugin {

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(AstralMekanismID.MODID, "jei_plugin");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeCategories(
				new IRecipeCategory[] { new GreenHouseRecipeCategory(guiHelper,
						MekJEIRecipeTypes.Greenhouse_recipe) });
		registry.addRecipeCategories(
				new IRecipeCategory[] {
						new MelterRecipeCategory(guiHelper, MekJEIRecipeTypes.Melter_recipe) });
		registry.addRecipeCategories(
				new IRecipeCategory[] {
						new FluidInfuserRecipeCategory(guiHelper, MekJEIRecipeTypes.FLUID_INFUSER_RECIPE)
				}
		);
	}

	@Override
	public void registerRecipes(IRecipeRegistration registry) {
		RecipeRegistryHelper.register(registry, MekJEIRecipeTypes.Greenhouse_recipe,
				AstralMekanismRecipeTypes.Greenhouse_recipe);
		RecipeRegistryHelper.register(registry, MekJEIRecipeTypes.Melter_recipe,
				AstralMekanismRecipeTypes.Melter_recipe);
		RecipeRegistryHelper.register(registry, MekJEIRecipeTypes.FLUID_INFUSER_RECIPE,
				AstralMekanismRecipeTypes.FLUID_INFUSER_RECIPE);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
		CatalystRegistryHelper.register(registry, MekJEIRecipeTypes.Greenhouse_recipe,
				AstralMekanismBlocks.Greenhouse);
		CatalystRegistryHelper.register(registry, MekJEIRecipeTypes.Melter_recipe,
				AstralMekanismBlocks.Melter);
		CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.ACTIVATING,
				AstralMekanismBlocks.Glowstone_neutron_activator);
		CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.EVAPORATING,
				AstralMekanismBlocks.CompactTEP);
		CatalystRegistryHelper.register(registry, MekJEIRecipeTypes.FLUID_INFUSER_RECIPE,
				AstralMekanismBlocks.FLUID_INFUSER);
	}
}
