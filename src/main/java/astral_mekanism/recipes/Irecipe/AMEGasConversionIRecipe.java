package astral_mekanism.recipes.irecipe;

import astral_mekanism.registries.AstralMekanismRecipeSerializers;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
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
        return AstralMekanismRecipeSerializers.GAS_CONVERSION.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AstralMekanismRecipeTypes.GAS_CONVERSION.get();
    }
    
}
