package astral_mekanism.block.blockentity.basemachine;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.fxd927.mekanismelements.api.recipes.AdsorptionRecipe;
import com.fxd927.mekanismelements.api.recipes.cache.AdsorptionCachedRecipe;
import com.fxd927.mekanismelements.common.inventory.slot.MSInputInventorySlot;
import com.fxd927.mekanismelements.common.recipe.IMSRecipeTypeProvider;
import com.fxd927.mekanismelements.common.recipe.MSRecipeType;
import com.fxd927.mekanismelements.common.recipe.lookup.IMSDoubleRecipeLookupHandler;
import com.fxd927.mekanismelements.common.recipe.lookup.cache.MSInputRecipeCache;
import com.fxd927.mekanismelements.common.tile.prefab.MSTileEntityRecipeMachine;

import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.merged.MergedChemicalTank;
import mekanism.api.chemical.pigment.IPigmentTank;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.ISlurryTank;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.BoxedChemicalOutputHandler;
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
import mekanism.common.integration.computer.SpecialComputerMethodWrapper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.integration.computer.computercraft.ComputerConstants;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.chemical.MergedChemicalInventorySlot;
import mekanism.common.inventory.warning.WarningTracker;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.base.SubstanceType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.StatUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

import static mekanism.common.tile.machine.TileEntityPressurizedReactionChamber.NOT_ENOUGH_FLUID_INPUT_ERROR;

public abstract class BEAMEAdsorptionSeparator extends MSTileEntityRecipeMachine<AdsorptionRecipe> implements
        IMSDoubleRecipeLookupHandler.ItemFluidRecipeLookupHandler<AdsorptionRecipe> {
    private static final List<CachedRecipe.OperationTracker.RecipeError> TRACKED_ERROR_TYPES = List.of(
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY_REDUCED_RATE,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);
    private static final long MAX_CHEMICAL = Long.MAX_VALUE;

    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerChemicalTankWrapper.class, methodNames = {
            "getGasInput", "getGasInputCapacity", "getGasInputNeeded",
            "getGasInputFilledPercentage" }, docPlaceholder = "gas input tank")
    public BasicFluidTank inputTank;
    public MergedChemicalTank outputTank;
    public double injectUsage = 1;

    private final BoxedChemicalOutputHandler outputHandler;
    private final IInputHandler<@NotNull ItemStack> itemInputHandler;
    private final IInputHandler<@NotNull FluidStack> fluidInputHandler;

    private MachineEnergyContainer<BEAMEAdsorptionSeparator> energyContainer;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getInputItem", docPlaceholder = "input slot")
    protected MSInputInventorySlot inputSlot;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getOutputItem", docPlaceholder = "output slot")
    MergedChemicalInventorySlot<MergedChemicalTank> outputSlot;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getEnergyItem", docPlaceholder = "energy slot")
    EnergyInventorySlot energySlot;

    public BEAMEAdsorptionSeparator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.FLUID,
                TransmissionType.GAS, TransmissionType.INFUSION, TransmissionType.PIGMENT,
                TransmissionType.SLURRY, TransmissionType.ENERGY);
        configComponent.setupItemIOConfig(inputSlot, outputSlot, energySlot);
        configComponent.setupInputConfig(TransmissionType.FLUID, inputTank);
        configComponent.setupOutputConfig(TransmissionType.GAS, outputTank.getGasTank(), RelativeSide.RIGHT);
        configComponent.setupOutputConfig(TransmissionType.INFUSION, outputTank.getInfusionTank(), RelativeSide.RIGHT);
        configComponent.setupOutputConfig(TransmissionType.PIGMENT, outputTank.getPigmentTank(), RelativeSide.RIGHT);
        configComponent.setupOutputConfig(TransmissionType.SLURRY, outputTank.getSlurryTank(), RelativeSide.RIGHT);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);

        ejectorComponent = new TileComponentEjector(this ,() -> 0x7fffffffffffffffl);
        ejectorComponent
                .setOutputData(configComponent, TransmissionType.ITEM, TransmissionType.FLUID, TransmissionType.GAS,
                        TransmissionType.INFUSION, TransmissionType.PIGMENT,
                        TransmissionType.SLURRY)
                .setCanTankEject(tank -> tank != inputTank);

        itemInputHandler = InputHelper.getInputHandler(inputSlot,
                CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT);
        fluidInputHandler = InputHelper.getInputHandler(inputTank, NOT_ENOUGH_FLUID_INPUT_ERROR);
        outputHandler = new BoxedChemicalOutputHandler(outputTank,
                CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
    }

    @Override
    protected void presetVariables() {
        super.presetVariables();
        IContentsListener saveOnlyListener = this::markForSave;
        outputTank = MergedChemicalTank.create(
                ChemicalTankBuilder.GAS.output(MAX_CHEMICAL, getListener(SubstanceType.GAS, saveOnlyListener)),
                ChemicalTankBuilder.INFUSION.output(MAX_CHEMICAL,
                        getListener(SubstanceType.INFUSION, saveOnlyListener)),
                ChemicalTankBuilder.PIGMENT.output(MAX_CHEMICAL, getListener(SubstanceType.PIGMENT, saveOnlyListener)),
                ChemicalTankBuilder.SLURRY.output(MAX_CHEMICAL, getListener(SubstanceType.SLURRY, saveOnlyListener)));
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputTank = createInputTank(recipeCacheListener));
        return builder.build();
    }

    protected abstract BasicFluidTank createInputTank(IContentsListener recipeCacheListener);

    @NotNull
    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper
                .forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(outputTank.getGasTank());
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<InfuseType, InfusionStack, IInfusionTank> getInitialInfusionTanks(
            IContentsListener listener, IContentsListener recipeCacheListener) {
        ChemicalTankHelper<InfuseType, InfusionStack, IInfusionTank> builder = ChemicalTankHelper
                .forSideInfusionWithConfig(this::getDirection, this::getConfig);
        builder.addTank(outputTank.getInfusionTank());
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Pigment, PigmentStack, IPigmentTank> getInitialPigmentTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        ChemicalTankHelper<Pigment, PigmentStack, IPigmentTank> builder = ChemicalTankHelper
                .forSidePigmentWithConfig(this::getDirection, this::getConfig);
        builder.addTank(outputTank.getPigmentTank());
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Slurry, SlurryStack, ISlurryTank> getInitialSlurryTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        ChemicalTankHelper<Slurry, SlurryStack, ISlurryTank> builder = ChemicalTankHelper
                .forSideSlurryWithConfig(this::getDirection, this::getConfig);
        builder.addTank(outputTank.getSlurryTank());
        return builder.build();
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(inputSlot = MSInputInventorySlot.at(item -> containsRecipeAB(item, inputTank.getFluid()),
                this::containsRecipeA, recipeCacheListener, 80, 22))
                .tracksWarnings(slot -> slot.warning(WarningTracker.WarningType.NO_MATCHING_RECIPE,
                        getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT)));
        builder.addSlot(outputSlot = MergedChemicalInventorySlot.drain(outputTank, listener, 152, 55));
        builder.addSlot(
                energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 152, 14));
        outputSlot.setSlotOverlay(SlotOverlay.PLUS);
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        outputSlot.drainChemicalTanks();
        recipeCacheLookupMonitor.updateAndProcess();
    }

    @Override
    public IMSRecipeTypeProvider<AdsorptionRecipe, MSInputRecipeCache.ItemFluid<AdsorptionRecipe>> getMSRecipeType() {
        return MSRecipeType.ADSORPTION;
    }

    @Nullable
    @Override
    public AdsorptionRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(itemInputHandler, fluidInputHandler);
    }

    @NotNull
    @Override
    public CachedRecipe<AdsorptionRecipe> createNewCachedRecipe(@NotNull AdsorptionRecipe recipe, int cacheIndex) {
        return new AdsorptionCachedRecipe(recipe, recheckAllRecipeErrors, itemInputHandler, fluidInputHandler,
                () -> StatUtils.inversePoisson(injectUsage), outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setBaselineMaxOperations(this::getBaselineMaxOperations)
                .setOnFinish(this::markForSave);
    }

    protected abstract int getBaselineMaxOperations();

    public MachineEnergyContainer<BEAMEAdsorptionSeparator> getEnergyContainer() {
        return energyContainer;
    }

    @ComputerMethod(methodDescription = ComputerConstants.DESCRIPTION_GET_ENERGY_USAGE)
    FloatingLong getEnergyUsage() {
        return getActive() ? energyContainer.getEnergyPerTick() : FloatingLong.ZERO;
    }

    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerChemicalTankWrapper.class, methodNames = {
            "getOutput", "getOutputCapacity", "getOutputNeeded",
            "getOutputFilledPercentage" }, docPlaceholder = "output tank")
    IChemicalTank<?, ?> getOutputTank() {
        MergedChemicalTank.Current current = outputTank.getCurrent();
        return outputTank.getTankFromCurrent(
                current == MergedChemicalTank.Current.EMPTY ? MergedChemicalTank.Current.GAS : current);
    }
}