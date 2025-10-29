package astral_mekanism.block.blockentity.astralmachine;

import astral_mekanism.recipes.recipe.GreenHouseRecipe;
import astral_mekanism.registries.AstralMekanismRecipeTypes;

import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.recipes.cachedRecipe.GreenHouseCachedRecipe;
import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.ItemFluid;
import astral_mekanism.recipes.lookup.AMIDoubleRecipeLookUpHandler.ItemFluidRecipeLookupHandler;
import astral_mekanism.recipes.output.AMOutputHelper;
import astral_mekanism.recipes.output.DoubleItemStackOutput;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
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
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BEAstralGreenHouse extends TileEntityRecipeMachine<GreenHouseRecipe>
        implements ItemFluidRecipeLookupHandler<GreenHouseRecipe> {

    public static final RecipeError NOT_ENOUGH_ITEM_INPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_FLUID_INPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_SPACE_ITEMA_OUTPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_SPACE_ITEMB_OUTPUT_ERROR = RecipeError.create();
    public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            NOT_ENOUGH_FLUID_INPUT_ERROR,
            NOT_ENOUGH_FLUID_INPUT_ERROR,
            NOT_ENOUGH_SPACE_ITEMA_OUTPUT_ERROR,
            NOT_ENOUGH_SPACE_ITEMB_OUTPUT_ERROR,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    public BasicFluidTank inputFluidTank;
    InputInventorySlot inputSlot;
    OutputInventorySlot outputSlotA;
    OutputInventorySlot outputSlotB;
    MachineEnergyContainer<BEAstralGreenHouse> energyContainer;
    EnergyInventorySlot energySlot;
    FluidInventorySlot fluidSlot;

    private FloatingLong recipeEnergyRequired = FloatingLong.ZERO;
    private final IOutputHandler<DoubleItemStackOutput> outputHandler;
    private final IInputHandler<@NotNull ItemStack> itemInputHandler;
    private final IInputHandler<@NotNull FluidStack> fluidInputHandler;

    public BEAstralGreenHouse(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
        this.recipeEnergyRequired = FloatingLong.ZERO;
        this.configComponent = new TileComponentConfig(this, TransmissionType.ENERGY, TransmissionType.FLUID,
                TransmissionType.ITEM);
        this.configComponent.setupInputConfig(TransmissionType.FLUID, inputFluidTank);
        this.configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ConfigInfo itemConfig = this.configComponent.getConfig(TransmissionType.ITEM);
        if (itemConfig != null) {
            itemConfig.addSlotInfo(DataType.INPUT, new InventorySlotInfo(true, false, inputSlot));
            itemConfig.addSlotInfo(DataType.OUTPUT_1, new InventorySlotInfo(false, true, outputSlotA));
            itemConfig.addSlotInfo(DataType.OUTPUT_2, new InventorySlotInfo(false, true, outputSlotB));
            itemConfig.setEjecting(true);
        }
        this.ejectorComponent = new TileComponentEjector(this);
        this.ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);
        itemInputHandler = InputHelper.getInputHandler(inputSlot, NOT_ENOUGH_ITEM_INPUT_ERROR);
        fluidInputHandler = InputHelper.getInputHandler(inputFluidTank, NOT_ENOUGH_FLUID_INPUT_ERROR);
        outputHandler = AMOutputHelper.getOutputHandler(outputSlotA, NOT_ENOUGH_SPACE_ITEMA_OUTPUT_ERROR,
                outputSlotB,
                NOT_ENOUGH_SPACE_ITEMB_OUTPUT_ERROR);
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection,
                this::getConfig);
        builder.addSlot(inputSlot = InputInventorySlot.at(
                item -> containsRecipeAB(item, inputFluidTank.getFluid()),
                this::containsRecipeA, recipeCacheListener, 54, 40))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE,
                        getWarningCheck(NOT_ENOUGH_ITEM_INPUT_ERROR)));
        builder.addSlot(outputSlotA = OutputInventorySlot.at(recipeCacheListener, 116, 40))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_SPACE_IN_OUTPUT,
                        getWarningCheck(NOT_ENOUGH_SPACE_ITEMA_OUTPUT_ERROR)));
        builder.addSlot(outputSlotB = OutputInventorySlot.at(recipeCacheListener, 116, 60))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_SPACE_IN_OUTPUT,
                        getWarningCheck(NOT_ENOUGH_SPACE_ITEMB_OUTPUT_ERROR)));
        builder.addSlot(this.energySlot = EnergyInventorySlot.fillOrConvert(this.energyContainer,
                this::getLevel,
                listener, 141, 17));
        builder.addSlot(this.fluidSlot = FluidInventorySlot.fill(this.inputFluidTank, listener, 34, 17));
        return builder.build();
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputFluidTank = BasicFluidTank.input(Integer.MAX_VALUE,
                fluid -> containsRecipeBA(inputSlot.getStack(), fluid), this::containsRecipeB,
                recipeCacheListener));
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
        outputSlotB.setStack(inputSlot.insertItem(outputSlotB.getStack(), Action.EXECUTE, AutomationType.INTERNAL));
    }

    @Override
    public @NotNull CachedRecipe<GreenHouseRecipe> createNewCachedRecipe(@NotNull GreenHouseRecipe recipe,
            int cacheIndex) {
        CachedRecipe<GreenHouseRecipe> cachedRecipe = new GreenHouseCachedRecipe(recipe, recheckAllRecipeErrors,
                itemInputHandler,
                fluidInputHandler, outputHandler).setErrorsChanged((x$0) -> {
                    this.onErrorsChanged(x$0);
                }).setCanHolderFunction(() -> {
                    return MekanismUtils.canFunction(this);
                }).setActive(this::setActive);
        MachineEnergyContainer<BEAstralGreenHouse> energyContainer = this.energyContainer;
        Objects.requireNonNull(energyContainer);
        return cachedRecipe.setEnergyRequirements(energyContainer::getEnergyPerTick, this.energyContainer)
                .setRequiredTicks(() -> 1).setBaselineMaxOperations(() -> AstralMekanismID.Int1B)
                .setEnergyRequirements(() -> recipeEnergyRequired, energyContainer)
                .setOnFinish(this::markForSave);
    }

    @Override
    public void onCachedRecipeChanged(@Nullable CachedRecipe<GreenHouseRecipe> cachedRecipe, int cacheIndex){
        super.onCachedRecipeChanged(cachedRecipe, cacheIndex);
        if (cachedRecipe == null) {
            this.recipeEnergyRequired=FloatingLong.ZERO;
        } else {
            this.recipeEnergyRequired = cachedRecipe.getRecipe().getEnergyRequired();
        }
    }


    @Override
    public @Nullable GreenHouseRecipe getRecipe(int arg0) {
        return (GreenHouseRecipe) this.findFirstRecipe(this.itemInputHandler, this.fluidInputHandler);
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<GreenHouseRecipe, ItemFluid<GreenHouseRecipe>> getRecipeType() {
        return AstralMekanismRecipeTypes.Greenhouse_recipe;
    }

    public MachineEnergyContainer<BEAstralGreenHouse> getEnergyContainer(){
        return this.energyContainer;
    }

	public FloatingLong getEnergyUsage() {
		return this.getActive() ? this.energyContainer.getEnergyPerTick() : FloatingLong.ZERO;
	}

    public BasicFluidTank getFluidTank(){
        return inputFluidTank;
    }
}
