package astral_mekanism.recipes.serializer;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;
import astral_mekanism.recipes.output.TripleItemOutput;
import astral_mekanism.recipes.recipe.GreenhouseRecipe;
import astral_mekanism.util.AMJsonUtils;
import mekanism.api.SerializerHelper;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class GreenHouseRecipeSerializer<RECIPE extends GreenhouseRecipe> implements RecipeSerializer<RECIPE> {

    private static final String ISEED = "inputSeed";
    private static final String FARMLAND = "farmland";
    private static final String FLUID = "inputFluid";
    private static final String HARVEST = "harvest";
    private static final String OSEED = "outputSeed";
    private static final String EXTRA = "extra";

    private final IFactory<RECIPE> factory;

    public GreenHouseRecipeSerializer(IFactory<RECIPE> factory) {
        this.factory = factory;
    }

    @Override
    public RECIPE fromJson(ResourceLocation id, JsonObject json) {
        return factory.create(id,
                IngredientCreatorAccess.item().deserialize(AMJsonUtils.read(json, ISEED)),
                IngredientCreatorAccess.item().deserialize(AMJsonUtils.read(json, FARMLAND)),
                IngredientCreatorAccess.fluid().deserialize(AMJsonUtils.read(json, FLUID)),
                new TripleItemOutput(
                        SerializerHelper.getItemStack(json, HARVEST),
                        SerializerHelper.getItemStack(json, OSEED),
                        json.has(EXTRA) ? SerializerHelper.getItemStack(json, EXTRA) : ItemStack.EMPTY));
    }

    @Override
    public @Nullable RECIPE fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        return factory.create(id,
                IngredientCreatorAccess.item().read(buffer),
                IngredientCreatorAccess.item().read(buffer),
                IngredientCreatorAccess.fluid().read(buffer),
                TripleItemOutput.readFromBuf(buffer));
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, RECIPE recipe) {
        recipe.write(buf);
    }

    @FunctionalInterface
    public interface IFactory<RECIPE extends GreenhouseRecipe> {
        RECIPE create(ResourceLocation id, ItemStackIngredient inputSeed, ItemStackIngredient farmland,
                FluidStackIngredient water,
                TripleItemOutput output);
    }
}
