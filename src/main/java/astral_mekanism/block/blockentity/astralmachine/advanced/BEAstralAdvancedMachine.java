package astral_mekanism.block.blockentity.astralmachine.advanced;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.AstralMekanismID;
import mekanism.api.IContentsListener;
import mekanism.api.Upgrade;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.ItemStackConstantChemicalToItemStackCachedRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.ILongInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.slot.chemical.GasInventorySlot;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.lookup.IDoubleRecipeLookupHandler.ItemChemicalRecipeLookupHandler;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BEAstralAdvancedMachine
        extends TileEntityRecipeMachine<ItemStackGasToItemStackRecipe>
        implements ItemChemicalRecipeLookupHandler<Gas, GasStack, ItemStackGasToItemStackRecipe> {

    protected static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    protected InputInventorySlot inputInventorySlot;
    protected OutputInventorySlot outputInventorySlot;
    protected GasInventorySlot gasInventorySlot;
    protected EnergyInventorySlot energyInventorySlot;
    public IGasTank gasTank;
    protected MachineEnergyContainer<BEAstralAdvancedMachine> energyContainer;

    protected final IInputHandler<ItemStack> inputHandler;
    protected final IOutputHandler<ItemStack> outputHandler;
    protected final ILongInputHandler<GasStack> gasInputHandler;

    protected float gas = 1;

    private final String jeiRecipeType;

    protected BEAstralAdvancedMachine(IBlockProvider blockProvider, BlockPos pos, BlockState state,
            String jeiRecipeType) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
        this.configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.GAS,
                TransmissionType.ENERGY);
        this.configComponent.setupItemIOExtraConfig(inputInventorySlot, outputInventorySlot, gasInventorySlot,
                energyInventorySlot);
        this.configComponent.setupInputConfig(TransmissionType.GAS, gasTank);
        this.ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);

        inputHandler = InputHelper.getInputHandler(inputInventorySlot, RecipeError.NOT_ENOUGH_INPUT);
        outputHandler = OutputHelper.getOutputHandler(outputInventorySlot, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
        gasInputHandler = InputHelper.getInputHandler(gasTank, RecipeError.NOT_ENOUGH_SECONDARY_INPUT);
        gas = 1;
        this.jeiRecipeType = jeiRecipeType;
    }

    @NotNull
    @Override
    public IInventorySlotHolder getInitialInventory(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection,
                this::getConfig);
        builder.addSlot(inputInventorySlot = InputInventorySlot.at(
                item -> containsRecipeAB(item, gasTank.getStack()),
                this::containsRecipeA, recipeCacheListener, 64, 17))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE,
                        getWarningCheck(RecipeError.NOT_ENOUGH_INPUT)));
        builder.addSlot(gasInventorySlot = GasInventorySlot.fillOrConvert(gasTank, this::getLevel, listener, 64,
                53));
        builder.addSlot(outputInventorySlot = OutputInventorySlot.at(listener, 116, 35))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_SPACE_IN_OUTPUT,
                        getWarningCheck(RecipeError.NOT_ENOUGH_OUTPUT_SPACE)));
        builder.addSlot(energyInventorySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel,
                listener, 39, 35));
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper
                .forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(gasTank = ChemicalTankBuilder.GAS.create(Long.MAX_VALUE,
                ChemicalTankBuilder.GAS.notExternal,
                (gas, automationType) -> containsRecipeBA(inputInventorySlot.getStack(), gas),
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

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        this.energyInventorySlot.fillContainerOrConvert();
        this.gasInventorySlot.fillTankOrConvert();
        this.recipeCacheLookupMonitor.updateAndProcess();
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (supportsUpgrade(Upgrade.GAS)) {
            gas = 1 - 9 * upgradeComponent.getUpgrades(Upgrade.GAS) / 80;
        }
    }

    @Override
    public CachedRecipe<ItemStackGasToItemStackRecipe> createNewCachedRecipe(@NotNull ItemStackGasToItemStackRecipe recipe, int index) {

        CachedRecipe<ItemStackGasToItemStackRecipe> cachedRecipe = new ItemStackConstantChemicalToItemStackCachedRecipe<Gas, GasStack, GasStackIngredient, ItemStackGasToItemStackRecipe>(
                recipe,
                recheckAllRecipeErrors, inputHandler, gasInputHandler,
                (usedSoFar, operatingTicks) -> Math.max(1l,
                        (long) (recipe.getChemicalInput()
                                .getNeededAmount(recipe.getChemicalInput()
                                        .getRepresentations().get(0))
                                * gas)),
                (u) -> {
                }, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(() -> 1)
                .setBaselineMaxOperations(() -> AstralMekanismID.Int1B)
                .setOnFinish(this::markForSave);
        return cachedRecipe;
    }

    @Override
    public @Nullable ItemStackGasToItemStackRecipe getRecipe(int arg0) {
        return (ItemStackGasToItemStackRecipe) this.findFirstRecipe(inputHandler, gasInputHandler);
    }

    public FloatingLong getEnergyUsage() {
        return getActive() ? energyContainer.getEnergyPerTick() : FloatingLong.ZERO;
    }

    public String getJEI() {
        return this.jeiRecipeType;
    }

    public MachineEnergyContainer<BEAstralAdvancedMachine> getEnergyContainer(){
        return this.energyContainer;
    }

}
