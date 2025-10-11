package astral_mekanism.block.blockentity.elements.slot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.common.inventory.slot.BasicInventorySlot;
import net.minecraft.world.item.ItemStack;

public class GhostInventorySlot extends BasicInventorySlot {

    //Container which has this slot should override quickMoveStack(Player player, int slotID)

    public GhostInventorySlot(@Nullable IContentsListener listener, int x, int y) {
        super(1, alwaysTrueBi, alwaysTrueBi, alwaysTrue, listener, x, y);
    }

    @Override
    public ItemStack insertItem(@NotNull ItemStack stack, @NotNull Action action,
            @NotNull AutomationType automationType) {
        if (automationType == AutomationType.MANUAL) {
            if (stack.isEmpty()) {
                this.current = ItemStack.EMPTY;
            } else {
                this.current = stack.copyWithCount(1);
            }
        }
        return stack;
    }

    @Override
    public void setStack(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            this.current = ItemStack.EMPTY;
        } else {
            if (current.isEmpty()) {
                this.current = stack.copyWithCount(1);
            } else {
                this.current = ItemStack.EMPTY;
            }
        }
        this.onContentsChanged();
    }

    @Override
    public ItemStack extractItem(int amount, Action action, AutomationType automationType) {
        this.current = ItemStack.EMPTY;
        return ItemStack.EMPTY;
    }

}
