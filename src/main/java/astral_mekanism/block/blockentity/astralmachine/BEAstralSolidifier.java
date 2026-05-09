package astral_mekanism.block.blockentity.astralmachine;

import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.basemachine.BEAMESolidifier;
import mekanism.api.IContentsListener;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BEAstralSolidifier extends BEAMESolidifier {

    public BEAstralSolidifier(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected BasicFluidTank createFluidTank(Predicate<FluidStack> canInsert, Predicate<FluidStack> validator,
            @Nullable IContentsListener listener) {
        return BasicFluidTank.input(0x7fffffff, canInsert, validator, listener);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }
}