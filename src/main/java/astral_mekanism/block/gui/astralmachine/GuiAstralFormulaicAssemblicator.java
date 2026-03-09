package astral_mekanism.block.gui.astralmachine;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import astral_mekanism.AstralMekanismLang;
import astral_mekanism.block.blockentity.astralmachine.BEAstralFormulaicAssemblicator;
import astral_mekanism.block.container.astralmachine.ContainerAstralFAssemblicator;
import astral_mekanism.jei.AstralMekanismJEIPlugin;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.button.MekanismImageButton;
import mekanism.client.gui.element.button.ToggleButton;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.client.jei.MekanismJEIRecipeType;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
        addRenderableWidget(new ToggleButton(this, 86, 17, 18, 16,
                new ResourceLocation("minecraft", "textures/block/redstone_torch_off.png"),
                new ResourceLocation("minecraft", "textures/block/redstone_torch.png"),
                () -> tile.getSavedRecipe() != null, () -> {
                }, getOnHover(AstralMekanismLang.EXPLAIN_ASSEMBLICATOR_TORCHBUTTON)));
        addRenderableWidget(new MekanismImageButton(this, 86, 53,
                18, 18, 16, 16,
                new ResourceLocation("minecraft", "textures/item/knowledge_book.png"),
                this::viewSavedRecipeInJEI, getOnHover(AstralMekanismLang.EXPLAIN_ASSEMBLICATOR_BOOKBUTTON)));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

    private void viewSavedRecipeInJEI() {
        IJeiRuntime jeiRuntime = AstralMekanismJEIPlugin.getRuntime();
        if (jeiRuntime != null && tile.getSavedRecipe() != null) {
            jeiRuntime.getRecipesGui().showRecipes(
                    jeiRuntime.getRecipeManager().getRecipeCategory(RecipeTypes.CRAFTING),
                    List.of(tile.getSavedRecipe()), List.of());
        }
    }

}
