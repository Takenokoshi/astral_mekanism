package astral_mekanism.block.blockentity.base;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.IntConsumer;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.generalrecipe.lookup.monitor.UnifiedFactoryRecipeCacheLookupMonitor;
import astral_mekanism.generalrecipe.lookup.monitor.UnifiedRecipeCacheLookupMonitor;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mekanism.api.IContentsListener;
import mekanism.api.NBTConstants;
import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.common.CommonWorldTickHandler;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableBoolean;
import mekanism.common.tile.interfaces.ISustainedData;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NBTUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockEntityProgressFactory<RECIPE extends Recipe<?>, BE extends BlockEntityProgressFactory<RECIPE, BE>>
        extends BlockEntityRecipeFactory<RECIPE, BE>
        implements ISustainedData {

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
    protected UnifiedRecipeCacheLookupMonitor<RECIPE> createRecipeCacheLookupMonitor(int cacheIndex) {
        return new UnifiedFactoryRecipeCacheLookupMonitor<>(this, cacheIndex, () -> sortingNeeded = true);
    }

    @Override
    protected IContentsListener getSecondLister(IContentsListener listener) {
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

    public int getProgress(int cacheIndex) {
        return progress[cacheIndex];
    }

    protected int getTicksRequired() {
        return ticksRequired;
    }

    @Override
    public int getSavedOperatingTicks(int cacheIndex) {
        return getProgress(cacheIndex);
    }

    public double getScaledProgress(int i, int process) {
        return (double) getProgress(process) * i / ticksRequired;
    }

    public IntConsumer getProgressSetter(int cacheIndex) {
        return p -> progress[cacheIndex] = p;
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains(NBTConstants.PROGRESS, Tag.TAG_INT_ARRAY)) {
            int[] savedProgress = nbt.getIntArray(NBTConstants.PROGRESS);
            if (tier.processes != savedProgress.length) {
                Arrays.fill(progress, 0);
            }
            for (int i = 0; i < tier.processes && i < savedProgress.length; i++) {
                progress[i] = savedProgress[i];
            }
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags) {
        super.saveAdditional(nbtTags);
        nbtTags.put(NBTConstants.PROGRESS, new IntArrayTag(Arrays.copyOf(progress, progress.length)));
    }

    @Override
    public void writeSustainedData(CompoundTag data) {
        data.putBoolean(NBTConstants.SORTING, isSorting());
    }

    @Override
    public void readSustainedData(CompoundTag data) {
        NBTUtils.setBooleanIfPresent(data, NBTConstants.SORTING, value -> sorting = value);
    }

    @Override
    public Map<String, String> getTileDataRemap() {
        Map<String, String> remap = new Object2ObjectOpenHashMap<>();
        remap.put(NBTConstants.SORTING, NBTConstants.SORTING);
        return remap;
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == Upgrade.SPEED) {
            ticksRequired = MekanismUtils.getTicks(this, baseTicksRequired);
        }
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableBoolean.create(this::isSorting, v -> sorting = v));
    }

}
