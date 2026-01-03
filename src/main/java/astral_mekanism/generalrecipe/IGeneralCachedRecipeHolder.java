package astral_mekanism.generalrecipe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.generalrecipe.cachedrecipe.GeneralCachedRecipe;
import net.minecraft.world.item.crafting.Recipe;

public interface IGeneralCachedRecipeHolder<RECIPE extends Recipe<?>> {

    @Nullable
    default GeneralCachedRecipe<RECIPE> getUpdatedCache(int cacheIndex) {
        boolean cacheInvalid = invalidateCache();
        GeneralCachedRecipe<RECIPE> currentCache = cacheInvalid ? null : getCachedRecipe(cacheIndex);
        if (currentCache == null || !currentCache.isInputValid()) {
            if (cacheInvalid || !hasNoRecipe(cacheIndex)) {
                RECIPE recipe = getRecipe(cacheIndex);
                if (recipe == null) {
                    setHasNoRecipe(cacheIndex);
                } else {
                    GeneralCachedRecipe<RECIPE> cached = createNewCachedRecipe(recipe, cacheIndex);
                    if (currentCache == null || cached != null) {
                        if (currentCache == null && cached != null) {
                            loadSavedData(cached, cacheIndex);
                        }
                        return cached;
                    }
                }
            }
        }
        return currentCache;
    }

    default void loadSavedData(@NotNull GeneralCachedRecipe<RECIPE> cached, int cacheIndex) {
        cached.loadSavedOperatingTicks(getSavedOperatingTicks(cacheIndex));
    }

    default int getSavedOperatingTicks(int cacheIndex) {
        return 0;
    }

    @Nullable
    GeneralCachedRecipe<RECIPE> getCachedRecipe(int cacheIndex);

    @Nullable
    RECIPE getRecipe(int cacheIndex);

    @Nullable
    GeneralCachedRecipe<RECIPE> createNewCachedRecipe(@NotNull RECIPE recipe, int cacheIndex);

    default boolean invalidateCache() {
        return false;
    }

    default void setHasNoRecipe(int cacheIndex) {
    }

    default boolean hasNoRecipe(int cacheIndex) {
        return false;
    }
}
