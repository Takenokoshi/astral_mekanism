package astral_mekanism.cablepart;

import java.util.Arrays;
import appeng.api.parts.IPartHost;
import appeng.api.parts.IPartItem;
import appeng.api.parts.IPartModel;
import appeng.core.AppEng;
import appeng.parts.p2p.CapabilityP2PTunnelPart;
import appeng.parts.p2p.P2PModels;
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

        public final IHeatCapacitor heatCapacitor;

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
            if (arg0 == -1) {
                heatCapacitor.handleHeat(arg1);
            } else if ((arg1 > 0) == (heatCapacitor.getTemperature() > 300d)) {
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
            if (arg0 == -1) {
                heatCapacitor.handleHeat(arg1);
            } else if ((arg1 > 0) != (heatCapacitor.getTemperature() > 300d)) {
                IHeatHandler input = getInputCapability().get();
                if (input == null) {
                    return;
                }
                IHeatHandler[] output = getOutputStream().map(p2p -> p2p.outputHandler).toArray(IHeatHandler[]::new);
                IHeatHandler[] handlers = Arrays.copyOf(output, output.length + 1);
                handlers[output.length] = input;
                double avtemp = Arrays.stream(handlers).map(ha -> ha.getTemperature(0) / handlers.length).reduce(0d,
                        (a, b) -> a + b);
                for (IHeatHandler handler : handlers) {
                    if (handler == this) {
                        heatCapacitor.handleHeat(
                                heatCapacitor.getHeatCapacity() * (avtemp - heatCapacitor.getTemperature()));
                    } else {
                        handler.handleHeat(-1,
                                handler.getHeatCapacity(0) * (avtemp - handler.getTemperature(0)));
                    }
                }
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
