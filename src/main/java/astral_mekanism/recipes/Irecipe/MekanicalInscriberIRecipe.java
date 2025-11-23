package astral_mekanism.recipes.irecipe;

import astral_mekanism.registries.AstralMekanismRecipeSerializers;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.recipes.CombinerRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class MekanicalInscriberIRecipe extends CombinerRecipe {

    public MekanicalInscriberIRecipe(ResourceLocation id, ItemStackIngredient mainInput, ItemStackIngredient extraInput,
            ItemStack output) {
        super(id, mainInput, extraInput, output);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AstralMekanismRecipeSerializers.MEKANICAL_INSCRIBER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AstralMekanismRecipeTypes.MEKANICAL_INSCRIBER_RECIPE.get();
    }
    
}
