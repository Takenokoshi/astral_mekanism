package astral_mekanism.recipes.serializer;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import astral_mekanism.recipes.recipe.ReconstructionRecipe;
import mekanism.common.recipe.serializer.PressurizedReactionRecipeSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ReconstructionRecipeSerializer<RECIPE extends ReconstructionRecipe>
        extends PressurizedReactionRecipeSerializer<RECIPE> {

    public ReconstructionRecipeSerializer(IFactory<RECIPE> factory) {
        super(factory);
    }
    @NotNull
    @Override
    public RECIPE fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json){
        RECIPE result = super.fromJson(recipeId, json);
        JsonElement value = json.get("itemNotConsumed");
        result.setItemNotConsumed(value.getAsBoolean());
        return result;
    }

    @Override
    public RECIPE fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer){
        RECIPE result = super.fromNetwork(recipeId, buffer);
        result.setItemNotConsumed(buffer.readBoolean());
        return result;
    }

}
