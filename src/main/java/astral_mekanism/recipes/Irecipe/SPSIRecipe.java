package astral_mekanism.recipes.irecipe;

import astral_mekanism.registries.AMERecipeSerializers;
import astral_mekanism.registries.AMERecipeTypes;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class SPSIRecipe extends GasToGasRecipe {

    public SPSIRecipe(ResourceLocation id, GasStackIngredient input, GasStack output) {
        super(id, input, output);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AMERecipeSerializers.SPS_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AMERecipeTypes.SPS_RECIPE.get();
    }

}
