package astral_mekanism.block.blockentity.normalmachine;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.interf.IMekanicalTransformer;
import astral_mekanism.recipes.cachedRecipe.MekanicalTransformCachedRecipe;
import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.QuadItem;
import astral_mekanism.recipes.output.AMOutputHelper;
import astral_mekanism.recipes.output.ItemFluidOutput;
import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.math.FloatingLong;
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
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.prefab.TileEntityProgressMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BEMekanicalTransformer extends TileEntityProgressMachine<MekanicalTransformRecipe>
        implements IMekanicalTransformer<BEMekanicalTransformer> {

    public static final RecipeError NOT_ENOUGH_INPUT_A = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_INPUT_B = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_INPUT_C = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_INPUT_D = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_SPACE_ITEM_OUTPUT = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_SPACE_FLUID_OUTPUT = RecipeError.create();

    public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_ENERGY_REDUCED_RATE,
            NOT_ENOUGH_INPUT_A,
            NOT_ENOUGH_INPUT_B,
            NOT_ENOUGH_INPUT_C,
            NOT_ENOUGH_INPUT_D,
            NOT_ENOUGH_SPACE_ITEM_OUTPUT,
            NOT_ENOUGH_SPACE_FLUID_OUTPUT,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    private InputInventorySlot inputSlotA;
    private InputInventorySlot inputSlotB;
    private InputInventorySlot inputSlotC;
    private InputInventorySlot inputSlotD;
    private OutputInventorySlot outputSlot;
    private EnergyInventorySlot energySlot;
    private FluidInventorySlot fluidSlot;

    private MachineEnergyContainer<BEMekanicalTransformer> energyContainer;
    private BasicFluidTank fluidTank;

    private final IInputHandler<ItemStack> inputHandlerA;
    private final IInputHandler<ItemStack> inputHandlerB;
    private final IInputHandler<ItemStack> inputHandlerC;
    private final IInputHandler<ItemStack> inputHandlerD;
    private final IOutputHandler<ItemFluidOutput> outputHandler;

    public BEMekanicalTransformer(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES, 40);
        configComponent = new TileComponentConfig(this,
                TransmissionType.ENERGY, TransmissionType.ITEM, TransmissionType.FLUID);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        configComponent.setupItemIOConfig(
                List.<IInventorySlot>of(inputSlotA, inputSlotB, inputSlotC, inputSlotD),
                List.<IInventorySlot>of(outputSlot), energySlot, false)
                .addSlotInfo(DataType.EXTRA, new InventorySlotInfo(true, true, fluidSlot));
        configComponent.setupOutputConfig(TransmissionType.FLUID, fluidTank, RelativeSide.values());
        ejectorComponent = new TileComponentEjector(this, () -> 0, () -> 0x7fffffff, () -> FloatingLong.ZERO);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM, TransmissionType.FLUID);
        inputHandlerA = InputHelper.getInputHandler(inputSlotA, NOT_ENOUGH_INPUT_A);
        inputHandlerB = InputHelper.getInputHandler(inputSlotB, NOT_ENOUGH_INPUT_B);
        inputHandlerC = InputHelper.getInputHandler(inputSlotC, NOT_ENOUGH_INPUT_C);
        inputHandlerD = InputHelper.getInputHandler(inputSlotD, NOT_ENOUGH_INPUT_D);
        outputHandler = AMOutputHelper.getOutputHandler(outputSlot, NOT_ENOUGH_SPACE_ITEM_OUTPUT,
                fluidTank, NOT_ENOUGH_SPACE_FLUID_OUTPUT);
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
        builder.addSlot(inputSlotA = InputInventorySlot.at(
                stack -> containsRecipeABCD(stack, inputSlotB.getStack(), inputSlotC.getStack(), inputSlotD.getStack()),
                this::containsRecipeA, recipeCacheListener, 10, 35))
                .tracksWarnings(
                        slot -> slot.warning(WarningType.NO_MATCHING_RECIPE, getWarningCheck(NOT_ENOUGH_INPUT_A)));
        builder.addSlot(inputSlotB = InputInventorySlot.at(
                stack -> containsRecipeBACD(inputSlotA.getStack(), stack, inputSlotC.getStack(), inputSlotD.getStack()),
                this::containsRecipeB, recipeCacheListener, 28, 35))
                .tracksWarnings(
                        slot -> slot.warning(WarningType.NO_MATCHING_RECIPE, getWarningCheck(NOT_ENOUGH_INPUT_B)));
        builder.addSlot(inputSlotC = InputInventorySlot.at(
                stack -> containsRecipeCABD(inputSlotA.getStack(), inputSlotB.getStack(), stack, inputSlotD.getStack()),
                this::containsRecipeC, recipeCacheListener, 46, 35))
                .tracksWarnings(
                        slot -> slot.warning(WarningType.NO_MATCHING_RECIPE, getWarningCheck(NOT_ENOUGH_INPUT_C)));
        builder.addSlot(inputSlotD = InputInventorySlot.at(
                stack -> containsRecipeDABC(inputSlotA.getStack(), inputSlotB.getStack(), inputSlotC.getStack(), stack),
                this::containsRecipeD, recipeCacheListener, 64, 35))
                .tracksWarnings(
                        slot -> slot.warning(WarningType.NO_MATCHING_RECIPE, getWarningCheck(NOT_ENOUGH_INPUT_D)));
        builder.addSlot(outputSlot = OutputInventorySlot.at(recipeCacheListener, 116, 35));
        builder.addSlot(
                energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 155, 14));
        builder.addSlot(fluidSlot = FluidInventorySlot.drain(fluidTank, listener, 134, 53));
        return builder.build();
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(fluidTank = BasicFluidTank.output(0x7fffffff, listener));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        recipeCacheLookupMonitor.updateAndProcess();
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<MekanicalTransformRecipe, QuadItem<MekanicalTransformRecipe>> getRecipeType() {
        return AstralMekanismRecipeTypes.MEKANICAL_TRANSFORM;
    }

    @Override
    public @NotNull CachedRecipe<MekanicalTransformRecipe> createNewCachedRecipe(@NotNull MekanicalTransformRecipe recipe, int arg1) {
        return new MekanicalTransformCachedRecipe(recipe, recheckAllRecipeErrors,
                inputHandlerA, inputHandlerB, inputHandlerC, inputHandlerD, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(this::setOperatingTicks);
    }

    @Override
    public @Nullable MekanicalTransformRecipe getRecipe(int arg0) {
        return findFirstRecipe(inputHandlerA, inputHandlerB, inputHandlerC, inputHandlerD);
    }

    @Override
    public BasicFluidTank getFluidTank() {
        return fluidTank;
    }

    @Override
    public MachineEnergyContainer<BEMekanicalTransformer> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public double getProgressScaled() {
        return getScaledProgress();
    }

}
