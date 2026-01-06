package astral_mekanism.generalrecipe.lookup.handler;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.generalrecipe.IUnifiedRecipeTypeProvider;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import net.minecraft.world.item.crafting.Recipe;

public interface IUnifiedRecipeTypedLookupHandler<RECIPE extends Recipe<?>, INPUT_CACHE extends IInputRecipeCache>
        extends IUnifiedRecipeLookUpHandler<RECIPE> {

    @NotNull
    @Override
    IUnifiedRecipeTypeProvider<RECIPE,INPUT_CACHE> getRecipeType();
}
