package astral_mekanism.block.blockentity.normalmachine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.prefab.BEGasToGasBlock;
import astral_mekanism.registries.AMERecipeTypes;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleChemical;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEGasConverter extends BEGasToGasBlock {

    public BEGasConverter(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<GasToGasRecipe, SingleChemical<Gas, GasStack, GasToGasRecipe>> getRecipeType() {
        return AMERecipeTypes.GAS_CONVERSION;
    }

    @Override
    protected long tankCapacity() {
        return Long.MAX_VALUE;
    }

    @Override
    protected int maxOperation() {
        return 0x7fffffff;
    }

    @Override
    public String getJEI() {
        return "astral_mekanism:gas_converter";
    }

}
