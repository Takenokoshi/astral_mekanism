package astral_mekanism.block.gui.factory;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.base.IAstralMekanismFactory;
import astral_mekanism.block.container.factory.ContainerAstralMekanismFactory;
import astral_mekanism.block.container.slot.PagedInventoryContainerSlot;
import astral_mekanism.block.gui.element.IPagedGuiElement;
import astral_mekanism.block.gui.element.PagedGuiSlot;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiElement;
import mekanism.client.gui.element.button.MekanismButton;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.slot.InventoryContainerSlot;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class GuiAstralMekanismFactory<BE extends TileEntityConfigurableMachine & IAstralMekanismFactory<BE>>
        extends GuiConfigurableTile<BE, ContainerAstralMekanismFactory<BE>> {

    protected int page;
    protected final int maxPage;
    protected final List<IPagedGuiElement> pagedElements;

    public GuiAstralMekanismFactory(ContainerAstralMekanismFactory<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
        this.maxPage = tile.getAllPages();
        this.page = 0;
        dynamicSlots = true;
        imageHeight = tile.getPageHeight();
        imageWidth = tile.getPageWidth();
        inventoryLabelX = imageWidth / 2 - 81;
        inventoryLabelY = imageHeight - 92;
        pagedElements = new ArrayList<>();
    }

    protected void changePage(int i) {
        page = Math.floorMod(page + i, maxPage);
        menu.pagedSlots.forEach(p -> p.setPage(page));
        pagedElements.forEach(p -> p.setPage(page));
    }

    @Override
    protected <T extends GuiElement> T addRenderableWidget(T element) {
        if (element instanceof IPagedGuiElement paged) {
            pagedElements.add(paged);
        }
        return super.addRenderableWidget(element);
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new MekanismButton(this, tile.getSideSpaceWidth(), -30, 18, 18,
                Component.literal("<"), () -> {
                    changePage(-1);
                }, null));
        addRenderableWidget(new MekanismButton(this, imageWidth - tile.getSideSpaceWidth() - 18, -30, 18, 18,
                Component.literal(">"), () -> {
                    changePage(1);
                }, null));
    }

    @Override
    protected void addSlots() {
        int size = menu.slots.size();
        for (int i = 0; i < size; i++) {
            Slot slot = menu.slots.get(i);
            if (slot instanceof InventoryContainerSlot containerSlot) {
                ContainerSlotType slotType = containerSlot.getSlotType();
                DataType dataType = findDataType(containerSlot);
                SlotType type;
                if (dataType != null) {
                    type = SlotType.get(dataType);
                } else if (slotType == ContainerSlotType.INPUT || slotType == ContainerSlotType.OUTPUT
                        || slotType == ContainerSlotType.EXTRA) {
                    type = SlotType.NORMAL;
                } else if (slotType == ContainerSlotType.POWER) {
                    type = SlotType.POWER;
                } else if (slotType == ContainerSlotType.NORMAL || slotType == ContainerSlotType.VALIDITY) {
                    type = SlotType.NORMAL;
                } else {
                    continue;
                }
                GuiSlot guiSlot = containerSlot instanceof PagedInventoryContainerSlot paged
                        ? new PagedGuiSlot(type, this, paged.x - 1, paged.y - 1, paged.page)
                        : new GuiSlot(type, this, slot.x - 1, slot.y - 1);
                containerSlot.addWarnings(guiSlot);
                SlotOverlay slotOverlay = containerSlot.getSlotOverlay();
                if (slotOverlay != null) {
                    guiSlot.with(slotOverlay);
                }
                if (slotType == ContainerSlotType.VALIDITY) {
                    int index = i;
                    guiSlot.validity(() -> checkValidity(index));
                }
                addRenderableWidget(guiSlot);
            } else {
                addRenderableWidget(new GuiSlot(SlotType.NORMAL, this, slot.x - 1, slot.y - 1));
            }
        }
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
        drawString(guiGraphics, Component.literal("Page: " + (page + 1) + " / " + maxPage),
                tile.getSideSpaceWidth() + 24, -30, 0xffffff);
    }
}
