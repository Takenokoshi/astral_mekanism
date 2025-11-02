package astral_mekanism.recipes.serializer;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import astral_mekanism.recipes.recipe.AstralCraftingRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.api.SerializerHelper;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class AstralCraftingRecipeSerializer<RECIPE extends AstralCraftingRecipe> implements RecipeSerializer<RECIPE> {

    private final IFactory<RECIPE> factory;

    private static final String[] INPUT_ITEM_KEYS = new String[] {
            "itemInputA1", "itemInputA2", "itemInputA3", "itemInputA4", "itemInputA5",
            "itemInputB1", "itemInputB2", "itemInputB3", "itemInputB4", "itemInputB5",
            "itemInputC1", "itemInputC2", "itemInputC3", "itemInputC4", "itemInputC5",
            "itemInputD1", "itemInputD2", "itemInputD3", "itemInputD4", "itemInputD5",
            "itemInputE1", "itemInputE2", "itemInputE3", "itemInputE4", "itemInputE5"
    };
    private static final String INPUT_FLUID_KEY = "fluidInput";
    private static final String INPUT_GAS_KEY = "gasInput";
    private static final String OUTPUT_KEY = "output";

    public AstralCraftingRecipeSerializer(IFactory<RECIPE> factory) {
        this.factory = factory;
    }

    @Override
    public RECIPE fromJson(ResourceLocation id, JsonObject json) {
        ItemStackIngredient[] inputItems = new ItemStackIngredient[25];
        for (int i = 0; i < 25; i++) {
            inputItems[i] = IngredientCreatorAccess.item().deserialize(
                    GsonHelper.isArrayNode(json, INPUT_ITEM_KEYS[i])
                            ? GsonHelper.getAsJsonArray(json, INPUT_ITEM_KEYS[i])
                            : GsonHelper.getAsJsonObject(json, INPUT_ITEM_KEYS[i]));
        }
        return factory.create(id, inputItems,
                IngredientCreatorAccess.fluid().deserialize(
                        GsonHelper.isArrayNode(json, INPUT_FLUID_KEY)
                                ? GsonHelper.getAsJsonArray(json, INPUT_FLUID_KEY)
                                : GsonHelper.getAsJsonObject(json, INPUT_FLUID_KEY)),
                IngredientCreatorAccess.gas().deserialize(
                        GsonHelper.isArrayNode(json, INPUT_GAS_KEY)
                                ? GsonHelper.getAsJsonArray(json, INPUT_GAS_KEY)
                                : GsonHelper.getAsJsonObject(json, INPUT_GAS_KEY)),
                SerializerHelper.getItemStack(json, OUTPUT_KEY));
    }

    @Override
    public @Nullable RECIPE fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        ItemStackIngredient[] inputItems = new ItemStackIngredient[25];
        for (int i = 0; i < 25; i++) {
            inputItems[i] = IngredientCreatorAccess.item().read(buffer);
        }
        FluidStackIngredient inputFluid = IngredientCreatorAccess.fluid().read(buffer);
        GasStackIngredient inputGas = IngredientCreatorAccess.gas().read(buffer);
        ItemStack output = buffer.readItem();
        return factory.create(id, inputItems, inputFluid, inputGas, output);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RECIPE recipe) {
        recipe.write(buffer);
    }

    @FunctionalInterface
    public interface IFactory<RECIPE extends AstralCraftingRecipe> {
        RECIPE create(ResourceLocation id,
                ItemStackIngredient[] inputItems,
                FluidStackIngredient inputFluid,
                GasStackIngredient inputGas,
                ItemStack output);
    }

}
