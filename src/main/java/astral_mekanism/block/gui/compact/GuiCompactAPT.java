package astral_mekanism.block.gui.compact;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.interf.ICompactAPT;
import fr.iglee42.evolvedmekanism.client.bars.GuiCustomDynamicHorizontalRateBar;
import fr.iglee42.evolvedmekanism.config.EMConfig;
import fr.iglee42.evolvedmekanism.jei.EMJEI;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.bar.GuiBar.IBarInfoHandler;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiEnergyGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.util.text.EnergyDisplay;
import mekanism.common.util.text.TextUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiCompactAPT<BE extends TileEntityConfigurableMachine & ICompactAPT<BE>>
        extends GuiConfigurableTile<BE, MekanismTileContainer<BE>> {

    public GuiCompactAPT(MekanismTileContainer<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiGasGauge(tile::getInputTank, () -> tile.getGasTanks(null),
                GaugeType.SMALL_MED, this, 7, 5));
        addRenderableWidget(new GuiEnergyGauge(tile.getEnergyContainer(), GaugeType.SMALL_MED, this, 151, 5));
        addRenderableWidget(new GuiCustomDynamicHorizontalRateBar(this, new IBarInfoHandler() {
            @Override
            public Component getTooltip() {
                return MekanismLang.PROGRESS.translate(TextUtils.getPercent(Math.max(0, tile.getProgressScaled())));
            }

            @Override
            public double getLevel() {
                return Math.max(0.0D, tile.getProgressScaled());
            }
        }, 7, 55, 160, progress -> tile.getColor()));
        addRenderableWidget(new GuiInnerScreen(this, 47, 17, 82, 36, () -> {
            List<Component> list = new ArrayList<>();
            boolean active = tile.getActive();
            list.add(MekanismLang.STATUS.translate(active ? MekanismLang.ACTIVE : MekanismLang.IDLE));
            if (active) {
                list.add(MekanismLang.USING
                        .translate(EnergyDisplay.of(EMConfig.general.aptEnergyConsumption.getOrDefault())));
            }
            return list;
        }).jeiCategories(EMJEI.APT));
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getEnergyUsage));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}