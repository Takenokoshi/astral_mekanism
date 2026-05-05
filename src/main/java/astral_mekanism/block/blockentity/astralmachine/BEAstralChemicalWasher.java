package astral_mekanism.block.blockentity.astralmachine;

import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import astral_mekanism.block.blockentity.basemachine.BEAMEChemicalWasher;

public class BEAstralChemicalWasher extends BEAMEChemicalWasher {

    public BEAstralChemicalWasher(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }
}