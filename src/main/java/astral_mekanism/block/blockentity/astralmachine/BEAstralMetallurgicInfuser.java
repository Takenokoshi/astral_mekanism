package astral_mekanism.block.blockentity.astralmachine;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.basemachine.BEAMEMetallurgicInfuser;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralMetallurgicInfuser extends BEAMEMetallurgicInfuser {

    public BEAstralMetallurgicInfuser(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

    @Override
    protected IInfusionTank createInfusionTank(BiPredicate<InfuseType, AutomationType> canExtract,
            BiPredicate<InfuseType, AutomationType> canInsert, Predicate<InfuseType> validator,
            @Nullable IContentsListener listener) {
        return ChemicalTankBuilder.INFUSION.create(Long.MAX_VALUE, validator, validator, validator, listener);
    }
}