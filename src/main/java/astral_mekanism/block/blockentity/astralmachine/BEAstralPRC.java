package astral_mekanism.block.blockentity.astralmachine;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.PressurizedReactionRecipe;
import mekanism.api.recipes.PressurizedReactionRecipe.PressurizedReactionRecipeOutput;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.cache.PressurizedReactionCachedRecipe;
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
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.ITripleRecipeLookupHandler.ItemFluidChemicalRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.ItemFluidChemical;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BEAstralPRC extends TileEntityRecipeMachine<PressurizedReactionRecipe>
        implements ItemFluidChemicalRecipeLookupHandler<Gas, GasStack, PressurizedReactionRecipe> {
    public static final RecipeError NOT_ENOUGH_ITEM_INPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_FLUID_INPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_GAS_INPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_SPACE_ITEM_OUTPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_SPACE_GAS_OUTPUT_ERROR = RecipeError.create();
    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            NOT_ENOUGH_ITEM_INPUT_ERROR,
            NOT_ENOUGH_FLUID_INPUT_ERROR,
            NOT_ENOUGH_GAS_INPUT_ERROR,
            NOT_ENOUGH_SPACE_ITEM_OUTPUT_ERROR,
            NOT_ENOUGH_SPACE_GAS_OUTPUT_ERROR,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    private BasicFluidTank inputFluidTank;
    private IGasTank inputGasTank;
    private IGasTank outputGasTank;

    private FloatingLong recipeEnergyRequired = FloatingLong.ZERO;
    private final IOutputHandler<@NotNull PressurizedReactionRecipeOutput> outputHandler;
    private final IInputHandler<@NotNull ItemStack> itemInputHandler;
    private final IInputHandler<@NotNull FluidStack> fluidInputHandler;
    private final IInputHandler<@NotNull GasStack> gasInputHandler;
    InputInventorySlot inputSlot;
    OutputInventorySlot outputSlot;
    EnergyInventorySlot energySlot;
    private MachineEnergyContainer<BEAstralPRC> energyContainer;

    public BEAstralPRC(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
        configComponent = new TileComponentConfig(this, TransmissionType.ENERGY, TransmissionType.ITEM,
                TransmissionType.GAS, TransmissionType.FLUID);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        configComponent.setupItemIOConfig(inputSlot, outputSlot, energySlot);
        configComponent.setupIOConfig(TransmissionType.GAS, inputGasTank, outputGasTank, RelativeSide.RIGHT);
        configComponent.setupInputConfig(TransmissionType.FLUID, inputFluidTank);
        ejectorComponent = new TileComponentEjector(this, () -> Long.MAX_VALUE);
        ejectorComponent.setCanTankEject(t -> t == outputGasTank);
        itemInputHandler = InputHelper.getInputHandler(inputSlot, NOT_ENOUGH_ITEM_INPUT_ERROR);
        fluidInputHandler = InputHelper.getInputHandler(inputFluidTank, NOT_ENOUGH_FLUID_INPUT_ERROR);
        gasInputHandler = InputHelper.getInputHandler(inputGasTank, NOT_ENOUGH_GAS_INPUT_ERROR);
        outputHandler = OutputHelper.getOutputHandler(outputSlot, NOT_ENOUGH_SPACE_ITEM_OUTPUT_ERROR, outputGasTank,
                NOT_ENOUGH_SPACE_GAS_OUTPUT_ERROR);
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper
                .forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputGasTank = ChemicalTankBuilder.GAS.create(Long.MAX_VALUE,
                ChemicalTankHelper.radioactiveInputTankPredicate(() -> outputGasTank),
                (gas, automationType) -> containsRecipeCAB(inputSlot.getStack(), inputFluidTank.getFluid(), gas),
                this::containsRecipeC,
                ChemicalAttributeValidator.ALWAYS_ALLOW, recipeCacheListener));
        builder.addTank(outputGasTank = ChemicalTankBuilder.GAS.output(Long.MAX_VALUE, listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputFluidTank = BasicFluidTank.input(Integer.MAX_VALUE,
                fluid -> containsRecipeBAC(inputSlot.getStack(), fluid, inputGasTank.getStack()),
                this::containsRecipeB, recipeCacheListener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(inputSlot = InputInventorySlot.at(
                item -> containsRecipeABC(item, inputFluidTank.getFluid(), inputGasTank.getStack()),
                this::containsRecipeA,
                recipeCacheListener, 54, 35))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE,
                        getWarningCheck(NOT_ENOUGH_ITEM_INPUT_ERROR)));
        builder.addSlot(outputSlot = OutputInventorySlot.at(listener, 116, 35))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_SPACE_IN_OUTPUT,
                        getWarningCheck(NOT_ENOUGH_SPACE_ITEM_OUTPUT_ERROR)));
        builder.addSlot(
                energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 141, 17));
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
        recipeCacheLookupMonitor.updateAndProcess();
    }

    @Override
    public @NotNull CachedRecipe<PressurizedReactionRecipe> createNewCachedRecipe(
            @NotNull PressurizedReactionRecipe recipe, int index) {
        return new PressurizedReactionCachedRecipe(recipe, recheckAllRecipeErrors, itemInputHandler, fluidInputHandler,
                gasInputHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(this::getRecipeEnergyRequired, energyContainer)
                .setBaselineMaxOperations(() -> Integer.MAX_VALUE)
                .setOnFinish(this::markForSave);
    }

    @Override
    public void onCachedRecipeChanged(@Nullable CachedRecipe<PressurizedReactionRecipe> cachedRecipe, int cacheIndex) {
        super.onCachedRecipeChanged(cachedRecipe, cacheIndex);
        if (cachedRecipe == null) {
            this.recipeEnergyRequired = FloatingLong.ZERO;
        } else {
            this.recipeEnergyRequired = cachedRecipe.getRecipe().getEnergyRequired()
                    .add(MekanismConfig.usage.pressurizedReactionBase.get());
        }
    }

    public FloatingLong getRecipeEnergyRequired() {
        return recipeEnergyRequired;
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<PressurizedReactionRecipe, ItemFluidChemical<Gas, GasStack, PressurizedReactionRecipe>> getRecipeType() {
        return MekanismRecipeType.REACTION;
    }

    @Nullable
    @Override
    public PressurizedReactionRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(itemInputHandler, fluidInputHandler, gasInputHandler);
    }

    public MachineEnergyContainer<BEAstralPRC> getEnergyContainer() {
        return this.energyContainer;
    }

    public FloatingLong getEnergyUsage() {
        return getActive() ? energyContainer.getEnergyPerTick() : FloatingLong.ZERO;
    }

    public BasicFluidTank getFluidTank() {
        return inputFluidTank;
    }

    public IGasTank getInputGasTank() {
        return inputGasTank;
    }

    public IGasTank getOutputGasTank() {
        return outputGasTank;
    }

}
