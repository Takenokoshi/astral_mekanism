package astral_mekanism.recipes.Irecipe;

import astral_mekanism.recipes.recipe.FluidFluidToFluidRecipe;
import astral_mekanism.registries.AstralMekanismRecipeSerializers;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;

public class FluidInfuserIRecipe extends FluidFluidToFluidRecipe {

	public FluidInfuserIRecipe(ResourceLocation id, FluidStackIngredient inputFluidA,
			FluidStackIngredient inputFluidB, FluidStack outputFluid) {
		super(id, inputFluidA, inputFluidB, outputFluid);
	}

	@Override
	public RecipeSerializer<FluidFluidToFluidRecipe> getSerializer() {
		return AstralMekanismRecipeSerializers.FLUID_INFUSER_RECIPE.get();
	}

	@Override
	public RecipeType<FluidFluidToFluidRecipe> getType() {
		return AstralMekanismRecipeTypes.FLUID_INFUSER_RECIPE.get();
	}
	
}
