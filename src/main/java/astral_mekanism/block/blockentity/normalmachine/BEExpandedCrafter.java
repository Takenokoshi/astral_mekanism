package astral_mekanism.block.blockentity.normalmachine;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.recipes.cachedRecipe.ExpandedCrafterCachedRecipe;
import astral_mekanism.recipes.input.AMInputHelper;
import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.ArrayItemFluidGas;
import astral_mekanism.recipes.lookup.AMITripleRecipeLookUpHandler.ArrayItemFluidGasRecipeLookUpHandler;
import astral_mekanism.recipes.recipe.ExpandedCrafterRecipe;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
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
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
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
import mekanism.common.inventory.slot.chemical.GasInventorySlot;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.tile.prefab.TileEntityProgressMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BEExpandedCrafter extends TileEntityProgressMachine<ExpandedCrafterRecipe>
        implements ArrayItemFluidGasRecipeLookUpHandler<ExpandedCrafterRecipe> {

    public static final RecipeError NOT_ENOUGH_ITEMS_INPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_FLUID_INPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_GAS_INPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_SPACE_OUTPUT_ERROR = RecipeError.create();
    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            NOT_ENOUGH_ITEMS_INPUT_ERROR,
            NOT_ENOUGH_FLUID_INPUT_ERROR,
            NOT_ENOUGH_GAS_INPUT_ERROR,
            NOT_ENOUGH_SPACE_OUTPUT_ERROR,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    private MachineEnergyContainer<BEExpandedCrafter> energyContainer;
    private InputInventorySlot[] inputSlots;
    private BasicFluidTank fluidTank;
    private IGasTank gasTank;
    private OutputInventorySlot outputSlot;
    private EnergyInventorySlot energySlot;
    private FluidInventorySlot fluidSlot;
    private GasInventorySlot gasSlot;
    private final IInputHandler<ItemStack[]> itemsInputHandler;
    private final IInputHandler<FluidStack> fluidInputHandler;
    private final IInputHandler<GasStack> gasInputHandler;
    private final IOutputHandler<ItemStack> outputHandler;

    public BEExpandedCrafter(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES, 100);
        itemsInputHandler = AMInputHelper.getInputHandler(inputSlots, NOT_ENOUGH_ITEMS_INPUT_ERROR);
        fluidInputHandler = InputHelper.getInputHandler(fluidTank, NOT_ENOUGH_FLUID_INPUT_ERROR);
        gasInputHandler = InputHelper.getInputHandler(gasTank, NOT_ENOUGH_GAS_INPUT_ERROR);
        outputHandler = OutputHelper.getOutputHandler(outputSlot, NOT_ENOUGH_SPACE_OUTPUT_ERROR);
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

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection,
                this::getConfig);
        inputSlots = new InputInventorySlot[25];
        for (int i = 0; i < inputSlots.length; i++) {
            builder.addSlot(inputSlots[i] = InputInventorySlot.at(recipeCacheListener,
                    44 + (i % 5) * 18, 18 + (i / 5) * 18));
        }
        builder.addSlot(outputSlot = OutputInventorySlot.at(recipeCacheListener, 170, 54));
        builder.addSlot(this.energySlot = EnergyInventorySlot.fillOrConvert(
                this.energyContainer, this::getLevel, listener, 141, 17));
        builder.addSlot(fluidSlot = FluidInventorySlot.fill(fluidTank, listener, 8, 76));
        builder.addSlot(gasSlot = GasInventorySlot.fill(gasTank, listener, 26, 76));
        return builder.build();
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(fluidTank = BasicFluidTank.input(10000,
                this::containsRecipeB, recipeCacheListener));
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper
                .forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(gasTank = ChemicalTankBuilder.GAS.input(10000,
                g -> containsRecipeC(g.getStack(1)), recipeCacheListener));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        fluidSlot.fillTank();
        gasSlot.fillTankOrConvert();
        recipeCacheLookupMonitor.updateAndProcess();
    }

    @Override
    public @NotNull CachedRecipe<ExpandedCrafterRecipe> createNewCachedRecipe(
            @NotNull ExpandedCrafterRecipe recipe, int cacheIndex) {
        return new ExpandedCrafterCachedRecipe(recipe, recheckAllRecipeErrors, itemsInputHandler, fluidInputHandler,
                gasInputHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(this::setOperatingTicks);
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<ExpandedCrafterRecipe, ArrayItemFluidGas<ExpandedCrafterRecipe>> getRecipeType() {
        return AstralMekanismRecipeTypes.EXPANDED_CRAFTER_RECIPE;
    }

    @Override
    public @Nullable ExpandedCrafterRecipe getRecipe(int arg0) {
        return findFirstRecipe(itemsInputHandler, fluidInputHandler, gasInputHandler);
    }

    private ItemStack[] getInputItems() {
        ItemStack[] result = new ItemStack[25];
        for (int i = 0; i < result.length; i++) {
            result[i] = inputSlots[i].getStack();
        }
        return result;
    }

    public MachineEnergyContainer<BEExpandedCrafter> getEnergyContainer() {
        return energyContainer;
    }

    public BasicFluidTank getFluidTank() {
        return fluidTank;
    }

    public IGasTank getGasTank() {
        return gasTank;
    }

    FloatingLong getEnergyUsage() {
        return getActive() ? energyContainer.getEnergyPerTick() : FloatingLong.ZERO;
    }

}
