package astral_mekanism.block.blockentity.appliedmachine;

import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedGasToGasMachine;
import astral_mekanism.registries.AMERecipeTypes;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.common.config.MekanismConfig;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEAppliedSPS extends BEAppliedGasToGasMachine {

    public BEAppliedSPS(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state,  MekanismConfig.general.spsEnergyPerInput.get().multiply(1000)
                .divideToLong(MekanismConfig.general.forgeConversionRate.get()));
    }

    @Override
    protected IMekanismRecipeTypeProvider<GasToGasRecipe, ?> getRecipeType() {
        return AMERecipeTypes.SPS_RECIPE;
    }
    
}
