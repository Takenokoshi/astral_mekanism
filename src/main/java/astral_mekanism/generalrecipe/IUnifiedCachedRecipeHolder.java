package astral_mekanism.generalrecipe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.generalrecipe.cachedrecipe.ICachedRecipe;
import net.minecraft.world.item.crafting.Recipe;

public interface IUnifiedCachedRecipeHolder<RECIPE extends Recipe<?>> {

    @Nullable
    default ICachedRecipe<RECIPE> getUpdatedCache(int cacheIndex) {
        boolean cacheInvalid = invalidateCache();
        ICachedRecipe<RECIPE> currentCache = cacheInvalid ? null : getCachedRecipe(cacheIndex);
        if (currentCache == null || !currentCache.isInputValid()) {
            if (cacheInvalid || !hasNoRecipe(cacheIndex)) {
                RECIPE recipe = getRecipe(cacheIndex);
                if (recipe == null) {
                    setHasNoRecipe(cacheIndex);
                } else {
                    ICachedRecipe<RECIPE> cached = createNewCachedRecipe(recipe, cacheIndex);
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

    default void loadSavedData(@NotNull ICachedRecipe<RECIPE> cached, int cacheIndex) {
        cached.loadSavedOperatingTicks(getSavedOperatingTicks(cacheIndex));
    }

    default int getSavedOperatingTicks(int cacheIndex) {
        return 0;
    }

    @Nullable
    ICachedRecipe<RECIPE> getCachedRecipe(int cacheIndex);

    @Nullable
    RECIPE getRecipe(int cacheIndex);

    @Nullable
    ICachedRecipe<RECIPE> createNewCachedRecipe(@NotNull RECIPE recipe, int cacheIndex);

    default boolean invalidateCache() {
        return false;
    }

    default void setHasNoRecipe(int cacheIndex) {
    }

    default boolean hasNoRecipe(int cacheIndex) {
        return false;
    }
}
