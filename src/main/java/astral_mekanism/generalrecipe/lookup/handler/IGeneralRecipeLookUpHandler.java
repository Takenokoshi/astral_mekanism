package astral_mekanism.generalrecipe.lookup.handler;

import mekanism.api.IContentsListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.generalrecipe.IGeneralRecipeTypeProvider;
import astral_mekanism.generalrecipe.cachedrecipe.GeneralCachedRecipe;

public interface IGeneralRecipeLookUpHandler<RECIPE extends Recipe<?>> extends IContentsListener {

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
    GeneralCachedRecipe<RECIPE> createNewCachedRecipe(@NotNull RECIPE recipe, int cacheIndex);
    default void onCachedRecipeChanged(@Nullable GeneralCachedRecipe<RECIPE> cachedRecipe, int cacheIndex) {
        clearRecipeErrors(cacheIndex);
    }
    default void clearRecipeErrors(int cacheIndex) {
    }
    @NotNull
    IGeneralRecipeTypeProvider<?, RECIPE, ?> getRecipeType();
}