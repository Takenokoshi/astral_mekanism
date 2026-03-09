package astral_mekanism.block.gui.astralmachine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.astralmachine.BEAstralFormulaicAssemblicator;
import astral_mekanism.block.container.astralmachine.ContainerAstralFAssemblicator;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.client.jei.MekanismJEIRecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAstralFormulaicAssemblicator extends
        GuiConfigurableTile<BEAstralFormulaicAssemblicator, ContainerAstralFAssemblicator> {

    public GuiAstralFormulaicAssemblicator(ContainerAstralFAssemblicator container,
            Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
        imageWidth += 60;
        inventoryLabelX += 30;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiProgress(tile::getProgressScaled, ProgressType.BAR, this, 86, 38))
                .jeiCategories(MekanismJEIRecipeType.VANILLA_CRAFTING);
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), 224, 15));
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getActive));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        drawString(guiGraphics, tile.savedRecipe != null
                ? Component.literal("Filter recipe id:" + tile.savedRecipe.getId().toString())
                : Component.literal("I don't have filter recipe. You can set it with JEI."),
                0, -30, 0xffffff);
        drawString(guiGraphics, Component.literal("Filter recipe can block item which cannot use for the recipe."),
                0, -50, 0xffffff);
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
