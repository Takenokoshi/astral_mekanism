package astral_mekanism.block.gui.normalmachine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.interf.IGreenhouse;
import astral_mekanism.jei.AMEJEIRecipeType;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiGreenhouse<BE extends TileEntityConfigurableMachine & IGreenhouse<BE>>
        extends GuiConfigurableTile<BE, MekanismTileContainer<BE>> {

    public GuiGreenhouse(MekanismTileContainer<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiFluidGauge(tile::getFluidTank, () -> tile.getFluidTanks(null),
                GaugeType.STANDARD, this, 27, 12));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), 170, 16));
        addRenderableWidget(new GuiProgress(tile::getScaledProgress, ProgressType.BAR, this, 68, 38))
                .jeiCategories(AMEJEIRecipeType.CROP_SOIL);
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getActive));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}
