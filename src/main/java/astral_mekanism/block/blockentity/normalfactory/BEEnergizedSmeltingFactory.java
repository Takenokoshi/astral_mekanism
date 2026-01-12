package astral_mekanism.block.blockentity.normalfactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.base.BlockEntityProgressFactory;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedInputInventorySlot;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedOutputInventorySlot;
import astral_mekanism.block.blockentity.interf.IEnergizedSmeltingFactory;
import astral_mekanism.block.blockentity.interf.IEssentialEnergizedSmelter;
import astral_mekanism.generalrecipe.GeneralRecipeType;
import astral_mekanism.generalrecipe.IUnifiedRecipeTypeProvider;
import astral_mekanism.generalrecipe.cachedrecipe.EssentialSmeltingCachedRecipe;
import astral_mekanism.generalrecipe.cachedrecipe.GeneralCachedRecipe;
import astral_mekanism.generalrecipe.lookup.cache.recipe.SingleInputGeneralRecipeCache.GeneralSingleItem;
import astral_mekanism.recipes.output.AMOutputHelper;
import astral_mekanism.recipes.output.ItemInfuseOutput;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mekanism.api.IContentsListener;
import mekanism.api.NBTConstants;
import mekanism.api.RelativeSide;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableEnum;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.TileEntityChemicalTank.GasMode;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NBTUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.state.BlockState;

public class BEEnergizedSmeltingFactory extends BlockEntityProgressFactory<SmeltingRecipe, BEEnergizedSmeltingFactory>
        implements IEnergizedSmeltingFactory<BEEnergizedSmeltingFactory> {

    private PagedInputInventorySlot[] inputSlots;
    private PagedOutputInventorySlot[] outputSlots;
    private IInfusionTank infusionTank;
    private final IInputHandler<ItemStack>[] inputHandlers;
    private final IOutputHandler<ItemInfuseOutput>[] outputHandlers;
    private GasMode gasMode;

    @SuppressWarnings("unchecked")
    public BEEnergizedSmeltingFactory(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, 200, TRACKED_ERROR_TYPES, GLOBAL_ERROR_TYPES);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.INFUSION,
                TransmissionType.ENERGY);
        configComponent.setupItemIOConfig(Arrays.asList(inputSlots), Arrays.asList(outputSlots), energySlot, false);
        configComponent.setupOutputConfig(TransmissionType.INFUSION, infusionTank, RelativeSide.values());
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ejectorComponent = new TileComponentEjector(this, () -> Long.MAX_VALUE);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM, TransmissionType.INFUSION);
        this.inputHandlers = new IInputHandler[tier.processes];
        this.outputHandlers = new IOutputHandler[tier.processes];
        for (int i = 0; i < tier.processes; i++) {
            inputHandlers[i] = InputHelper.getInputHandler(inputSlots[i], RecipeError.NOT_ENOUGH_INPUT);
            outputHandlers[i] = AMOutputHelper.getOutputHandler(outputSlots[i],
                    IEssentialEnergizedSmelter.NOT_ENOUGH_ITEM_OUTPUT_SPACE, infusionTank,
                    IEssentialEnergizedSmelter.NOT_ENOUGH_INFUSE_OUTPUT_SPACE);
        }
        gasMode = GasMode.IDLE;
    }

    @Override
    public @NotNull IUnifiedRecipeTypeProvider<SmeltingRecipe, GeneralSingleItem<Container, SmeltingRecipe>> getRecipeType() {
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
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(getProgressSetter(cacheIndex));
    }

    @Override
    public MachineEnergyContainer<BEEnergizedSmeltingFactory> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public BEEnergizedSmeltingFactory getSelf() {
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
        return 36;
    }

    @Override
    protected InventorySlotHelper addSlots(InventorySlotHelper builder, IContentsListener listener,
            IContentsListener updateSortingListener) {
        inputSlots = new PagedInputInventorySlot[tier.processes];
        outputSlots = new PagedOutputInventorySlot[tier.processes];
        for (int i = 0; i < tier.processes; i++) {
            int index = i;
            int x = getXByIndex(index);
            int y = getY();
            int page = getPageByIndex(index);
            builder.addSlot(inputSlots[i] = PagedInputInventorySlot.at(this::containsRecipe, () -> {
                updateSortingListener.onContentsChanged();
                recipeCacheLookupMonitors[index].onChange();
            }, x, y, page));
            builder.addSlot(outputSlots[i] = PagedOutputInventorySlot.at(updateSortingListener, x, y + 44, page));
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

    @Override
    public void nextMode(int tank) {
        gasMode = gasMode.getNext();
        markForSave();
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableEnum.create(GasMode::byIndexStatic, GasMode.IDLE, this::getGasMode, v -> gasMode = v));
    }

    @Override
    public GasMode getGasMode() {
        return gasMode;
    }

    @Override
    public void writeSustainedData(CompoundTag dataMap) {
        NBTUtils.writeEnum(dataMap, NBTConstants.DUMP_MODE, gasMode);
    }

    @Override
    public void readSustainedData(CompoundTag dataMap) {
        NBTUtils.setEnumIfPresent(dataMap, NBTConstants.DUMP_MODE, GasMode::byIndexStatic, v -> gasMode = v);
    }

    @Override
    public Map<String, String> getTileDataRemap() {
        Map<String, String> remap = new Object2ObjectOpenHashMap<>();
        remap.put(NBTConstants.DUMP_MODE, NBTConstants.DUMP_MODE);
        return remap;
    }

    @Override
    protected void sort() {
        PagedInputInventorySlot manySlot = Arrays.stream(inputSlots).reduce(inputSlots[0],
                (a, b) -> a.getCount() > b.getCount() ? a : b);
        if (manySlot.isEmpty()) {
            return;
        }
        List<PagedInputInventorySlot> emptySlots = Arrays.stream(inputSlots).filter(IInventorySlot::isEmpty).toList();
        if (emptySlots.isEmpty()) {
            return;
        }
        List<PagedInputInventorySlot> targetSlots = new ArrayList<>(emptySlots);
        targetSlots.add(0, manySlot);
        ItemStack stack = manySlot.getStack().copy();
        int size = targetSlots.size();
        int base = stack.getCount() / size;
        int left = stack.getCount() % size;
        for (int index = 0; index < size; index++) {
            targetSlots.get(index).setStack(stack.copyWithCount(index < left ? base + 1 : base));
        }
    }

}
