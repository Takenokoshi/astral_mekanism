package astral_mekanism.block.gui.basemachine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.basemachine.BEAMEPaintingMachine;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiPigmentGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.GuiProgress.ColorDetails;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAMEPaintingMachine<BE extends BEAMEPaintingMachine> extends GuiConfigurableTile<BE,MekanismTileContainer<BE>> {

    public GuiAMEPaintingMachine(MekanismTileContainer<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots=true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), 164, 15))
              .warning(WarningType.NOT_ENOUGH_ENERGY, tile.getWarningCheck(RecipeError.NOT_ENOUGH_ENERGY));
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getActive));
        addRenderableWidget(new GuiPigmentGauge(() -> tile.pigmentTank, () -> tile.getPigmentTanks(null), GaugeType.STANDARD, this, 25, 13))
              .warning(WarningType.NO_MATCHING_RECIPE, tile.getWarningCheck(RecipeError.NOT_ENOUGH_SECONDARY_INPUT));
        addRenderableWidget(new GuiProgress(tile::getActive, ProgressType.LARGE_RIGHT, this, 64, 39).colored(new PigmentColorDetails()))
              .warning(WarningType.INPUT_DOESNT_PRODUCE_OUTPUT, tile.getWarningCheck(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT)).jeiCategories(MekanismJEIRecipeType.PAINTING);
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

    private class PigmentColorDetails implements ColorDetails {

        @Override
        public int getColorFrom() {
            if (tile == null) {
                //Should never actually be null, but just in case check it to make intellij happy
                return 0xFFFFFFFF;
            }
            int tint = tile.pigmentTank.getType().getColorRepresentation();
            if ((tint & 0xFF000000) == 0) {
                return 0xFF000000 | tint;
            }
            return tint;
        }

        @Override
        public int getColorTo() {
            return 0xFFFFFFFF;
        }
    }
    
}
