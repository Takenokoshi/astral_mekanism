package astral_mekanism.block.gui.element;

import java.util.List;
import java.util.function.Supplier;

import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiInfusionGauge;

public class PagedGuiInfusiongauge extends GuiInfusionGauge implements IPagedGuiElement {

    private final int page;

    public PagedGuiInfusiongauge(Supplier<IInfusionTank> tankSupplier, Supplier<List<IInfusionTank>> tanksSupplier,
            GaugeType type, IGuiWrapper gui, int x, int y, int sizeX, int sizeY, int page) {
        super(tankSupplier, tanksSupplier, type, gui, x, y, sizeX, sizeY);
        this.page = page;
        setPage(0);
    }

    public PagedGuiInfusiongauge(Supplier<IInfusionTank> tankSupplier, Supplier<List<IInfusionTank>> tanksSupplier,
            GaugeType type, IGuiWrapper gui, int x, int y, int page) {
        super(tankSupplier, tanksSupplier, type, gui, x, y);
        this.page = page;
        setPage(0);
    }

    @Override
    public void setPage(int page) {
        visible = this.page == page;
    }

}
