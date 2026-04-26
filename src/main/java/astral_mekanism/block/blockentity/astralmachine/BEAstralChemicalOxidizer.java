package astral_mekanism.block.blockentity.astralmachine;

import astral_mekanism.block.blockentity.prefab.BEAbstractChemicalOxidizer;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralChemicalOxidizer extends BEAbstractChemicalOxidizer {

    public BEAstralChemicalOxidizer(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }
}