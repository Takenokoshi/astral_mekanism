package astral_mekanism.blocks.universal_storage;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiHorizontalPowerBar;
import mekanism.client.gui.element.button.GuiGasMode;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiMergedChemicalTankGauge;
import mekanism.client.gui.element.tab.GuiHeatTab;
import mekanism.common.MekanismLang;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiUniversalStorage
		extends GuiConfigurableTile<BlockEntityUniversalStorage, ContainerUniversalStorage> {

	public GuiUniversalStorage(ContainerUniversalStorage container, Inventory inv,
			Component title) {
		super(container, inv, title);
		dynamicSlots = true;
		this.imageHeight = 274;
		this.imageWidth = 302;
		this.inventoryLabelX = 72;
		this.inventoryLabelY = 181;
	}

	@Override
	protected void addGuiElements() {
		super.addGuiElements();
		addRenderableWidget(
				new GuiFluidGauge(() -> tile.fluidTank0, () -> tile.getFluidTanks(null), GaugeType.STANDARD, this, 206,
						18));
		addRenderableWidget(
				new GuiFluidGauge(() -> tile.fluidTank1, () -> tile.getFluidTanks(null), GaugeType.STANDARD, this, 206,
						94));
		addRenderableWidget(
				new GuiMergedChemicalTankGauge<>(() -> tile.mergedChemicalTank0, () -> tile, GaugeType.STANDARD, this,
						260, 18));
		addRenderableWidget(
				new GuiMergedChemicalTankGauge<>(() -> tile.mergedChemicalTank1, () -> tile, GaugeType.STANDARD, this,
						260, 94));
		addRenderableWidget(new GuiGasMode(this, 284, 80, true, () -> tile.dumping0, tile.getBlockPos(), 0));
		addRenderableWidget(new GuiGasMode(this, 284, 156, true, () -> tile.dumping1, tile.getBlockPos(), 1));
		addRenderableWidget(new GuiHorizontalPowerBar(this, tile.energyContainer, 242, 172));
        addRenderableWidget(new GuiHeatTab(this, () -> {
            Component temp = MekanismUtils.getTemperatureDisplay(tile.getTotalTemperature(), TemperatureUnit.KELVIN, true);
            Component transfer = MekanismUtils.getTemperatureDisplay(tile.getLastTransferLoss(), TemperatureUnit.KELVIN, false);
            Component environment = MekanismUtils.getTemperatureDisplay(tile.getLastEnvironmentLoss(), TemperatureUnit.KELVIN, false);
            return List.of(MekanismLang.TEMPERATURE.translate(temp), MekanismLang.TRANSFERRED_RATE.translate(transfer), MekanismLang.DISSIPATED_RATE.translate(environment));
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
