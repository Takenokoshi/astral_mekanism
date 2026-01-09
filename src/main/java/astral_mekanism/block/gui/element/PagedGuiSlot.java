package astral_mekanism.block.gui.element;

import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;

public class PagedGuiSlot extends GuiSlot implements IPagedGuiElement {

    private final int page;

    public PagedGuiSlot(SlotType type, IGuiWrapper gui, int x, int y, int page) {
        super(type, gui, x, y);
        this.page = page;
        setPage(0);
    }

    @Override
    public void setPage(int page) {
        visible = page == this.page;
    }

}
