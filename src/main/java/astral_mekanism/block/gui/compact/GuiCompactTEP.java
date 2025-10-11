package astral_mekanism.block.gui.compact;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.compact.BECompactTEP;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.bar.GuiBar.IBarInfoHandler;
import mekanism.client.gui.element.bar.GuiHorizontalRateBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.tab.GuiHeatTab;
import mekanism.client.gui.element.tab.GuiWarningTab;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.MekanismLang;
import mekanism.common.content.evaporation.EvaporationMultiblockData;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.IWarningTracker;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiCompactTEP
		extends GuiConfigurableTile<BECompactTEP, MekanismTileContainer<BECompactTEP>> {

	public GuiCompactTEP(MekanismTileContainer<BECompactTEP> container, Inventory inv,
			Component title) {
		super(container, inv, title);
		dynamicSlots = true;
	}

	@Override
	protected void addGuiElements() {
		super.addGuiElements();
		addRenderableWidget(new GuiInnerScreen(this, 48, 19, 80, 40, () -> {
			return List.of(MekanismLang.MULTIBLOCK_FORMED.translate(),
					MekanismLang.EVAPORATION_HEIGHT.translate(18),
					MekanismLang.TEMPERATURE.translate(MekanismUtils.getTemperatureDisplay(
							tile.heatCapacitor.getTemperature(), TemperatureUnit.KELVIN,
							true)),
					MekanismLang.FLUID_PRODUCTION.translate(tile.getTempMultipleier()));
		}).spacing(1).jeiCategories(MekanismJEIRecipeType.EVAPORATING));
		addRenderableWidget(new GuiHorizontalRateBar(this, new IBarInfoHandler() {
			@Override
			public Component getTooltip() {
				return MekanismUtils.getTemperatureDisplay(tile.getMultiblock().getTemperature(),
						TemperatureUnit.KELVIN, true);
			}

			@Override
			public double getLevel() {
				return Math.min(1, tile.getMultiblock().getTemperature()
						/ EvaporationMultiblockData.MAX_MULTIPLIER_TEMP);
			}
		}, 48, 63)).warning(WarningType.INPUT_DOESNT_PRODUCE_OUTPUT,
				tile.getWarningCheck(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT));
		addRenderableWidget(new GuiFluidGauge(() -> tile.getMultiblock().inputTank,
				() -> tile.getMultiblock().getFluidTanks(null), GaugeType.STANDARD, this, 25, 13))
				.warning(WarningType.NO_MATCHING_RECIPE,
						tile.getWarningCheck(RecipeError.NOT_ENOUGH_INPUT));
		addRenderableWidget(new GuiFluidGauge(() -> tile.getMultiblock().outputTank,
				() -> tile.getMultiblock().getFluidTanks(null), GaugeType.STANDARD, this, 133, 13))
				.warning(WarningType.NO_SPACE_IN_OUTPUT,
						tile.getWarningCheck(RecipeError.NOT_ENOUGH_OUTPUT_SPACE));
		addRenderableWidget(new GuiHeatTab(this, () -> {
			Component environment = MekanismUtils.getTemperatureDisplay(
					tile.getMultiblock().getLastEnvironmentLoss(), TemperatureUnit.KELVIN, false);
			return Collections.singletonList(MekanismLang.DISSIPATED_RATE.translate(environment));
		}));
	}

	@Override
	protected void addWarningTab(IWarningTracker warningTracker) {
		addRenderableWidget(new GuiWarningTab(this, warningTracker, 137));
	}

	@Override
	protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
		renderTitleText(guiGraphics);
		drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
		super.drawForegroundText(guiGraphics, mouseX, mouseY);
	}

}
