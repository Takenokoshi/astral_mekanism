package astral_mekanism.block.blockentity.astralmachine;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.basemachine.BEAMEDissolutionChamber;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralDissolutionChamber extends BEAMEDissolutionChamber {

    public BEAstralDissolutionChamber(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

    @Override
    protected IGasTank createInjectTank(BiPredicate<Gas, AutomationType> canExtract,
            BiPredicate<Gas, AutomationType> canInsert, Predicate<Gas> validator,
            @Nullable ChemicalAttributeValidator attributeValidator, @Nullable IContentsListener listener) {
        return ChemicalTankBuilder.GAS.create(ticker, canExtract, canInsert, validator, attributeValidator, listener);
    }
}