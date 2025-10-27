package astral_mekanism.recipes.serializer;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import astral_mekanism.recipes.ingredient.ArrayItemStackIngredient;
import astral_mekanism.recipes.recipe.ExpandedCrafterRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.api.SerializerHelper;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ExpandedCrafterRecipeSerializer<RECIPE extends ExpandedCrafterRecipe> implements RecipeSerializer<RECIPE> {

    private final IFactory<RECIPE> factory;

    public ExpandedCrafterRecipeSerializer(IFactory<RECIPE> factory) {
        this.factory = factory;
    }

    private static final String ITEMS_INPUT_JSON_KEY = "itemsInput";
    private static final String FLUID_INPUT_JSON_KEY = "fluidInput";
    private static final String GAS_INPUT_JSON_KEY = "gasInput";
    private static final String OUTPUT_JSON_KEY = "output";

    @Override
    public RECIPE fromJson(ResourceLocation recipeId, JsonObject json) {
        ArrayItemStackIngredient inputItems = ArrayItemStackIngredient
                .deserialize(GsonHelper.getAsJsonObject(json, ITEMS_INPUT_JSON_KEY));
        if (inputItems.ingredients.length != 25) {
            throw new JsonSyntaxException("itemsInput length must be 25");
        }
        return factory.create(
                recipeId,
                inputItems,
                IngredientCreatorAccess.fluid().deserialize(
                        GsonHelper.isArrayNode(json, FLUID_INPUT_JSON_KEY)
                                ? GsonHelper.getAsJsonArray(json, FLUID_INPUT_JSON_KEY)
                                : GsonHelper.getAsJsonObject(json, FLUID_INPUT_JSON_KEY)),
                IngredientCreatorAccess.gas().deserialize(GsonHelper.isArrayNode(json, GAS_INPUT_JSON_KEY)
                        ? GsonHelper.getAsJsonArray(json, GAS_INPUT_JSON_KEY)
                        : GsonHelper.getAsJsonObject(json, GAS_INPUT_JSON_KEY)),
                SerializerHelper.getItemStack(json, OUTPUT_JSON_KEY));
    }

    @Override
    public @Nullable RECIPE fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        ArrayItemStackIngredient inputItems = ArrayItemStackIngredient.readFromBuf(buffer);
        FluidStackIngredient inputFluid = IngredientCreatorAccess.fluid().read(buffer);
        GasStackIngredient inputGas = IngredientCreatorAccess.gas().read(buffer);
        ItemStack output = buffer.readItem();
        return factory.create(recipeId, inputItems, inputFluid, inputGas, output);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RECIPE recipe) {
        recipe.write(buffer);
    }

    @FunctionalInterface
    public interface IFactory<RECIPE extends ExpandedCrafterRecipe> {
        RECIPE create(ResourceLocation id,
                ArrayItemStackIngredient inputItems,
                FluidStackIngredient inputFluid,
                GasStackIngredient inputGas,
                ItemStack output);
    }

}
