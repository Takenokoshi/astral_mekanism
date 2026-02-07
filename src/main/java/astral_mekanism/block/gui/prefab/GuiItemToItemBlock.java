package astral_mekanism.block.gui.prefab;

import org.jetbrains.annotations.NotNull;

import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiItemToItemBlock<BE extends TileEntityRecipeMachine<ItemStackToItemStackRecipe>>
        extends GuiConfigurableTile<BE, MekanismTileContainer<BE>> {

    public GuiItemToItemBlock(MekanismTileContainer<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiProgress(tile::getActive, ProgressType.LARGE_RIGHT, this, 64, 39))
                .jeiCategory(tile).warning(WarningType.INPUT_DOESNT_PRODUCE_OUTPUT,
                        tile.getWarningCheck(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
