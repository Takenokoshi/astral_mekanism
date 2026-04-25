package astral_mekanism.block.blockentity.astralmachine;

import astral_mekanism.block.blockentity.prefab.BEAbstractChemixer;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralChemixer extends BEAbstractChemixer {

    public BEAstralChemixer(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }
}