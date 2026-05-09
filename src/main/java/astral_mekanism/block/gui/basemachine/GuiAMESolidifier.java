package astral_mekanism.block.gui.basemachine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.astralmachine.BEAstralSolidifier;
import astral_mekanism.block.blockentity.basemachine.BEAMESolidifier;
import fr.iglee42.evolvedmekanism.jei.EMJEI;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAMESolidifier<BE extends BEAMESolidifier>
        extends GuiConfigurableTile<BE, MekanismTileContainer<BE>> {

    public GuiAMESolidifier(MekanismTileContainer<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getActive));
        addRenderableWidget(new GuiFluidGauge(tile::getInputFluidTank, () -> tile.getFluidTanks(null),
                GaugeType.STANDARD, this, 28, 10)
                .warning(WarningType.NO_MATCHING_RECIPE,
                        tile.getWarningCheck(BEAstralSolidifier.NOT_ENOUGH_FLUID_INPUT_ERROR)));
        addRenderableWidget(new GuiFluidGauge(tile::getInputFluidExtraTank, () -> tile.getFluidTanks(null),
                GaugeType.STANDARD, this, 5, 10)
                .warning(WarningType.NO_MATCHING_RECIPE,
                        tile.getWarningCheck(BEAstralSolidifier.NOT_ENOUGH_EXTRA_FLUID_INPUT_ERROR)));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), 163, 16)
                .warning(WarningType.NOT_ENOUGH_ENERGY, tile.getWarningCheck(RecipeError.NOT_ENOUGH_ENERGY)));
        addRenderableWidget(new GuiProgress(tile::getActive, ProgressType.RIGHT, this, 77, 38))
                .jeiCategories(EMJEI.SOLIDIFICATION)
                .warning(WarningType.INPUT_DOESNT_PRODUCE_OUTPUT,
                        tile.getWarningCheck(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        float widthThird = imageWidth / 3F;
        drawTextScaledBound(guiGraphics, title, widthThird - 7, titleLabelY, titleTextColor(), 2 * widthThird);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
