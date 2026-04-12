package astral_mekanism.block.gui.element.aeKey;

import org.jetbrains.annotations.NotNull;

import appeng.api.client.AEKeyRendering;
import appeng.api.stacks.AEKey;
import astral_mekanism.AstralMekanism;
import astral_mekanism.block.blockentity.elements.aekey.AEKeySlot;
import astral_mekanism.network.to_server.PacketGuiAEKeySlotSet;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.GuiElement;
import mekanism.client.gui.element.slot.SlotType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;

public class GuiAEKeySlot extends GuiElement {

    public final AEKeySlot<?> slot;
    private final BlockPos pos;

    public GuiAEKeySlot(AEKeySlot<?> slot, IGuiWrapper gui, int x, int y, BlockPos pos) {
        super(gui, x, y, 18, 18);
        this.slot = slot;
        this.pos = pos;
    }

    @Override
    public void drawBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        guiGraphics.blit(SlotType.NORMAL.getTexture(), relativeX + 1, relativeY + 1, 0, 0, 16, 16, 16, 16);
        AEKey key = slot.getKey();
        if (key != null) {
            AEKeyRendering.drawInGui(minecraft, guiGraphics, relativeX + 1, relativeY + 1, key);
        }
    }

    @Override
    public void renderToolTip(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderToolTip(guiGraphics, mouseX, mouseY);
        AEKey key = slot.getKey();
        if (key != null && isMouseOver(mouseX, mouseY)) {
            displayTooltips(guiGraphics, mouseX, mouseY, AEKeyRendering.getTooltip(key));
        }
    }

    public void sendKeyToServer(AEKey key){
        AstralMekanism.packetHandler().sendToServer(new PacketGuiAEKeySlotSet(pos, slot.slotIndex, key));
    }
}
