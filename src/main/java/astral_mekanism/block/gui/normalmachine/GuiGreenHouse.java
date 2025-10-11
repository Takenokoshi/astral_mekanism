package astral_mekanism.block.gui.normalmachine;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.normalmachine.BEGreenHouse;
import astral_mekanism.jei.AstralMekanismJEIRecipeType;
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

public class GuiGreenHouse
		extends GuiConfigurableTile<BEGreenHouse, MekanismTileContainer<BEGreenHouse>> {

	public GuiGreenHouse(MekanismTileContainer<BEGreenHouse> container, Inventory inv, Component title) {
		super(container, inv, title);
		dynamicSlots = true;
	}

	@Override
	protected void addGuiElements() {
		super.addGuiElements();
		addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getActive));
		addRenderableWidget(new GuiFluidGauge(() -> tile.inputFluidTank, () -> List.of(tile.inputFluidTank),
				GaugeType.STANDARD, this, 5, 10)
				.warning(WarningType.NO_MATCHING_RECIPE,
						tile.getWarningCheck(
								BEGreenHouse.NOT_ENOUGH_FLUID_INPUT_ERROR)));
		addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), 163, 16)
				.warning(WarningType.NOT_ENOUGH_ENERGY,
						tile.getWarningCheck(RecipeError.NOT_ENOUGH_ENERGY)));
		addRenderableWidget(new GuiProgress(tile::getScaledProgress, ProgressType.RIGHT, this, 77, 38))
				.jeiCategories(AstralMekanismJEIRecipeType.Greenhouse_recipe)
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
