package astral_mekanism.recipes.irecipe;

import astral_mekanism.registries.AstralMekanismRecipeSerializers;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class CompressingAMIRecipe extends ItemStackGasToItemStackRecipe {

    public CompressingAMIRecipe(ResourceLocation id, ItemStackIngredient itemInput, GasStackIngredient gasInput,
            ItemStack output) {
        super(id, itemInput, gasInput, output);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AstralMekanismRecipeSerializers.AM_COMPRESSING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AstralMekanismRecipeTypes.AM_COMPRESSING.get();
    }

}
