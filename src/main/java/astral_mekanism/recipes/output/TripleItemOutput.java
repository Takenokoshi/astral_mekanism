package astral_mekanism.recipes.output;

import org.jetbrains.annotations.NotNull;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public record TripleItemOutput(@NotNull ItemStack itemA, @NotNull ItemStack itemB, @NotNull ItemStack itemC) {
    public TripleItemOutput {
    };

    public TripleItemOutput copy() {
        return new TripleItemOutput(this.itemA, this.itemB, this.itemC);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeItem(itemA);
        buf.writeItem(itemB);
        buf.writeItem(itemC);
    }

    public static TripleItemOutput readFromBuf(FriendlyByteBuf buf) {
        ItemStack a = buf.readItem();
        ItemStack b = buf.readItem();
        ItemStack c = buf.readItem();
        return new TripleItemOutput(a, b, c);
    }

    public boolean isEmpty() {
        return itemA.isEmpty() && itemB.isEmpty() && itemC.isEmpty();
    }
}
