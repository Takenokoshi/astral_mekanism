package astral_mekanism.recipes.irecipe;

import astral_mekanism.registries.AstralMekanismRecipeSerializers;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ItemUnzippingIRecipe extends ItemStackToItemStackRecipe {

    public ItemUnzippingIRecipe(ResourceLocation id, ItemStackIngredient input, ItemStack output) {
        super(id, input, output);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AstralMekanismRecipeSerializers.ITEM_UNZIPPING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AstralMekanismRecipeTypes.ITEM_UNZIPPING.get();
    }

}
