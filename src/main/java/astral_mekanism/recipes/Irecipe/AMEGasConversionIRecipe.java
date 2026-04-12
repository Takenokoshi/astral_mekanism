package astral_mekanism.recipes.irecipe;

import astral_mekanism.registries.AMERecipeSerializers;
import astral_mekanism.registries.AMERecipeTypes;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class AMEGasConversionIRecipe extends GasToGasRecipe {

    public AMEGasConversionIRecipe(ResourceLocation id, GasStackIngredient input, GasStack output) {
        super(id, input, output);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AMERecipeSerializers.GAS_CONVERSION.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AMERecipeTypes.GAS_CONVERSION.get();
    }
    
}
