package astral_mekanism.block.gui.basemachine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.astralmachine.BEAstralChemixer;
import astral_mekanism.block.blockentity.basemachine.BEAMEChemixer;
import fr.iglee42.evolvedmekanism.jei.EMJEI;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAMEChemixer<BE extends BEAMEChemixer> extends GuiConfigurableTile<BE, MekanismTileContainer<BE>> {

    public GuiAMEChemixer(MekanismTileContainer<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiGasGauge(tile::getInputGasTank, () -> tile.getGasTanks(null),
                GaugeType.STANDARD, this, 28, 10))
                .warning(WarningType.NO_MATCHING_RECIPE,
                        tile.getWarningCheck(BEAstralChemixer.NOT_ENOUGH_GAS_INPUT_ERROR));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), 164, 15))
                .warning(WarningType.NOT_ENOUGH_ENERGY, tile.getWarningCheck(RecipeError.NOT_ENOUGH_ENERGY));
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getActive));
        addRenderableWidget(new GuiProgress(tile::getActive, ProgressType.BAR, this, 86, 38))
                .jeiCategories(EMJEI.CHEMIXING)
                .warning(WarningType.INPUT_DOESNT_PRODUCE_OUTPUT,
                        tile.getWarningCheck(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
