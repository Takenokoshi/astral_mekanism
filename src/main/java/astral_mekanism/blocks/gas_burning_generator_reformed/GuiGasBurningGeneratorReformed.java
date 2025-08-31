package astral_mekanism.blocks.gas_burning_generator_reformed;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiEnergyGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiGasBurningGeneratorReformed extends GuiConfigurableTile<BlockEntityGasBurningGeneratorReformed, MekanismTileContainer<BlockEntityGasBurningGeneratorReformed>> {

    public GuiGasBurningGeneratorReformed(MekanismTileContainer<BlockEntityGasBurningGeneratorReformed> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiEnergyGauge(tile.energyContainer, GaugeType.SMALL_MED, this, 100, 18));
        addRenderableWidget(
                new GuiGasGauge(() -> tile.gasTank, () -> List.of(tile.gasTank), GaugeType.SMALL_MED, this, 60, 18));
    }

	

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        float widthThird = imageWidth / 3F;
        drawTextScaledBound(guiGraphics, title, widthThird - 7, titleLabelY, titleTextColor(), 2 * widthThird);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
