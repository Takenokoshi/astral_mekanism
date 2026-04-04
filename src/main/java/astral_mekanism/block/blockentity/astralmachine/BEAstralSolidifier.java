package astral_mekanism.block.blockentity.astralmachine;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.elements.energyContainer.EnergyRequiredRecipeMachineEnergyContainer;
import astral_mekanism.block.blockentity.interf.IEnergyRequiredRecipeMachine;
import fr.iglee42.evolvedmekanism.interfaces.EMInputRecipeCache;
import fr.iglee42.evolvedmekanism.interfaces.SolidificationCachedRecipe;
import fr.iglee42.evolvedmekanism.recipes.SolidificationRecipe;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.FluidSlotInfo;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BEAstralSolidifier extends TileEntityRecipeMachine<SolidificationRecipe> implements
        EMInputRecipeCache.ItemFluidFluidRecipeLookupHandler<SolidificationRecipe>, IEnergyRequiredRecipeMachine {

    public static final RecipeError NOT_ENOUGH_ITEM_INPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_FLUID_INPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_EXTRA_FLUID_INPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_SPACE_ITEM_OUTPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_SPACE_GAS_OUTPUT_ERROR = RecipeError.create();
    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            NOT_ENOUGH_ITEM_INPUT_ERROR,
            NOT_ENOUGH_FLUID_INPUT_ERROR,
            NOT_ENOUGH_EXTRA_FLUID_INPUT_ERROR,
            NOT_ENOUGH_SPACE_ITEM_OUTPUT_ERROR,
            NOT_ENOUGH_SPACE_GAS_OUTPUT_ERROR,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);
    private BasicFluidTank inputFluidTank;
    private BasicFluidTank inputFluidExtraTank;
    private FloatingLong recipeEnergyRequired = FloatingLong.ZERO;
    private final IOutputHandler<@NotNull ItemStack> outputHandler;
    private final IInputHandler<@NotNull ItemStack> itemInputHandler;
    private final IInputHandler<@NotNull FluidStack> fluidInputHandler;
    private final IInputHandler<@NotNull FluidStack> fluidExtraInputHandler;
    private EnergyRequiredRecipeMachineEnergyContainer<BEAstralSolidifier> energyContainer;
    private InputInventorySlot inputSlot;
    private OutputInventorySlot outputSlot;
    private EnergyInventorySlot energySlot;

    public BEAstralSolidifier(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.ENERGY,
                TransmissionType.FLUID);
        configComponent.setupItemIOConfig(inputSlot, outputSlot, energySlot);
        ConfigInfo fluidConfig = configComponent.getConfig(TransmissionType.FLUID);
        if (fluidConfig != null) {
            fluidConfig.addSlotInfo(DataType.INPUT_1, new FluidSlotInfo(true, false, inputFluidExtraTank));
            fluidConfig.addSlotInfo(DataType.INPUT_2, new FluidSlotInfo(true, false, inputFluidTank));
            fluidConfig.setDataType(DataType.INPUT_1, RelativeSide.LEFT);
            fluidConfig.setDataType(DataType.INPUT_2, RelativeSide.RIGHT);
            fluidConfig.setCanEject(false);
        }
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);

        itemInputHandler = new SolidiferItemInputHandler(inputSlot, NOT_ENOUGH_ITEM_INPUT_ERROR);
        fluidInputHandler = InputHelper.getInputHandler(inputFluidTank, NOT_ENOUGH_FLUID_INPUT_ERROR);
        fluidExtraInputHandler = InputHelper.getInputHandler(inputFluidExtraTank, NOT_ENOUGH_EXTRA_FLUID_INPUT_ERROR);
        outputHandler = OutputHelper.getOutputHandler(outputSlot, NOT_ENOUGH_SPACE_ITEM_OUTPUT_ERROR);
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputFluidTank = BasicFluidTank.input(0x7fffffff,
                fluid -> containsRecipeBAC(inputSlot.getStack(), fluid, inputFluidExtraTank.getFluid()),
                this::containsRecipeB, recipeCacheListener));
        builder.addTank(inputFluidExtraTank = BasicFluidTank.input(0x7fffffff,
                fluid -> containsRecipeCAB(inputSlot.getStack(), inputFluidTank.getFluid(), fluid),
                this::containsRecipeC, recipeCacheListener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(energyContainer = EnergyRequiredRecipeMachineEnergyContainer.createInput(this, listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(inputSlot = InputInventorySlot.at(
                item -> containsRecipeABC(item, inputFluidTank.getFluid(), inputFluidExtraTank.getFluid()),
                this::containsRecipeA,
                recipeCacheListener, 54, 35))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE,
                        getWarningCheck(NOT_ENOUGH_ITEM_INPUT_ERROR)));
        builder.addSlot(outputSlot = OutputInventorySlot.at(listener, 116, 35))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_SPACE_IN_OUTPUT,
                        getWarningCheck(NOT_ENOUGH_SPACE_ITEM_OUTPUT_ERROR)));
        builder.addSlot(
                energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 141, 35));
        return builder.build();
    }

    @Override
    public void onCachedRecipeChanged(@Nullable CachedRecipe<SolidificationRecipe> cachedRecipe, int cacheIndex) {
        super.onCachedRecipeChanged(cachedRecipe, cacheIndex);
        if (cachedRecipe == null) {
            recipeEnergyRequired = FloatingLong.ZERO;
        } else {
            SolidificationRecipe recipe = cachedRecipe.getRecipe();
            recipeEnergyRequired = recipe.getEnergyRequired();
        }
        energyContainer.updateEnergyPerTick();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        recipeCacheLookupMonitor.updateAndProcess();
    }

    public FloatingLong getRecipeEnergyRequired() {
        return recipeEnergyRequired;
    }

    @NotNull
    @Override
    public IMekanismRecipeTypeProvider<SolidificationRecipe, EMInputRecipeCache.ItemFluidFluid<SolidificationRecipe>> getRecipeType() {
        return EMRecipeType.SOLIDIFICATION;
    }

    @Nullable
    @Override
    public SolidificationRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(itemInputHandler, fluidInputHandler, fluidExtraInputHandler);
    }

    @NotNull
    @Override
    public CachedRecipe<SolidificationRecipe> createNewCachedRecipe(@NotNull SolidificationRecipe recipe,
            int cacheIndex) {
        return new SolidificationCachedRecipe(recipe, recheckAllRecipeErrors, itemInputHandler, fluidInputHandler,
                fluidExtraInputHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setOnFinish(this::markForSave)
                .setBaselineMaxOperations(() -> 0x7fffffff);
    }

    public EnergyRequiredRecipeMachineEnergyContainer<BEAstralSolidifier> getEnergyContainer() {
        return energyContainer;
    }

    FloatingLong getEnergyUsage() {
        return getActive() ? energyContainer.getEnergyPerTick() : FloatingLong.ZERO;
    }

    public IExtendedFluidTank getInputFluidTank() {
        return inputFluidTank;
    }

    public IExtendedFluidTank getInputFluidExtraTank() {
        return inputFluidExtraTank;
    }

    private static class SolidiferItemInputHandler implements IInputHandler<ItemStack> {

        private final IInventorySlot slot;
        private final RecipeError notEnoughError;

        private SolidiferItemInputHandler(IInventorySlot slot, RecipeError notEnoughError) {
            this.slot = slot;
            this.notEnoughError = notEnoughError;
        }

        @Override
        public ItemStack getInput() {
            return slot.getStack();
        }

        @Override
        public ItemStack getRecipeInput(InputIngredient<ItemStack> recipeIngredient) {
            return recipeIngredient.getMatchingInstance(slot.getStack());
        }

        @Override
        public void use(ItemStack recipeInput, int operations) {
        }

        @Override
        public void calculateOperationsCanSupport(OperationTracker tracker, ItemStack recipeInput,
                int usageMultiplier) {
            if (slot.isEmpty()) {
                tracker.updateOperations(0);
                tracker.addError(notEnoughError);
                return;
            }
            if (ItemStack.isSameItemSameTags(slot.getStack(), recipeInput)
                    && slot.getCount() >= recipeInput.getCount()) {
                return;
            }
            tracker.mismatchedRecipe();
            return;
        }

    }

}
