package astral_mekanism.recipes.irecipe;

import astral_mekanism.registries.AstralMekanismRecipeSerializers;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;

public class MelterIRecipe extends ItemStackToFluidRecipe {

	public MelterIRecipe(ResourceLocation id, ItemStackIngredient input, FluidStack output) {
		super(id, input, output);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return AstralMekanismRecipeSerializers.Melter_recipe.get();
	}

	@Override
	public RecipeType<?> getType() {
		return AstralMekanismRecipeTypes.Melter_recipe.get();
	}

}
