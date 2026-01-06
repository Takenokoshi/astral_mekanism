package astral_mekanism.generalrecipe.lookup.monitor;

import net.minecraft.world.item.crafting.Recipe;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.generalrecipe.lookup.handler.IUnifiedRecipeLookUpHandler;

public class UnifiedFactoryRecipeCacheLookupMonitor<RECIPE extends Recipe<?>>
        extends UnifiedRecipeCacheLookupMonitor<RECIPE> {

    private final Runnable setSortingNeeded;

    public UnifiedFactoryRecipeCacheLookupMonitor(IUnifiedRecipeLookUpHandler<RECIPE> handler, int cacheIndex,
            Runnable setSortingNeeded) {
        super(handler, cacheIndex);
        this.setSortingNeeded = setSortingNeeded;
    }

    @Override
    public void onChange() {
        super.onChange();
        setSortingNeeded.run();
    }

    public void updateCachedRecipe(@NotNull RECIPE recipe) {
        cachedRecipe = createNewCachedRecipe(recipe, cacheIndex);
        hasNoRecipe = false;
    }
}