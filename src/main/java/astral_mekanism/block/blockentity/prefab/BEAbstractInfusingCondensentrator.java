package astral_mekanism.block.blockentity.prefab;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.recipes.cachedRecipe.GasInfusionToFluidCachedRecipe;
import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.GasInfusion;
import astral_mekanism.recipes.lookup.AMIRecipeLookUpHandler.GasInfusionRecipeLookUpHandler;
import astral_mekanism.recipes.recipe.GasInfusionToFluidRecipe;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.chemical.GasInventorySlot;
import mekanism.common.inventory.slot.chemical.InfusionInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.interfaces.IHasDumpButton;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public abstract class BEAbstractInfusingCondensentrator extends TileEntityRecipeMachine<GasInfusionToFluidRecipe>
        implements GasInfusionRecipeLookUpHandler<GasInfusionToFluidRecipe>,IHasDumpButton {

    public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE);

    private IGasTank gasTank;
    private IInfusionTank infusionTank;
    private BasicFluidTank outputTank;
    private MachineEnergyContainer<BEAbstractInfusingCondensentrator> energyContainer;

    private GasInventorySlot gasSlot;
    private InfusionInventorySlot infusionSlot;
    private EnergyInventorySlot energySlot;

    private final IInputHandler<GasStack> gasInputHandler;
    private final IInputHandler<InfusionStack> infusionInputHandler;
    private final IOutputHandler<FluidStack> outputHandler;

    protected BEAbstractInfusingCondensentrator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.FLUID,
                TransmissionType.GAS, TransmissionType.INFUSION, TransmissionType.ENERGY);
        ConfigInfo itemConfig = configComponent.getConfig(TransmissionType.ITEM);
        itemConfig.addSlotInfo(DataType.INPUT_1, new InventorySlotInfo(true, false, gasSlot));
        itemConfig.addSlotInfo(DataType.INPUT_2, new InventorySlotInfo(true, false, infusionSlot));
        itemConfig.addSlotInfo(DataType.ENERGY, new InventorySlotInfo(true, true, energySlot));
        configComponent.setupInputConfig(TransmissionType.GAS, gasTank);
        configComponent.setupInputConfig(TransmissionType.INFUSION, infusionTank);
        configComponent.setupOutputConfig(TransmissionType.FLUID, outputTank, RelativeSide.values());
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ejectorComponent = new TileComponentEjector(this, this::getFluidTankCapacity)
                .setOutputData(configComponent, TransmissionType.FLUID, TransmissionType.ITEM);
        gasInputHandler = InputHelper.getInputHandler(gasTank, RecipeError.NOT_ENOUGH_INPUT);
        infusionInputHandler = InputHelper.getInputHandler(infusionTank, RecipeError.NOT_ENOUGH_SECONDARY_INPUT);
        outputHandler = OutputHelper.getOutputHandler(outputTank, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection,
                this::getConfig);
        builder.addSlot(gasSlot = GasInventorySlot.fillOrConvert(gasTank, this::getLevel, listener, 69, 55));
        builder.addSlot(infusionSlot = InfusionInventorySlot.fillOrConvert(infusionTank, this::getLevel, listener,
                17, 35));
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel,
                listener, 143, 35));
        return builder.build();
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(outputTank = BasicFluidTank.output(getFluidTankCapacity(), recipeCacheListener));
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(
            IContentsListener listener, IContentsListener recipeCacheListener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper
                .forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(gasTank = ChemicalTankBuilder.GAS.create(getChemicalTankCapacity(),
                ChemicalTankBuilder.GAS.notExternal,
                (gas, automationType) -> containsRecipeAB(gas, infusionTank.getType()),
                this::containsRecipeA,
                ChemicalAttributeValidator.ALWAYS_ALLOW, recipeCacheListener));
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<InfuseType, InfusionStack, IInfusionTank> getInitialInfusionTanks(
            IContentsListener listener, IContentsListener recipeCacheListener) {
        ChemicalTankHelper<InfuseType, InfusionStack, IInfusionTank> builder = ChemicalTankHelper
                .forSideInfusionWithConfig(this::getDirection, this::getConfig);
        builder.addTank(infusionTank = ChemicalTankBuilder.INFUSION.create(getChemicalTankCapacity(),
                ChemicalTankBuilder.INFUSION.alwaysTrueBi,
                (infuseType, automationType) -> containsRecipeBA(gasTank.getType(), infuseType),
                this::containsRecipeB, recipeCacheListener));
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

    protected void onUpdateServer() {
        super.onUpdateServer();
        gasSlot.fillTankOrConvert();
        infusionSlot.fillTankOrConvert();
        energySlot.fillContainerOrConvert();
        recipeCacheLookupMonitor.updateAndProcess();
    }

    @Override
    public void dump(){
        infusionTank.setEmpty();
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<GasInfusionToFluidRecipe, GasInfusion<GasInfusionToFluidRecipe>> getRecipeType() {
        return AstralMekanismRecipeTypes.INFUSING_CONDENSE;
    }

    @Override
    public @Nullable GasInfusionToFluidRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(gasInputHandler, infusionInputHandler);
    }

    @Override
    public @NotNull CachedRecipe<GasInfusionToFluidRecipe> createNewCachedRecipe(
            @NotNull GasInfusionToFluidRecipe recipe, int cacheIndex) {
        return new GasInfusionToFluidCachedRecipe(recipe, recheckAllRecipeErrors, gasInputHandler, infusionInputHandler,
                outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setOnFinish(this::markForSave)
                .setBaselineMaxOperations(this::getBaselineMaxOperations);
    }

    protected abstract int getBaselineMaxOperations();

    protected abstract int getFluidTankCapacity();

    protected abstract long getChemicalTankCapacity();

    public BasicFluidTank getOutputTank() {
        return outputTank;
    }

    public IGasTank getGasTank() {
        return gasTank;
    }

    public IInfusionTank getInfusionTank() {
        return infusionTank;
    }

    public MachineEnergyContainer<BEAbstractInfusingCondensentrator> getEnergyContainer() {
        return energyContainer;
    }

    private boolean containsRecipeA(Gas gas) {
        return containsRecipeA(gas.getStack(1));
    }

    private boolean containsRecipeB(InfuseType infuseType) {
        return containsRecipeB(infuseType.getStack(1));
    }

    private boolean containsRecipeAB(Gas gas, InfuseType infuseType) {
        return containsRecipeAB(gas.getStack(1), infuseType.getStack(1));
    }

    private boolean containsRecipeBA(Gas gas, InfuseType infuseType) {
        return containsRecipeBA(gas.getStack(1), infuseType.getStack(1));
    }

}
