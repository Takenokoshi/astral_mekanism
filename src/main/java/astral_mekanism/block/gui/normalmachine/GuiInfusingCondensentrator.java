package astral_mekanism.block.gui.normalmachine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.basemachine.BEAbstractInfusingCondensentrator;
import astral_mekanism.jei.AMEJEIRecipeType;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiDumpButton;
import mekanism.client.gui.element.bar.GuiChemicalBar;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiInfusingCondensentrator<BE extends BEAbstractInfusingCondensentrator>
        extends GuiConfigurableTile<BE, MekanismTileContainer<BE>> {

    public GuiInfusingCondensentrator(MekanismTileContainer<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), 164, 15))
                .warning(WarningType.NOT_ENOUGH_ENERGY, tile.getWarningCheck(RecipeError.NOT_ENOUGH_ENERGY));
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getActive));
        addRenderableWidget(new GuiProgress(tile::getActive, ProgressType.RIGHT, this, 72, 37))
                .warning(WarningType.INPUT_DOESNT_PRODUCE_OUTPUT,
                        tile.getWarningCheck(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT))
                .jeiCategories(AMEJEIRecipeType.INFUSING_CONDENSE);
        addRenderableWidget(new GuiChemicalBar<>(this,
                GuiChemicalBar.getProvider(tile.getInfusionTank(), tile.getInfusionTanks(null)), 7, 15, 4, 52, false))
                .warning(WarningType.NO_MATCHING_RECIPE, tile.getWarningCheck(RecipeError.NOT_ENOUGH_SECONDARY_INPUT));
        addRenderableWidget(new GuiDumpButton<>(this, tile, 16, 59));
        addRenderableWidget(new GuiGasGauge(tile::getGasTank, () -> tile.getGasTanks(null),
                GaugeType.STANDARD, this, 50, 12));
        addRenderableWidget(new GuiFluidGauge(tile::getOutputTank, () -> tile.getFluidTanks(null),
                GaugeType.STANDARD, this, 108, 12));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
