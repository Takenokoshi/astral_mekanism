package astral_mekanism.cablepart;

import java.util.Arrays;

import appeng.api.parts.IPartHost;
import appeng.api.parts.IPartItem;
import appeng.api.parts.IPartModel;
import appeng.core.AppEng;
import appeng.parts.p2p.CapabilityP2PTunnelPart;
import appeng.parts.p2p.P2PModels;
import astral_mekanism.util.AMHeatUtils;
import mekanism.api.IContentsListener;
import mekanism.api.heat.IHeatCapacitor;
import mekanism.api.heat.IHeatHandler;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.heat.BasicHeatCapacitor;
import net.minecraftforge.common.capabilities.Capability;

public class HeatP2PTunnelPart extends CapabilityP2PTunnelPart<HeatP2PTunnelPart, IHeatHandler>
        implements IContentsListener {

    public static final Capability<IHeatHandler> HEAT_CAPABILITY = Capabilities.HEAT_HANDLER;
    private static final IHeatHandler NULL_HANDLER = new P2PEmptyHeatHandler(null);
    private static final P2PModels MODELS = new P2PModels(AppEng.makeId("part/p2p/p2p_tunnel_fe"));

    public HeatP2PTunnelPart(IPartItem<?> partItem) {
        super(partItem, HEAT_CAPABILITY);
        inputHandler = new P2PInputHeatHandler(this);
        outputHandler = new P2POutputHeatHandler(this);
        emptyHandler = NULL_HANDLER;
    }

    @Override
    public void onContentsChanged() {
        IHeatHandler input = getInputCapability().get();
        IHeatHandler[] outputs = getOutputStream().map(h -> h.outputHandler).toArray(IHeatHandler[]::new);
        IHeatHandler[] used = Arrays.copyOf(outputs, outputs.length + 1);
        used[outputs.length] = input;
        AMHeatUtils.averagingTemp(used);
        IPartHost host = getHost();
        if (host != null) {
            host.markForSave();
            host.markForUpdate();
        }
    }

    @Override
    public IPartModel getStaticModels() {
        return MODELS.getModel(this.isPowered(), this.isActive());
    }

    private abstract static class P2PHeatHandler implements IHeatHandler {

        protected final IHeatCapacitor heatCapacitor;

        public P2PHeatHandler(IContentsListener listener) {
            this.heatCapacitor = BasicHeatCapacitor.create(10000d, () -> 300d, listener);
        }

        @Override
        public int getHeatCapacitorCount() {
            return 1;
        }

        @Override
        public double getHeatCapacity(int arg0) {
            return heatCapacitor.getHeatCapacity();
        }

        @Override
        public double getInverseConduction(int arg0) {
            return heatCapacitor.getInverseConduction();
        }

        @Override
        public double getTemperature(int arg0) {
            return heatCapacitor.getTemperature();
        }

    }

    private class P2PInputHeatHandler extends P2PHeatHandler {

        public P2PInputHeatHandler(IContentsListener listener) {
            super(listener);
        }

        @Override
        public void handleHeat(int arg0, double arg1) {
            if ((arg1 > 0) == (heatCapacitor.getTemperature() > 300d)) {
                heatCapacitor.handleHeat(arg1);
            }
        }
    }

    private class P2POutputHeatHandler extends P2PHeatHandler {

        public P2POutputHeatHandler(IContentsListener listener) {
            super(listener);
        }

        @Override
        public void handleHeat(int arg0, double arg1) {
            if ((arg1 > 0) != (heatCapacitor.getTemperature() > 300d)) {
                heatCapacitor.handleHeat(arg1);
            }
        }
    }

    private static class P2PEmptyHeatHandler extends P2PHeatHandler {

        public P2PEmptyHeatHandler(IContentsListener listener) {
            super(listener);
        }

        @Override
        public void handleHeat(int arg0, double arg1) {
        }
    }

}
