package astral_mekanism.recipes.output;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public record ItemFluidOutput(ItemStack item, FluidStack fluid) {
    public ItemFluidOutput {
    }

    public ItemFluidOutput copy() {
        return new ItemFluidOutput(item.copy(), fluid.copy());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeItem(item);
        buf.writeFluidStack(fluid);
    }

    public static ItemFluidOutput readFromBuf(FriendlyByteBuf buf) {
        ItemStack itemStack = buf.readItem();
        FluidStack fluidStack = buf.readFluidStack();
        return new ItemFluidOutput(itemStack, fluidStack);
    }

    public boolean isEmpty() {
        return item.isEmpty() && fluid.isEmpty();
    }
}
