package astral_mekanism.block.blockentity.astralmachine;

import astral_mekanism.block.blockentity.prefab.BEAbstractMekanicalComposter;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralComposter extends BEAbstractMekanicalComposter {

    public BEAstralComposter(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getDefaltBaseline() {
        return 0x7fffffff;
    }

}
