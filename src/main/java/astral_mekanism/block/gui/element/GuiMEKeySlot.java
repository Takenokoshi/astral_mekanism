package astral_mekanism.block.gui.element;

import java.util.List;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import appeng.api.client.AEKeyRendering;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import appeng.core.localization.ButtonToolTips;
import appeng.core.localization.Tooltips;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.GuiElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class GuiMEKeySlot extends GuiElement {

    private final Supplier<MEStorage> storageSupplier;
    private final Supplier<AEKey> keySupplier;

    public GuiMEKeySlot(
            IGuiWrapper gui,
            int x, int y,
            Supplier<MEStorage> storageSupplier,
            Supplier<AEKey> keySupplier) {
        super(gui, x, y, 18, 18);
        this.storageSupplier = storageSupplier;
        this.keySupplier = keySupplier;
    }

    @Override
    public void drawBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.fill(relativeX, relativeY, relativeX + 18, relativeY + 18, 0xFF8B8B8B);
        graphics.fill(relativeX + 1, relativeY + 1, relativeX + 17, relativeY + 17, 0xFF3C3C3C);
        AEKey key = keySupplier.get();
        if (key == null) {
            return;
        }
        AEKeyRendering.drawInGui(
                minecraft,
                graphics,
                relativeX + 1,
                relativeY + 1,
                key);
    }

    @Override
    public void renderToolTip(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (isMouseOver(mouseX, mouseY)) {
            AEKey key = keySupplier.get();
            if (key == null) {
                return;
            }
            List<Component> tooltips = AEKeyRendering.getTooltip(key);
            MEStorage storage = storageSupplier.get();
            if (storage != null) {
                tooltips.add(Tooltips.getAmountTooltip(ButtonToolTips.Amount, key,
                        storage.extract(key, Long.MAX_VALUE, Actionable.SIMULATE, IActionSource.empty())));
            }
            guiGraphics.renderComponentTooltip(getFont(), tooltips, mouseX, mouseY);
        }
    }
}