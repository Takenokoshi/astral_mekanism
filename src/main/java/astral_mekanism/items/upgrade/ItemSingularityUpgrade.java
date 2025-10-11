package astral_mekanism.items.upgrade;

import java.util.Arrays;
import java.util.function.Predicate;

import appeng.core.definitions.AEItems;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.inventory.slot.BasicInventorySlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemSingularityUpgrade extends Item {

    public ItemSingularityUpgrade(Properties p_41383_) {
        super(p_41383_.stacksTo(1));
    }

    private static final String PROGRESSKEY = "progress";

    public static int getProgress(ItemStack stack) {
        return stack.getOrCreateTag().getInt(PROGRESSKEY);
    }

    public static void setProgress(ItemStack stack, int progress) {
        stack.getOrCreateTag().putInt(PROGRESSKEY, progress);
    }

    public static ItemStack running(ItemStack stack, IInventorySlot... slots) {
        int progress = getProgress(stack);
        for (IInventorySlot slot : slots) {
            if (progress < 100000000) {
                if (!slot.isEmpty()
                        && !ItemStack.isSameItem(slot.getStack(), AEItems.SINGULARITY.stack())) {
                    progress += slot.getCount();
                    slot.setEmpty();
                }
            } else {
                break;
            }
        }
        int p = progress / 256000;
        if (p > 0) {
            Arrays.stream(slots)
                    .filter(IInventorySlot::isEmpty)
                    .findFirst()
                    .ifPresent(slot -> {
                        slot.insertItem(AEItems.SINGULARITY.stack(p), Action.EXECUTE,
                                AutomationType.EXTERNAL);
                    });
        }
        setProgress(stack, progress % 256000);
        return stack;
    }

    public static void running(BasicInventorySlot slot, Predicate<ItemStack> isSingularityUpgrade,
            IInventorySlot... slots) {
        ItemStack stack = slot.getStack();
        if (isSingularityUpgrade.test(stack)) {
            slot.setStack(running(stack, slots));
        }
    }

}
