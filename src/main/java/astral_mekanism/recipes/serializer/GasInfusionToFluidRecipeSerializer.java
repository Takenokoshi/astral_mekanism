package astral_mekanism.recipes.serializer;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import astral_mekanism.recipes.recipe.GasInfusionToFluidRecipe;
import astral_mekanism.util.AMEJsonUtils;
import mekanism.api.SerializerHelper;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.InfusionStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

public class GasInfusionToFluidRecipeSerializer<RECIPE extends GasInfusionToFluidRecipe>
        implements RecipeSerializer<RECIPE> {

    private static final String INPUT_GAS = "inputGas";
    private static final String INPUT_INFUSION = "inputInfusion";
    private static final String OUTPUT = "output";

    private final IGasInfusionToFluidRecipeFactory<RECIPE> factory;

    public GasInfusionToFluidRecipeSerializer(IGasInfusionToFluidRecipeFactory<RECIPE> factory) {
        this.factory = factory;
    }

    @Override
    public RECIPE fromJson(ResourceLocation id, JsonObject json) {
        GasStackIngredient gas = IngredientCreatorAccess.gas().deserialize(AMEJsonUtils.read(json, INPUT_GAS));
        InfusionStackIngredient infusion = IngredientCreatorAccess.infusion()
                .deserialize(AMEJsonUtils.read(json, INPUT_INFUSION));
        FluidStack output = SerializerHelper.getFluidStack(json, OUTPUT);
        return factory.create(id, gas, infusion, output);
    }

    @Override
    public @Nullable RECIPE fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        GasStackIngredient gas = IngredientCreatorAccess.gas().read(buf);
        InfusionStackIngredient infusion = IngredientCreatorAccess.infusion().read(buf);
        FluidStack output = FluidStack.readFromPacket(buf);
        return factory.create(id, gas, infusion, output);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, RECIPE recipe) {
        recipe.write(buf);
    }

    @FunctionalInterface
    public interface IGasInfusionToFluidRecipeFactory<RECIPE extends GasInfusionToFluidRecipe> {
        RECIPE create(ResourceLocation id, GasStackIngredient inputGas,
                InfusionStackIngredient inputInfusion, FluidStack output);
    }

}
