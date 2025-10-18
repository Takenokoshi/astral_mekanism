package astral_mekanism.block.blockentity.other;

import java.util.List;

import astral_mekanism.block.blockentity.core.BlockEntityUtils;
import astral_mekanism.block.blockentity.elements.AstralMekDataType;
import astral_mekanism.block.blockentity.elements.slot.GhostInventorySlot;
import astral_mekanism.block.blockentity.prefab.BEAbstractItemSortble;
import astral_mekanism.block.blockentity.prefab.BEAbstractStorage;
import astral_mekanism.items.upgrade.ItemSingularityUpgrade;
import mekanism.api.IContentsListener;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BEItemSortableStorage extends BEAbstractStorage implements BEAbstractItemSortble {

    private static final FloatingLong energyCapacity = FloatingLong.create(2000000);
    private static final int fluidTankCapacity = 200000;
    private static final long chemicalTankCapacity = 200000l;

    private BasicInventorySlot[] inputInventorySlots;
    private BasicInventorySlot[] outputInventorySlotsA;
    private BasicInventorySlot[] outputInventorySlotsB;
    private BasicInventorySlot[] outputInventorySlotsC;
    private GhostInventorySlot[] filterInventorySlotsA;
    private GhostInventorySlot[] filterInventorySlotsB;
    private BasicInventorySlot insertUpgradeSlot;
    private BasicInventorySlot singularityUpgradeSlotA;
    private BasicInventorySlot singularityUpgradeSlotB;
    private BasicInventorySlot singularityUpgradeSlotC;

    public BEItemSortableStorage(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected void setUpItemConfig(ConfigInfo itemConfig) {
        itemConfig.addSlotInfo(DataType.INPUT, new InventorySlotInfo(true, false, inputInventorySlots));
        itemConfig.addSlotInfo(DataType.OUTPUT_1, new InventorySlotInfo(false, true, outputInventorySlotsA));
        itemConfig.addSlotInfo(DataType.OUTPUT_2, new InventorySlotInfo(false, true, outputInventorySlotsB));
        itemConfig.addSlotInfo(AstralMekDataType.OUTPUTleft, new InventorySlotInfo(false, true, outputInventorySlotsC));
        itemConfig.addSlotInfo(AstralMekDataType.INPUT_OUTPUT1,
                new InventorySlotInfo(true, false, inputInventorySlots));
        itemConfig.addSlotInfo(AstralMekDataType.INPUT_OUTPUT2,
                new InventorySlotInfo(true, false, inputInventorySlots));
        itemConfig.addSlotInfo(AstralMekDataType.INPUT_OUTPUTleft,
                new InventorySlotInfo(true, false, inputInventorySlots));
        itemConfig.setCanEject(true);
    }

    @Override
    protected InventorySlotHelper addUniqueSlots(InventorySlotHelper builder, IContentsListener listener) {
        inputInventorySlots = new BasicInventorySlot[9];
        filterInventorySlotsA = new GhostInventorySlot[9];
        outputInventorySlotsA = new BasicInventorySlot[9];
        filterInventorySlotsB = new GhostInventorySlot[9];
        outputInventorySlotsB = new BasicInventorySlot[9];
        outputInventorySlotsC = new BasicInventorySlot[9];
        for (int i = 0; i < 9; i++) {
            builder.addSlot(inputInventorySlots[i] = BasicInventorySlot.at(listener, 8 + 18 * i, 18));
            builder.addSlot(filterInventorySlotsA[i] = new GhostInventorySlot(listener, 8 + 18 * i, 54));
            builder.addSlot(outputInventorySlotsA[i] = BasicInventorySlot.at(listener, 8 + 18 * i, 72));
            builder.addSlot(filterInventorySlotsB[i] = new GhostInventorySlot(listener, 8 + 18 * i, 108));
            builder.addSlot(outputInventorySlotsB[i] = BasicInventorySlot.at(listener, 8 + 18 * i, 126));
            builder.addSlot(outputInventorySlotsC[i] = BasicInventorySlot.at(listener, 8 + 18 * i, 162));
        }
        builder.addSlot(insertUpgradeSlot = BasicInventorySlot.at(listener, 170, 18));
        builder.addSlot(singularityUpgradeSlotA = BasicInventorySlot.at(listener, 170, 72));
        builder.addSlot(singularityUpgradeSlotB = BasicInventorySlot.at(listener, 170, 126));
        builder.addSlot(singularityUpgradeSlotC = BasicInventorySlot.at(listener, 170, 162));
        return builder;
    }

    @Override
    protected void onUSUnique() {
        if (isInsertUpgrade.test(insertUpgradeSlot.getStack())) {
            BlockEntityUtils.itemInsert(this, List.of(DataType.INPUT, AstralMekDataType.INPUT_OUTPUT1,
                    AstralMekDataType.INPUT_OUTPUT2, AstralMekDataType.INPUT_OUTPUTleft));
        }
        filtering();
        ItemSingularityUpgrade.running(singularityUpgradeSlotA, isSingularityUpgrade, outputInventorySlotsA);
        ItemSingularityUpgrade.running(singularityUpgradeSlotB, isSingularityUpgrade, outputInventorySlotsB);
        ItemSingularityUpgrade.running(singularityUpgradeSlotC, isSingularityUpgrade, outputInventorySlotsC);
        BlockEntityUtils.itemEject(this, List.of(AstralMekDataType.INPUT_OUTPUT1), DataType.OUTPUT_1);
        BlockEntityUtils.itemEject(this, List.of(AstralMekDataType.INPUT_OUTPUT2), DataType.OUTPUT_2);
        BlockEntityUtils.itemEject(this, List.of(AstralMekDataType.INPUT_OUTPUTleft, AstralMekDataType.OUTPUTleft),
                AstralMekDataType.OUTPUTleft);
    }

    private void filtering() {
        for (BasicInventorySlot inputSlot : inputInventorySlots) {
            if (inputSlot.isEmpty()) {
                continue;
            }
            ItemStack stack = inputSlot.getStack();
            for (GhostInventorySlot filterSlot : filterInventorySlotsA) {
                if (ItemStack.isSameItemSameTags(filterSlot.getStack(), stack)) {
                    stack = BlockEntityUtils.insertItem(outputInventorySlotsA, stack);
                    break;
                }
            }
            if (stack.isEmpty()) {
                inputSlot.setEmpty();
                continue;
            }
            for (GhostInventorySlot filterSlot : filterInventorySlotsB) {
                if (ItemStack.isSameItemSameTags(filterSlot.getStack(), stack)) {
                    stack = BlockEntityUtils.insertItem(outputInventorySlotsB, stack);
                    break;
                }
            }
            stack = BlockEntityUtils.insertItem(outputInventorySlotsC, stack);
            inputSlot.setStack(stack);
        }
    }

    @Override
    protected FloatingLong energyCapacitySetter() {
        return energyCapacity;
    }

    @Override
    protected int fluidTankCapacitySetter() {
        return fluidTankCapacity;
    }

    @Override
    protected long chemicalTankCapacitySetter() {
        return chemicalTankCapacity;
    }

    @Override
    public SlotKind getSlotKind(IInventorySlot slot) {
        for (int i = 0; i < 9; i++) {
            if (inputInventorySlots[i] == slot) {
                return SlotKind.INPUT;
            }
            if (filterInventorySlotsA[i] == slot || filterInventorySlotsB[i] == slot) {
                return SlotKind.FILTER;
            }
            if (outputInventorySlotsA[i] == slot || outputInventorySlotsB[i] == slot
                    || outputInventorySlotsC[i] == slot) {
                return SlotKind.OUTPUT;
            }
        }
        return SlotKind.OTHER;
    }

    @Override
    public IInventorySlot[] getSlots(SlotKind slotType) {
        if (slotType == SlotKind.INPUT) {
            return inputInventorySlots;
        } else if (slotType == SlotKind.OUTPUT) {
            IInventorySlot[] result = new IInventorySlot[27];
            for (int i = 0; i < 9; i++) {
                result[i] = outputInventorySlotsA[i];
                result[i + 9] = outputInventorySlotsB[i];
                result[i + 18] = outputInventorySlotsC[i];
            }
            return result;
        } else if (slotType == SlotKind.FILTER) {
            IInventorySlot[] result = new IInventorySlot[18];
            for (int i = 0; i < 9; i++) {
                result[i] = filterInventorySlotsA[i];
                result[i + 9] = filterInventorySlotsB[i];
            }
            return result;
        } else {
            return new IInventorySlot[] {};
        }
    }

}
