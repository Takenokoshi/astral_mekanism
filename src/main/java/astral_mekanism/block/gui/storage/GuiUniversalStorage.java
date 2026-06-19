package astral_mekanism.block.gui.storage;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.storage.BEAbstractStorage;
import astral_mekanism.block.container.prefab.ContainerPagedMachine;
import astral_mekanism.block.gui.element.PagedGuiEnergyGauge;
import astral_mekanism.block.gui.element.PagedGuiFluidGauge;
import astral_mekanism.block.gui.element.PagedGuiGasGauge;
import astral_mekanism.block.gui.element.PagedGuiInfusionGauge;
import astral_mekanism.block.gui.element.PagedGuiPigmentGauge;
import astral_mekanism.block.gui.element.PagedGuiSlurryGauge;
import astral_mekanism.block.gui.prefab.GuiPagedMachine;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.client.gui.element.tab.GuiHeatTab;
import mekanism.common.MekanismLang;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import mekanism.common.util.text.EnergyDisplay;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiUniversalStorage<BE extends BEAbstractStorage>
        extends GuiPagedMachine<BE> {

    public GuiUniversalStorage(ContainerPagedMachine<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    protected int getMaxPage() {
        return tile.getPagesForItem() + 6;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        int pageBase = tile.getPagesForItem();
        for (int index = 0; index < BEAbstractStorage.TANKS; index++) {
            int value = index;
            addRenderableWidget(new PagedGuiFluidGauge(
                    () -> tile.getFluidTank(value), () -> tile.getFluidTanks(null),
                    GaugeType.STANDARD, this, 13 + 36 * index, 15, pageBase) {
                @Override
                public void renderContents(GuiGraphics guiGraphics) {
                    if (!tile.tankCapacityChanged) {
                        super.renderContents(guiGraphics);
                    }
                }
            });
            addRenderableWidget(new PagedGuiGasGauge(
                    () -> tile.getGasTank(value), () -> tile.getGasTanks(null),
                    GaugeType.STANDARD, this, 13 + 36 * index, 15, pageBase + 1) {
                @Override
                public void renderContents(GuiGraphics guiGraphics) {
                    if (!tile.tankCapacityChanged) {
                        super.renderContents(guiGraphics);
                    }
                }
            });
            addRenderableWidget(new PagedGuiInfusionGauge(
                    () -> tile.getInfusionTank(value), () -> tile.getInfusionTanks(null),
                    GaugeType.STANDARD, this, 13 + 36 * index, 15, pageBase + 2) {
                @Override
                public void renderContents(GuiGraphics guiGraphics) {
                    if (!tile.tankCapacityChanged) {
                        super.renderContents(guiGraphics);
                    }
                }
            });
            addRenderableWidget(new PagedGuiPigmentGauge(
                    () -> tile.getPigmentTank(value), () -> tile.getPigmentTanks(null),
                    GaugeType.STANDARD, this, 13 + 36 * index, 15, pageBase + 3) {
                @Override
                public void renderContents(GuiGraphics guiGraphics) {
                    if (!tile.tankCapacityChanged) {
                        super.renderContents(guiGraphics);
                    }
                }
            });
            addRenderableWidget(new PagedGuiSlurryGauge(
                    () -> tile.getSlurryTank(value), () -> tile.getSlurryTanks(null),
                    GaugeType.STANDARD, this, 13 + 36 * index, 15, pageBase + 4) {
                @Override
                public void renderContents(GuiGraphics guiGraphics) {
                    if (!tile.tankCapacityChanged) {
                        super.renderContents(guiGraphics);
                    }
                }
            });
        }
        addRenderableWidget(new PagedGuiEnergyGauge(tile.getEnergyContainer(),
                GaugeType.WIDE, this, 55, 18, pageBase + 5) {
            @Override
            public void renderContents(GuiGraphics guiGraphics) {
                if (!tile.tankCapacityChanged) {
                    super.renderContents(guiGraphics);
                }
            }
        });
        addRenderableWidget(new GuiEnergyTab(this, () -> {
            return List.of(MekanismLang.NEEDED.translate(EnergyDisplay.of(tile.getEnergyContainer().getNeeded())));
        }));
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
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
        drawString(guiGraphics, getPageDiscription(), 100, -30, 0xffffff);
    }

    private Component getPageDiscription() {
        switch (page - tile.getPagesForItem()) {
            case 0:
                return MekanismLang.FLUIDS.translate();
            case 1:
                return MekanismLang.GAS.translate();
            case 2:
                return MekanismLang.INFUSE_TYPE.translate();
            case 3:
                return MekanismLang.PIGMENT.translate();
            case 4:
                return MekanismLang.SLURRY.translate();
            case 5:
                return MekanismLang.ENERGY.translate();
            default:
                return MekanismLang.ITEMS.translate();
        }
    }

}
