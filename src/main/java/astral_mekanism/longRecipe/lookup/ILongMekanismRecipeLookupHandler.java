package astral_mekanism.longRecipe.lookup;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.generalrecipe.cachedrecipe.ICachedRecipe;
import mekanism.api.IContentsListener;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface ILongMekanismRecipeLookupHandler<RECIPE extends MekanismRecipe> extends IContentsListener {
    
    @Nullable
    default Level getHandlerWorld() {
        if (this instanceof BlockEntity tile) {
            return tile.getLevel();
        } else if (this instanceof Entity entity) {
            return entity.level();
        }
        return null;
    }

    default int getSavedOperatingTicks(int cacheIndex) {
        return 0;
    }

    @Nullable
    RECIPE getRecipe(int cacheIndex);

    @NotNull
    ICachedRecipe<RECIPE> createNewCachedRecipe(@NotNull RECIPE recipe, int cacheIndex);

    default void onCachedRecipeChanged(@Nullable ICachedRecipe<RECIPE> cachedRecipe, int cacheIndex) {
        clearRecipeErrors(cacheIndex);
    }

    default void clearRecipeErrors(int cacheIndex) {
    }

    @NotNull
    IMekanismRecipeTypeProvider<RECIPE, ?> getRecipeType();
}
