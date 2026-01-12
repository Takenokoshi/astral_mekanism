package astral_mekanism.block.gui.element;

import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.text.GuiTextField;

public class PagedGuiTextField extends GuiTextField implements IPagedGuiElement {

    private final int page;

    public PagedGuiTextField(IGuiWrapper gui, int x, int y, int width, int height, int page) {
        super(gui, x, y, width, height);
        this.page = page;
        setPage(0);
    }

    @Override
    public void setPage(int page) {
        visible = this.page == page;
        setVisible(visible);
    }

}
