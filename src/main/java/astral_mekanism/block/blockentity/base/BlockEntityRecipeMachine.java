package astral_mekanism.block.blockentity.base;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.generalrecipe.lookup.handler.IUnifiedRecipeLookUpHandler;
import astral_mekanism.generalrecipe.lookup.monitor.UnifiedRecipeCacheLookupMonitor;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.pigment.IPigmentTank;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.ISlurryTank;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockEntityRecipeMachine<RECIPE extends Recipe<?>> extends TileEntityConfigurableMachine
        implements IUnifiedRecipeLookUpHandler<RECIPE> {

    protected final BooleanSupplier recheckAllRecipeErrors;
    private final List<RecipeError> errorTypes;
    private final boolean[] trackedErrors;
    protected UnifiedRecipeCacheLookupMonitor<RECIPE> recipeCacheLookupMonitor;
    @Nullable
    private IContentsListener recipeCacheSaveOnlyListener;

    protected BlockEntityRecipeMachine(IBlockProvider blockProvider, BlockPos pos, BlockState state,
            List<RecipeError> errorTypes) {
        super(blockProvider, pos, state);
        this.recheckAllRecipeErrors = TileEntityRecipeMachine.shouldRecheckAllErrors(this);
        this.errorTypes = List.copyOf(errorTypes);
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
    public final IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
        return getInitialGasTanks(listener,
                listener == this ? recipeCacheLookupMonitor : getRecipeCacheSaveOnlyListener());
    }

    @Nullable
    protected IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        return null;
    }

    @Nullable
    @Override
    public final IChemicalTankHolder<InfuseType, InfusionStack, IInfusionTank> getInitialInfusionTanks(IContentsListener listener) {
        return getInitialInfusionTanks(listener, listener == this ? recipeCacheLookupMonitor : getRecipeCacheSaveOnlyListener());
    }
    @Nullable
    protected IChemicalTankHolder<InfuseType, InfusionStack, IInfusionTank> getInitialInfusionTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        return null;
    }

    @Nullable
    @Override
    public final IChemicalTankHolder<Pigment, PigmentStack, IPigmentTank> getInitialPigmentTanks(IContentsListener listener) {
        return getInitialPigmentTanks(listener, listener == this ? recipeCacheLookupMonitor : getRecipeCacheSaveOnlyListener());
    }
    @Nullable
    protected IChemicalTankHolder<Pigment, PigmentStack, IPigmentTank> getInitialPigmentTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        return null;
    }

    @Nullable
    @Override
    public final IChemicalTankHolder<Slurry, SlurryStack, ISlurryTank> getInitialSlurryTanks(IContentsListener listener) {
        return getInitialSlurryTanks(listener, listener == this ? recipeCacheLookupMonitor : getRecipeCacheSaveOnlyListener());
    }
    @Nullable
    protected IChemicalTankHolder<Slurry, SlurryStack, ISlurryTank> getInitialSlurryTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        return null;
    }

    @Nullable
    @Override
    protected final IFluidTankHolder getInitialFluidTanks(IContentsListener listener) {
        return getInitialFluidTanks(listener, listener == this ? recipeCacheLookupMonitor : getRecipeCacheSaveOnlyListener());
    }
    @Nullable
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        return null;
    }

    @Nullable
    @Override
    protected final IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        return getInitialEnergyContainers(listener, listener == this ? recipeCacheLookupMonitor : getRecipeCacheSaveOnlyListener());
    }
    @Nullable
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener, IContentsListener recipeCacheListener) {
        return null;
    }

    @Nullable
    @Override
    protected final IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        return getInitialInventory(listener, listener == this ? recipeCacheLookupMonitor : getRecipeCacheSaveOnlyListener());
    }
    @Nullable
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener, IContentsListener recipeCacheListener) {
        return null;
    }

    @Nullable
    @Override
    protected final IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener, CachedAmbientTemperature ambientTemperature) {
        return getInitialHeatCapacitors(listener, listener == this ? recipeCacheLookupMonitor : getRecipeCacheSaveOnlyListener(), ambientTemperature);
    }
    @Nullable
    protected IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener, IContentsListener recipeCacheListener, CachedAmbientTemperature ambientTemperature) {
        return null;
    }

}
