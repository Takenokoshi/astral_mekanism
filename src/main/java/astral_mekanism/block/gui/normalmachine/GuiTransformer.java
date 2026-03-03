package astral_mekanism.block.gui.normalmachine;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import appeng.integration.modules.jei.TransformCategory;
import astral_mekanism.AstralMekanism;
import astral_mekanism.block.blockentity.prefab.BEAbstractTransformer;
import astral_mekanism.block.container.normalmachine.ContainerTransformer;
import astral_mekanism.jei.AstralMekanismJEIPlugin;
import astral_mekanism.jei.AstralMekanismJEIRecipeType;
import astral_mekanism.network.to_server.PacketGuiTransformerMode;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiDownArrow;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.button.MekanismImageButton;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fml.ModList;

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
                GaugeType.STANDARD, this, 27, 10))
                .warning(WarningType.NO_MATCHING_RECIPE,
                        tile.getWarningCheck(BEAbstractTransformer.NOT_ENOUGH_INPUT_FA));
        addRenderableWidget(new GuiFluidGauge(tile::getInputTankB, tile::getFluidTanks,
                GaugeType.STANDARD, this, 63, 10))
                .warning(WarningType.NO_MATCHING_RECIPE,
                        tile.getWarningCheck(BEAbstractTransformer.NOT_ENOUGH_INPUT_FB));
        addRenderableWidget(new GuiFluidGauge(tile::getOutputTank, tile::getFluidTanks,
                GaugeType.STANDARD, this, 150, 10))
                .warning(WarningType.NO_MATCHING_RECIPE,
                        tile.getWarningCheck(RecipeError.NOT_ENOUGH_OUTPUT_SPACE));
        addRenderableWidget(new GuiProgress(tile::getScaledProgress, ProgressType.BAR, this, 100, 38))
                .warning(WarningType.INPUT_DOESNT_PRODUCE_OUTPUT,
                        tile.getWarningCheck(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT))
                .jeiCategories(AstralMekanismJEIRecipeType.MEKANICAL_TRANSFORM);
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.energyContainer, 200, 25))
                .warning(WarningType.NOT_ENOUGH_ENERGY, tile.getWarningCheck(RecipeError.NOT_ENOUGH_ENERGY));
        addRenderableWidget(new MekanismImageButton(this, 100, 16, 18, 18, 16, 16,
                new ResourceLocation("minecraft", "textures/block/lever.png"), this::onPush));
        if (ModList.get().isLoaded("jei")) {
            addRenderableWidget(new MekanismImageButton(
                    this,
                    100, 52,
                    18, 18,
                    16, 16,
                    new ResourceLocation("minecraft", "textures/item/knowledge_book.png"),
                    GuiTransformer::connectJEI));
        }
        addRenderableWidget(new GuiDownArrow(this, 10, 35));
        addRenderableWidget(new GuiDownArrow(this, 82, 35));
        addRenderableWidget(new GuiDownArrow(this, 169, 35));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        drawString(guiGraphics, Component.literal("Mode:" + (tile.getMode() ? "Mekanical Transform" : "AE2 Transform")),
                imageWidth / 2, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

    private void onPush() {
        AstralMekanism.packetHandler().sendToServer(new PacketGuiTransformerMode(tile.getBlockPos()));
    }

    private static void connectJEI() {
        IJeiRuntime runtime = AstralMekanismJEIPlugin.getRuntime();
        if (runtime == null)
            return;
        runtime.getRecipesGui().showTypes(
                List.of(TransformCategory.RECIPE_TYPE));
    }

}
