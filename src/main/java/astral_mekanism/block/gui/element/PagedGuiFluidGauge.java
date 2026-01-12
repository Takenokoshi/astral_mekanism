package astral_mekanism.block.gui.element;

import java.util.List;
import java.util.function.Supplier;

import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;

public class PagedGuiFluidGauge extends GuiFluidGauge implements IPagedGuiElement {

    private final int page;

    public PagedGuiFluidGauge(Supplier<IExtendedFluidTank> tankSupplier, Supplier<List<IExtendedFluidTank>> tanksSupplier,
            GaugeType type, IGuiWrapper gui, int x, int y, int sizeX, int sizeY, int page) {
        super(tankSupplier, tanksSupplier, type, gui, x, y, sizeX, sizeY);
        this.page = page;
        setPage(0);
    }

    public PagedGuiFluidGauge(Supplier<IExtendedFluidTank> tankSupplier, Supplier<List<IExtendedFluidTank>> tanksSupplier,
            GaugeType type, IGuiWrapper gui, int x, int y, int page) {
        this(tankSupplier, tanksSupplier, type, gui, x, y, type.getGaugeOverlay().getWidth() + 2,
                type.getGaugeOverlay().getHeight() + 2, page);
    }

    @Override
    public void setPage(int page) {
        visible = this.page == page;
    }

}
