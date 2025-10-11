package astral_mekanism.block.gui.normalmachine;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.normalmachine.BEMelter;
import astral_mekanism.jei.AstralMekanismJEIRecipeType;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiBar.IBarInfoHandler;
import mekanism.client.gui.element.bar.GuiHorizontalRateBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiHeatTab;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiMelter
		extends GuiConfigurableTile<BEMelter, MekanismTileContainer<BEMelter>> {

	public GuiMelter(MekanismTileContainer<BEMelter> container, Inventory inv, Component title) {
		super(container, inv, title);
		dynamicSlots = true;
	}

	@Override
	protected void addGuiElements() {
		super.addGuiElements();
		addRenderableWidget(new GuiFluidGauge(() -> tile.outputFluidTank, () -> List.of(tile.outputFluidTank),
				GaugeType.STANDARD, this, 133, 10))
				.warning(WarningType.NO_SPACE_IN_OUTPUT, tile.getWarningCheck(
						BEMelter.NOT_ENOUGH_SPACE_FLUID_OUTPUT_ERROR));
		addRenderableWidget(new GuiHorizontalRateBar(this, new IBarInfoHandler() {
			@Override
			public Component getTooltip() {
				return MekanismUtils.getTemperatureDisplay(tile.heatCapacitor.getTemperature(),
						TemperatureUnit.KELVIN, true);
			};

			@Override
			public double getLevel() {
				return Math.min(1, tile.heatCapacitor.getTemperature() / 100000);
			}

		}, 48, 63));
		addRenderableWidget(new GuiHeatTab(this,
				() -> Collections.singletonList(MekanismLang.DISSIPATED_RATE.translate(
						MekanismUtils.getTemperatureDisplay(tile.getLastEnvironmentLoss(),
								TemperatureUnit.KELVIN, false)))));
		addRenderableWidget(new GuiProgress(tile::getScaledProgress, ProgressType.RIGHT, this, 77, 38))
				.jeiCategories(AstralMekanismJEIRecipeType.Melter_recipe)
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
