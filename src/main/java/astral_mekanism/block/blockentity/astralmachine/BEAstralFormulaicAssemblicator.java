package astral_mekanism.block.blockentity.astralmachine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.interf.IEnergizedMachine;
import astral_mekanism.block.blockentity.interf.IHasCustomSizeContainer;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.list.SyncableStringList;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralFormulaicAssemblicator extends TileEntityConfigurableMachine
        implements IEnergizedMachine<BEAstralFormulaicAssemblicator>,
        IHasCustomSizeContainer {

    private static final String NBT_SAVED_RECIPE_NAMESPACE = "saved_recipe_namespace";
    private static final String NBT_SAVED_RECIPE_PATH = "saved_recipe_path";
    private static final String NBT_RECIPE_ISNULL = "saved_recipe_isnull";
    public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    private static List<CraftingRecipe> craftingRecipes = List.of();

    private IInventorySlot[] inputSlots;
    private OutputInventorySlot outputSlot;
    private IInventorySlot[] secondaryOutputSlots;

    private MachineEnergyContainer<BEAstralFormulaicAssemblicator> energyContainer;
    private EnergyInventorySlot energySlot;

    @Nullable
    public CraftingRecipe savedRecipe;
    private final Ingredient[] savedIngredients;
    private ItemStack savedOutputItem;
    private final ItemStack[] savedRemainingItems;
    private final boolean[] marksRemainingItemChange;
    private final boolean[] marksNeedSorting;
    private FloatingLong lastEnergyUsed;
    private String savedRecipeNameSpace;
    private String savedRecipePath;
    private boolean tryingLoadRecipe;

    public BEAstralFormulaicAssemblicator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        List<IInventorySlot> outputSlots = new ArrayList<>();
        outputSlots.add(outputSlot);
        outputSlots.addAll(Arrays.asList(secondaryOutputSlots));
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.ENERGY);
        configComponent.setupItemIOConfig(List.<IInventorySlot>of(inputSlots), outputSlots, energySlot, false);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ejectorComponent = new TileComponentEjector(this).setOutputData(configComponent, TransmissionType.ITEM);
        savedRecipe = null;
        savedIngredients = new Ingredient[9];
        Arrays.fill(savedIngredients, Ingredient.EMPTY);
        savedOutputItem = ItemStack.EMPTY;
        savedRemainingItems = new ItemStack[9];
        Arrays.fill(savedRemainingItems, ItemStack.EMPTY);
        marksRemainingItemChange = new boolean[9];
        Arrays.fill(marksRemainingItemChange, false);
        marksNeedSorting = new boolean[9];
        Arrays.fill(marksNeedSorting, false);
        lastEnergyUsed = FloatingLong.ZERO;
        savedRecipeNameSpace = "";
        savedRecipePath = "";
        tryingLoadRecipe = false;
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        inputSlots = new IInventorySlot[9];
        secondaryOutputSlots = new IInventorySlot[9];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                builder.addSlot(inputSlots[x + y * 3] = createInputSlot(x, y));
            }
        }
        builder.addSlot(outputSlot = OutputInventorySlot.at(listener, 116, 35));
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                builder.addSlot(
                        secondaryOutputSlots[x + y * 3] = OutputInventorySlot.at(listener, x * 18 + 140, y * 18 + 17));
            }
        }
        builder.addSlot(
                energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 201, 17));
        return builder.build();
    }

    private IInventorySlot createInputSlot(int x, int y) {
        return InputInventorySlot.at(stack -> {
            int index = x + y * 3;
            return savedIngredients[index].isEmpty() ? false : savedIngredients[index].test(stack);
        }, stack -> !stack.isEmpty(),
                () -> {
                    marksRemainingItemChange[x + y * 3] = true;
                    markForSave();
                }, x * 18 + 28, y * 18 + 17);
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        if (tryingLoadRecipe) {
            tryLoadRecipe();
        }
        energySlot.fillContainerOrConvert();
        if (MekanismUtils.canFunction(this)) {
            runSort();
            setRemainingItems();
            setActive(runCraft());
        } else {
            setActive(false);
        }
    }

    private void runSort() {
        for (int p = 0; p < 9; p++) {
            marksNeedSorting[p] = savedIngredients[p].isEmpty();
        }
        for (int i = 0; i < 9; i++) {
            if (marksNeedSorting[i]) {
                continue;
            }
            ItemStack stack = inputSlots[i].getStack();
            if (stack.isEmpty()) {
                continue;
            }
            marksNeedSorting[i] = true;
            if (stack.getMaxStackSize() < 2) {
                continue;
            }
            List<IInventorySlot> targetSlots = new ArrayList<>();
            targetSlots.add(inputSlots[i]);
            long totalAmount = stack.getCount();
            for (int j = 0; j < 9; j++) {
                if (i != j && !marksNeedSorting[j] && savedIngredients[j].test(stack)) {
                    if (inputSlots[j].isEmpty() || ItemStack.isSameItemSameTags(stack, inputSlots[j].getStack())) {
                        targetSlots.add(inputSlots[j]);
                        marksNeedSorting[j] = true;
                        totalAmount += inputSlots[j].getCount();
                    }
                }
            }
            int size = targetSlots.size();
            int amountPerStack = (int) (totalAmount / size);
            int left = (int) (totalAmount % size);
            for (int k = 0; k < size; k++) {
                targetSlots.get(k).setStack(stack.copyWithCount(k < left ? amountPerStack + 1 : amountPerStack));
            }
        }
    }

    private void setRemainingItems() {
        for (int i = 0; i < 9; i++) {
            if (marksRemainingItemChange[i]) {
                savedRemainingItems[i] = inputSlots[i].isEmpty() ? ItemStack.EMPTY
                        : inputSlots[i].getStack().getCraftingRemainingItem();
                marksRemainingItemChange[i] = false;
            }
        }
    }

    private boolean runCraft() {
        if (savedRecipe == null) {
            return false;
        }
        int operations = 0x7fffffff;
        if (outputSlot.isEmpty() || ItemStack.isSameItemSameTags(savedOutputItem, outputSlot.getStack())) {
            operations = Math.min(operations,
                    (savedOutputItem.getMaxStackSize() - outputSlot.getCount()) / savedOutputItem.getCount());
        } else {
            return false;
        }
        FloatingLong energyPerCraft = MekanismConfig.usage.formulaicAssemblicator.getOrDefault();
        operations = Math.min(operations, energyContainer.getEnergy().divideToInt(energyPerCraft));
        for (int i = 0; i < 9; i++) {
            if (savedIngredients[i].isEmpty()) {
                if (!inputSlots[i].isEmpty()) {
                    return false;
                }
            } else if (inputSlots[i].isEmpty() || !savedIngredients[i].test(inputSlots[i].getStack())) {
                return false;
            } else {
                operations = Math.min(operations, inputSlots[i].getCount());
            }
            if (!savedRemainingItems[i].isEmpty()) {
                if (secondaryOutputSlots[i].isEmpty()
                        || ItemStack.isSameItemSameTags(secondaryOutputSlots[i].getStack(), savedRemainingItems[i])) {
                    operations = Math.min(operations,
                            (savedRemainingItems[i].getMaxStackSize() - secondaryOutputSlots[i].getCount())
                                    / savedRemainingItems[i].getCount());
                } else {
                    return false;
                }
            }
        }
        if (operations < 1) {
            return false;
        }
        outputSlot.insertItem(savedOutputItem.copyWithCount(operations * savedOutputItem.getCount()), Action.EXECUTE,
                AutomationType.INTERNAL);
        for (int j = 0; j < 9; j++) {
            inputSlots[j].shrinkStack(operations, Action.EXECUTE);
            if (!savedRemainingItems[j].isEmpty()) {
                secondaryOutputSlots[j].insertItem(
                        savedRemainingItems[j].copyWithCount(operations * savedRemainingItems[j].getCount()),
                        Action.EXECUTE, AutomationType.INTERNAL);
            }
        }
        lastEnergyUsed = energyPerCraft.multiply(operations);
        energyContainer.extract(lastEnergyUsed, Action.EXECUTE, AutomationType.INTERNAL);
        return true;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags) {
        super.saveAdditional(nbtTags);
        nbtTags.putBoolean(NBT_RECIPE_ISNULL, savedRecipe == null);
        if (savedRecipe != null) {
            nbtTags.putString(NBT_SAVED_RECIPE_NAMESPACE, savedRecipe.getId().getNamespace());
            nbtTags.putString(NBT_SAVED_RECIPE_PATH, savedRecipe.getId().getPath());
        } else {
            nbtTags.putString(NBT_SAVED_RECIPE_NAMESPACE, "null");
            nbtTags.putString(NBT_SAVED_RECIPE_PATH, "null");
        }

    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains(NBT_RECIPE_ISNULL)) {
            if (nbt.getBoolean(NBT_RECIPE_ISNULL)) {
                setSavedRecipe(null);
            } else if (nbt.contains(NBT_SAVED_RECIPE_NAMESPACE) && nbt.contains(NBT_SAVED_RECIPE_PATH)) {
                tryingLoadRecipe = true;
                savedRecipeNameSpace = nbt.getString(NBT_SAVED_RECIPE_NAMESPACE);
                savedRecipePath = nbt.getString(NBT_SAVED_RECIPE_PATH);
            } else {
                setSavedRecipe(null);
            }
        }
    }

    private void tryLoadRecipe() {
        if (craftingRecipes.isEmpty()) {
            craftingRecipes = level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING);
        }
        if (!craftingRecipes.isEmpty()) {
            tryingLoadRecipe = false;
            craftingRecipes.stream().filter(
                    r -> r.getId().getNamespace() == savedRecipeNameSpace
                            && r.getId().getPath() == savedRecipePath)
                    .findFirst().ifPresentOrElse(this::setSavedRecipe, () -> setSavedRecipe(null));
        }
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableStringList.create(
                () -> savedRecipe != null
                        ? List.of(savedRecipe.getId().getNamespace(), savedRecipe.getId().getPath())
                        : List.of(),
                v -> {
                    if (v.size() != 2) {
                        setSavedRecipe(null);
                    } else {
                        level.getRecipeManager()
                                .byKey(new ResourceLocation(v.get(0), v.get(1)))
                                .ifPresentOrElse(r -> {
                                    if (r != null && r instanceof CraftingRecipe recipe) {
                                        setSavedRecipe(recipe);
                                    } else {
                                        setSavedRecipe(null);
                                    }
                                }, () -> setSavedRecipe(null));
                        ;
                    }
                }));
    }

    public FloatingLong getEnergyUsage() {
        return getActive() ? lastEnergyUsed : FloatingLong.ZERO;
    }

    @Override
    public double getProgressScaled() {
        return getActive() ? 1 : 0;
    }

    @Override
    public MachineEnergyContainer<BEAstralFormulaicAssemblicator> getEnergyContainer() {
        return energyContainer;
    }

    public void setSavedRecipe(CraftingRecipe recipe) {
        savedRecipe = recipe;
        if (savedRecipe == null) {
            Arrays.fill(savedIngredients, Ingredient.EMPTY);
            savedOutputItem = ItemStack.EMPTY;
        } else if (savedRecipe instanceof ShapedRecipe shapedRecipe) {
            int width = shapedRecipe.getWidth();
            int height = shapedRecipe.getHeight();
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    savedIngredients[x + y * 3] = x < width && y < height
                            ? shapedRecipe.getIngredients().get(x + y * width)
                            : Ingredient.EMPTY;
                }
            }
            savedOutputItem = savedRecipe.getResultItem(getLevel().registryAccess());
            savedRecipeNameSpace = savedRecipe.getId().getNamespace();
            savedRecipePath = savedRecipe.getId().getPath();
        } else {
            int size = savedRecipe.getIngredients().size();
            for (int i = 0; i < 9; i++) {
                savedIngredients[i] = i < size ? savedRecipe.getIngredients().get(i) : Ingredient.EMPTY;
            }
            savedOutputItem = savedRecipe.getResultItem(getLevel().registryAccess());
            savedRecipeNameSpace = savedRecipe.getId().getNamespace();
            savedRecipePath = savedRecipe.getId().getPath();
        }
    }

    @Override
    public int getInventoryXOffset() {
        return IHasCustomSizeContainer.super.getInventoryXOffset() + 30;
    }

}
