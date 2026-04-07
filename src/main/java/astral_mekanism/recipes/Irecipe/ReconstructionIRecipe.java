package astral_mekanism.recipes.irecipe;

import astral_mekanism.recipes.recipe.ReconstructionRecipe;
import astral_mekanism.registries.AMERecipeSerializers;
import astral_mekanism.registries.AMERecipeTypes;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ReconstructionIRecipe extends ReconstructionRecipe {

    public ReconstructionIRecipe(ResourceLocation id, ItemStackIngredient inputSolid,
            FluidStackIngredient inputFluid, GasStackIngredient inputGas, FloatingLong energyRequired, int duration,
            ItemStack outputItem, GasStack outputGas) {
        super(id, inputSolid, inputFluid, inputGas, energyRequired, duration, outputItem, outputGas);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AMERecipeSerializers.RECONSTRUCTION.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AMERecipeTypes.RECONSTRUCTION.get();
    }

}
