package astral_mekanism.block.gui.normalmachine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.AstralMekanism;
import astral_mekanism.block.blockentity.prefab.BEAbstractTransformer;
import astral_mekanism.block.container.normalmachine.ContainerTransformer;
import astral_mekanism.network.to_server.PacketGuiTransformerMode;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.button.MekanismImageButton;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiTransformer<BE extends BEAbstractTransformer>
        extends GuiConfigurableTile<BE, ContainerTransformer<BE>> {

    public GuiTransformer(ContainerTransformer<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
        imageWidth += 36;
        inventoryLabelX += 18;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiFluidGauge(tile::getInputTankA, tile::getFluidTanks,
                GaugeType.STANDARD, this, 45, 30))
                .warning(WarningType.NO_MATCHING_RECIPE,
                        tile.getWarningCheck(BEAbstractTransformer.NOT_ENOUGH_INPUT_FA));
        addRenderableWidget(new GuiFluidGauge(tile::getInputTankB, tile::getFluidTanks,
                GaugeType.STANDARD, this, 81, 10))
                .warning(WarningType.NO_MATCHING_RECIPE,
                        tile.getWarningCheck(BEAbstractTransformer.NOT_ENOUGH_INPUT_FB));
        addRenderableWidget(new GuiFluidGauge(tile::getOutputTank, tile::getFluidTanks,
                GaugeType.STANDARD, this, 153, 10))
                .warning(WarningType.NO_MATCHING_RECIPE,
                        tile.getWarningCheck(RecipeError.NOT_ENOUGH_OUTPUT_SPACE));
        addRenderableWidget(new GuiProgress(tile::getScaledProgress, ProgressType.BAR, this, 118, 38))
                .warning(WarningType.INPUT_DOESNT_PRODUCE_OUTPUT,
                        tile.getWarningCheck(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.energyContainer, 200, 15))
                .warning(WarningType.NOT_ENOUGH_ENERGY, tile.getWarningCheck(RecipeError.NOT_ENOUGH_ENERGY));
        addRenderableWidget(new MekanismImageButton(this, 118, 16, 18, 18, 16, 16,
                new ResourceLocation("minecraft", "textures/block/lever.png"), this::onPush));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

    private void onPush() {
        AstralMekanism.packetHandler().sendToServer(new PacketGuiTransformerMode(tile.getBlockPos()));
    }

}
