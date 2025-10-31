package astral_mekanism.block.gui.normalmachine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.normalmachine.BEExpandedCrafter;
import astral_mekanism.block.container.normal_machine.ContainerExpandedCrafter;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiExpandedCrafter extends GuiConfigurableTile<BEExpandedCrafter, ContainerExpandedCrafter> {

    public GuiExpandedCrafter(ContainerExpandedCrafter container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
        imageHeight += 36;
        imageWidth += 18;
        inventoryLabelX += 9;
        inventoryLabelY += 18;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiFluidGauge(tile::getFluidTank, () -> tile.getFluidTanks(null),
                GaugeType.STANDARD, this, 7, 17));
        addRenderableWidget(new GuiGasGauge(tile::getGasTank, () -> tile.getGasTanks(null),
                GaugeType.STANDARD, this, 25, 17));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), 163, 16)
                .warning(WarningType.NOT_ENOUGH_ENERGY,
                        tile.getWarningCheck(RecipeError.NOT_ENOUGH_ENERGY)));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        float widthThird = imageWidth / 3F;
        drawTextScaledBound(guiGraphics, title, widthThird - 7, titleLabelY, titleTextColor(), 2 * widthThird);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
