package astral_mekanism.block.gui.normalmachine;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import appeng.integration.modules.jei.ChargerCategory;
import appeng.recipes.handlers.ChargerRecipe;
import astral_mekanism.block.blockentity.base.BlockEntityRecipeMachine;
import astral_mekanism.block.blockentity.interf.IEnergizedMachine;
import astral_mekanism.jei.AstralMekanismJEIPlugin;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.button.MekanismImageButton;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fml.ModList;

public class GuiMekanicalCharger<BE extends BlockEntityRecipeMachine<ChargerRecipe> & IEnergizedMachine<BE>>
        extends GuiConfigurableTile<BE, MekanismTileContainer<BE>> {

    public GuiMekanicalCharger(MekanismTileContainer<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiUpArrow(this, 68, 38));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), 164, 15))
              .warning(WarningType.NOT_ENOUGH_ENERGY, tile.getWarningCheck(RecipeError.NOT_ENOUGH_ENERGY));
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getActive));
        addRenderableWidget(new GuiProgress(tile::getProgressScaled, ProgressType.BAR, this, 86, 38))
              .warning(WarningType.INPUT_DOESNT_PRODUCE_OUTPUT, tile.getWarningCheck(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT));
        if (ModList.get().isLoaded("jei")) {
            addRenderableWidget(new MekanismImageButton(
                    this,
                    90, 53,
                    18, 18,
                    16, 16,
                    new ResourceLocation("minecraft", "textures/item/knowledge_book.png"),
                    GuiMekanicalCharger::connectJEI));
        }
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

    public static void connectJEI(){
        IJeiRuntime runtime = AstralMekanismJEIPlugin.getRuntime();
        if (runtime == null)
            return;
        runtime.getRecipesGui().showTypes(
                List.of(ChargerCategory.RECIPE_TYPE));
    }

}
