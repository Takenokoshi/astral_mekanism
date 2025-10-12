package astral_mekanism.block.blockentity.astralmachine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.prefab.BEGasToGasBlock;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleChemical;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralGNA extends BEGasToGasBlock {

    public BEAstralGNA(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<GasToGasRecipe, SingleChemical<Gas, GasStack, GasToGasRecipe>> getRecipeType() {
        return MekanismRecipeType.ACTIVATING;
    }

    @Override
    protected long tankCapacity() {
        return Long.MAX_VALUE;
    }

    @Override
    protected int maxOperation() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String getJEI() {
        return "mekanism:solar_newtron_activator";
    }
    
}
