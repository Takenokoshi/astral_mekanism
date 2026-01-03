package astral_mekanism.block.blockentity.normalmachine;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.elements.slot.DrainInfusionSlot;
import astral_mekanism.block.blockentity.interf.IEssentialEnergizedSmelter;
import astral_mekanism.block.blockentity.prefab.BlockEntityProgressMachine;
import astral_mekanism.generalrecipe.GeneralRecipeType;
import astral_mekanism.generalrecipe.IGeneralRecipeTypeProvider;
import astral_mekanism.generalrecipe.cachedrecipe.EssentialSmeltingCachedRecipe;
import astral_mekanism.generalrecipe.cachedrecipe.GeneralCachedRecipe;
import astral_mekanism.generalrecipe.lookup.cache.recipe.SingleInputGeneralRecipeCache.GeneralSingleItem;
import astral_mekanism.recipes.output.AMOutputHelper;
import astral_mekanism.recipes.output.ItemInfuseOutput;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
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
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.state.BlockState;

public class BEEssentialEnergizedSmelter extends BlockEntityProgressMachine<SmeltingRecipe> implements
        IEssentialEnergizedSmelter<BEEssentialEnergizedSmelter> {

    private MachineEnergyContainer<BEEssentialEnergizedSmelter> energyContainer;
    private InputInventorySlot inputSlot;
    private OutputInventorySlot outputSlot;
    private IInfusionTank infusionTank;
    private DrainInfusionSlot infusionSlot;
    private EnergyInventorySlot energySlot;
    private final IInputHandler<ItemStack> inputHandler;
    private final IOutputHandler<ItemInfuseOutput> outputHandler;

    public BEEssentialEnergizedSmelter(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES, 200);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.INFUSION,TransmissionType.ENERGY);
        configComponent.setupItemIOExtraConfig(inputSlot, outputSlot, infusionSlot, energySlot);
        configComponent.setupOutputConfig(TransmissionType.INFUSION, infusionTank, RelativeSide.values());
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ejectorComponent = new TileComponentEjector(this, infusionTank::getCapacity);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM, TransmissionType.INFUSION);
        this.inputHandler = InputHelper.getInputHandler(inputSlot, RecipeError.NOT_ENOUGH_INPUT);
        this.outputHandler = AMOutputHelper.getOutputHandler(outputSlot, NOT_ENOUGH_ITEM_OUTPUT_SPACE, infusionTank,
                NOT_ENOUGH_INFUSE_OUTPUT_SPACE);
    }

    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection,
                this::getConfig);
        builder.addSlot(inputSlot = InputInventorySlot.at(this::containsRecipe, recipeCacheListener, 64, 17))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE,
                        getWarningCheck(RecipeError.NOT_ENOUGH_INPUT)));
        builder.addSlot(outputSlot = OutputInventorySlot.at(listener, 116, 35))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_SPACE_IN_OUTPUT,
                        getWarningCheck(NOT_ENOUGH_ITEM_OUTPUT_SPACE)));
        builder.addSlot(infusionSlot = new DrainInfusionSlot(infusionTank, listener, 116, 53));
        builder.addSlot(
                energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 64, 53));
        return builder.build();
    }

    @Override
    protected IChemicalTankHolder<InfuseType, InfusionStack, IInfusionTank> getInitialInfusionTanks(
            IContentsListener listener, IContentsListener recipeCacheListener) {
        ChemicalTankHelper<InfuseType, InfusionStack, IInfusionTank> builder = ChemicalTankHelper
                .forSideInfusionWithConfig(this::getDirection, this::getConfig);
        builder.addTank(infusionTank = ChemicalTankBuilder.INFUSION.create(Long.MAX_VALUE, listener));
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
        recipeCacheLookupMonitor.updateAndProcess();
        infusionSlot.drainTank();
    }

    @Override
    public @Nullable SmeltingRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandler);
    }

    @Override
    public @NotNull GeneralCachedRecipe<SmeltingRecipe> createNewCachedRecipe(@NotNull SmeltingRecipe recipe,
            int cacheIndex) {
        return new EssentialSmeltingCachedRecipe(recipe, recheckAllRecipeErrors, inputHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(this::setOperatingTicks);
    }

    @Override
    public @NotNull IGeneralRecipeTypeProvider<Container, SmeltingRecipe, GeneralSingleItem<Container, SmeltingRecipe>> getRecipeType() {
        return GeneralRecipeType.SMELTING;
    }

    @Override
    public IInfusionTank getInfusionTank() {
        return infusionTank;
    }

    @Override
    public MachineEnergyContainer<BEEssentialEnergizedSmelter> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public double getProgressScaled() {
        return getScaledProgress();
    }

}
