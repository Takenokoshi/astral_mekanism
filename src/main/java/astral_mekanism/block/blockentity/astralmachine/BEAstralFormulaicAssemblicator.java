package astral_mekanism.block.blockentity.astralmachine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import appeng.api.stacks.GenericStack;
import appeng.crafting.pattern.AECraftingPattern;
import appeng.crafting.pattern.CraftingPatternItem;
import astral_mekanism.block.blockentity.core.BlockEntityUtils;
import mekanism.api.Action;
import mekanism.api.IContentsListener;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.content.assemblicator.RecipeFormula;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.item.ItemCraftingFormula;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralFormulaicAssemblicator extends TileEntityConfigurableMachine {

    private static final Predicate<ItemStack> IS_PATTAEN = (i) -> {
        Item item = i.getItem();
        if (item instanceof CraftingPatternItem || item instanceof ItemCraftingFormula) {
            return true;
        }
        return false;
    };

    BasicInventorySlot[] pattarnSlots;
    BasicInventorySlot[] inputSlots;
    BasicInventorySlot[] outputSlots;
    BasicEnergyContainer energyContainer;
    EnergyInventorySlot energySlot;

    public BEAstralFormulaicAssemblicator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        configComponent = new TileComponentConfig(this, TransmissionType.ENERGY, TransmissionType.ITEM);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        configComponent.setupItemIOConfig(Arrays.asList(inputSlots), Arrays.asList(outputSlots), energySlot, false);
        ejectorComponent = new TileComponentEjector(this);
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(
                energyContainer = BasicEnergyContainer.create(FloatingLong.MAX_VALUE, listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection,
                this::getConfig);
        pattarnSlots = new BasicInventorySlot[9];
        inputSlots = new BasicInventorySlot[18];
        outputSlots = new BasicInventorySlot[9];
        for (int i = 0; i < 9; i++) {
            builder.addSlot(pattarnSlots[i] = BasicInventorySlot.at(IS_PATTAEN, listener, i, i));
            builder.addSlot(inputSlots[i] = BasicInventorySlot.at(listener, i, i));
            builder.addSlot(inputSlots[i + 9] = BasicInventorySlot.at(listener, i, i));
            builder.addSlot(outputSlots[i] = BasicInventorySlot.at(listener, i, i));
        }
        builder.addSlot(energySlot = EnergyInventorySlot.fill(energyContainer, listener, ticker, ticker));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        energySlot.fillContainerOrConvert();
        super.onUpdateServer();
        Craft9(pattarnSlots);
    }

    private CraftItemList getCraftlist(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        Item item = stack.getItem();
        if (item instanceof CraftingPatternItem patternItem) {
            ItemStack output = patternItem.getOutput(stack);
            CraftItemList result = new CraftItemList(output);
            AECraftingPattern aeCraftingPattern = patternItem.decode(stack, level, false);
            if (aeCraftingPattern == null) {
                return null;
            }
            GenericStack[] genericStacks = aeCraftingPattern.getSparseInputs();
            for (GenericStack genericStack : genericStacks) {
                ItemStack required = GenericStack.wrapInItemStack(genericStack);
                if (!required.isEmpty()) {
                    result.add(required);
                }
            }
            return result;
        } else if (item instanceof ItemCraftingFormula formula) {
            NonNullList<ItemStack> formulaInv = formula.getInventory(stack);
            RecipeFormula recipe = new RecipeFormula(level, formulaInv);
            ItemStack output = recipe.assemble(level.registryAccess());
            CraftItemList result = new CraftItemList(output);
            for (ItemStack required : formulaInv) {
                if (!required.isEmpty()) {
                    result.add(required);
                }
            }
            return result;
        }
        return null;
    }

    private void Craft(@Nullable CraftItemList craftItemList) {
        if (craftItemList == null) {
            return;
        }
        int craftCount = Math.min(craftItemList.calculateByInput(inputSlots),
                craftItemList.calcurateBySpace(outputSlots));
        craftItemList.addItems(outputSlots, craftCount);
        craftItemList.removeItems(inputSlots, craftCount);
    }

    private void Craft9(BasicInventorySlot[] patternSlots) {
        for (BasicInventorySlot patternSlot : patternSlots) {
            Craft(getCraftlist(patternSlot.getStack()));
        }
    }

    private class CraftItemList {
        private List<ItemStack> requiredList;
        private List<ItemStack> leftItemList;
        private ItemStack outputStack;

        public CraftItemList(ItemStack outputStack) {
            this.requiredList = new ArrayList<ItemStack>();
            this.leftItemList = new ArrayList<ItemStack>();
            this.outputStack = outputStack;
        }

        public void add(ItemStack addStack) {
            boolean ljhvu = true;
            for (ItemStack stack : requiredList) {
                if (ljhvu && ItemStack.isSameItemSameTags(stack, addStack)
                        && stack.getMaxStackSize() > stack.getCount()) {
                    stack.grow(1);
                    ljhvu = false;
                    break;
                }
            }
            if (ljhvu) {
                requiredList.add(addStack.copyWithCount(1));
            }
            ItemStack leftItem = addStack.getItem().getCraftingRemainingItem(addStack);
            if (leftItem.isEmpty()) {
                return;
            }
            for (ItemStack stack : leftItemList) {
                if (ItemStack.isSameItemSameTags(stack, leftItem) && stack.getMaxStackSize() > stack.getCount()) {
                    stack.grow(1);
                    return;
                }
            }
            leftItemList.add(leftItem.copyWithCount(1));
            return;
        }

        public int calculateByInput(BasicInventorySlot[] inputSlots) {
            int result = Integer.MAX_VALUE;
            for (ItemStack item : requiredList) {
                long useable = 0;
                for (BasicInventorySlot inputSlot : inputSlots) {
                    if (ItemStack.isSameItemSameTags(item, inputSlot.getStack())) {
                        useable += inputSlot.getCount();
                    }
                }
                result = (int) Math.min(result, useable / item.getCount());
            }
            return result;
        }

        public int calcurateBySpace(BasicInventorySlot[] outputSlots) {
            int emptyCount = 0;
            for (BasicInventorySlot outputSlot : outputSlots) {
                if (outputSlot.isEmpty()) {
                    emptyCount++;
                }
            }
            int stackSize = outputStack.getMaxStackSize() / outputStack.getCount();
            for (ItemStack left : leftItemList) {
                stackSize = Math.min(stackSize, left.getMaxStackSize() / left.getCount());
            }
            return (int) Math.min((emptyCount / (leftItemList.size() + 1)) * (long) stackSize, Integer.MAX_VALUE);
        }

        public void removeItems(BasicInventorySlot[] inputSlots, long craftCount) {
            for (ItemStack usedItem : requiredList) {
                long usedAmount = craftCount * usedItem.getCount();
                for (BasicInventorySlot inputSlot : inputSlots) {
                    if (ItemStack.isSameItemSameTags(usedItem, inputSlot.getStack())) {
                        if (usedAmount >= inputSlot.getCount()) {
                            usedAmount -= inputSlot.getCount();
                            inputSlot.setEmpty();
                        } else {
                            inputSlot.shrinkStack((int) usedAmount, Action.EXECUTE);
                            break;
                        }
                    }
                }
            }
        }

        public void addItems(BasicInventorySlot[] outputSlots, long craftCount) {
            long createdo = outputStack.getCount() * craftCount;
            for (int i = 0; i < createdo / outputStack.getMaxStackSize(); i++) {
                BlockEntityUtils.insertItem(outputSlots, outputStack.copyWithCount(outputStack.getMaxStackSize()));
            }
            BlockEntityUtils.insertItem(outputSlots,
                    outputStack.copyWithCount((int) (createdo % outputStack.getMaxStackSize())));
            for (ItemStack left : leftItemList) {
                long createdl = left.getCount() * craftCount;
                for (int i = 0; i < createdl / left.getMaxStackSize(); i++) {
                    BlockEntityUtils.insertItem(outputSlots, left.copyWithCount(left.getMaxStackSize()));
                }
                BlockEntityUtils.insertItem(outputSlots,
                        left.copyWithCount((int) (createdl % left.getMaxStackSize())));
            }
        }
    }

}
