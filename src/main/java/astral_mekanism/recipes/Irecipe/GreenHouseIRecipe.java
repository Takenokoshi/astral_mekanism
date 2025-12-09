package astral_mekanism.recipes.irecipe;

import astral_mekanism.recipes.output.TripleItemOutput;
import astral_mekanism.recipes.recipe.GreenhouseRecipe;
import astral_mekanism.registries.AstralMekanismRecipeSerializers;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class GreenhouseIRecipe extends GreenhouseRecipe {

    public GreenhouseIRecipe(ResourceLocation id, ItemStackIngredient inputSeed, ItemStackIngredient farmland,
            FluidStackIngredient water, TripleItemOutput output) {
        super(id, inputSeed, farmland, water, output);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AstralMekanismRecipeSerializers.GREENHOUSE_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AstralMekanismRecipeTypes.GREENHOUSE_RECIPE.get();
    }

}
