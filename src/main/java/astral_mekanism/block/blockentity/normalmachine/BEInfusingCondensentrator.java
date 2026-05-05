package astral_mekanism.block.blockentity.normalmachine;

import astral_mekanism.block.blockentity.basemachine.BEAbstractInfusingCondensentrator;
import astral_mekanism.integration.AMEEmpowered;
import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEInfusingCondensentrator extends BEAbstractInfusingCondensentrator {

    private int baselineMaxOperations;

    public BEInfusingCondensentrator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        baselineMaxOperations = 100;
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (AMEEmpowered.empoweredIsLoaded()) {
            baselineMaxOperations = 100 << AMEEmpowered.getAllSpeeds(this);
        } else if (upgrade == Upgrade.SPEED) {
            baselineMaxOperations = 100 << upgradeComponent.getUpgrades(Upgrade.SPEED);
        }
    }

    @Override
    protected int getBaselineMaxOperations() {
        return baselineMaxOperations;
    }

    @Override
    protected int getFluidTankCapacity() {
        return 100000;
    }

    @Override
    protected long getChemicalTankCapacity() {
        return 1000000000l;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableInt.create(this::getBaselineMaxOperations, v -> baselineMaxOperations = v));
    }

}
