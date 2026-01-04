package astral_mekanism.block.blockentity.astralfactory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.base.BlockEntityRecipeFactory;
import astral_mekanism.block.blockentity.base.FactoryGuiHelper;
import astral_mekanism.block.blockentity.interf.IEnergizedSmeltingFactory;
import astral_mekanism.generalrecipe.GeneralRecipeType;
import astral_mekanism.generalrecipe.IGeneralRecipeTypeProvider;
import astral_mekanism.generalrecipe.cachedrecipe.EssentialSmeltingCachedRecipe;
import astral_mekanism.generalrecipe.cachedrecipe.GeneralCachedRecipe;
import astral_mekanism.generalrecipe.lookup.cache.recipe.SingleInputGeneralRecipeCache.GeneralSingleItem;
import astral_mekanism.recipes.output.AMOutputHelper;
import astral_mekanism.recipes.output.ItemInfuseOutput;
import mekanism.api.IContentsListener;
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
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralEnergizedSmeltingFactory
        extends BlockEntityRecipeFactory<SmeltingRecipe, BEAstralEnergizedSmeltingFactory>
        implements IEnergizedSmeltingFactory<BEAstralEnergizedSmeltingFactory> {

    private InputInventorySlot[] inputSlots;
    private OutputInventorySlot[] outputSlots;
    private IInfusionTank infusionTank;
    private final IInputHandler<ItemStack>[] inputHandlers;
    private final IOutputHandler<ItemInfuseOutput>[] outputHandlers;

    @SuppressWarnings("unchecked")
    public BEAstralEnergizedSmeltingFactory(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES, GLOBAL_ERROR_TYPES);
        this.inputHandlers = new IInputHandler[tier.processes];
        this.outputHandlers = new IOutputHandler[tier.processes];
        for (int i = 0; i < tier.processes; i++) {
            inputHandlers[i] = InputHelper.getInputHandler(inputSlots[i], RecipeError.NOT_ENOUGH_INPUT);
            outputHandlers[i] = AMOutputHelper.getOutputHandler(outputSlots[i], null, infusionTank, null);
        }
    }

    @Override
    public @NotNull IGeneralRecipeTypeProvider<?, SmeltingRecipe, GeneralSingleItem<Container, SmeltingRecipe>> getRecipeType() {
        return GeneralRecipeType.SMELTING;
    }

    @Override
    public @Nullable SmeltingRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandlers[cacheIndex]);
    }

    @Override
    public @NotNull GeneralCachedRecipe<SmeltingRecipe> createNewCachedRecipe(@NotNull SmeltingRecipe recipe,
            int cacheIndex) {
        return new EssentialSmeltingCachedRecipe(recipe, recheckAllRecipeErrors[cacheIndex], inputHandlers[cacheIndex],
                outputHandlers[cacheIndex])
                .setErrorsChanged(errors -> errorTracker.onErrorsChanged(errors, cacheIndex))
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(active -> setActiveState(active, cacheIndex))
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setOnFinish(this::markForSave)
                .setBaselineMaxOperations(() -> 0x7fffffff);
    }

    @Override
    public MachineEnergyContainer<BEAstralEnergizedSmeltingFactory> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    protected BEAstralEnergizedSmeltingFactory getSelf() {
        return this;
    }

    @Override
    public int getWidthPerProcess() {
        return 18;
    }

    @Override
    public int getHeightPerProcess() {
        return 62;
    }

    @Override
    public int getSideSpaceWidth() {
        return 24;
    }

    @Override
    protected InventorySlotHelper addSlots(InventorySlotHelper builder, IContentsListener listener,
            IContentsListener updateSortingListener) {
        inputSlots = new InputInventorySlot[tier.processes];
        outputSlots = new OutputInventorySlot[tier.processes];
        for (int i = 0; i < tier.processes; i++) {
            int x = FactoryGuiHelper.getXofOneLine(i, tier, getWidthPerProcess(), getSideSpaceWidth());
            int y = FactoryGuiHelper.getYofOneLine(i, tier, getHeightPerProcess());
            builder.addSlot(inputSlots[i] = InputInventorySlot.at(this::containsRecipe, updateSortingListener, x, y));
            builder.addSlot(outputSlots[i] = OutputInventorySlot.at(updateSortingListener, x, y + 44));
        }
        return builder;
    }

    @Override
    protected ChemicalTankHelper<InfuseType, InfusionStack, IInfusionTank> addInfusionTanks(
            ChemicalTankHelper<InfuseType, InfusionStack, IInfusionTank> builder, IContentsListener listener,
            IContentsListener updateSortingListener) {
        builder.addTank(infusionTank = ChemicalTankBuilder.INFUSION.create(Long.MAX_VALUE, listener));
        return builder;
    }

    @Override
    public IInfusionTank getInfusionTank() {
        return infusionTank;
    }
}
