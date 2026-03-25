package astral_mekanism.block.blockentity.normalmachine;

import astral_mekanism.block.blockentity.prefab.BEAbstractFluidInfuser;
import astral_mekanism.integration.AMEEmpowered;
import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEFluidInfuser extends BEAbstractFluidInfuser {

    int baselineMaxOperations;

    public BEFluidInfuser(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        baselineMaxOperations = 1;
    }

    @Override
    protected int getBaselineMaxOperations() {
        return baselineMaxOperations;
    }

    @Override
    protected int getFluidTankCapacity() {
        return 200000;
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (AMEEmpowered.empoweredIsLoaded()) {
            baselineMaxOperations = 5 << AMEEmpowered.getAllSpeeds(this);
        } else if (upgrade == Upgrade.SPEED) {
            baselineMaxOperations = 5 << upgradeComponent.getUpgrades(Upgrade.SPEED);
        }
    }
}