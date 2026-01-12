package astral_mekanism.block.gui.compact;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.AstralMekanism;
import astral_mekanism.block.blockentity.compact.BECompactTEP;
import astral_mekanism.block.container.prefab.ContainerPagedMachine;
import astral_mekanism.block.gui.element.PagedGuiFluidGauge;
import astral_mekanism.block.gui.element.PagedGuiHorizontalRateBar;
import astral_mekanism.block.gui.element.PagedGuiInnerScreen;
import astral_mekanism.block.gui.element.PagedGuiTextField;
import astral_mekanism.block.gui.element.PagedGuiVerticalPowerBar;
import astral_mekanism.block.gui.prefab.GuiPagedMachine;
import astral_mekanism.network.to_server.PacketGuiCompactTEP;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.element.bar.GuiBar.IBarInfoHandler;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.client.gui.element.tab.GuiHeatTab;
import mekanism.client.gui.element.text.GuiTextField;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.MekanismLang;
import mekanism.common.content.evaporation.EvaporationMultiblockData;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import mekanism.common.util.text.EnergyDisplay;
import mekanism.common.util.text.InputValidator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiCompactTEP extends GuiPagedMachine<BECompactTEP> {

    private GuiTextField energyUsageField;

    public GuiCompactTEP(ContainerPagedMachine<BECompactTEP> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    protected int getMaxPage() {
        return 2;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new PagedGuiInnerScreen(this, 48, 19, 80, 40, () -> {
            return List.of(MekanismLang.MULTIBLOCK_FORMED.translate(),
                    MekanismLang.EVAPORATION_HEIGHT.translate(18),
                    MekanismLang.TEMPERATURE.translate(MekanismUtils.getTemperatureDisplay(
                            tile.heatCapacitor.getTemperature(), TemperatureUnit.KELVIN,
                            true)),
                    MekanismLang.FLUID_PRODUCTION.translate((int) (tile.getTempMultipleier())));
        }, 0)).spacing(1).jeiCategories(MekanismJEIRecipeType.EVAPORATING);
        addRenderableWidget(new PagedGuiHorizontalRateBar(this, new IBarInfoHandler() {
            @Override
            public Component getTooltip() {
                return MekanismUtils.getTemperatureDisplay(tile.getMultiblock().getTemperature(),
                        TemperatureUnit.KELVIN, true);
            }

            @Override
            public double getLevel() {
                return Math.min(1, tile.getMultiblock().getTemperature()
                        / EvaporationMultiblockData.MAX_MULTIPLIER_TEMP);
            }
        }, 48, 63, 0)).warning(WarningType.INPUT_DOESNT_PRODUCE_OUTPUT,
                tile.getWarningCheck(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT));
        addRenderableWidget(
                new PagedGuiFluidGauge(tile::getInputTank, tile::getFluidTanks, GaugeType.STANDARD, this, 25, 13, 0))
                .warning(WarningType.NO_MATCHING_RECIPE,
                        tile.getWarningCheck(RecipeError.NOT_ENOUGH_INPUT));
        addRenderableWidget(
                new PagedGuiFluidGauge(tile::getOutputTank, tile::getFluidTanks, GaugeType.STANDARD, this, 133, 13, 0))
                .warning(WarningType.NO_SPACE_IN_OUTPUT,
                        tile.getWarningCheck(RecipeError.NOT_ENOUGH_OUTPUT_SPACE));
        addRenderableWidget(new GuiHeatTab(this, () -> {
            Component environment = MekanismUtils.getTemperatureDisplay(
                    tile.getMultiblock().getLastEnvironmentLoss(), TemperatureUnit.KELVIN, false);
            return Collections.singletonList(MekanismLang.DISSIPATED_RATE.translate(environment));
        }));
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getEnergyUsed));
        addRenderableWidget(new PagedGuiVerticalPowerBar(this, tile.getEnergyContainer(), 164, 15, 1));
        addRenderableWidget(new PagedGuiInnerScreen(this, 48, 23, 80, 42, () -> List.of(
                MekanismLang.TEMPERATURE.translate(
                        MekanismUtils.getTemperatureDisplay(tile.getTotalTemperature(), TemperatureUnit.KELVIN, true)),
                MekanismLang.RESISTIVE_HEATER_USAGE
                        .translate(EnergyDisplay.of(tile.getEnergyContainer().getEnergyPerTick()))),
                1)).clearFormat();
        energyUsageField = addRenderableWidget(new PagedGuiTextField(this, 50, 51, 76, 12, 1));
        energyUsageField.setMaxLength(40);
        energyUsageField.setInputValidator(InputValidator.DIGIT)
                .configureDigitalInput(this::setEnergyUsage);
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

    private void setEnergyUsage() {
        if (!energyUsageField.getText().isEmpty()) {
            FloatingLong toSet = null;
            try {
                toSet = MekanismUtils.convertToJoules(FloatingLong.parseFloatingLong(energyUsageField.getText()));
            } catch (Exception e) {
                toSet = null;
            }
            if (toSet != null) {
                AstralMekanism.packetHandler().sendToServer(new PacketGuiCompactTEP(tile.getBlockPos(), toSet));
            }
            energyUsageField.setText("");
        }
    }

}
