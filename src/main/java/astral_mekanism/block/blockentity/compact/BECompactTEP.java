package astral_mekanism.block.blockentity.compact;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.elements.AstralMekDataType;
import mekanism.api.IContentsListener;
import mekanism.api.heat.HeatAPI;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.FluidToFluidRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.OneInputCachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.heat.VariableHeatCapacitor;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.heat.HeatCapacitorHelper;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableDouble;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.ISingleRecipeLookupHandler.FluidRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleFluid;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.FluidSlotInfo;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.util.FluidUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BECompactTEP extends TileEntityRecipeMachine<FluidToFluidRecipe>
        implements FluidRecipeLookupHandler<FluidToFluidRecipe> {

    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    public static final double MAX_MULTIPLIER_TEMP = 3000;

    public BasicFluidTank inputTank;
    public BasicFluidTank outputTank;
    public VariableHeatCapacitor heatCapacitor;

    FluidInventorySlot inputSlot;
    FluidInventorySlot outputSlot;

    double lastEnvironmentLoss;
    private boolean settingsChecked;
    public double tempMultiplier;
    private final IOutputHandler<@NotNull FluidStack> outputHandler;
    private final IInputHandler<@NotNull FluidStack> inputHandler;

    public BECompactTEP(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
        configComponent = new TileComponentConfig(this, TransmissionType.FLUID, TransmissionType.HEAT);
        ConfigInfo fluidConfig = configComponent.getConfig(TransmissionType.FLUID);
        if (fluidConfig != null) {
            fluidConfig.addSlotInfo(DataType.INPUT_1, new FluidSlotInfo(true, false, inputTank));
            fluidConfig.addSlotInfo(DataType.OUTPUT_2, new FluidSlotInfo(false, true, outputTank));
            fluidConfig.addSlotInfo(AstralMekDataType.INPUT1_OUTPUT2, new FluidSlotInfo(true, false, inputTank));
            fluidConfig.setCanEject(true);
        }
        configComponent.setupInputConfig(TransmissionType.HEAT, heatCapacitor);
        ejectorComponent = new TileComponentEjector(this, () -> Long.MAX_VALUE, () -> 2147483647,
                () -> FloatingLong.MAX_VALUE);
        ejectorComponent.setOutputData(configComponent, TransmissionType.FLUID);
        inputHandler = InputHelper.getInputHandler(inputTank, RecipeError.NOT_ENOUGH_INPUT);
        outputHandler = OutputHelper.getOutputHandler(outputTank, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
        this.heatCapacitor.setHeatCapacity(MekanismConfig.general.evaporationHeatCapacity.get() * 18, true);
        lastEnvironmentLoss = 0;
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper
                .forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(inputSlot = FluidInventorySlot.fill(inputTank, listener, 5, 56));
        builder.addSlot(outputSlot = FluidInventorySlot.drain(outputTank, listener, 155, 56));
        inputSlot.setSlotType(ContainerSlotType.INPUT);
        inputSlot.setSlotOverlay(SlotOverlay.MINUS);
        outputSlot.setSlotType(ContainerSlotType.OUTPUT);
        outputSlot.setSlotOverlay(SlotOverlay.PLUS);
        return builder.build();
    }

    @NotNull
    @Override
    public IFluidTankHolder getInitialFluidTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputTank = BasicFluidTank.input(2147483647, fluid -> containsRecipe(fluid),
                recipeCacheListener));
        builder.addTank(outputTank = BasicFluidTank.output(2147483647, recipeCacheListener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener,
            IContentsListener recipeCacheListener, CachedAmbientTemperature ambientTemperature) {
        HeatCapacitorHelper builder = HeatCapacitorHelper.forSide(this::getDirection);
        builder.addCapacitor(
                heatCapacitor = VariableHeatCapacitor.create(
                        MekanismConfig.general.evaporationHeatCapacity.get() * 3.0, () -> {
                            return 300;
                        }, this));
        return builder.build();
    }

    @Override
    public @NotNull CachedRecipe<FluidToFluidRecipe> createNewCachedRecipe(@NotNull FluidToFluidRecipe recipe,
            int arg1) {
        return OneInputCachedRecipe.fluidToFluid(recipe, recheckAllRecipeErrors, inputHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(this::canFunction)
                .setActive(this::setActive)
                .setOnFinish(this::markForSave)
                .setRequiredTicks(() -> tempMultiplier > 0 && tempMultiplier < 1
                        ? (int) Math.ceil(1 / tempMultiplier)
                        : 1)
                .setBaselineMaxOperations(() -> tempMultiplier > 0 && tempMultiplier < 1 ? 1
                        : (int) tempMultiplier);
    }

    protected void onUpdateServer() {
        super.onUpdateServer();
        this.inputSlot.fillTank();
        this.outputSlot.drainTank(outputSlot);
        if (!settingsChecked) {
            recheckSettings();
        }
        lastEnvironmentLoss = simulateEnvironment();
        tempMultiplier = Math.floor(Math.min(Integer.MAX_VALUE, (heatCapacitor.getTemperature() - HeatAPI.AMBIENT_TEMP)
                * MekanismConfig.general.evaporationTempMultiplier.get()));
        recipeCacheLookupMonitor.updateAndProcess();
        if (ejectorComponent.isEjecting(configComponent.getConfig(TransmissionType.FLUID),
                TransmissionType.FLUID)) {
            FluidUtils.emit(configComponent.getConfig(TransmissionType.FLUID)
                    .getSidesForData(AstralMekDataType.INPUT1_OUTPUT2), outputTank, this, 2147483647);
        }
    }

    private void recheckSettings() {
        Level world = getLevel();
        if (world == null) {
            return;
        }
        settingsChecked = true;
    }

    public double simulateEnvironment() {
        double currentTemperature = this.getTemperature();
        double heatCapacity = this.heatCapacitor.getHeatCapacity();
        if (Math.abs(currentTemperature - 300) < 0.001) {
            this.heatCapacitor.handleHeat(
                    300 * heatCapacity - this.heatCapacitor.getHeat());
        } else {
            double incr = MekanismConfig.general.evaporationHeatDissipation.get()
                    * Math.sqrt(Math.abs(currentTemperature - 300));
            if (currentTemperature > 300) {
                incr = -incr;
            }

            this.heatCapacitor.handleHeat(heatCapacity * incr);
            if (incr < 0.0) {
                return -incr;
            }
        }

        return 0.0;
    }

    @Override
    public @Nullable FluidToFluidRecipe getRecipe(int arg0) {
        return findFirstRecipe(inputHandler);
    }

    private boolean canFunction() {
        return MekanismUtils.canFunction(this);
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<FluidToFluidRecipe, SingleFluid<FluidToFluidRecipe>> getRecipeType() {
        return MekanismRecipeType.EVAPORATING;
    }

    public double getLastEnvironmentLoss() {
        return lastEnvironmentLoss;
    }

    public BECompactTEP getMultiblock() {
        return this;
    }

    public List<BasicFluidTank> getFluidTanks(@Nullable Object arg0) {
        return List.of(inputTank, outputTank);
    }

    public double getTemperature() {
        return this.heatCapacitor.getTemperature();
    }

    public double getTempMultipleier() {
        return this.tempMultiplier;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableDouble.create(this::getLastEnvironmentLoss,
                value -> lastEnvironmentLoss = value));
        container.track(SyncableDouble.create(this::getTempMultipleier,
                value -> tempMultiplier = value));
    }

}
