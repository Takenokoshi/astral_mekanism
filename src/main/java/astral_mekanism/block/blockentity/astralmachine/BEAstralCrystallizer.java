package astral_mekanism.block.blockentity.astralmachine;

import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.basemachine.BEAMECrystallizer;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.pigment.IPigmentTank;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.slurry.ISlurryTank;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralCrystallizer extends BEAMECrystallizer {

    public BEAstralCrystallizer(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

    @Override
    protected IGasTank createGasTank(Predicate<Gas> validator, @Nullable IContentsListener listener) {
        return ChemicalTankBuilder.GAS.input(Long.MAX_VALUE, validator, listener);
    }

    @Override
    protected IInfusionTank createInfusionTank(Predicate<InfuseType> validator, @Nullable IContentsListener listener) {
        return ChemicalTankBuilder.INFUSION.input(Long.MAX_VALUE, validator, listener);
    }

    @Override
    protected IPigmentTank createPigmentTank(Predicate<Pigment> validator, @Nullable IContentsListener listener) {
        return ChemicalTankBuilder.PIGMENT.input(Long.MAX_VALUE, validator, listener);
    }

    @Override
    protected ISlurryTank createSlurryTank(Predicate<Slurry> validator, @Nullable IContentsListener listener) {
        return ChemicalTankBuilder.SLURRY.input(Long.MAX_VALUE, validator, listener);
    }
}