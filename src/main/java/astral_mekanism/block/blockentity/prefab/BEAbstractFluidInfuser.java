package astral_mekanism.block.blockentity.prefab;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.elements.AstralMekDataType;
import astral_mekanism.block.blockentity.elements.ExtendedComponentEjector;
import astral_mekanism.recipes.cachedRecipe.FluidFluidToFluidCachedRecipe;
import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.FluidFluid;
import astral_mekanism.recipes.lookup.AMIRecipeLookUpHandler;
import astral_mekanism.recipes.recipe.FluidFluidToFluidRecipe;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.IContentsListener;
import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableFloatingLong;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.FluidSlotInfo;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public abstract class BEAbstractFluidInfuser extends TileEntityRecipeMachine<FluidFluidToFluidRecipe>
        implements AMIRecipeLookUpHandler.FluidFluidRecipeLookupHandler<FluidFluidToFluidRecipe> {
    public static final RecipeError NOT_ENOUGH_FLUIDA_INPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_FLUIDB_INPUT_ERROR = RecipeError.create();
    public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY, NOT_ENOUGH_FLUIDA_INPUT_ERROR, NOT_ENOUGH_FLUIDB_INPUT_ERROR,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    private BasicFluidTank inputTankA;
    private BasicFluidTank inputTankB;
    private BasicFluidTank outputTank;
    private MachineEnergyContainer<BEAbstractFluidInfuser> energyContainer;
    private FluidInventorySlot inputSlotA;
    private FluidInventorySlot inputSlotB;
    private FluidInventorySlot outputSlot;
    private EnergyInventorySlot energySlot;
    private final IInputHandler<FluidStack> inputHandlerA;
    private final IInputHandler<FluidStack> inputHandlerB;
    private final IOutputHandler<FluidStack> outputHandler;
    private FloatingLong clientEnergyUsed = FloatingLong.ZERO;

    protected BEAbstractFluidInfuser(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
        this.configComponent = new TileComponentConfig(this, TransmissionType.ENERGY, TransmissionType.FLUID);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ConfigInfo fluidConfig = configComponent.getConfig(TransmissionType.FLUID);
        if (fluidConfig != null) {
            fluidConfig.addSlotInfo(DataType.INPUT, new FluidSlotInfo(true, false, inputTankA, inputTankB));
            fluidConfig.addSlotInfo(DataType.INPUT_1, new FluidSlotInfo(true, false, inputTankA));
            fluidConfig.addSlotInfo(DataType.INPUT_2, new FluidSlotInfo(true, false, inputTankB));
            fluidConfig.addSlotInfo(DataType.OUTPUT, new FluidSlotInfo(false, true, outputTank));
            fluidConfig.addSlotInfo(DataType.INPUT_OUTPUT,
                    new FluidSlotInfo(true, true, inputTankA, inputTankB, outputTank));
            fluidConfig.addSlotInfo(AstralMekDataType.INPUT1_OUTPUT,
                    new FluidSlotInfo(true, true, inputTankA, outputTank));
            fluidConfig.addSlotInfo(AstralMekDataType.INPUT2_OUTPUT,
                    new FluidSlotInfo(true, true, inputTankB, outputTank));
            fluidConfig.setCanEject(true);
        }
        this.ejectorComponent = new ExtendedComponentEjector(this, () -> 0x7fffffff)
                .setOutputData(configComponent, TransmissionType.FLUID)
                .setCanFluidTankEject((tank, type) -> {
                    return tank == outputTank
                            ? type == DataType.OUTPUT
                                    || type == DataType.INPUT_OUTPUT
                                    || type == AstralMekDataType.INPUT1_OUTPUT
                                    || type == AstralMekDataType.INPUT2_OUTPUT
                            : false;
                });
        inputHandlerA = InputHelper.getInputHandler(inputTankA, NOT_ENOUGH_FLUIDA_INPUT_ERROR);
        inputHandlerB = InputHelper.getInputHandler(inputTankB, NOT_ENOUGH_FLUIDB_INPUT_ERROR);
        outputHandler = OutputHelper.getOutputHandler(outputTank, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection,
                this::getConfig);
        builder.addSlot(inputSlotA = FluidInventorySlot.fill(inputTankA, listener, 6, 56));
        builder.addSlot(inputSlotB = FluidInventorySlot.fill(inputTankB, listener, 154, 56));
        builder.addSlot(outputSlot = FluidInventorySlot.drain(outputTank, listener, 80, 65));
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel,
                listener, 154, 14));
        inputSlotA.setSlotType(ContainerSlotType.INPUT);
        inputSlotB.setSlotType(ContainerSlotType.INPUT);
        outputSlot.setSlotType(ContainerSlotType.OUTPUT);
        inputSlotA.setSlotOverlay(SlotOverlay.MINUS);
        inputSlotB.setSlotOverlay(SlotOverlay.MINUS);
        outputSlot.setSlotOverlay(SlotOverlay.PLUS);
        return builder.build();
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputTankA = BasicFluidTank.input(getFluidTankCapacity(),
                fluid -> containsRecipe(fluid, inputTankB.getFluid()), this::containsRecipe,
                recipeCacheListener));
        builder.addTank(inputTankB = BasicFluidTank.input(getFluidTankCapacity(),
                fluid -> containsRecipe(inputTankA.getFluid(), fluid), this::containsRecipe,
                recipeCacheListener));
        builder.addTank(outputTank = BasicFluidTank.output(getFluidTankCapacity(), recipeCacheListener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection,
                this::getConfig);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        inputSlotA.fillTank(inputSlotA);
        inputSlotB.fillTank(inputSlotB);
        outputSlot.drainTank(outputSlot);
        clientEnergyUsed = recipeCacheLookupMonitor.updateAndProcess(energyContainer);
    }

    @Override
    public @NotNull CachedRecipe<FluidFluidToFluidRecipe> createNewCachedRecipe(
            @NotNull FluidFluidToFluidRecipe recipe, int cacheIndex) {
        return new FluidFluidToFluidCachedRecipe(recipe,
                recheckAllRecipeErrors, inputHandlerA, inputHandlerB, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setOnFinish(this::markForSave)
                .setBaselineMaxOperations(this::getBaselineMaxOperations);
    }

    @Override
    public @Nullable FluidFluidToFluidRecipe getRecipe(int arg0) {
        return (FluidFluidToFluidRecipe) this.findFirstRecipe(inputHandlerA, inputHandlerB);
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<FluidFluidToFluidRecipe, FluidFluid<FluidFluidToFluidRecipe>> getRecipeType() {
        return AstralMekanismRecipeTypes.FLUID_INFUSER_RECIPE;
    }

    protected abstract int getBaselineMaxOperations();

    protected abstract int getFluidTankCapacity();

    public FloatingLong getEnergyUsed() {
        return clientEnergyUsed;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableFloatingLong.create(this::getEnergyUsed, value -> clientEnergyUsed = value));
    }

    public IExtendedFluidTank getInputTankA() {
        return inputTankA;
    }

    public IExtendedFluidTank getInputTankB() {
        return inputTankB;
    }

    public IExtendedFluidTank getOutputTank() {
        return outputTank;
    }

    public MachineEnergyContainer<BEAbstractFluidInfuser> getEnergyContainer(){
        return energyContainer;
    }

}
