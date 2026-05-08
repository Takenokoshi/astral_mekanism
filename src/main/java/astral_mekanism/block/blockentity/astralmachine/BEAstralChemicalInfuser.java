package astral_mekanism.block.blockentity.astralmachine;

import java.util.function.Predicate;

import astral_mekanism.block.blockentity.basemachine.BEAMEChemicalInfuser;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralChemicalInfuser extends BEAMEChemicalInfuser {

    public BEAstralChemicalInfuser(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

    @Override
    protected IGasTank createGasTank(Predicate<Gas> canInsert, Predicate<Gas> validator,
            IContentsListener recipeCacheListener) {
                return ChemicalTankBuilder.GAS.input(Long.MAX_VALUE, validator, canInsert, recipeCacheListener);
    }
}