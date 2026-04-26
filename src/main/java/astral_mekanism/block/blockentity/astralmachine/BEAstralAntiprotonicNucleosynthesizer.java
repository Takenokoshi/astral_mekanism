package astral_mekanism.block.blockentity.astralmachine;

import astral_mekanism.block.blockentity.prefab.BEAbstractAntiprotonicNucleosynthesizer;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralAntiprotonicNucleosynthesizer extends BEAbstractAntiprotonicNucleosynthesizer<BEAstralAntiprotonicNucleosynthesizer> {

    public BEAstralAntiprotonicNucleosynthesizer(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }
}