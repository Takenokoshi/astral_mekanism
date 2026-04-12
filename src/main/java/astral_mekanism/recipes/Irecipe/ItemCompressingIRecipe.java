package astral_mekanism.recipes.irecipe;

import astral_mekanism.registries.AMERecipeSerializers;
import astral_mekanism.registries.AMERecipeTypes;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ItemCompressingIRecipe extends ItemStackToItemStackRecipe {

    public ItemCompressingIRecipe(ResourceLocation id, ItemStackIngredient input, ItemStack output) {
        super(id, input, output);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AMERecipeSerializers.ITEM_COMPRESSING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AMERecipeTypes.ITEM_COMPRESSING.get();
    }
    
}
