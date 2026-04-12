package astral_mekanism.recipes.irecipe;

import astral_mekanism.recipes.recipe.AstralCraftingRecipe;
import astral_mekanism.registries.AMERecipeSerializers;
import astral_mekanism.registries.AMERecipeTypes;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class AstralCraftingIRecipe extends AstralCraftingRecipe {

    public AstralCraftingIRecipe(ResourceLocation id, ItemStackIngredient[] inputItems,
            FluidStackIngredient inputFluid, GasStackIngredient inputGas, ItemStack output) {
        super(id, inputItems, inputFluid, inputGas, output);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AMERecipeSerializers.ASTRAL_CRAFTING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AMERecipeTypes.ASTRAL_CRAFTING.get();
    }

}
