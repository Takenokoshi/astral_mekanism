package astral_mekanism.block.gui.appliedmachine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.appliedmachine.BEAppliedElectrolyticSeparator;
import astral_mekanism.block.gui.element.GuiMEKeySlot;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedElectrolyticSeparator
        extends GuiMekanismTile<BEAppliedElectrolyticSeparator, MekanismTileContainer<BEAppliedElectrolyticSeparator>> {

    public GuiAppliedElectrolyticSeparator(MekanismTileContainer<BEAppliedElectrolyticSeparator> container,
            Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots=true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiMEKeySlot(this, 63, 16, tile::getMeStorage, tile::getInputKey));
        addRenderableWidget(new GuiMEKeySlot(this, 115, 16, tile::getMeStorage, tile::getFirstOutputKey));
        addRenderableWidget(new GuiMEKeySlot(this, 115, 52, tile::getMeStorage, tile::getSecondOutputKey));
        addRenderableWidget(new GuiMEKeySlot(this, 38, 34, tile::getMeStorage, () -> tile.feKey));
        addRenderableWidget(new GuiUpArrow(this, 68, 38));
        addRenderableWidget(new GuiProgress(tile::getActive, ProgressType.BAR, this, 86, 38))
                .jeiCategories(MekanismJEIRecipeType.SEPARATING);
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        drawString(guiGraphics, title, titleLabelX, titleLabelY, titleTextColor());
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
