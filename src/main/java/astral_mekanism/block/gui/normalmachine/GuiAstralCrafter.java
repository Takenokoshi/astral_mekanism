package astral_mekanism.block.gui.normalmachine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.normalmachine.BEAstralCrafter;
import astral_mekanism.block.container.normal_machine.ContainerAstralCrafter;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAstralCrafter extends GuiConfigurableTile<BEAstralCrafter, ContainerAstralCrafter> {

    public GuiAstralCrafter(ContainerAstralCrafter container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
        imageHeight += 36;
        imageWidth += 36;
        inventoryLabelX += 18;
        inventoryLabelY += 36;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getActive));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), 188, 16)
                .warning(WarningType.NOT_ENOUGH_ENERGY,
                        tile.getWarningCheck(RecipeError.NOT_ENOUGH_ENERGY)));
        addRenderableWidget(new GuiFluidGauge(tile::getFluidTank, () -> tile.getFluidTanks(null),
                GaugeType.STANDARD, this, 6, 30));
        addRenderableWidget(new GuiGasGauge(tile::getGasTank, () -> tile.getGasTanks(null),
                GaugeType.STANDARD, this, 24, 30));
        addRenderableWidget(
                new GuiProgress(tile::getScaledProgress, ProgressType.RIGHT, this, 134, 60).jeiCategory(tile)
                        .warning(WarningType.INPUT_DOESNT_PRODUCE_OUTPUT,
                                tile.getWarningCheck(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT)));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
