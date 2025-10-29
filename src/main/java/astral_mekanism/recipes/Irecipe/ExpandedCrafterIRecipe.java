package astral_mekanism.recipes.Irecipe;

import astral_mekanism.recipes.ingredient.ArrayItemStackIngredient;
import astral_mekanism.recipes.recipe.ExpandedCrafterRecipe;
import astral_mekanism.registries.AstralMekanismRecipeSerializers;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ExpandedCrafterIRecipe extends ExpandedCrafterRecipe {

    public ExpandedCrafterIRecipe(ResourceLocation id, ArrayItemStackIngredient inputItems,
            FluidStackIngredient inputFluid, GasStackIngredient inputGas, ItemStack output) {
        super(id, inputItems, inputFluid, inputGas, output);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AstralMekanismRecipeSerializers.EXPANDED_CRAFTER_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AstralMekanismRecipeTypes.EXPANDED_CRAFTER_RECIPE.get();
    }
    
}
