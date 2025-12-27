package astral_mekanism.block.gui.normalmachine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.normalmachine.BEInfuseSynthesizer;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiInfusionGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiInfuseSynthesizer
        extends GuiConfigurableTile<BEInfuseSynthesizer, MekanismTileContainer<BEInfuseSynthesizer>> {

    public GuiInfuseSynthesizer(MekanismTileContainer<BEInfuseSynthesizer> container, Inventory inv,
            Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiInfusionGauge(tile::getInfusionTank, () -> tile.getInfusionTanks(null),
                GaugeType.SMALL, this, 114, 23))
                .warning(WarningType.NO_SPACE_IN_OUTPUT, tile.getWarningCheck(RecipeError.NOT_ENOUGH_OUTPUT_SPACE));
        addRenderableWidget(new GuiProgress(tile::getActive, ProgressType.SMALL_RIGHT, this, 82, 39))
                .jeiCategories(MekanismJEIRecipeType.INFUSION_CONVERSION)
                .warning(WarningType.NO_MATCHING_RECIPE, tile.getWarningCheck(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
