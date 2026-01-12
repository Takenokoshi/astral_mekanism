package astral_mekanism.block.gui.element;

import java.util.List;
import java.util.function.Supplier;

import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.GuiInnerScreen;
import net.minecraft.network.chat.Component;

public class PagedGuiInnerScreen extends GuiInnerScreen implements IPagedGuiElement {

    private final int page;

    public PagedGuiInnerScreen(IGuiWrapper gui, int x, int y, int width, int height,
            Supplier<List<Component>> renderStrings, int page) {
        super(gui, x, y, width, height, renderStrings);
        this.page = page;
        setPage(0);
    }

    @Override
    public void setPage(int page) {
        visible = this.page == page;
    }

}
