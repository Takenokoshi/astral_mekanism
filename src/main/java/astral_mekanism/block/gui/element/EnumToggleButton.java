package astral_mekanism.block.gui.element;

import java.util.Map;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.button.MekanismButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class EnumToggleButton<T extends Enum<T>> extends MekanismButton {

    private final Supplier<T> stateSupplier;
    private final Map<T, ResourceLocation> textureMap;
    private final int textureWidth;
    private final int textureHeight;

    public EnumToggleButton(IGuiWrapper gui, int x, int y, int width, int height,
            int textureWidth, int textureHeight,
            Supplier<T> stateSupplier, Map<T, ResourceLocation> textureMap,
            @NotNull Runnable onLeftClick, @Nullable Runnable onRightClick,
            @Nullable IHoverable onHover) {
        super(gui, x, y, width, height, Component.empty(), onLeftClick, onRightClick, onHover);
        this.stateSupplier = stateSupplier;
        this.textureMap = textureMap;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    @Override
    public void drawBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.drawBackground(guiGraphics, mouseX, mouseY, partialTicks);
        ResourceLocation r = getTexture();
        if (r != null) {
            guiGraphics.blit(r, getButtonX(), getButtonY(),
                    getButtonWidth(), getButtonHeight(), 0, 0,
                    textureWidth, textureHeight, textureWidth, textureHeight);

        }
    }

    protected ResourceLocation getTexture() {
        T t = stateSupplier.get();
        if (textureMap.containsKey(t)) {
            return textureMap.get(t);
        }
        return null;
    }
}
