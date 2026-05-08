package astral_mekanism.block.blockentity.astralmachine;

import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.basemachine.BEAMERotaryCondensentrator;

public class BEAstralRotaryCondensentrator extends BEAMERotaryCondensentrator {

    public BEAstralRotaryCondensentrator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

    @Override
    protected IGasTank createGasTank(BiPredicate<Gas, AutomationType> canExtract,
            BiPredicate<Gas, AutomationType> canInsert, Predicate<Gas> validator,
            @Nullable ChemicalAttributeValidator attributeValidator, @Nullable IContentsListener listener) {
        return ChemicalTankBuilder.GAS.create(Long.MAX_VALUE, validator, validator, validator, attributeValidator,
                listener);
    }

    @Override
    protected BasicFluidTank createFluidTank(BiPredicate<FluidStack, AutomationType> canExtract,
            BiPredicate<FluidStack, AutomationType> canInsert, Predicate<FluidStack> validator,
            @Nullable IContentsListener listener) {
        return BasicFluidTank.create(0x7fffffff, canExtract, canInsert, validator, listener);
    }
}