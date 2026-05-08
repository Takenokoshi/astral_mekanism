package astral_mekanism.block.blockentity.astralmachine;

import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.slurry.ISlurryTank;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.basemachine.BEAMEChemicalWasher;

public class BEAstralChemicalWasher extends BEAMEChemicalWasher {

    public BEAstralChemicalWasher(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

    @Override
    protected ISlurryTank createInputTank(Predicate<Slurry> canInsert, Predicate<Slurry> validator,
            @Nullable IContentsListener listener) {
        return ChemicalTankBuilder.SLURRY.input(Long.MAX_VALUE, canInsert, validator, listener);
    }

    @Override
    protected BasicFluidTank createFluidTank(Predicate<FluidStack> canInsert, Predicate<FluidStack> validator,
            @Nullable IContentsListener listener) {
        return BasicFluidTank.input(0x7fffffff, canInsert, validator, listener);
    }
}