package astral_mekanism.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import astral_mekanism.AMEConstants;
import astral_mekanism.recipe.builder.AstralMekanismRecipeBuilder;
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CompressUnzipRecipeBuilding {

    public static void buildRecipes(Consumer<FinishedRecipe> consumer) {
        final IItemStackIngredientCreator creator = IngredientCreatorAccess.item();
        for (RecipeItems recipeItems : LIST) {
            new AstralMekanismRecipeBuilder.ItemCompressingRecipeBuilder(creator.from(recipeItems.unzipped),
                    recipeItems.compressed).build(consumer, AMEConstants.rl("item_compressing/" + recipeItems.name));
            new AstralMekanismRecipeBuilder.ItemUnzippingRecipeBuilder(creator.from(recipeItems.compressed), recipeItems.unzipped).build(consumer,
                    AMEConstants.rl("item_unzipping/" + recipeItems.name));
        }
    }

    private static final List<RecipeItems> LIST = new ArrayList<>();

    static {
        LIST.add(new RecipeItems(new ItemStack(Items.IRON_BLOCK), new ItemStack(Items.IRON_INGOT, 9),
                "iron_block_ingot"));
    }

    private static record RecipeItems(ItemStack compressed, ItemStack unzipped, String name) {
    }
}
