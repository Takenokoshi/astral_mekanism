package astral_mekanism.recipes.serializer;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import astral_mekanism.recipes.output.ItemInfuseOutput;
import astral_mekanism.recipes.recipe.ItemToItemInfuseRecipe;
import astral_mekanism.util.AMJsonUtils;
import mekanism.api.SerializerHelper;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ItemToItemInfuseRecipeSerializer<RECIPE extends ItemToItemInfuseRecipe>
        implements RecipeSerializer<RECIPE> {

    private final IFactory<RECIPE> factory;

    public ItemToItemInfuseRecipeSerializer(IFactory<RECIPE> factory) {
        this.factory = factory;
    }

    @Override
    public RECIPE fromJson(ResourceLocation id, JsonObject json) {
        return factory.create(id, IngredientCreatorAccess.item().deserialize(AMJsonUtils.read(json, "input")),
                new ItemInfuseOutput(SerializerHelper.getItemStack(json, "outputItem"),
                        SerializerHelper.getInfusionStack(json, "outputInfusion")));
    }

    @Override
    public @Nullable RECIPE fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        return factory.create(id, IngredientCreatorAccess.item().read(buf), ItemInfuseOutput.read(buf));
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, RECIPE recipe) {
        recipe.write(buf);
    }

    @FunctionalInterface
    public interface IFactory<RECIPE extends ItemToItemInfuseRecipe> {
        RECIPE create(ResourceLocation id, ItemStackIngredient item, ItemInfuseOutput output);
    }

}
