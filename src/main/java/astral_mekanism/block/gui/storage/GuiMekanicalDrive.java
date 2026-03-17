package astral_mekanism.block.gui.storage;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.storage.BEMekanicalDrive;
import astral_mekanism.block.container.prefab.ContainerPagedMachine;
import astral_mekanism.block.gui.element.PagedGuiInnerScreen;
import astral_mekanism.block.gui.element.PagedGuiTextField;
import astral_mekanism.block.gui.prefab.GuiPagedMachine;
import mekanism.client.gui.element.text.GuiTextField;
import mekanism.common.util.text.InputValidator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiMekanicalDrive extends GuiPagedMachine<BEMekanicalDrive> {

    private GuiTextField priorityField;

    public GuiMekanicalDrive(ContainerPagedMachine<BEMekanicalDrive> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new PagedGuiInnerScreen(this, 8, 14, 90, 36,
                () -> List.of(Component.literal("Priority setting.")), maxPage - 1));
        priorityField = addRenderableWidget(new PagedGuiTextField(this, 26, 14, 90, 18, maxPage - 1))
                .setInputValidator(InputValidator.DIGIT);

    }

    protected int getMaxPage() {
        return tile.getTier().processes + 1;
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
