package astral_mekanism.block.gui.compact;

import java.util.List;

import astral_mekanism.AstralMekanism;
import astral_mekanism.AstralMekanismLang;
import astral_mekanism.block.blockentity.compact.BECompactFIR;
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
import mekanism.generators.client.jei.GeneratorsJEIRecipeType;
import mekanism.generators.common.GeneratorsLang;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiCompactFissionReactor
        extends GuiConfigurableTile<BECompactFIR, MekanismTileContainer<BECompactFIR>> {

    private GuiTextField field;

    public GuiCompactFissionReactor(MekanismTileContainer<BECompactFIR> container, Inventory inv,
            Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(
                new GuiGasGauge(tile::getFuelTank, tile::getGasTanks,
                        GaugeType.STANDARD, this, 7, 4))
                .setLabel(GeneratorsLang.FISSION_FUEL_TANK.translateColored(EnumColor.DARK_GREEN));
        addRenderableWidget(
                new GuiFluidGauge(tile::getFluidCoolantTank, () -> tile.getFluidTanks(null),
                        GaugeType.SMALL, this, 25, 34))
                .setLabel(AstralMekanismLang.LABEL_FLUID_COOLANT.translateColored(EnumColor.DARK_BLUE));
        addRenderableWidget(
                new GuiGasGauge(tile::getGasCoolantTank, tile::getGasTanks,
                        GaugeType.SMALL, this, 43, 34))
                .setLabel(AstralMekanismLang.LABEL_GAS_COOLANT.translateColored(EnumColor.INDIGO));
        addRenderableWidget(
                new GuiGasGauge(tile::getHeatedGasTank, tile::getGasTanks,
                        GaugeType.SMALL, this, 115, 34))
                .setLabel(AstralMekanismLang.LABEL_HEATED_GAS_COOLANT.translateColored(EnumColor.RED));
        addRenderableWidget(
                new GuiGasGauge(tile::getHeatedFluidTank, tile::getGasTanks,
                        GaugeType.SMALL, this, 133, 34))
                .setLabel(AstralMekanismLang.LABEL_HEATED_FLUID_COOLANT.translateColored(EnumColor.DARK_RED));
        addRenderableWidget(
                new GuiGasGauge(tile::getWasteTank, tile::getGasTanks,
                        GaugeType.STANDARD, this, 151, 4))
                .setLabel(GeneratorsLang.FISSION_WASTE_TANK.translateColored(EnumColor.BROWN));
        addRenderableWidget(new GuiInnerScreen(this, 25, 4, 126, 30, () -> {
            return List.of(this.title, GeneratorsLang.GAS_BURN_RATE.translate(this.tile.getEfficiency()));
        }).clearFormat()).jeiCategories(GeneratorsJEIRecipeType.FISSION);
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
