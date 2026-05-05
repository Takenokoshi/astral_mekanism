package astral_mekanism.block.gui.basemachine;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.fxd927.mekanismelements.common.tile.machine.TileEntityAirCompressor;

import astral_mekanism.block.blockentity.basemachine.BEAMEAirCompressor;
import mekanism.api.chemical.gas.GasStack;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.MekanismLang;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.WarningTracker;
import mekanism.common.util.text.EnergyDisplay;
import mekanism.common.util.text.TextUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAMEAirCompressor<BE extends BEAMEAirCompressor>
        extends GuiConfigurableTile<BE, MekanismTileContainer<BE>> {

    public GuiAMEAirCompressor(MekanismTileContainer<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiInnerScreen(this, 54, 23, 80, 41, () -> {
            List<Component> list = new ArrayList<>();
            list.add(EnergyDisplay.of(tile.getEnergyContainer()).getTextComponent());
            GasStack gasStack = tile.gasTank.getStack();
            if (gasStack.isEmpty()) {
                list.add(MekanismLang.NO_GAS.translate());
            } else {
                list.add(MekanismLang.GENERIC_STORED_MB.translate(gasStack, TextUtils.format(gasStack.getAmount())));
            }
            return list;
        }));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), 164, 15))
                .warning(WarningTracker.WarningType.NOT_ENOUGH_ENERGY, () -> {
                    MachineEnergyContainer<BEAMEAirCompressor> energyContainer = tile.getEnergyContainer();
                    return energyContainer.getEnergyPerTick().greaterThan(energyContainer.getEnergy());
                });
        addRenderableWidget(new GuiGasGauge(() -> tile.gasTank, () -> tile.getGasTanks(null), GaugeType.STANDARD, this, 6, 13))
                .warning(WarningTracker.WarningType.NO_SPACE_IN_OUTPUT, () -> tile.gasTank.getNeeded() < TileEntityAirCompressor.COMPRESSED_AIR_STACK.getAmount());

        addRenderableWidget(new GuiEnergyTab(this, () -> {
            return List.of(EnergyDisplay.of(tile.getEnergyContainer()).getTextComponent());
        }));
    }


    @Override
    protected void drawForegroundText(@Nonnull GuiGraphics matrix, int mouseX, int mouseY) {
        renderTitleText(matrix);
        drawString(matrix, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(matrix, mouseX, mouseY);
    }

}
