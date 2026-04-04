package astral_mekanism.block.gui.normalmachine;

import org.jetbrains.annotations.NotNull;
import appeng.api.config.CondenserOutput;
import appeng.core.AppEng;
import astral_mekanism.block.blockentity.normalmachine.BEMekanicalMatterCondenser;
import astral_mekanism.jei.AMEJEIRecipeType;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.button.MekanismButton;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.common.Mekanism;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.network.to_server.PacketGuiInteract;
import mekanism.common.network.to_server.PacketGuiInteract.GuiInteraction;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiMekanicalMatterCondenser
        extends GuiConfigurableTile<BEMekanicalMatterCondenser, MekanismTileContainer<BEMekanicalMatterCondenser>> {

    public GuiMekanicalMatterCondenser(MekanismTileContainer<BEMekanicalMatterCondenser> container, Inventory inv,
            Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiProgress(tile::getScaledProgress, ProgressType.BAR, this, 86, 38))
                .jeiCategories(AMEJEIRecipeType.MATTER_CONDENSER);
        addRenderableWidget(new GuiFluidGauge(tile::getInputTank, () -> tile.getFluidTanks(null),
                GaugeType.STANDARD, this, 27, 12));
        addRenderableWidget(new CondenserToggleButton(this));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

    private static class CondenserToggleButton extends MekanismButton {

        private final GuiMekanicalMatterCondenser gui;

        public CondenserToggleButton(GuiMekanicalMatterCondenser gui) {
            super(gui, 135, 34, 18, 18, Component.empty(),
                    () -> Mekanism.packetHandler()
                            .sendToServer(new PacketGuiInteract(GuiInteraction.NEXT_MODE, gui.tile)),
                    () -> Mekanism.packetHandler()
                            .sendToServer(new PacketGuiInteract(GuiInteraction.PREVIOUS_MODE, gui.tile)),
                    gui.getOnHover(() -> {
                        CondenserOutput t = gui.tile.getMode();
                        if (t == CondenserOutput.MATTER_BALLS) {
                            return Component.translatable("gui.tooltips.ae2.MatterBalls",
                                    CondenserOutput.MATTER_BALLS.requiredPower);
                        }
                        if (t == CondenserOutput.SINGULARITY) {
                            return Component.translatable("gui.tooltips.ae2.Singularity",
                                    CondenserOutput.SINGULARITY.requiredPower);
                        }
                        return Component.translatable("gui.tooltips.ae2.Trash");
                    }));
            this.gui = gui;
        }

        @Override
        public void drawBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
            super.drawBackground(guiGraphics, mouseX, mouseY, partialTicks);
            guiGraphics.blit(AppEng.makeId("textures/guis/states.png"),
                    135, 34, // position in gui
                    18, 18, // size in gui
                    gui.tile.getMode().ordinal() * 16, 112, // position in texture
                    16, 16, // size in texture
                    256, 256);// size of texture
        }

    }

}
