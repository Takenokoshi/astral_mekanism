package astral_mekanism.generalrecipe.lookup.handler;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.generalrecipe.IGeneralRecipeTypeProvider;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import net.minecraft.world.item.crafting.Recipe;

public interface IGeneralRecipeTypedLookupHandler<RECIPE extends Recipe<?>, INPUT_CACHE extends IInputRecipeCache>
        extends IGeneralRecipeLookUpHandler<RECIPE> {

    @NotNull
    @Override
    IGeneralRecipeTypeProvider<?, RECIPE, INPUT_CACHE> getRecipeType();
}
