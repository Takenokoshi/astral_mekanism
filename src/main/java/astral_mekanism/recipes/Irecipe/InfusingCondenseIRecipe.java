package astral_mekanism.recipes.irecipe;

import astral_mekanism.recipes.recipe.GasInfusionToFluidRecipe;
import astral_mekanism.registries.AMERecipeSerializers;
import astral_mekanism.registries.AMERecipeTypes;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.InfusionStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;

public class InfusingCondenseIRecipe extends GasInfusionToFluidRecipe {

    public InfusingCondenseIRecipe(ResourceLocation id, GasStackIngredient inputGas,
            InfusionStackIngredient inputInfusion, FluidStack output) {
        super(id, inputGas, inputInfusion, output);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AMERecipeSerializers.INFUSING_CONDENSE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AMERecipeTypes.INFUSING_CONDENSE.get();
    }

}
