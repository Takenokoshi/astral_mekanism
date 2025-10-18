package astral_mekanism.recipes.Irecipe;

import astral_mekanism.recipes.recipe.ItemToItemItemRecipe;
import astral_mekanism.registries.AstralMekanismRecipeSerializers;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class FormulizedSawingIRecipe extends ItemToItemItemRecipe {

    public FormulizedSawingIRecipe(ResourceLocation id, ItemStackIngredient inputItem, ItemStack outputA,
            ItemStack outputB) {
        super(id, inputItem, outputA, outputB);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AstralMekanismRecipeSerializers.FORMULIZED_SAWING_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AstralMekanismRecipeTypes.FORMULIZED_SAWING_RECIPE.get();
    }
    
}
