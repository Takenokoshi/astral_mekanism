package astral_mekanism.recipes.serializer;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import astral_mekanism.recipes.recipe.GreenHouseRecipe;
import mekanism.api.SerializerHelper;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.Mekanism;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class GreenHouseRecipeSerializer<RECIPE extends GreenHouseRecipe> implements RecipeSerializer<RECIPE> {

    private final IFactory<RECIPE> factory;

    public GreenHouseRecipeSerializer(IFactory<RECIPE> factory){
        this.factory = factory;
    }

    private final String itemInputJsonKey = "itemInput";
    private final String fluidInputJsonKey = "fluidInput";
    private final String energyRequiredJsonKey = "energyRequired";
    private final String durationJsonKey = "duration";
    private final String itemOutputAJsonKey = "itemOutputA";
    private final String itemOutputBJsonKey = "itemOutputB";

    @NotNull
    @Override
    public RECIPE fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
        JsonElement itemInput = GsonHelper.isArrayNode(json, itemInputJsonKey)
                ? GsonHelper.getAsJsonArray(json, itemInputJsonKey)
                : GsonHelper.getAsJsonObject(json, itemInputJsonKey);
        ItemStackIngredient itemIngredient = IngredientCreatorAccess.item().deserialize(itemInput);

        JsonElement fluidInput = GsonHelper.isArrayNode(json, fluidInputJsonKey)
                ? GsonHelper.getAsJsonArray(json, fluidInputJsonKey)
                : GsonHelper.getAsJsonObject(json, fluidInputJsonKey);
        FluidStackIngredient fluidIngredient = IngredientCreatorAccess.fluid().deserialize(fluidInput);
        FloatingLong energyRequired = FloatingLong.ZERO;
        if (json.has(energyRequiredJsonKey)) {
            energyRequired = SerializerHelper.getFloatingLong(json, energyRequiredJsonKey);
        }

        JsonElement ticks = json.get(durationJsonKey);
        if (!GsonHelper.isNumberValue(ticks)) {
            throw new JsonSyntaxException("Expected duration to be a number greater than zero.");
        }
        int duration = ticks.getAsJsonPrimitive().getAsInt();
        if (duration <= 0) {
            throw new JsonSyntaxException("Expected duration to be a number greater than zero.");
        }

        ItemStack itemOutputA = SerializerHelper.getItemStack(json, itemOutputAJsonKey);
        ItemStack itemOutputB = SerializerHelper.getItemStack(json, itemOutputBJsonKey);
        return this.factory.create(id, itemIngredient, fluidIngredient, energyRequired, duration, itemOutputA,
                itemOutputB);
    }
    
    @Override
    public RECIPE fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
        try {
            ItemStackIngredient inputitem = IngredientCreatorAccess.item().read(buffer);
            FluidStackIngredient inputFluid = IngredientCreatorAccess.fluid().read(buffer);
            FloatingLong energyRequired = FloatingLong.readFromBuffer(buffer);
            int duration = buffer.readVarInt();
            ItemStack outputItemA = buffer.readItem();
            ItemStack outputItemB = buffer.readItem();
            return this.factory.create(recipeId, inputitem, inputFluid, energyRequired, duration, outputItemA, outputItemB);
        } catch (Exception e) {
            Mekanism.logger.error("Error reading greenhouse recipe from packet.", e);
            throw e;
        }
    }

    @FunctionalInterface
    public interface IFactory<RECIPE extends GreenHouseRecipe> {

        RECIPE create(ResourceLocation id, ItemStackIngredient itemInput, FluidStackIngredient fluidInput,
                FloatingLong energyRequired, int duration,
                ItemStack outputItemA, ItemStack outputItemB);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull RECIPE recipe) {
        try {
            recipe.write(buffer);
        } catch (Exception e) {
            Mekanism.logger.error("Error writing greenhouse recipe to packet.", e);
            throw e;
        }
    }
}
