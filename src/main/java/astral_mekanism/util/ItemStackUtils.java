package astral_mekanism.util;

import astral_mekanism.recipes.output.TripleItemOutput;
import net.minecraft.world.item.ItemStack;

public class ItemStackUtils {
    public static ItemStack copyWithMultiply(ItemStack before, int multiply) {
        int p = Math.max(1, multiply);
        return before.copyWithCount(before.getCount() * p);
    }

    public static TripleItemOutput copyWithMultiply(TripleItemOutput before, int multiply) {
        return new TripleItemOutput(
                copyWithMultiply(before.itemA(), multiply),
                copyWithMultiply(before.itemB(), multiply),
                copyWithMultiply(before.itemC(), multiply));
    }
}
