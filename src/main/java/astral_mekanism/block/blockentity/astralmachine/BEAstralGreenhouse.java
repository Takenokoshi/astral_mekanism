package astral_mekanism.block.blockentity.astralmachine;

import java.util.function.Predicate;

import astral_mekanism.block.blockentity.basemachine.BETickWorkGreenHouse;
import mekanism.api.IContentsListener;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BEAstralGreenHouse extends BETickWorkGreenHouse {

    public BEAstralGreenHouse(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected BasicFluidTank createFluidTank(Predicate<FluidStack> canInsert, Predicate<FluidStack> validator,
            IContentsListener listener) {
        return BasicFluidTank.input(0x7fffffff, canInsert, validator, listener);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }
}