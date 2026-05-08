package astral_mekanism.block.blockentity.astralmachine;

import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.basemachine.BEAMEElectrolyticSeparator;
import mekanism.api.IContentsListener;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BEAstralElectrolyticSeparator extends BEAMEElectrolyticSeparator {

    public BEAstralElectrolyticSeparator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

    @Override
    protected BasicFluidTank createFluidTank(Predicate<FluidStack> validator, @Nullable IContentsListener listener) {
        return BasicFluidTank.input(0x7fffffff, validator, listener);
    }
}