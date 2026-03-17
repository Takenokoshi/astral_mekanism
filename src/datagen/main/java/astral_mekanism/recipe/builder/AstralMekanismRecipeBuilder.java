package astral_mekanism.recipe.builder;

import astral_mekanism.AMEConstants;
import mekanism.api.datagen.recipe.builder.ItemStackToItemStackRecipeBuilder;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.world.item.ItemStack;

public class AstralMekanismRecipeBuilder {
    public static class ItemCompressingRecipeBuilder extends ItemStackToItemStackRecipeBuilder {
        public ItemCompressingRecipeBuilder(ItemStackIngredient input, ItemStack output) {
            super(input, output, AMEConstants.rl("item_compressing"));
        }
    }
    public static class ItemUnzippingRecipeBuilder extends ItemStackToItemStackRecipeBuilder {
        public ItemUnzippingRecipeBuilder(ItemStackIngredient input, ItemStack output) {
            super(input, output, AMEConstants.rl("item_unzipping"));
        }
    }
}
