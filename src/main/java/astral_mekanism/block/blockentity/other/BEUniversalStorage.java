package astral_mekanism.block.blockentity.other;

import java.util.List;

import astral_mekanism.block.blockentity.core.BlockEntityUtils;
import astral_mekanism.block.blockentity.elements.AstralMekDataType;
import astral_mekanism.block.blockentity.prefab.BEAbstractStorage;
import astral_mekanism.items.upgrade.ItemSingularityUpgrade;
import astral_mekanism.registries.AstralMekanismItems;
import mekanism.api.IContentsListener;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BEUniversalStorage extends BEAbstractStorage {

    private static final FloatingLong energyCapacity = FloatingLong.MAX_VALUE;
    private static final int fluidTankCapacity = Integer.MAX_VALUE;
    private static final long chemicalTankCapacity = Long.MAX_VALUE;

    BasicInventorySlot[] inventorySlotsA;
    BasicInventorySlot[] inventorySlotsB;
    BasicInventorySlot insertUpgradeSlotA;
    BasicInventorySlot insertUpgradeSlotB;
    BasicInventorySlot singularityUpgradeSlotA;
    BasicInventorySlot singularityUpgradeSlotB;

    public BEUniversalStorage(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected void setUpItemConfig(ConfigInfo itemConfig) {
        itemConfig.addSlotInfo(DataType.INPUT_1, new InventorySlotInfo(true, false, inventorySlotsA));
        itemConfig.addSlotInfo(DataType.INPUT_2, new InventorySlotInfo(true, false, inventorySlotsB));
        itemConfig.addSlotInfo(DataType.OUTPUT_1, new InventorySlotInfo(false, true, inventorySlotsA));
        itemConfig.addSlotInfo(DataType.OUTPUT_2, new InventorySlotInfo(false, true, inventorySlotsB));
        itemConfig.addSlotInfo(AstralMekDataType.INPUT1_OUTPUT1, new InventorySlotInfo(true, false, inventorySlotsA));
        itemConfig.addSlotInfo(AstralMekDataType.INPUT1_OUTPUT2, new InventorySlotInfo(true, false, inventorySlotsA));
        itemConfig.addSlotInfo(AstralMekDataType.INPUT2_OUTPUT1, new InventorySlotInfo(true, false, inventorySlotsB));
        itemConfig.addSlotInfo(AstralMekDataType.INPUT2_OUTPUT2, new InventorySlotInfo(true, false, inventorySlotsB));
        itemConfig.setCanEject(true);
    }

    @Override
    protected InventorySlotHelper addUniqueSlots(InventorySlotHelper builder, IContentsListener listener) {
        inventorySlotsA = new BasicInventorySlot[36];
        inventorySlotsB = new BasicInventorySlot[36];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 4; j++) {
                builder.addSlot(inventorySlotsA[i + 9 * j] = BasicInventorySlot.at(listener, 8 + 18 * i, 18 + 18 * j));
                builder.addSlot(inventorySlotsB[i + 9 * j] = BasicInventorySlot.at(listener, 8 + 18 * i, 108 + 18 * j));
            }
        }
        builder.addSlot(insertUpgradeSlotA = BasicInventorySlot.at(isInsertUpgrade, listener, 170, 18));
        builder.addSlot(insertUpgradeSlotB = BasicInventorySlot.at(isInsertUpgrade, listener, 170, 108));
        builder.addSlot(singularityUpgradeSlotA = BasicInventorySlot.at(isSingularityUpgrade, listener, 170, 72));
        builder.addSlot(singularityUpgradeSlotB = BasicInventorySlot.at(isSingularityUpgrade, listener, 170, 162));
        insertUpgradeSlotA.setSlotOverlay(SlotOverlay.UPGRADE);
        singularityUpgradeSlotA.setSlotOverlay(SlotOverlay.UPGRADE);
        insertUpgradeSlotB.setSlotOverlay(SlotOverlay.UPGRADE);
        singularityUpgradeSlotB.setSlotOverlay(SlotOverlay.UPGRADE);
        return builder;
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
    protected void onUSUnique() {
        if (ItemStack.isSameItem(insertUpgradeSlotA.getStack(), AstralMekanismItems.INSERT_UPGRADE.getItemStack())) {
            BlockEntityUtils.itemInsert(this, List.<DataType>of(DataType.INPUT_1, AstralMekDataType.INPUT1_OUTPUT1,
                    AstralMekDataType.INPUT1_OUTPUT2));
        }
        if (ItemStack.isSameItem(insertUpgradeSlotB.getStack(), AstralMekanismItems.INSERT_UPGRADE.getItemStack())) {
            BlockEntityUtils.itemInsert(this, List.<DataType>of(DataType.INPUT_2, AstralMekDataType.INPUT2_OUTPUT1,
                    AstralMekDataType.INPUT2_OUTPUT2));
        }
        ItemSingularityUpgrade.running(singularityUpgradeSlotA, isSingularityUpgrade, inventorySlotsA);
        ItemSingularityUpgrade.running(singularityUpgradeSlotB, isSingularityUpgrade, inventorySlotsB);
        BlockEntityUtils.itemEject(this,
                List.<DataType>of(AstralMekDataType.INPUT1_OUTPUT1, AstralMekDataType.INPUT2_OUTPUT1),
                DataType.OUTPUT_1);
        BlockEntityUtils.itemEject(this,
                List.<DataType>of(AstralMekDataType.INPUT1_OUTPUT2, AstralMekDataType.INPUT2_OUTPUT2),
                DataType.OUTPUT_2);
    }

}
