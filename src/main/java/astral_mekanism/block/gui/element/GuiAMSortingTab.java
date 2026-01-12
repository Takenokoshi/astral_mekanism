package astral_mekanism.block.gui.element;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.AstralMekanism;
import astral_mekanism.block.blockentity.base.BlockEntityProgressFactory;
import astral_mekanism.network.to_server.PacketGuiProgressFactory;
import mekanism.client.SpecialColors;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.GuiInsetElement;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.MekanismLang;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import mekanism.common.util.text.BooleanStateDisplay.OnOff;
import net.minecraft.client.gui.GuiGraphics;

public class GuiAMSortingTab extends GuiInsetElement<BlockEntityProgressFactory<?, ?>> {

    private final BlockEntityProgressFactory<?, ?> factory;

    public GuiAMSortingTab(IGuiWrapper gui, BlockEntityProgressFactory<?, ?> dataSource) {
        super(MekanismUtils.getResource(ResourceType.GUI, "sorting.png"), gui, dataSource, -26, 62, 35, 18, true);
        this.factory = dataSource;
    }

    @Override
    public void drawBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.drawBackground(guiGraphics, mouseX, mouseY, partialTicks);
        drawTextScaledBound(guiGraphics, OnOff.of(dataSource.isSorting()).getTextComponent(), relativeX + 3,
                relativeY + 24, titleTextColor(), 21);
    }

    @Override
    public void renderToolTip(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderToolTip(guiGraphics, mouseX, mouseY);
        displayTooltips(guiGraphics, mouseX, mouseY, MekanismLang.AUTO_SORT.translate());
    }

    @Override
    protected void colorTab(GuiGraphics guiGraphics) {
        MekanismRenderer.color(guiGraphics, SpecialColors.TAB_FACTORY_SORT);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        AstralMekanism.packetHandler().sendToServer(new PacketGuiProgressFactory(factory.getBlockPos()));
    }
}