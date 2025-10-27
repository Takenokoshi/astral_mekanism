package astral_mekanism.recipes.serializer;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mekanism.api.SerializerHelper;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.Mekanism;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

public class ItemStackToFluidRecipeSerializer<RECIPE extends ItemStackToFluidRecipe>
		implements RecipeSerializer<RECIPE> {

	private final Ifactory<RECIPE> factory;

	public ItemStackToFluidRecipeSerializer(Ifactory<RECIPE> factory) {
		this.factory = factory;
	}

	private static final String itemInputJsonKey = "itemInput";
	private static final String fluidOutputJsonKey = "fluidOutput";

	@Override
	public RECIPE fromJson(ResourceLocation recipeId, JsonObject json) {
		JsonElement itemInput = GsonHelper.isArrayNode(json, itemInputJsonKey)
				? GsonHelper.getAsJsonArray(json, itemInputJsonKey)
				: GsonHelper.getAsJsonObject(json, itemInputJsonKey);
		ItemStackIngredient itemIngredient = IngredientCreatorAccess.item().deserialize(itemInput);

		FluidStack fluidOutput = SerializerHelper.getFluidStack(json, fluidOutputJsonKey);
		return this.factory.create(recipeId, itemIngredient, fluidOutput);
	}

	@Override
	public @Nullable RECIPE fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
		try {
			ItemStackIngredient inputItem = IngredientCreatorAccess.item().read(buffer);
			FluidStack outputFluid = buffer.readFluidStack();
			return this.factory.create(recipeId, inputItem, outputFluid);
		} catch (Exception e) {
            Mekanism.logger.error("Error reading 'itemstack to fluid' recipe from packet.", e);
            throw e;
		}
	}

	@Override
	public void toNetwork(FriendlyByteBuf buffer, RECIPE recipe) {
		try {
			recipe.write(buffer);
		} catch (Exception e) {
            Mekanism.logger.error("Error writing 'itemstack to fluid' recipe to packet.", e);
            throw e;
		}
	}

	@FunctionalInterface
	public interface Ifactory<RECIPE extends ItemStackToFluidRecipe> {
		RECIPE create(ResourceLocation id, ItemStackIngredient itemInput, FluidStack fluidOutput);
	}
}
