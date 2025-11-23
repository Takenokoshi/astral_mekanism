package astral_mekanism.recipes.serializer;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import astral_mekanism.AstralMekanism;
import astral_mekanism.recipes.recipe.TripleItemToItemRecipe;
import astral_mekanism.util.AMJsonUtils;
import mekanism.api.SerializerHelper;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class TripleItemToItemRecipeSerializer<RECIPE extends TripleItemToItemRecipe>
        implements RecipeSerializer<RECIPE> {

    private final Ifactory<RECIPE> factory;

    public TripleItemToItemRecipeSerializer(Ifactory<RECIPE> factory) {
        this.factory = factory;
    }

    @Override
    public RECIPE fromJson(ResourceLocation id, JsonObject json) {
        return factory.create(id,
                IngredientCreatorAccess.item().deserialize(AMJsonUtils.read(json, "itemInputA")),
                IngredientCreatorAccess.item().deserialize(AMJsonUtils.read(json, "itemInputB")),
                IngredientCreatorAccess.item().deserialize(AMJsonUtils.read(json, "itemInputC")),
                SerializerHelper.getItemStack(json, "output"));
    }

    @Override
    public @Nullable RECIPE fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        return factory.create(id,
                IngredientCreatorAccess.item().read(buffer),
                IngredientCreatorAccess.item().read(buffer),
                IngredientCreatorAccess.item().read(buffer),
                buffer.readItem());
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RECIPE recipe) {
        try {
            recipe.write(buffer);
        } catch (Exception e) {
            AstralMekanism.LOGGER.error("Error writing recipe to packet.", e);
            throw e;
        }
    }

    @FunctionalInterface
    public interface Ifactory<RECIPE extends TripleItemToItemRecipe> {
        RECIPE create(ResourceLocation id,
                ItemStackIngredient inputItemA,
                ItemStackIngredient inputItemB,
                ItemStackIngredient inputItemC,
                ItemStack output);

    }

}
