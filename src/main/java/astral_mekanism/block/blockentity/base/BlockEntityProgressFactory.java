package astral_mekanism.block.blockentity.base;

import java.util.List;
import java.util.Set;

import astral_mekanism.generalrecipe.lookup.monitor.GeneralFactoryRecipeCacheLookupMonitor;
import astral_mekanism.generalrecipe.lookup.monitor.GeneralRecipeCacheLookupMonitor;
import mekanism.api.IContentsListener;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.common.CommonWorldTickHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockEntityProgressFactory<RECIPE extends Recipe<?>, BE extends BlockEntityProgressFactory<RECIPE, BE>>
        extends BlockEntityRecipeFactory<RECIPE, BE> {

    protected final int baseTicksRequired;
    public final int[] progress;
    private int ticksRequired;
    private boolean sorting;
    private boolean sortingNeeded = true;

    protected BlockEntityProgressFactory(IBlockProvider blockProvider, BlockPos pos, BlockState state,
            int baseTicksRequired, List<RecipeError> errorTypes, Set<RecipeError> globalErrorTypes) {
        super(blockProvider, pos, state, errorTypes, globalErrorTypes);
        this.baseTicksRequired = baseTicksRequired;
        this.progress = new int[tier.processes];
    }

    @Override
    protected GeneralRecipeCacheLookupMonitor<RECIPE> createRecipeCacheLookupMonitor(int cacheIndex) {
        return new GeneralFactoryRecipeCacheLookupMonitor<>(this, cacheIndex, () -> sortingNeeded = true);
    }

    @Override
    protected IContentsListener createUpdateSortingListener(IContentsListener listener) {
        return () -> {
            listener.onContentsChanged();
            sortingNeeded = true;
        };
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        if (sortingNeeded && isSorting()) {
            sortingNeeded = false;
            sort();
        } else if (!sortingNeeded && CommonWorldTickHandler.flushTagAndRecipeCaches) {
            sortingNeeded = true;
        }
    }

    public void toggleSorting() {
        sorting = !isSorting();
        markForSave();
    }

    public boolean isSorting() {
        return sorting;
    }

    protected abstract void sort();

}
