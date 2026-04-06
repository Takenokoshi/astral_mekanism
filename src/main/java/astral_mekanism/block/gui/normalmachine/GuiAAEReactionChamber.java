package astral_mekanism.block.gui.normalmachine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.base.BlockEntityRecipeMachine;
import astral_mekanism.block.blockentity.interf.IAAEReactionChamber;
import astral_mekanism.jei.AMEJEIRecipeType;
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
import net.pedroksl.advanced_ae.recipes.ReactionChamberRecipe;

public class GuiAAEReactionChamber<BE extends BlockEntityRecipeMachine<ReactionChamberRecipe> & IAAEReactionChamber<BE>>
        extends GuiConfigurableTile<BE, MekanismTileContainer<BE>> {

    public GuiAAEReactionChamber(MekanismTileContainer<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiFluidGauge(tile::getInputTank, () -> tile.getFluidTanks(null),
                GaugeType.STANDARD, this, 7, 13));
        addRenderableWidget(new GuiFluidGauge(tile::getOutputTank, () -> tile.getFluidTanks(null),
                GaugeType.STANDARD, this, 144, 13));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), 164, 15))
                .warning(WarningType.NOT_ENOUGH_ENERGY, tile.getWarningCheck(RecipeError.NOT_ENOUGH_ENERGY));
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getActive));
        addRenderableWidget(new GuiProgress(tile::getScaledProgress, ProgressType.BAR, this, 86, 38))
                .warning(WarningType.INPUT_DOESNT_PRODUCE_OUTPUT,
                        tile.getWarningCheck(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT))
                .jeiCategories(AMEJEIRecipeType.AAE_REACTION);
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
