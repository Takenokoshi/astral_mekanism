package astral_mekanism.block.blockentity.other;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.block.AttributeIntTier;
import astral_mekanism.block.blockentity.elements.MekanicalMagmaBlockHeatCapacitor;
import mekanism.api.IContentsListener;
import mekanism.api.heat.HeatAPI.HeatTransfer;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.holder.heat.HeatCapacitorHelper;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableDouble;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEMekanicalMagmaBlock extends TileEntityMekanism {

    private int tier;
    public MekanicalMagmaBlockHeatCapacitor heatCapacitor;
    private double lastEnvironmentLoss;
    private double lastTransferLoss;

    public BEMekanicalMagmaBlock(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected void presetVariables() {
        tier = Attribute.get(getBlockType(), AttributeIntTier.class).tier;
    }

    @Nullable
    protected IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener,
            CachedAmbientTemperature ambientTemperature) {
        HeatCapacitorHelper builder = HeatCapacitorHelper.forSide(this::getDirection);
        builder.addCapacitor(heatCapacitor = MekanicalMagmaBlockHeatCapacitor.createTiered(tier));
        return builder.build();
    }

    @Override
    protected void onUpdateServer(){
        super.onUpdateServer();
        HeatTransfer loss = simulate();
        lastEnvironmentLoss = loss.environmentTransfer();
        lastTransferLoss = loss.adjacentTransfer();
    }

    public double getLastTransferLoss() {
        return lastTransferLoss;
    }

    public double getLastEnvironmentLoss() {
        return lastEnvironmentLoss;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableDouble.create(this::getLastTransferLoss, value -> lastTransferLoss = value));
        container.track(SyncableDouble.create(this::getLastEnvironmentLoss, value -> lastEnvironmentLoss = value));
    }

}
