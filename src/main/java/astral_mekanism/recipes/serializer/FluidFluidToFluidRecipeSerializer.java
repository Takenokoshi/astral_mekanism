package astral_mekanism.recipes.serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import astral_mekanism.recipes.recipe.FluidFluidToFluidRecipe;
import astral_mekanism.util.AMEJsonUtils;
import mekanism.api.SerializerHelper;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

public class FluidFluidToFluidRecipeSerializer<RECIPE extends FluidFluidToFluidRecipe>
        implements RecipeSerializer<RECIPE> {

    private final IFactory<RECIPE> factory;

    public FluidFluidToFluidRecipeSerializer(IFactory<RECIPE> factory) {
        this.factory = factory;
    }

    private static final String fluidInputAJsonKey = "fluidInputA";
    private static final String fluidInputBJsonKey = "fluidInputB";
    private static final String fluidOutputJsonKey = "fluidOutput";

    @Override
    public RECIPE fromJson(ResourceLocation recipeId, JsonObject json) {
        FluidStackIngredient inputA = IngredientCreatorAccess.fluid()
                .deserialize(AMEJsonUtils.read(json, fluidInputAJsonKey));
        FluidStackIngredient inputB = IngredientCreatorAccess.fluid()
                .deserialize(AMEJsonUtils.read(json, fluidInputBJsonKey));
        FluidStack output = SerializerHelper.getFluidStack(json, fluidOutputJsonKey);
        return this.factory.create(recipeId, inputA, inputB, output);
    }

    @Override
    public @Nullable RECIPE fromNetwork(@NotNull ResourceLocation recipeId,
            @NotNull FriendlyByteBuf buffer) {
        try {
            FluidStackIngredient inputA = IngredientCreatorAccess.fluid().read(buffer);
            FluidStackIngredient inputB = IngredientCreatorAccess.fluid().read(buffer);
            FluidStack output = FluidStack.readFromPacket(buffer);
            return this.factory.create(recipeId, inputA, inputB, output);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RECIPE recipe) {
        try {
            recipe.write(buffer);
        } catch (Exception e) {
            throw e;
        }
    }

    @FunctionalInterface
    public interface IFactory<RECIPE extends FluidFluidToFluidRecipe> {
        RECIPE create(ResourceLocation id, FluidStackIngredient inputFluidA,
                FluidStackIngredient inputFluidB, FluidStack outputFluid);
    }

}
