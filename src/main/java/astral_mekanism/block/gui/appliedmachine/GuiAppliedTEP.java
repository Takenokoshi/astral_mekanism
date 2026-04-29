package astral_mekanism.block.gui.appliedmachine;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.appliedmachine.BEAppliedTEP;
import astral_mekanism.block.gui.element.GuiMEKeySlot;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiHeatTab;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedTEP extends GuiConfigurableTile<BEAppliedTEP, MekanismTileContainer<BEAppliedTEP>> {

    public GuiAppliedTEP(MekanismTileContainer<BEAppliedTEP> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiMEKeySlot(this, 63, 16, tile::getMeStorage, tile::getInputKey));
        addRenderableWidget(new GuiMEKeySlot(this, 115, 34, tile::getMeStorage, tile::getOutputKey));
        addRenderableWidget(new GuiUpArrow(this, 68, 38));
        addRenderableWidget(new GuiProgress(tile::getActive, ProgressType.BAR, this, 86, 38))
                .jeiCategories(MekanismJEIRecipeType.EVAPORATING);
        addRenderableWidget(new GuiHeatTab(this, () -> {
            Component temp = MekanismUtils.getTemperatureDisplay(tile.getHeatCapacitor().getTemperature(),
                    TemperatureUnit.KELVIN, true);
            Component transfer = MekanismUtils.getTemperatureDisplay(tile.getLastTransferLoss(),
                    TemperatureUnit.KELVIN, false);
            Component environment = MekanismUtils.getTemperatureDisplay(tile.getLastEnvironmentLoss(),
                    TemperatureUnit.KELVIN, false);
            return List.of(MekanismLang.TEMPERATURE.translate(temp), MekanismLang.TRANSFERRED_RATE.translate(transfer),
                    MekanismLang.DISSIPATED_RATE.translate(environment));
        }));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        drawString(guiGraphics, title, titleLabelX, titleLabelY, titleTextColor());
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
