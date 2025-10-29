package astral_mekanism.util;

import java.util.Arrays;

import net.minecraft.world.item.ItemStack;

public class ArrayItemStackUtils {
    public static boolean includeNullOrEmpty(ItemStack[] stacks) {
        if (stacks == null) {
            return true;
        }
        for (ItemStack itemStack : stacks) {
            if (itemStack == null) {
                return true;
            } else if (itemStack.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack[] createArrayFilledEmpty(int size) {
        ItemStack[] result = new ItemStack[size];
        for (int i = 0; i < size; i++) {
            result[i] = ItemStack.EMPTY.copy();
        }
        return result;
    }

    public static ItemStack[] copyWithLength(ItemStack[] before, int size) {
        ItemStack[] result = Arrays.copyOf(before, size);
        for (int i = before.length; i < size; i++) {
            result[i] = ItemStack.EMPTY;
        }
        return result;
    }
}
