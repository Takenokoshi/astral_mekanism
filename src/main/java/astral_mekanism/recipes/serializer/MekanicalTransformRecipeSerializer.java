package astral_mekanism.recipes.serializer;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import astral_mekanism.recipes.output.ItemFluidOutput;
import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
import astral_mekanism.util.AMJsonUtils;
import mekanism.api.SerializerHelper;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class MekanicalTransformRecipeSerializer<RECIPE extends MekanicalTransformRecipe> implements RecipeSerializer<RECIPE> {

    private final IFactory<RECIPE> factory;

    public MekanicalTransformRecipeSerializer(IFactory<RECIPE> factory) {
        this.factory = factory;
    }

    @Override
    public RECIPE fromJson(ResourceLocation id, JsonObject json) {
        return factory.create(id,
                IngredientCreatorAccess.item().deserialize(AMJsonUtils.read(json, "itemInputA")),
                IngredientCreatorAccess.item().deserialize(AMJsonUtils.read(json, "itemInputB")),
                IngredientCreatorAccess.item().deserialize(AMJsonUtils.read(json, "itemInputC")),
                IngredientCreatorAccess.item().deserialize(AMJsonUtils.read(json, "itemInputD")),
                new ItemFluidOutput(SerializerHelper.getItemStack(json, "outputItem"),SerializerHelper.getFluidStack(json, "outputFluid")),
                GsonHelper.getAsBoolean(json, "isCatalystA"),
                GsonHelper.getAsBoolean(json, "isCatalystB"),
                GsonHelper.getAsBoolean(json, "isCatalystC"),
                GsonHelper.getAsBoolean(json, "isCatalystD"));
    }

    @Override
    public @Nullable RECIPE fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        return factory.create(id,
                IngredientCreatorAccess.item().read(buffer),
                IngredientCreatorAccess.item().read(buffer),
                IngredientCreatorAccess.item().read(buffer),
                IngredientCreatorAccess.item().read(buffer),
                ItemFluidOutput.readFromBuf(buffer),
                buffer.readBoolean(),
                buffer.readBoolean(),
                buffer.readBoolean(),
                buffer.readBoolean());
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RECIPE recipe) {
        recipe.write(buffer);
    }

    @FunctionalInterface
    public interface IFactory<RECIPE extends MekanicalTransformRecipe> {
        RECIPE create(ResourceLocation id,
                ItemStackIngredient inputItemA,
                ItemStackIngredient inputItemB,
                ItemStackIngredient inputItemC,
                ItemStackIngredient inputItemD,
                ItemFluidOutput output,
                boolean a,
                boolean b,
                boolean c,
                boolean d);
    }

}
