package astral_mekanism.block.blockentity.appliedmachine;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.base.BEAppliedProgressMachine;
import astral_mekanism.block.blockentity.elements.aekey.AEItemKeySlot;
import astral_mekanism.block.blockentity.elements.aekey.AEKeySlot;
import astral_mekanism.block.blockentity.elements.aekey.MekanismKeySlot;
import astral_mekanism.enumexpansion.AMEUpgrade;
import astral_mekanism.generalrecipe.GeneralRecipeType;
import astral_mekanism.generalrecipe.IUnifiedRecipeTypeProvider;
import astral_mekanism.generalrecipe.cachedrecipe.EssentialSmeltingCachedRecipe;
import astral_mekanism.generalrecipe.cachedrecipe.ICachedRecipe;
import astral_mekanism.generalrecipe.lookup.cache.recipe.SingleInputGeneralRecipeCache.GeneralSingleItem;
import astral_mekanism.generalrecipe.lookup.handler.IUnifiedSingelRecipeLookupHandler;
import astral_mekanism.recipes.output.ItemInfuseOutput;
import mekanism.api.IContentsListener;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.state.BlockState;

public class BEAppliedEnergizedSmelter extends BEAppliedProgressMachine<SmeltingRecipe> implements
        IUnifiedSingelRecipeLookupHandler<ItemStack, SmeltingRecipe, GeneralSingleItem<Container, SmeltingRecipe>> {

    private AEItemKeySlot inputSlot;
    private AEItemKeySlot outputSlot;
    private MekanismKeySlot xpSlot;

    private MachineEnergyContainer<BEAppliedEnergizedSmelter> energyContainer;

    private final IInputHandler<ItemStack> inputHandler;
    private final IOutputHandler<ItemInfuseOutput> outputHandler;

    public BEAppliedEnergizedSmelter(IBlockProvider blockProvider, BlockPos pos, BlockState state,
            int baseTicksRequired) {
        super(blockProvider, pos, state, 200);
        inputHandler = inputSlot.getInputHandler(() -> 0, null, RecipeError.NOT_ENOUGH_INPUT);
        outputHandler = EssentialSmeltingCachedRecipe.merge(
                outputSlot.getOutputHandler(() -> Long.MAX_VALUE, null, RecipeError.NOT_ENOUGH_OUTPUT_SPACE),
                xpSlot.getInfusionOutputHandler(() -> Long.MAX_VALUE, null, RecipeError.NOT_ENOUGH_OUTPUT_SPACE));
    }

    @Override
    protected TileComponentConfig createConfigComponent() {
        TileComponentConfig config = new TileComponentConfig(this, TransmissionType.ENERGY);
        config.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        return config;
    }

    @Override
    protected TileComponentEjector createEjectorComponent(TileComponentConfig config) {
        return new TileComponentEjector(this).setOutputData(config);
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
    protected List<AEKeySlot<?>> getInitialAeKeySlots(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        List<AEKeySlot<?>> list = new ArrayList<>();
        list.add(inputSlot = new AEItemKeySlot(this::getMeStorage, key -> containsRecipe(key.getReadOnlyStack()),
                recipeCacheListener, 0));
        list.add(outputSlot = new AEItemKeySlot(this::getMeStorage, key -> true, listener, 1));
        list.add(xpSlot = new MekanismKeySlot(this::getMeStorage, key -> true, listener, 2));
        return list;
    }

    @Override
    public @Nullable SmeltingRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandler);
    }

    @Override
    public @NotNull ICachedRecipe<SmeltingRecipe> createNewCachedRecipe(@NotNull SmeltingRecipe recipe,
            int cacheIndex) {
        return new EssentialSmeltingCachedRecipe(recipe, recheckAllRecipeErrors, inputHandler, outputHandler,
                () -> upgradeComponent.getUpgrades(AMEUpgrade.XP.getValue()))
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setBaselineMaxOperations(this::getBaselineMaxOperations)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(this::setOperatingTicks);
    }

    @Override
    public @NotNull IUnifiedRecipeTypeProvider<SmeltingRecipe, GeneralSingleItem<Container, SmeltingRecipe>> getRecipeType() {
        return GeneralRecipeType.SMELTING;
    }

    public AEItemKeySlot getInputSlot() {
        return inputSlot;
    }

    public AEItemKeySlot getOutputSlot() {
        return outputSlot;
    }

    public MekanismKeySlot getXpSlot() {
        return xpSlot;
    }

    public MachineEnergyContainer<BEAppliedEnergizedSmelter> getEnergyContainer() {
        return energyContainer;
    }
}