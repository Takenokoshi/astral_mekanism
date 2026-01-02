package astral_mekanism.recipes.output;

import mekanism.api.chemical.infuse.InfusionStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public record ItemInfuseOutput(ItemStack itemStack, InfusionStack infusionStack) {
    public ItemInfuseOutput copy() {
        return new ItemInfuseOutput(this.itemStack, this.infusionStack);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeItem(itemStack);
        infusionStack.writeToPacket(buf);
    }

    public static ItemInfuseOutput read(FriendlyByteBuf buf) {
        return new ItemInfuseOutput(buf.readItem(), InfusionStack.readFromPacket(buf));
    }
}
