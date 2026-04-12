package astral_mekanism.block.gui.element.aeKey;

import org.jetbrains.annotations.Nullable;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.chemical.ChemicalStack;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GuiAEKeySlotTarget<I> implements IGhostIngredientHandler.Target<I> {

    private final GuiAEKeySlot slot;

    public GuiAEKeySlotTarget(GuiAEKeySlot slot) {
        this.slot = slot;
    }

    @Override
    public void accept(I arg0) {
        AEKey key = convertToAEKey(arg0);
        if (key != null) {
            slot.sendKeyToServer(key);
        }
    }

    @Override
    public Rect2i getArea() {
        return new Rect2i(slot.getRelativeX(), slot.getRelativeY(), slot.getWidth(), slot.getHeight());
    }

    @Nullable
    public static AEKey convertToAEKey(Object obj) {
        if (obj instanceof ItemStack is && !is.isEmpty()) {
            return AEItemKey.of(is);
        }
        if (obj instanceof FluidStack fs && !fs.isEmpty()) {
            return AEFluidKey.of(fs);
        }
        if (obj instanceof ChemicalStack<?> cs && !cs.isEmpty()) {
            return MekanismKey.of(cs);
        }
        return null;
    }

}
