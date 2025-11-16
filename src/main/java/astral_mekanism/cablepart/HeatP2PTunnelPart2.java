package astral_mekanism.cablepart;

import appeng.api.networking.IGridNodeListener;
import appeng.api.parts.IPartItem;
import appeng.api.parts.IPartModel;
import appeng.core.AppEng;
import appeng.parts.p2p.P2PModels;
import appeng.parts.p2p.P2PTunnelPart;
import net.minecraft.nbt.CompoundTag;

public class HeatP2PTunnelPart2 extends P2PTunnelPart<HeatP2PTunnelPart2> {

    private static final P2PModels MODELS = new P2PModels(AppEng.makeId("part/p2p/p2p_tunnel_fe"));

    private static final double heatCapacitor = 10000d;
    private double temperature;

    public HeatP2PTunnelPart2(IPartItem<?> partItem) {
        super(partItem);
        temperature = 300d;
    }

    @Override
    public IPartModel getStaticModels() {
        return MODELS.getModel(this.isPowered(), this.isActive());
    }

    @Override
    protected void onMainNodeStateChanged(IGridNodeListener.State reason) {
        super.onMainNodeStateChanged(reason);
        if (getMainNode().hasGridBooted()) {
            if (isOutput()) {
                final HeatP2PTunnelPart2 input = getInput();
                if (input != null) {

                }
            }
        }
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        super.readFromNBT(tag);
        this.temperature = tag.getDouble("temperature");
    }

    @Override
    public void writeToNBT(CompoundTag tag) {
        super.writeToNBT(tag);
        tag.putDouble("temperature", this.temperature);
    }

}
