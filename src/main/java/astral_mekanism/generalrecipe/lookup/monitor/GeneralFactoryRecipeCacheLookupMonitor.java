package astral_mekanism.generalrecipe.lookup.monitor;

import net.minecraft.world.item.crafting.Recipe;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.generalrecipe.lookup.handler.IGeneralRecipeLookUpHandler;

public class GeneralFactoryRecipeCacheLookupMonitor<RECIPE extends Recipe<?>>
        extends GeneralRecipeCacheLookupMonitor<RECIPE> {

    private final Runnable setSortingNeeded;

    public GeneralFactoryRecipeCacheLookupMonitor(IGeneralRecipeLookUpHandler<RECIPE> handler, int cacheIndex,
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