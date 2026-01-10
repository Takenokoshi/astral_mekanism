package astral_mekanism.block.blockentity.normalmachine;

import java.util.List;
import mekanism.api.IContentsListener;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.CombinerRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.cache.TwoInputCachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.integration.computer.computercraft.ComputerConstants;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.IDoubleRecipeLookupHandler.DoubleItemRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.DoubleItem;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityProgressMachine;
import mekanism.common.upgrade.CombinerUpgradeData;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.recipes.handler.CatalystHelper;
import astral_mekanism.recipes.handler.ICatalystHandler;
import astral_mekanism.registries.AstralMekanismRecipeTypes;

public class OldBEMekanicalInscriber extends TileEntityProgressMachine<CombinerRecipe>
        implements DoubleItemRecipeLookupHandler<CombinerRecipe> {

    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);
    private final IOutputHandler<@NotNull ItemStack> outputHandler;
    private final IInputHandler<@NotNull ItemStack> inputHandler;
    private final ICatalystHandler<@NotNull ItemStack> catalystHandler;

    private MachineEnergyContainer<OldBEMekanicalInscriber> energyContainer;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getMainInput", docPlaceholder = "main input slot")
    InputInventorySlot inputSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getSecondaryInput", docPlaceholder = "secondary input slot")
    InputInventorySlot catalystSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getOutput", docPlaceholder = "output slot")
    OutputInventorySlot outputSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getEnergyItem", docPlaceholder = "energy slot")
    EnergyInventorySlot energySlot;

    public OldBEMekanicalInscriber(IBlockProvider blockProvider,BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES, 200);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.ENERGY);
        configComponent.setupItemIOExtraConfig(inputSlot, outputSlot, catalystSlot, energySlot);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);

        inputHandler = InputHelper.getInputHandler(inputSlot, RecipeError.NOT_ENOUGH_INPUT);
        catalystHandler = CatalystHelper.getCatalystHandler(catalystSlot, RecipeError.NOT_ENOUGH_SECONDARY_INPUT);
        outputHandler = OutputHelper.getOutputHandler(outputSlot, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
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
        builder.addSlot(inputSlot = InputInventorySlot.at(item -> containsRecipeAB(item, catalystSlot.getStack()),
                this::containsRecipeA, recipeCacheListener,
                64, 17))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE,
                        getWarningCheck(RecipeError.NOT_ENOUGH_INPUT)));
        builder.addSlot(catalystSlot = InputInventorySlot.at(item -> containsRecipeBA(inputSlot.getStack(), item),
                this::containsRecipeB, recipeCacheListener,
                64, 53))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE,
                        getWarningCheck(RecipeError.NOT_ENOUGH_SECONDARY_INPUT)));
        builder.addSlot(outputSlot = OutputInventorySlot.at(listener, 116, 35))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_SPACE_IN_OUTPUT,
                        getWarningCheck(RecipeError.NOT_ENOUGH_OUTPUT_SPACE)));
        builder.addSlot(
                energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 39, 35));
        catalystSlot.setSlotType(ContainerSlotType.EXTRA);
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        recipeCacheLookupMonitor.updateAndProcess();
    }

    @NotNull
    @Override
    public IMekanismRecipeTypeProvider<CombinerRecipe, DoubleItem<CombinerRecipe>> getRecipeType() {
        return AstralMekanismRecipeTypes.MEKANICAL_INSCRIBER_RECIPE;
    }

    @Nullable
    @Override
    public CombinerRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandler, catalystHandler);
    }

    @NotNull
    @Override
    public CachedRecipe<CombinerRecipe> createNewCachedRecipe(@NotNull CombinerRecipe recipe, int cacheIndex) {
        return TwoInputCachedRecipe
                .combiner(recipe, recheckAllRecipeErrors, inputHandler, catalystHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(this::setOperatingTicks);
    }

    @NotNull
    @Override
    public CombinerUpgradeData getUpgradeData() {
        return new CombinerUpgradeData(redstone, getControlType(), getEnergyContainer(), getOperatingTicks(),
                energySlot, catalystSlot, inputSlot, outputSlot, getComponents());
    }

    public MachineEnergyContainer<OldBEMekanicalInscriber> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public boolean isConfigurationDataCompatible(BlockEntityType<?> tileType) {
        return super.isConfigurationDataCompatible(tileType)
                || MekanismUtils.isSameTypeFactory(getBlockType(), tileType);
    }

    @ComputerMethod(methodDescription = ComputerConstants.DESCRIPTION_GET_ENERGY_USAGE)
    FloatingLong getEnergyUsage() {
        return getActive() ? energyContainer.getEnergyPerTick() : FloatingLong.ZERO;
    }
}