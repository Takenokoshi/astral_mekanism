package astral_mekanism.block.blockentity.compact;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.AstralMekanismTier;
import astral_mekanism.block.blockentity.elements.ExtendedComponentEjector;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedEnergyInventorySlot;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedFluidInventorySlot;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedOutputInventorySlot;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.NBTConstants;
import mekanism.api.RelativeSide;
import mekanism.api.fluid.IExtendedFluidTank;
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
import mekanism.common.block.attribute.Attribute;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.heat.VariableHeatCapacitor;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
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
import mekanism.common.inventory.container.sync.SyncableFloatingLong;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.ISingleRecipeLookupHandler.FluidRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleFluid;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NBTUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BECompactTEP extends TileEntityRecipeMachine<FluidToFluidRecipe>
        implements FluidRecipeLookupHandler<FluidToFluidRecipe> {

    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    public BasicFluidTank inputTank;
    public BasicFluidTank outputTank;
    public VariableHeatCapacitor heatCapacitor;
    private MachineEnergyContainer<BECompactTEP> energyContainer;

    private FluidInventorySlot inputInputSlot;
    private FluidInventorySlot outputInputSlot;
    private PagedOutputInventorySlot inputOutputSlot;
    private PagedOutputInventorySlot outputOutputSlot;
    private PagedEnergyInventorySlot energySlot;

    private double lastEnvironmentLoss;
    private boolean settingsChecked;
    public double tempMultiplier;
    private final IOutputHandler<@NotNull FluidStack> outputHandler;
    private final IInputHandler<@NotNull FluidStack> inputHandler;
    protected final ExtendedComponentEjector ejectorComponetEx;

    private FloatingLong clientEnergyUsed = FloatingLong.ZERO;

    private AstralMekanismTier tier;

    public BECompactTEP(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
        configComponent = new TileComponentConfig(this, TransmissionType.FLUID, TransmissionType.HEAT,
                TransmissionType.ENERGY);
        configComponent.setupIOConfig(TransmissionType.FLUID, inputTank, outputTank, RelativeSide.RIGHT)
                .setEjecting(true);
        configComponent.setupInputConfig(TransmissionType.HEAT, heatCapacitor);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ejectorComponetEx = new ExtendedComponentEjector(this, outputTank::getCapacity)
                .setOutputData(configComponent, TransmissionType.FLUID)
                .setCanEject(t -> t == TransmissionType.FLUID)
                .setCanTypeEject((trans, data) -> {
                    if (trans == TransmissionType.FLUID) {
                        return data == DataType.OUTPUT || data == DataType.INPUT_OUTPUT;
                    }
                    return false;
                }).setCanFluidTankEject((tank, data) -> {
                    if (tank == outputTank) {
                        return data == DataType.OUTPUT || data == DataType.INPUT_OUTPUT;
                    }
                    return false;
                });
        ejectorComponent = ejectorComponetEx;
        inputHandler = InputHelper.getInputHandler(inputTank, RecipeError.NOT_ENOUGH_INPUT);
        outputHandler = OutputHelper.getOutputHandler(outputTank, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
        this.heatCapacitor.setHeatCapacity(MekanismConfig.general.evaporationHeatCapacity.get() * 18, true);
        lastEnvironmentLoss = 0;
    }

    @Override
    protected void presetVariables() {
        super.presetVariables();
        tier = Attribute.getTier(getBlockType(), AstralMekanismTier.class);
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper
                .forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(inputInputSlot = PagedFluidInventorySlot.fill(inputTank, listener, 5, 20, 0))
                .setSlotType(ContainerSlotType.INPUT);
        builder.addSlot(outputInputSlot = PagedFluidInventorySlot.drain(outputTank, listener, 155, 20, 0))
                .setSlotType(ContainerSlotType.INPUT);
        builder.addSlot(inputOutputSlot = PagedOutputInventorySlot.at(listener, 5, 56, 0))
                .setSlotType(ContainerSlotType.OUTPUT);
        builder.addSlot(outputOutputSlot = PagedOutputInventorySlot.at(listener, 155, 56, 0))
                .setSlotType(ContainerSlotType.OUTPUT);
        builder.addSlot(energySlot = PagedEnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener,
                15, 35, 1));
        inputInputSlot.setSlotOverlay(SlotOverlay.MINUS);
        outputInputSlot.setSlotOverlay(SlotOverlay.PLUS);
        return builder.build();
    }

    @NotNull
    @Override
    public IFluidTankHolder getInitialFluidTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputTank = BasicFluidTank.input((int) Math.min(0x7fffffff, tier.exToInt() * 100l),
                fluid -> containsRecipe(fluid),
                recipeCacheListener));
        builder.addTank(outputTank = BasicFluidTank.output(tier.exToInt(), recipeCacheListener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSide(this::getDirection);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener));
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
        energySlot.fillContainerOrConvert();
        FloatingLong toUse = FloatingLong.ZERO;
        if (MekanismUtils.canFunction(this)) {
            toUse = energyContainer.extract(energyContainer.getEnergyPerTick(), Action.SIMULATE,
                    AutomationType.INTERNAL);
            if (!toUse.isZero()) {
                heatCapacitor.handleHeat(
                        toUse.multiply(MekanismConfig.general.resistiveHeaterEfficiency.get()).doubleValue());
                energyContainer.extract(toUse, Action.EXECUTE, AutomationType.INTERNAL);
            }
        }
        clientEnergyUsed = toUse;
        this.inputInputSlot.fillTank(inputOutputSlot);
        this.outputInputSlot.drainTank(outputOutputSlot);
        if (!settingsChecked) {
            recheckSettings();
        }
        lastEnvironmentLoss = simulateEnvironment();
        tempMultiplier = Math.floor(Math.min(tier.exToInt(), (heatCapacitor.getTemperature() - HeatAPI.AMBIENT_TEMP)
                * MekanismConfig.general.evaporationTempMultiplier.get()));
        if (tempMultiplier > 0) {
            recipeCacheLookupMonitor.updateAndProcess();
        }
    }

    @Override
    public boolean getActive() {
        return !clientEnergyUsed.isZero();
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

    public BasicFluidTank getInputTank() {
        return inputTank;
    }

    public BasicFluidTank getOutputTank() {
        return outputTank;
    }

    public List<IExtendedFluidTank> getFluidTanks() {
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
        container.track(SyncableFloatingLong.create(this::getEnergyUsed, value -> clientEnergyUsed = value));
    }

    public void setEnergyUsageFromPacket(FloatingLong floatingLong) {
        energyContainer.setEnergyPerTick(floatingLong);
        energyContainer.setMaxEnergy(floatingLong.multiply(400));
        clientEnergyUsed = floatingLong;
        markForSave();
    }

    public FloatingLong getEnergyUsed() {
        return clientEnergyUsed;
    }

    public MachineEnergyContainer<BECompactTEP> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public CompoundTag getConfigurationData(Player player) {
        CompoundTag data = super.getConfigurationData(player);
        data.putString(NBTConstants.ENERGY_USAGE, energyContainer.getEnergyPerTick().toString());
        return data;
    }

    @Override
    public void setConfigurationData(Player player, CompoundTag data) {
        super.setConfigurationData(player, data);
        NBTUtils.setFloatingLongIfPresent(data, NBTConstants.ENERGY_USAGE, this::setEnergyUsageFromPacket);
    }
}
