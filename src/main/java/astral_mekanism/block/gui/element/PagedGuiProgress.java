package astral_mekanism.block.gui.element;

import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.IProgressInfoHandler;
import mekanism.client.gui.element.progress.ProgressType;

public class PagedGuiProgress extends GuiProgress implements IPagedGuiElement {

    private final int page;

    public PagedGuiProgress(IProgressInfoHandler handler, ProgressType type, IGuiWrapper gui, int x, int y, int page) {
        super(handler, type, gui, x, y);
        this.page = page;
        setPage(0);
    }

    @Override
    public void setPage(int page) {
        visible = this.page == page;
    }

}
