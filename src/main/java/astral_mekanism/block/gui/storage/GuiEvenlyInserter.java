package astral_mekanism.block.gui.storage;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.storage.BEEvenlyInserter;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiInfusionGauge;
import mekanism.client.gui.element.gauge.GuiPigmentGauge;
import mekanism.client.gui.element.gauge.GuiSlurryGauge;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiEvenlyInserter extends GuiConfigurableTile<BEEvenlyInserter, MekanismTileContainer<BEEvenlyInserter>> {

    public GuiEvenlyInserter(MekanismTileContainer<BEEvenlyInserter> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiFluidGauge(() -> tile.fluidTank, () -> tile.getFluidTanks(null),
                GaugeType.STANDARD, this, 27, 13));
        addRenderableWidget(new GuiGasGauge(() -> tile.gasTank, () -> tile.getGasTanks(null),
                GaugeType.STANDARD, this, 45, 13));
        addRenderableWidget(new GuiInfusionGauge(() -> tile.infusionTank, () -> tile.getInfusionTanks(null),
                GaugeType.STANDARD, this, 63, 13));
        addRenderableWidget(new GuiPigmentGauge(() -> tile.pigmentTank, () -> tile.getPigmentTanks(null),
                GaugeType.STANDARD, this, 81, 13));
        addRenderableWidget(new GuiSlurryGauge(() -> tile.slurryTank, () -> tile.getSlurryTanks(null),
                GaugeType.STANDARD, this, 99, 13));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
