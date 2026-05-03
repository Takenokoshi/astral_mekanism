package astral_mekanism.block.gui.other;

import org.jetbrains.annotations.NotNull;

import mekanism.client.gui.GuiConfigurableTile;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiNothing<BE extends TileEntityConfigurableMachine>
        extends GuiConfigurableTile<BE, MekanismTileContainer<BE>> {

    public GuiNothing(MekanismTileContainer<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
