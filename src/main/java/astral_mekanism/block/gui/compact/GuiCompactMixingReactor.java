package astral_mekanism.block.gui.compact;

import java.util.List;

import astral_mekanism.AstralMekanism;
import astral_mekanism.AstralMekanismLang;
import astral_mekanism.block.blockentity.prefab.BEAbstractCompactMixingReactor;
import astral_mekanism.network.to_server.PacketGuiSetLong;
import mekanism.api.text.EnumColor;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.tab.GuiHeatTab;
import mekanism.client.gui.element.text.GuiTextField;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import mekanism.common.util.text.InputValidator;
import mekanism.generators.common.GeneratorsLang;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiCompactMixingReactor<BE extends BEAbstractCompactMixingReactor>
        extends GuiConfigurableTile<BE, MekanismTileContainer<BE>> {

    private GuiTextField field;

    public GuiCompactMixingReactor(MekanismTileContainer<BE> container, Inventory inv,
            Component title) {
        super(container, inv, title);
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiGasGauge(tile::getLeftFuelTank, () -> tile.getGasTanks(null),
                GaugeType.STANDARD, this, 7, 4))
                .setLabel(AstralMekanismLang.LABEL_LEFT_FUEL.translateColored(EnumColor.RED));
        addRenderableWidget(new GuiGasGauge(tile::getMixedFuelTank, () -> tile.getGasTanks(null),
                GaugeType.STANDARD, this, 25, 4))
                .setLabel(AstralMekanismLang.LABEL_MIXED_FUEL.translateColored(EnumColor.PURPLE));
        addRenderableWidget(new GuiGasGauge(tile::getRightFuelTank, () -> tile.getGasTanks(null),
                GaugeType.STANDARD, this, 43, 4))
                .setLabel(AstralMekanismLang.LABEL_RIGHT_FUEL.translateColored(EnumColor.BRIGHT_GREEN));
        addRenderableWidget(new GuiGasGauge(tile::getSteamTank, () -> tile.getGasTanks(null),
                GaugeType.SMALL, this, 115, 34))
                .setLabel(GeneratorsLang.FISSION_HEATED_COOLANT_TANK.translateColored(EnumColor.WHITE));
        addRenderableWidget(new GuiFluidGauge(tile::getWaterTank, () -> tile.getFluidTanks(null),
                GaugeType.SMALL, this, 151, 34));
        addRenderableWidget(new GuiInnerScreen(this, 61, 4, 108, 30, () -> {
            return List.of(this.title, GeneratorsLang.INSUFFICIENT_FUEL.translate(tile.getMixingRate()));
        }).clearFormat());
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

        addRenderableWidget(new GuiInnerScreen(this, 61, 34, 54, 12));
        field = addRenderableWidget(new GuiTextField(this, 61, 34, 54, 12));
        field.setMaxLength(32);
        field.setInputValidator(InputValidator.DIGIT).configureDigitalInput(this::setEfficiency);
        field.setFocused(true);
    }

    private void setEfficiency() {
        if (!field.getText().isEmpty()) {
            try {
                AstralMekanism.packetHandler()
                        .sendToServer(new PacketGuiSetLong(0, Long.parseLong(field.getText()), tile.getBlockPos()));
            } catch (NumberFormatException ignored) {
            }
            field.setText("");
        }
    }

}
