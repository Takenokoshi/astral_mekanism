package astral_mekanism.util;

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
}
