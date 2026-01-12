package astral_mekanism.block.gui.element;

import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.bar.GuiHorizontalRateBar;

public class PagedGuiHorizontalRateBar extends GuiHorizontalRateBar implements IPagedGuiElement {

    private final int page;

    public PagedGuiHorizontalRateBar(IGuiWrapper gui, IBarInfoHandler handler, int x, int y, int page) {
        super(gui, handler, x, y);
        this.page = page;
        setPage(0);
    }

    @Override
    public void setPage(int page) {
        visible = this.page == page;
    }

}
