package astral_mekanism.block.blockentity.normalmachine;

import astral_mekanism.block.blockentity.basemachine.BEAbstractMekanicalComposter;
import astral_mekanism.integration.AMEEmpowered;
import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEMekanicalComposter extends BEAbstractMekanicalComposter {

    public BEMekanicalComposter(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getDefaltBaseline() {
        return 1;
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (AMEEmpowered.empoweredIsLoaded()) {
            setBaselineMaxOperations(1 << AMEEmpowered.getAllSpeeds(this));
        } else if (upgrade == Upgrade.SPEED) {
            setBaselineMaxOperations(1 << upgradeComponent.getUpgrades(Upgrade.SPEED));
        }
    }

}
