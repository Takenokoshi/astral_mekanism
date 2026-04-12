package astral_mekanism.block.blockentity.base;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.elements.aekey.AEKeySlot;
import astral_mekanism.generalrecipe.lookup.handler.IUnifiedRecipeLookUpHandler;
import astral_mekanism.generalrecipe.lookup.monitor.UnifiedRecipeCacheLookupMonitor;
import mekanism.api.IContentsListener;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BEAppliedRecipeMachine<RECIPE extends Recipe<?>> extends AppliedConfigurableBlockEntity
        implements IUnifiedRecipeLookUpHandler<RECIPE> {

    public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE);

    protected final BooleanSupplier recheckAllRecipeErrors;
    private final List<RecipeError> errorTypes;
    private final boolean[] trackedErrors;
    protected UnifiedRecipeCacheLookupMonitor<RECIPE> recipeCacheLookupMonitor;
    @Nullable
    private IContentsListener recipeCacheSaveOnlyListener;

    public BEAppliedRecipeMachine(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        this.recheckAllRecipeErrors = TileEntityRecipeMachine.shouldRecheckAllErrors(this);
        this.errorTypes = List.copyOf(TRACKED_ERROR_TYPES);
        trackedErrors = new boolean[this.errorTypes.size()];
        recipeCacheSaveOnlyListener = null;
    }

    @Override
    protected void presetVariables() {
        super.presetVariables();
        recipeCacheLookupMonitor = createNewCacheMonitor();
    }

    protected UnifiedRecipeCacheLookupMonitor<RECIPE> createNewCacheMonitor() {
        return new UnifiedRecipeCacheLookupMonitor<>(this);
    }

    protected IContentsListener getRecipeCacheSaveOnlyListener() {
        if (supportsComparator()) {
            if (recipeCacheSaveOnlyListener == null) {
                recipeCacheSaveOnlyListener = () -> {
                    markForSave();
                    recipeCacheLookupMonitor.onChange();
                };
            }
            return recipeCacheSaveOnlyListener;
        }
        return recipeCacheLookupMonitor;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.trackArray(trackedErrors);
    }

    @Override
    public void clearRecipeErrors(int cacheIndex) {
        Arrays.fill(trackedErrors, false);
    }

    protected void onErrorsChanged(Set<RecipeError> errors) {
        for (int i = 0; i < trackedErrors.length; i++) {
            trackedErrors[i] = errors.contains(errorTypes.get(i));
        }
    }

    public BooleanSupplier getWarningCheck(RecipeError error) {
        int errorIndex = errorTypes.indexOf(error);
        if (errorIndex == -1) {
            return () -> false;
        }
        return () -> trackedErrors[errorIndex];
    }

    @Nullable
    @Override
    protected final IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        return getInitialEnergyContainers(listener,
                listener == this ? recipeCacheLookupMonitor : getRecipeCacheSaveOnlyListener());
    }

    @Nullable
    protected abstract IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener,
            IContentsListener recipeCacheListener);

    @Override
    protected final List<AEKeySlot<?>> getInitialAeKeySlots(IContentsListener listener) {
        return getInitialAeKeySlots(listener,
                listener == this ? recipeCacheLookupMonitor : getRecipeCacheSaveOnlyListener());
    }

    protected abstract List<AEKeySlot<?>> getInitialAeKeySlots(IContentsListener listener,
            IContentsListener recipeCacheListener);
}
