package astral_mekanism.generalrecipe.lookup.handler;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.generalrecipe.lookup.cache.recipe.ISingleInputRecipeCache;
import mekanism.api.recipes.inputs.IInputHandler;
import net.minecraft.world.item.crafting.Recipe;

public interface IUnifiedSingelRecipeLookupHandler<INPUT, RECIPE extends Recipe<?>, INPUT_CACHE extends ISingleInputRecipeCache<INPUT,RECIPE>>
        extends IUnifiedRecipeTypedLookupHandler<RECIPE, INPUT_CACHE> {
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
