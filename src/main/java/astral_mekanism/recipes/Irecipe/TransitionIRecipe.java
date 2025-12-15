package astral_mekanism.recipes.irecipe;

import astral_mekanism.recipes.output.ItemFluidOutput;
import astral_mekanism.recipes.recipe.TransitionRecipe;
import astral_mekanism.registries.AstralMekanismRecipeSerializers;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class TransitionIRecipe extends TransitionRecipe {

    public TransitionIRecipe(ResourceLocation id, ItemStackIngredient inputItemA, ItemStackIngredient inputItemB,
            ItemStackIngredient inputItemC, ItemStackIngredient inputItemD, ItemFluidOutput output,
            boolean itemAIsCatalyst, boolean itemBIsCatalyst, boolean itemCIsCatalyst, boolean itemDIsCatalyst) {
        super(id, inputItemA, inputItemB, inputItemC, inputItemD, output, itemAIsCatalyst, itemBIsCatalyst,
                itemCIsCatalyst,
                itemDIsCatalyst);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AstralMekanismRecipeSerializers.TRANSITION.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AstralMekanismRecipeTypes.TRANSITION.get();
    }

}
