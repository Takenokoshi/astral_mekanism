package astral_mekanism.recipes.serializer;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import astral_mekanism.AstralMekanism;
import astral_mekanism.recipes.recipe.ItemToItemItemRecipe;
import mekanism.api.SerializerHelper;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ItemToItemItemRecipeSerializer<RECIPE extends ItemToItemItemRecipe> implements RecipeSerializer<RECIPE> {

    private final IFactory<RECIPE> factory;

    public ItemToItemItemRecipeSerializer(IFactory<RECIPE> factory) {
        this.factory = factory;
    }

    private static final String INPUT_KEY = "input";
    private static final String MAIN_OUTPUT_KEY = "mainOutput";
    private static final String SECONDARY_OUTPUT_KEY = "secondaryOutput";
    private static final String SECONDARY_CHANCE_KEY = "secondaryChance";

    @Override
    public RECIPE fromJson(ResourceLocation id, JsonObject json) {
        ItemStackIngredient inputIngredient = IngredientCreatorAccess.item()
                .deserialize(GsonHelper.isArrayNode(json, INPUT_KEY)
                        ? GsonHelper.getAsJsonArray(json, INPUT_KEY)
                        : GsonHelper.getAsJsonObject(json, INPUT_KEY));
        ItemStack outputA = SerializerHelper.getItemStack(json, MAIN_OUTPUT_KEY);
        ItemStack outputB = SerializerHelper.getItemStack(json, SECONDARY_OUTPUT_KEY);
        float chance = json.get(SECONDARY_CHANCE_KEY).getAsJsonPrimitive().getAsFloat();
        int multiplier = (int) Math.ceil(1 / chance);
        ItemStack outputAA = outputA.copy();
        outputAA.setCount(outputA.getCount() * multiplier);
        return this.factory.create(id,
                IngredientCreatorAccess.item().createMulti(
                        inputIngredient.getRepresentations().stream().map(stack -> {
                            ItemStack copy = stack.copy();
                            copy.setCount(stack.getCount() * multiplier);
                            ItemStackIngredient result = IngredientCreatorAccess.item().from(copy);
                            return result;
                        }).toArray(ItemStackIngredient[]::new)),
                outputAA, outputB);
    }

    @Override
    public @Nullable RECIPE fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        try {
            ItemStackIngredient input = IngredientCreatorAccess.item().read(buffer);
            ItemStack outputItemA = buffer.readItem();
            ItemStack outputItemB = buffer.readItem();
            return this.factory.create(recipeId, input, outputItemA, outputItemB);
        } catch (Exception e) {
           AstralMekanism.LOGGER.error("Error reading recipe from packet.", e);
            throw e;
        }
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RECIPE recipe) {
        try {
            recipe.write(buffer);
        } catch (Exception e) {
            AstralMekanism.LOGGER.error("Error writing recipe to packet.", e);
            throw e;
        };
    }

    @FunctionalInterface
    public interface IFactory<RECIPE extends ItemToItemItemRecipe> {
        RECIPE create(ResourceLocation id, ItemStackIngredient input, ItemStack outputItemA, ItemStack outputItemB);
    }

}
