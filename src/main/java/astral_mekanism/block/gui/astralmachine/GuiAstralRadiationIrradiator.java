package astral_mekanism.block.gui.astralmachine;

import org.jetbrains.annotations.NotNull;

import com.fxd927.mekanismelements.client.MSJEIRecipeType;

import astral_mekanism.block.blockentity.astralmachine.BEAstralRadiationIrradiator;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiHorizontalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiMergedChemicalTankGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.WarningTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAstralRadiationIrradiator
        extends GuiConfigurableTile<BEAstralRadiationIrradiator, MekanismTileContainer<BEAstralRadiationIrradiator>> {

    public GuiAstralRadiationIrradiator(MekanismTileContainer<BEAstralRadiationIrradiator> container, Inventory inv,
            Component title) {
        super(container, inv, title);
        dynamicSlots = true;
        titleLabelY = 4;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiHorizontalPowerBar(this, tile.getEnergyContainer(), 115, 75))
                .warning(WarningTracker.WarningType.NOT_ENOUGH_ENERGY,
                        tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY))
                .warning(WarningTracker.WarningType.NOT_ENOUGH_ENERGY_REDUCED_RATE,
                        tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY_REDUCED_RATE));
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getActive));
        addRenderableWidget(
                new GuiGasGauge(() -> tile.injectTank, () -> tile.getGasTanks(null), GaugeType.STANDARD, this, 28, 13))
                .warning(WarningTracker.WarningType.NO_MATCHING_RECIPE,
                        tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT));
        addRenderableWidget(
                new GuiMergedChemicalTankGauge<>(() -> tile.outputTank, () -> tile, GaugeType.STANDARD, this, 131, 13))
                .warning(WarningTracker.WarningType.NO_SPACE_IN_OUTPUT,
                        tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE));
        addRenderableWidget(new GuiProgress(tile::getActive, ProgressType.LARGE_RIGHT, this, 64, 40))
                .warning(WarningTracker.WarningType.INPUT_DOESNT_PRODUCE_OUTPUT,
                        tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT))
                .jeiCategories(MSJEIRecipeType.RADIATION_IRRADIATOR);
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
