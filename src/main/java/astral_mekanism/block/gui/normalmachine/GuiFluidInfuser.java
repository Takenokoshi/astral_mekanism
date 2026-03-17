package astral_mekanism.block.gui.normalmachine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.prefab.BEAbstractFluidInfuser;
import astral_mekanism.jei.AMEJEIRecipeType;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiHorizontalPowerBar;
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

public class GuiFluidInfuser<BE extends BEAbstractFluidInfuser>
        extends GuiConfigurableTile<BE, MekanismTileContainer<BE>> {

    public GuiFluidInfuser(MekanismTileContainer<BE> container, Inventory inv,
            Component title) {
        super(container, inv, title);
        dynamicSlots = true;
        this.titleLabelX = 8;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiHorizontalPowerBar(this, tile.getEnergyContainer(), 115, 75))
                .warning(WarningType.NOT_ENOUGH_ENERGY,
                        tile.getWarningCheck(RecipeError.NOT_ENOUGH_ENERGY))
                .warning(WarningType.NOT_ENOUGH_ENERGY_REDUCED_RATE,
                        tile.getWarningCheck(RecipeError.NOT_ENOUGH_ENERGY_REDUCED_RATE));
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getEnergyUsed));
        addRenderableWidget(new GuiFluidGauge(tile::getInputTankA, () -> tile.getFluidTanks(null),
                GaugeType.STANDARD, this, 25, 13))
                .warning(WarningType.NO_MATCHING_RECIPE,
                        tile.getWarningCheck(BEAbstractFluidInfuser.NOT_ENOUGH_FLUIDA_INPUT_ERROR));
        addRenderableWidget(new GuiFluidGauge(tile::getInputTankB, () -> tile.getFluidTanks(null),
                GaugeType.STANDARD, this, 133, 13))
                .warning(WarningType.NO_MATCHING_RECIPE,
                        tile.getWarningCheck(BEAbstractFluidInfuser.NOT_ENOUGH_FLUIDB_INPUT_ERROR));
        addRenderableWidget(new GuiFluidGauge(tile::getOutputTank, () -> tile.getFluidTanks(null),
                GaugeType.STANDARD, this, 79, 4))
                .warning(WarningType.NO_SPACE_IN_OUTPUT,
                        tile.getWarningCheck(RecipeError.NOT_ENOUGH_OUTPUT_SPACE));
        addRenderableWidget(new GuiProgress(tile::getActive, ProgressType.SMALL_RIGHT, this, 47, 39))
                .warning(WarningType.INPUT_DOESNT_PRODUCE_OUTPUT,
                        tile.getWarningCheck(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT))
                .jeiCategories(AMEJEIRecipeType.FLUID_INFUSER_RECIPE);
        addRenderableWidget(new GuiProgress(tile::getActive, ProgressType.SMALL_LEFT, this, 101, 39))
                .warning(WarningType.INPUT_DOESNT_PRODUCE_OUTPUT,
                        tile.getWarningCheck(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT))
                .jeiCategories(AMEJEIRecipeType.FLUID_INFUSER_RECIPE);

    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        drawString(guiGraphics, title, titleLabelX, titleLabelY, titleTextColor());
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}
