package astral_mekanism.block.gui.prefab;

import java.util.List;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.prefab.BEAbstractStorage;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiHorizontalPowerBar;
import mekanism.client.gui.element.button.GuiGasMode;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiMergedChemicalTankGauge;
import mekanism.client.gui.element.tab.GuiHeatTab;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAbstractStorage<STORAGE extends BEAbstractStorage, CONTAINER extends MekanismTileContainer<STORAGE>>
        extends GuiConfigurableTile<STORAGE, CONTAINER> {

    public GuiAbstractStorage(CONTAINER container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
        imageHeight = 274;
        imageWidth = 302;
        inventoryLabelX = 72;
        inventoryLabelY = 181;
    }

    @Override
    protected void addGuiElements() {
        final Supplier<STORAGE> storageSupplier = () -> tile;
        super.addGuiElements();
        addRenderableWidget(new GuiFluidGauge(tile::getFluidTank0, tile::getFluidTanks,
                GaugeType.STANDARD, this, 206, 18));
        addRenderableWidget(new GuiFluidGauge(tile::getFluidTank1, tile::getFluidTanks,
                GaugeType.STANDARD, this, 206, 94));
        addRenderableWidget(new GuiMergedChemicalTankGauge<>(tile::getChemicalTank0, storageSupplier,
                GaugeType.STANDARD, this, 260, 18));
        addRenderableWidget(new GuiMergedChemicalTankGauge<>(tile::getChemicalTank1, storageSupplier,
                GaugeType.STANDARD, this, 260, 94));
        addRenderableWidget(new GuiGasMode(this, 284, 80, true, tile::getGasMode0, tile.getBlockPos(), 0));
        addRenderableWidget(new GuiGasMode(this, 284, 156, true, tile::getGasMode1, tile.getBlockPos(), 1));
        addRenderableWidget(new GuiHorizontalPowerBar(this, tile.getEnergyContainer(), 242, 172));
        addRenderableWidget(new GuiHeatTab(this, () -> {
            Component temp = MekanismUtils.getTemperatureDisplay(tile.getTotalTemperature(),
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
        float widthThird = imageWidth / 3F;
        drawTextScaledBound(guiGraphics, title, widthThird - 7, titleLabelY, titleTextColor(), 2 * widthThird);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
