package astral_mekanism.block.blockentity.base;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.interf.IEnergizedMachine;
import astral_mekanism.generalrecipe.cachedrecipe.ICachedRecipe;
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
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableFloatingLong;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockEntityGeneralRecipeFactory<RECIPE extends Recipe<?>, BE extends BlockEntityGeneralRecipeFactory<RECIPE, BE>>
        extends TileEntityConfigurableMachine
        implements IUnifiedRecipeLookUpHandler<RECIPE>, IEnergizedMachine<BE>, IAstralMekanismFactory<BE> {

    public AstralMekanismFactoryTier tier;
    protected UnifiedRecipeCacheLookupMonitor<RECIPE>[] recipeCacheLookupMonitors;
    protected BooleanSupplier[] recheckAllRecipeErrors;
    protected final ErrorTracker errorTracker;
    private final boolean[] activeStates;
    private FloatingLong lastUsage = FloatingLong.ZERO;
    protected MachineEnergyContainer<BE> energyContainer;
    protected EnergyInventorySlot energySlot;

    protected BlockEntityGeneralRecipeFactory(IBlockProvider blockProvider, BlockPos pos, BlockState state,
            List<RecipeError> errorTypes, Set<RecipeError> globalErrorTypes) {
        super(blockProvider, pos, state);
        this.activeStates = new boolean[tier.processes];
        this.errorTracker = new ErrorTracker(errorTypes, globalErrorTypes, tier.processes);
        recheckAllRecipeErrors = new BooleanSupplier[tier.processes];
        for (int i = 0; i < recheckAllRecipeErrors.length; i++) {
            recheckAllRecipeErrors[i] = TileEntityRecipeMachine.shouldRecheckAllErrors(this);
        }
    }

    @Override
    public AstralMekanismFactoryTier getTier() {
        return tier;
    }

    protected IContentsListener markAllMonitorsChanged(IContentsListener listener) {
        return () -> {
            listener.onContentsChanged();
            for (UnifiedRecipeCacheLookupMonitor<RECIPE> cacheLookupMonitor : recipeCacheLookupMonitors) {
                cacheLookupMonitor.onChange();
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void presetVariables() {
        super.presetVariables();
        tier = Attribute.getTier(getBlockType(), AstralMekanismFactoryTier.class);
        recipeCacheLookupMonitors = new UnifiedRecipeCacheLookupMonitor[tier.processes];
        for (int i = 0; i < recipeCacheLookupMonitors.length; i++) {
            recipeCacheLookupMonitors[i] = createRecipeCacheLookupMonitor(i);
        }
    }

    protected UnifiedRecipeCacheLookupMonitor<RECIPE> createRecipeCacheLookupMonitor(int cacheIndex) {
        return new UnifiedRecipeCacheLookupMonitor<>(this, cacheIndex);
    }

    @Nullable
    protected ICachedRecipe<RECIPE> getCachedRecipe(int cacheIndex) {
        return recipeCacheLookupMonitors[cacheIndex].getCachedRecipe(cacheIndex);
    }

    @Override
    public void clearRecipeErrors(int cacheIndex) {
        Arrays.fill(errorTracker.trackedErrors[cacheIndex], false);
    }

    protected void setActiveState(boolean state, int cacheIndex) {
        activeStates[cacheIndex] = state;
    }

    public BooleanSupplier getWarningCheck(RecipeError error, int processIndex) {
        return errorTracker.getWarningCheck(error, processIndex);
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(getSelf(), listener));
        return builder.build();
    }

    protected IContentsListener getSecondLister(IContentsListener listener) {
        return listener;
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = addSlots(
                InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig), listener,
                getSecondLister(listener));
        builder.addSlot(
                energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 3, 18));
        return builder.build();
    }

    protected InventorySlotHelper addSlots(InventorySlotHelper builder, IContentsListener listener,
            IContentsListener updateSortingListener) {
        return builder;
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener) {
        return addFluidTanks(FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig), listener,
                getSecondLister(listener)).build();
    }

    protected FluidTankHelper addFluidTanks(FluidTankHelper builder, IContentsListener listener,
            IContentsListener updateSortingListener) {
        return builder;
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
        return addGasTanks(ChemicalTankHelper.forSideGasWithConfig(this::getDirection, this::getConfig), listener,
                getSecondLister(listener)).build();
    }

    protected ChemicalTankHelper<Gas, GasStack, IGasTank> addGasTanks(
            ChemicalTankHelper<Gas, GasStack, IGasTank> builder, IContentsListener listener,
            IContentsListener updateSortingListener) {
        return builder;
    }

    @NotNull
    @Override
    public IChemicalTankHolder<InfuseType, InfusionStack, IInfusionTank> getInitialInfusionTanks(
            IContentsListener listener) {
        return addInfusionTanks(ChemicalTankHelper.forSideInfusionWithConfig(this::getDirection, this::getConfig),
                listener,
                getSecondLister(listener)).build();
    }

    protected ChemicalTankHelper<InfuseType, InfusionStack, IInfusionTank> addInfusionTanks(
            ChemicalTankHelper<InfuseType, InfusionStack, IInfusionTank> builder, IContentsListener listener,
            IContentsListener updateSortingListener) {
        return builder;
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Pigment, PigmentStack, IPigmentTank> getInitialPigmentTanks(IContentsListener listener) {
        return addPigmentTanks(ChemicalTankHelper.forSidePigmentWithConfig(this::getDirection, this::getConfig),
                listener,
                getSecondLister(listener)).build();
    }

    protected ChemicalTankHelper<Pigment, PigmentStack, IPigmentTank> addPigmentTanks(
            ChemicalTankHelper<Pigment, PigmentStack, IPigmentTank> builder, IContentsListener listener,
            IContentsListener updateSortingListener) {
        return builder;
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Slurry, SlurryStack, ISlurryTank> getInitialSlurryTanks(IContentsListener listener) {
        return addSlurryTanks(ChemicalTankHelper.forSideSlurryWithConfig(this::getDirection, this::getConfig), listener,
                getSecondLister(listener)).build();
    }

    protected ChemicalTankHelper<Slurry, SlurryStack, ISlurryTank> addSlurryTanks(
            ChemicalTankHelper<Slurry, SlurryStack, ISlurryTank> builder, IContentsListener listener,
            IContentsListener updateSortingListener) {
        return builder;
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        FloatingLong prev = energyContainer.getEnergy().copy();
        for (int i = 0; i < recipeCacheLookupMonitors.length; i++) {
            if (!recipeCacheLookupMonitors[i].updateAndProcess()) {
                activeStates[i] = false;
            }
        }
        boolean isActive = false;
        for (boolean state : activeStates) {
            if (state) {
                isActive = true;
                break;
            }
        }
        setActive(isActive);
        lastUsage = isActive ? prev.minusEqual(energyContainer.getEnergy()) : FloatingLong.ZERO;
    }

    public FloatingLong getLastUsage() {
        return lastUsage;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        errorTracker.track(container);
        container.track(SyncableFloatingLong.create(this::getLastUsage, value -> lastUsage = value));
    }

    @Override
    public double getProgressScaled() {
        return 0;
    }

    public double getProgressScaled(int index) {
        return activeStates[index] ? 1 : 0;
    }

}
