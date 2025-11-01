package astral_mekanism.recipes.inputRecipeCache;

import java.util.Arrays;

import net.minecraft.world.item.ItemStack;

public class ItemStackArrayKey {
    private final ItemStack[] stacks;

    public ItemStackArrayKey(ItemStack[] stacks) {
        this.stacks = stacks;
        for (int i = 0; i < stacks.length; i++) {
            this.stacks[i] = stacks[i].copyWithCount(1);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ItemStackArrayKey other) {
            boolean result = this.stacks.length == other.stacks.length;
            int i = 0;
            while (result && i < this.stacks.length) {
                result &= ItemStack.isSameItem(this.stacks[i], other.stacks[i]);
                i++;
            }
            return result;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(stacks);
    }
}
