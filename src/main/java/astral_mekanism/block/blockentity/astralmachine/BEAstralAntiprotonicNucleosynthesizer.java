package astral_mekanism.block.blockentity.astralmachine;

import astral_mekanism.block.blockentity.basemachine.BEAMEAntiprotonicNucleosynthesizer;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralAntiprotonicNucleosynthesizer
        extends BEAMEAntiprotonicNucleosynthesizer<BEAstralAntiprotonicNucleosynthesizer> {

    public BEAstralAntiprotonicNucleosynthesizer(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

    @Override
    protected IGasTank createInputTank(IContentsListener recipeCacheListener) {
        return ChemicalTankBuilder.GAS.create(Long.MAX_VALUE,
                (gas, a) -> a == AutomationType.MANUAL,
                (gas, a) -> containsRecipeBA(inputSlot.getStack(), gas),
                this::containsRecipeB,
                ChemicalAttributeValidator.ALWAYS_ALLOW, recipeCacheListener);
    }
}