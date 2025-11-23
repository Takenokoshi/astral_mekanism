package astral_mekanism.recipes.irecipe;

import astral_mekanism.recipes.recipe.TripleItemToItemRecipe;
import astral_mekanism.registries.AstralMekanismRecipeSerializers;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class MekanicalPresserIRecipe extends TripleItemToItemRecipe {

    public MekanicalPresserIRecipe(ResourceLocation id, ItemStackIngredient inputItemA,
            ItemStackIngredient inputItemB, ItemStackIngredient inputItemC, ItemStack output) {
        super(id, inputItemA, inputItemB, inputItemC, output);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AstralMekanismRecipeSerializers.MEKANICAL_PRESSER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AstralMekanismRecipeTypes.MEKANICAL_PRESSER_RECIPE.get();
    }
    
}
