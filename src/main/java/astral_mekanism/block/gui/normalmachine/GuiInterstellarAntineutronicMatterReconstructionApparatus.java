package astral_mekanism.block.gui.normalmachine;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.normalmachine.BEInterstellarAntineutronicMatterReconstructionApparatus;
import astral_mekanism.block.container.prefab.ContainerMachineCustomSize;
import astral_mekanism.jei.AMEJEIRecipeType;
import fr.iglee42.evolvedmekanism.client.bars.GuiCustomDynamicHorizontalRateBar;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiEnergyGauge;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.lib.Color;
import mekanism.common.lib.Color.ColorFunction;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiInterstellarAntineutronicMatterReconstructionApparatus extends
        GuiConfigurableTile<BEInterstellarAntineutronicMatterReconstructionApparatus, ContainerMachineCustomSize<BEInterstellarAntineutronicMatterReconstructionApparatus>> {

    public GuiInterstellarAntineutronicMatterReconstructionApparatus(
            ContainerMachineCustomSize<BEInterstellarAntineutronicMatterReconstructionApparatus> container, Inventory inv,
            Component title) {
        super(container, inv, title);
        dynamicSlots = true;
        imageWidth += 46;
        imageHeight += 16;
        inventoryLabelX += 23;
        inventoryLabelY += 16;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getActive));
        addRenderableWidget(new GuiFluidGauge(tile::getFluidTank, () -> tile.getFluidTanks(null),
                GaugeType.STANDARD, this, 7, 17));
        addRenderableWidget(new GuiGasGauge(tile::getInputGasTank, () -> tile.getGasTanks(null),
                GaugeType.STANDARD, this, 30, 17));
        addRenderableWidget(new GuiGasGauge(tile::getOutputGasTank, () -> tile.getGasTanks(null),
                GaugeType.STANDARD, this, 174, 17));
        addRenderableWidget(new GuiEnergyGauge(tile.getEnergyContainer(),
                GaugeType.STANDARD, this, 197, 17));
        var bar = addRenderableWidget(
                new GuiCustomDynamicHorizontalRateBar(this, tile::getScaledProgress, 30, 79, 160));
        addRenderableWidget(new GuiInnerScreen(this, 70, 17, 82, 60, () -> {
            List<Component> result = new ArrayList<>();
            result.add(Component
                    .literal(tile.getItemNotConsumed() ? "Item will not be consumed." : "Item will be consumed."));
            result.add(Component.literal("Process per tick:" + tile.getProcessingSpeed()));
            return result;
        })).jeiCategories(AMEJEIRecipeType.RECONSTRUCTION);
        if (!tile.getInputGasTank().isEmpty()) {
            bar.setColorFunction(
                    ColorFunction.scale(Color.rgb(tile.getInputGasTank().getStack().getChemicalTint()), Color.WHITE));
        }
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
