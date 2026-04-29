package astral_mekanism.block.gui.generator;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.AstralMekanism;
import astral_mekanism.block.blockentity.generator.BEAppliedGasBurningGenerator;
import astral_mekanism.block.gui.element.GuiMEKeySlot;
import astral_mekanism.jei.AMEJEIRecipeType;
import astral_mekanism.network.to_server.PacketGuiSetLong;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.text.GuiTextField;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.util.text.InputValidator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedGasBurningGenerator
        extends GuiMekanismTile<BEAppliedGasBurningGenerator, MekanismTileContainer<BEAppliedGasBurningGenerator>> {

    private GuiTextField field;

    public GuiAppliedGasBurningGenerator(MekanismTileContainer<BEAppliedGasBurningGenerator> container,
            Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiMEKeySlot(this, 63, 16, tile::getMeStorage, tile::getFuelKey));
        addRenderableWidget(new GuiMEKeySlot(this, 115, 34, tile::getMeStorage, () -> tile.energyKey));
        addRenderableWidget(new GuiUpArrow(this, 68, 38));
        addRenderableWidget(new GuiProgress(tile::getActive, ProgressType.BAR, this, 86, 38))
                .jeiCategories(AMEJEIRecipeType.GAS_BURNING);
        addRenderableWidget(new GuiInnerScreen(this, 81, 53, 34, 12));
        field = addRenderableWidget(new GuiTextField(this, 81, 53, 34, 12));
        field.setMaxLength(19);
        field.setInputValidator(InputValidator.DIGIT).configureDigitalInput(this::setEfficiency);
        field.setFocused(true);
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        drawString(guiGraphics, title, titleLabelX, titleLabelY, titleTextColor());
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

    private void setEfficiency() {
        if (!field.getText().isEmpty()) {
            try {
                AstralMekanism.packetHandler()
                        .sendToServer(new PacketGuiSetLong(0, Long.parseLong(field.getText()), tile.getBlockPos()));
            } catch (NumberFormatException ignored) {
            }
            field.setText("");
        }
    }

}
