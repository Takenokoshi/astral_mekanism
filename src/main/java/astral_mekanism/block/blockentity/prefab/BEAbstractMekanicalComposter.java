package astral_mekanism.block.blockentity.prefab;

import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BEAbstractMekanicalComposter extends TileEntityConfigurableMachine {

    InputInventorySlot inputSlot;
    OutputInventorySlot outputSlot;
    int baselineMaxOperations;
    int cachedRequiredAmount;// 0 means not initialized,-1 means inputSlot is empty.

    public BEAbstractMekanicalComposter(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM);
        var itemInfo = configComponent.getConfig(TransmissionType.ITEM);
        itemInfo.addSlotInfo(DataType.INPUT, new InventorySlotInfo(true, false, inputSlot));
        itemInfo.addSlotInfo(DataType.OUTPUT, new InventorySlotInfo(false, true, outputSlot));
        itemInfo.addSlotInfo(DataType.INPUT_OUTPUT, new InventorySlotInfo(true, true, inputSlot, outputSlot));
        ejectorComponent = new TileComponentEjector(this).setOutputData(configComponent, TransmissionType.ITEM);
        baselineMaxOperations = getDefaltBaseline();
        cachedRequiredAmount = 0;
    }

    protected abstract int getDefaltBaseline();

    protected void setBaselineMaxOperations(int value) {
        baselineMaxOperations = value;
    }

    @Override
    public IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(inputSlot = InputInventorySlot.at(this::isCompostable, () -> {
            listener.onContentsChanged();
            recalculateRequiredAmount();
        }, 64, 35));
        builder.addSlot(outputSlot = OutputInventorySlot.at(listener, 116, 35));
        return builder.build();
    }

    private boolean isCompostable(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return ComposterBlock.COMPOSTABLES.containsKey(stack.getItem());
    }

    private void recalculateRequiredAmount() {
        ItemStack input = inputSlot.getStack();
        if (input.isEmpty() || !isCompostable(input)) {
            cachedRequiredAmount = -1;
        } else {
            cachedRequiredAmount = (int) Math.floor(7 / ComposterBlock.COMPOSTABLES.getFloat(input.getItem()));
        }
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        compost();
    }

    private void compost() {
        if (cachedRequiredAmount < 1) {
            if (cachedRequiredAmount == 0) {
                recalculateRequiredAmount();
            }
            return;
        }
        if (outputSlot.isEmpty()
                || ItemStack.isSameItem(outputSlot.getStack(),
                        Items.BONE_MEAL.getDefaultInstance())) {
            ItemStack input = inputSlot.getStack();
            int operations = baselineMaxOperations;
            operations = Math.min(operations, input.getCount() / cachedRequiredAmount);
            operations = Math.min(operations, Items.BONE_MEAL.getMaxStackSize() - outputSlot.getCount());
            if (operations > 0) {
                inputSlot.shrinkStack(cachedRequiredAmount * operations, Action.EXECUTE);
                outputSlot.insertItem(new ItemStack(Items.BONE_MEAL, operations), Action.EXECUTE,
                        AutomationType.INTERNAL);
            }
        }
    }

}
