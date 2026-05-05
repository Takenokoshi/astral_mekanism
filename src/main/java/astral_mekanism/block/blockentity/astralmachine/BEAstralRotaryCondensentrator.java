package astral_mekanism.block.blockentity.astralmachine;

import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import astral_mekanism.block.blockentity.basemachine.BEAMERotaryCondensentrator;

public class BEAstralRotaryCondensentrator extends BEAMERotaryCondensentrator {

    public BEAstralRotaryCondensentrator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }
}