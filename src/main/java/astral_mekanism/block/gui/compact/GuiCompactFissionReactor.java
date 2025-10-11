package astral_mekanism.block.gui.compact;

import java.util.List;

import astral_mekanism.block.blockentity.compact.BECompactFIR;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.tab.GuiHeatTab;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import mekanism.generators.common.GeneratorsLang;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiCompactFissionReactor
        extends GuiConfigurableTile<BECompactFIR, MekanismTileContainer<BECompactFIR>> {

    public GuiCompactFissionReactor(MekanismTileContainer<BECompactFIR> container, Inventory inv,
            Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(
                new GuiGasGauge(() -> tile.getFuelTank(), () -> tile.getGasTanks(),
                        GaugeType.STANDARD, this, 8, 4));
        addRenderableWidget(
                new GuiFluidGauge(() -> tile.getFluidCoolantTank(), () -> List.of(tile.getFluidCoolantTank()),
                        GaugeType.SMALL, this, 26, 34));
        addRenderableWidget(
                new GuiGasGauge(() -> tile.getGasCoolantTank(), () -> tile.getGasTanks(),
                        GaugeType.SMALL, this, 44, 34));
        addRenderableWidget(
                new GuiGasGauge(() -> tile.getHeatedGasTank(), () -> tile.getGasTanks(),
                        GaugeType.SMALL, this, 116, 34));
        addRenderableWidget(
                new GuiGasGauge(() -> tile.getHeatedFluidTank(), () -> tile.getGasTanks(),
                        GaugeType.SMALL, this, 134, 34));
        addRenderableWidget(
                new GuiGasGauge(() -> tile.getWasteTank(), () -> tile.getGasTanks(),
                        GaugeType.STANDARD, this, 152, 4));
        addRenderableWidget(new GuiInnerScreen(this, 26, 4, 126, 30, () -> {
            return List.of(this.title, GeneratorsLang.GAS_BURN_RATE.translate(this.tile.getEfficiency()));
        }));
        addRenderableWidget(new GuiHeatTab(this, () -> {
            Component temp = MekanismUtils.getTemperatureDisplay(tile.heatCapacitor.getTemperature(),
                    TemperatureUnit.KELVIN, true);
            Component transfer = MekanismUtils.getTemperatureDisplay(tile.getLastTransferLoss(),
                    TemperatureUnit.KELVIN, false);
            Component environment = MekanismUtils.getTemperatureDisplay(tile.getLastEnvironmentLoss(),
                    TemperatureUnit.KELVIN, false);
            return List.of(MekanismLang.TEMPERATURE.translate(temp), MekanismLang.TRANSFERRED_RATE.translate(transfer),
                    MekanismLang.DISSIPATED_RATE.translate(environment));
        }));
    }
}
