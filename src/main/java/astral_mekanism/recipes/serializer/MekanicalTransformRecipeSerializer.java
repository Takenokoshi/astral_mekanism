package astral_mekanism.recipes.serializer;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import astral_mekanism.recipes.output.ItemFluidOutput;
import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
import astral_mekanism.util.AMJsonUtils;
import mekanism.api.SerializerHelper;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IFluidStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

public class MekanicalTransformRecipeSerializer<RECIPE extends MekanicalTransformRecipe>
        implements RecipeSerializer<RECIPE> {

    private final IFactory<RECIPE> factory;

    public MekanicalTransformRecipeSerializer(IFactory<RECIPE> factory) {
        this.factory = factory;
    }

    @Override
    public RECIPE fromJson(ResourceLocation id, JsonObject json) {
        IItemStackIngredientCreator creatorI = IngredientCreatorAccess.item();
        IFluidStackIngredientCreator creatorF = IngredientCreatorAccess.fluid();
        return factory.create(id,
                creatorI.deserialize(AMJsonUtils.read(json, "itemInputA")),
                creatorI.deserialize(AMJsonUtils.read(json, "itemInputB")),
                creatorI.deserialize(AMJsonUtils.read(json, "itemInputC")),
                creatorF.deserialize(AMJsonUtils.read(json, "fluidInputA")),
                creatorF.deserialize(AMJsonUtils.read(json, "fluidInputB")),
                new ItemFluidOutput(
                        json.has("outputItem") ? SerializerHelper.getItemStack(json, "outputItem") : ItemStack.EMPTY,
                        json.has("outputFluid") ? SerializerHelper.getFluidStack(json, "outputFluid")
                                : FluidStack.EMPTY),
                GsonHelper.getAsBoolean(json, "itemAIsCatalyst"),
                GsonHelper.getAsBoolean(json, "itemBIsCatalyst"),
                GsonHelper.getAsBoolean(json, "itemCIsCatalyst"),
                GsonHelper.getAsBoolean(json, "fluidAIsCatalyst"),
                GsonHelper.getAsBoolean(json, "fluidBIsCatalyst"));
    }

    @Override
    public @Nullable RECIPE fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        IItemStackIngredientCreator creatorI = IngredientCreatorAccess.item();
        IFluidStackIngredientCreator creatorF = IngredientCreatorAccess.fluid();
        return factory.create(id,
                creatorI.read(buf),
                creatorI.read(buf),
                creatorI.read(buf),
                creatorF.read(buf),
                creatorF.read(buf),
                ItemFluidOutput.readFromBuf(buf),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean());
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, RECIPE recipe) {
        recipe.write(buf);
    }

    @FunctionalInterface
    public static interface IFactory<RECIPE extends MekanicalTransformRecipe> {
        RECIPE create(ResourceLocation id,
                ItemStackIngredient inputItemA,
                ItemStackIngredient inputItemB,
                ItemStackIngredient inputItemC,
                FluidStackIngredient inputFluidA,
                FluidStackIngredient inputFluidB,
                ItemFluidOutput output,
                boolean ia,
                boolean ib,
                boolean ic,
                boolean fa,
                boolean fb);
    }

}
