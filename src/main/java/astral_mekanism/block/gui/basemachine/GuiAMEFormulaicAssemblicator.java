package astral_mekanism.block.gui.basemachine;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import astral_mekanism.AMELang;
import astral_mekanism.block.blockentity.interf.IAMEFormulaicAssemblicator;
import astral_mekanism.block.blockentity.interf.IHasCustomSizeContainer;
import astral_mekanism.block.container.prefab.ContainerMachineCustomSize;
import astral_mekanism.jei.AMEJEIPlugin;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.button.MekanismImageButton;
import mekanism.client.gui.element.button.ToggleButton;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiAMEFormulaicAssemblicator<BE extends TileEntityConfigurableMachine & IAMEFormulaicAssemblicator & IHasCustomSizeContainer> extends
        GuiConfigurableTile<BE, ContainerMachineCustomSize<BE>> {

    public GuiAMEFormulaicAssemblicator(ContainerMachineCustomSize<BE> container,
            Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
        imageWidth += 60;
        inventoryLabelX += 30;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiProgress(tile::getScaledProgress, ProgressType.BAR, this, 86, 38))
                .jeiCategories(MekanismJEIRecipeType.VANILLA_CRAFTING);
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), 224, 15));
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getActive));
        addRenderableWidget(new ToggleButton(this, 86, 17, 18, 16,
                new ResourceLocation("minecraft", "textures/block/redstone_torch_off.png"),
                new ResourceLocation("minecraft", "textures/block/redstone_torch.png"),
                () -> tile.getSavedRecipe() != null, () -> {
                }, getOnHover(AMELang.EXPLAIN_ASSEMBLICATOR_TORCHBUTTON)));
        addRenderableWidget(new MekanismImageButton(this, 86, 53,
                18, 18, 16, 16,
                new ResourceLocation("minecraft", "textures/item/knowledge_book.png"),
                this::viewSavedRecipeInJEI, getOnHover(AMELang.EXPLAIN_ASSEMBLICATOR_BOOKBUTTON)));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

    private void viewSavedRecipeInJEI() {
        IJeiRuntime jeiRuntime = AMEJEIPlugin.getRuntime();
        if (jeiRuntime != null && tile.getSavedRecipe() != null) {
            jeiRuntime.getRecipesGui().showRecipes(
                    jeiRuntime.getRecipeManager().getRecipeCategory(RecipeTypes.CRAFTING),
                    List.of(tile.getSavedRecipe()), List.of());
        }
    }

}
