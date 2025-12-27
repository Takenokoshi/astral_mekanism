package astral_mekanism.block.blockentity.normalmachine;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.interf.IGreenhouse;
import astral_mekanism.recipes.cachedRecipe.GreenhouseCachedRecipe;
import astral_mekanism.recipes.handler.CatalystHelper;
import astral_mekanism.recipes.handler.ICatalystHandler;
import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.ItemItemFluid;
import astral_mekanism.recipes.output.AMOutputHelper;
import astral_mekanism.recipes.output.TripleItemOutput;
import astral_mekanism.recipes.recipe.GreenhouseRecipe;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.IContentsListener;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.prefab.TileEntityProgressMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BEGreenhouse extends TileEntityProgressMachine<GreenhouseRecipe> implements IGreenhouse<BEGreenhouse> {

    public static final RecipeError NOT_ENOUGH_SEED = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_FARMLAND = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_FLUID = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_SPACE_MAIN_OUTPUT = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_SPACE_SEED_OUTPUT = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_SPACE_EXTRA_OUTPUT = RecipeError.create();
    public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_ENERGY_REDUCED_RATE,
            NOT_ENOUGH_SEED, NOT_ENOUGH_FARMLAND, NOT_ENOUGH_FLUID,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    private InputInventorySlot seedInputSlot;
    private InputInventorySlot farmlandSlot;
    private OutputInventorySlot mainOutputSlot;
    private OutputInventorySlot seedOutputSlot;
    private OutputInventorySlot extraOutputSlot;

    private EnergyInventorySlot energySlot;
    private FluidInventorySlot fluidSlot;

    private MachineEnergyContainer<BEGreenhouse> energyContainer;
    private BasicFluidTank fluidTank;

    private final ICatalystHandler<ItemStack> seedHandler;
    private final ICatalystHandler<ItemStack> farmlandHandler;
    private final IInputHandler<FluidStack> fluidHandler;
    private final IOutputHandler<TripleItemOutput> outputHandler;

    public BEGreenhouse(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES, 1000);
        configComponent = new TileComponentConfig(this,
                TransmissionType.ENERGY, TransmissionType.ITEM, TransmissionType.FLUID);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ConfigInfo info = configComponent.getConfig(TransmissionType.ITEM);
        info.addSlotInfo(DataType.INPUT_1, new InventorySlotInfo(true, false, seedInputSlot));
        info.addSlotInfo(DataType.INPUT_2, new InventorySlotInfo(true, true, farmlandSlot));
        info.addSlotInfo(DataType.OUTPUT_1, new InventorySlotInfo(false, true, mainOutputSlot));
        info.addSlotInfo(DataType.OUTPUT_2, new InventorySlotInfo(false, true, seedOutputSlot, extraOutputSlot));
        info.addSlotInfo(DataType.OUTPUT,
                new InventorySlotInfo(false, true, mainOutputSlot, seedOutputSlot, extraOutputSlot));
        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);
        configComponent.setupInputConfig(TransmissionType.FLUID, fluidTank);
        seedHandler = CatalystHelper.getCatalystHandler(seedInputSlot, NOT_ENOUGH_SEED);
        farmlandHandler = CatalystHelper.getCatalystHandler(farmlandSlot, NOT_ENOUGH_FARMLAND);
        fluidHandler = InputHelper.getInputHandler(fluidTank, NOT_ENOUGH_FLUID);
        outputHandler = AMOutputHelper.getOutputhandler(
                mainOutputSlot, NOT_ENOUGH_SPACE_MAIN_OUTPUT,
                seedOutputSlot, NOT_ENOUGH_SPACE_SEED_OUTPUT,
                extraOutputSlot, NOT_ENOUGH_SPACE_EXTRA_OUTPUT);
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection,
                this::getConfig);
        builder.addSlot(seedInputSlot = InputInventorySlot.at(
                s -> containsRecipeABC(s, farmlandSlot.getStack(), fluidTank.getFluid()),
                this::containsRecipeA, recipeCacheListener, 64, 17));
        builder.addSlot(farmlandSlot = InputInventorySlot.at(
                f -> containsRecipeBAC(seedInputSlot.getStack(), f, fluidTank.getFluid()),
                this::containsRecipeA, recipeCacheListener, 64, 53));
        builder.addSlot(mainOutputSlot = OutputInventorySlot.at(recipeCacheListener, 116, 17));
        builder.addSlot(seedOutputSlot = OutputInventorySlot.at(recipeCacheListener, 116, 35));
        builder.addSlot(extraOutputSlot = OutputInventorySlot.at(recipeCacheListener, 116, 53));
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer,
                this::getLevel, recipeCacheListener, 155, 14));
        builder.addSlot(fluidSlot = FluidInventorySlot.fill(fluidTank, recipeCacheListener, 28, 45));
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
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(fluidTank = BasicFluidTank.input(100000,
                stack -> containsRecipeCAB(seedInputSlot.getStack(), farmlandSlot.getStack(), stack),
                this::containsRecipeC, recipeCacheListener));
        return builder.build();
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<GreenhouseRecipe, ItemItemFluid<GreenhouseRecipe>> getRecipeType() {
        return AstralMekanismRecipeTypes.GREENHOUSE_RECIPE;
    }

    @Override
    public @NotNull CachedRecipe<GreenhouseRecipe> createNewCachedRecipe(@NotNull GreenhouseRecipe recipe, int index) {
        return new GreenhouseCachedRecipe(recipe, recheckAllRecipeErrors,
                seedHandler, farmlandHandler, fluidHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setOnFinish(this::markForSave)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOperatingTicksChanged(this::setOperatingTicks);
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        fluidSlot.fillTank();
        recipeCacheLookupMonitor.updateAndProcess();
    }

    @Override
    public @Nullable GreenhouseRecipe getRecipe(int arg0) {
        return findFirstRecipe(seedHandler, farmlandHandler, fluidHandler);
    }

    @Override
    public BasicFluidTank getFluidTank() {
        return fluidTank;
    }

    @Override
    public MachineEnergyContainer<BEGreenhouse> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public double getProgressScaled() {
        return getScaledProgress();
    }

    @Override
    public RecipeError notEnoughFluid() {
        return NOT_ENOUGH_FLUID;
    }

}
