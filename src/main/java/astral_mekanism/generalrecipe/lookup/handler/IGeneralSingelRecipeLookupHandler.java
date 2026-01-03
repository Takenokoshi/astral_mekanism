package astral_mekanism.generalrecipe.lookup.handler;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.generalrecipe.lookup.cache.recipe.SingleInputGeneralRecipeCache;
import mekanism.api.recipes.inputs.IInputHandler;
import net.minecraft.world.item.crafting.Recipe;

public interface IGeneralSingelRecipeLookupHandler<INPUT, RECIPE extends Recipe<?>, INPUT_CACHE extends SingleInputGeneralRecipeCache<INPUT, ?, ?, RECIPE, ?>>
        extends IGeneralRecipeTypedLookupHandler<RECIPE, INPUT_CACHE> {
    default boolean containsRecipe(INPUT input) {
        return getRecipeType().getInputCache().containsInput(getHandlerWorld(), input);
    }

    default @Nullable RECIPE findFirstRecipe(INPUT input) {
        return getRecipeType().getInputCache().findFirstRecipe(getHandlerWorld(), input);
    }

    default @Nullable RECIPE findFirstRecipe(IInputHandler<INPUT> inputHandler) {
        return this.findFirstRecipe(inputHandler.getInput());
    }
}
