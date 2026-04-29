package astral_mekanism.block.blockentity.appliedmachine;

import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedGasToGasMachine;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.common.config.MekanismConfig;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEAppliedIsotopicCentrifuge extends BEAppliedGasToGasMachine {

    public BEAppliedIsotopicCentrifuge(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, MekanismConfig.usage.isotopicCentrifuge.get()
                .divideToLong(MekanismConfig.general.forgeConversionRate.get()));
    }

    @Override
    protected IMekanismRecipeTypeProvider<GasToGasRecipe, ?> getRecipeType() {
        return MekanismRecipeType.CENTRIFUGING;
    }
    
}
