package astral_mekanism.generalrecipe.lookup.monitor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.generalrecipe.IUnifiedCachedRecipeHolder;
import astral_mekanism.generalrecipe.cachedrecipe.ICachedRecipe;
import astral_mekanism.generalrecipe.lookup.handler.IUnifiedRecipeLookUpHandler;
import mekanism.api.IContentsListener;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.math.FloatingLong;
import mekanism.common.CommonWorldTickHandler;
import net.minecraft.world.item.crafting.Recipe;

public class UnifiedRecipeCacheLookupMonitor<RECIPE extends Recipe<?>>
        implements IUnifiedCachedRecipeHolder<RECIPE>, IContentsListener {

    private final IUnifiedRecipeLookUpHandler<RECIPE> handler;
    protected final int cacheIndex;
    protected ICachedRecipe<RECIPE> cachedRecipe;
    protected boolean hasNoRecipe;

    public UnifiedRecipeCacheLookupMonitor(IUnifiedRecipeLookUpHandler<RECIPE> handler) {
        this(handler, 0);
    }

    public UnifiedRecipeCacheLookupMonitor(IUnifiedRecipeLookUpHandler<RECIPE> handler, int cacheIndex) {
        this.handler = handler;
        this.cacheIndex = cacheIndex;
    }

    protected boolean cachedIndexMatches(int cacheIndex) {
        return this.cacheIndex == cacheIndex;
    }

    @Override
    public final void onContentsChanged() {
        handler.onContentsChanged();
        onChange();
    }

    public void onChange() {
        hasNoRecipe = false;
    }

    public FloatingLong updateAndProcess(IEnergyContainer energyContainer) {
        FloatingLong prev = energyContainer.getEnergy().copy();
        if (updateAndProcess()) {
            return prev.minusEqual(energyContainer.getEnergy());
        }
        return FloatingLong.ZERO;
    }

    public boolean updateAndProcess() {
        ICachedRecipe<RECIPE> oldCache = cachedRecipe;
        cachedRecipe = getUpdatedCache(cacheIndex);
        if (cachedRecipe != oldCache) {
            handler.onCachedRecipeChanged(cachedRecipe, cacheIndex);
        }
        if (cachedRecipe != null) {
            cachedRecipe.process();
            return true;
        }
        return false;
    }

    @Override
    public void loadSavedData(@NotNull ICachedRecipe<RECIPE> cached, int cacheIndex) {
        if (cachedIndexMatches(cacheIndex)) {
            IUnifiedCachedRecipeHolder.super.loadSavedData(cached, cacheIndex);
        }
    }

    @Override
    public int getSavedOperatingTicks(int cacheIndex) {
        return cachedIndexMatches(cacheIndex) ? handler.getSavedOperatingTicks(cacheIndex)
                : IUnifiedCachedRecipeHolder.super.getSavedOperatingTicks(cacheIndex);
    }

    @Override
    public @Nullable ICachedRecipe<RECIPE> getCachedRecipe(int cacheIndex) {
        return cachedIndexMatches(cacheIndex) ? cachedRecipe : null;
    }

    @Override
    public @Nullable RECIPE getRecipe(int cacheIndex) {
        return cachedIndexMatches(cacheIndex) ? handler.getRecipe(cacheIndex) : null;
    }

    @Override
    public @Nullable ICachedRecipe<RECIPE> createNewCachedRecipe(@NotNull RECIPE recipe, int cacheIndex) {
        return cachedIndexMatches(cacheIndex) ? handler.createNewCachedRecipe(recipe, cacheIndex) : null;
    }

    @Override
    public boolean invalidateCache() {
        return CommonWorldTickHandler.flushTagAndRecipeCaches;
    }

    @Override
    public void setHasNoRecipe(int cacheIndex) {
        if (cachedIndexMatches(cacheIndex)) {
            hasNoRecipe = true;
        }
    }

    @Override
    public boolean hasNoRecipe(int cacheIndex) {
        return cachedIndexMatches(cacheIndex) ? hasNoRecipe : IUnifiedCachedRecipeHolder.super.hasNoRecipe(cacheIndex);
    }

}
