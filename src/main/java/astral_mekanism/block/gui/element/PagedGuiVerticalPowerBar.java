package astral_mekanism.block.gui.element;

import mekanism.api.energy.IEnergyContainer;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;

public class PagedGuiVerticalPowerBar extends GuiVerticalPowerBar implements IPagedGuiElement {

    private final int page;

    public PagedGuiVerticalPowerBar(IGuiWrapper gui, IEnergyContainer container, int x, int y, int desiredHeight,
            int page) {
        super(gui, container, x, y, desiredHeight);
        this.page = page;
        setPage(0);
    }

    public PagedGuiVerticalPowerBar(IGuiWrapper gui, IEnergyContainer container, int x, int y,
            int page) {
        this(gui, container, x, y, 52, page);
    }

    @Override
    public void setPage(int page) {
        visible = this.page == page;
    }

}
