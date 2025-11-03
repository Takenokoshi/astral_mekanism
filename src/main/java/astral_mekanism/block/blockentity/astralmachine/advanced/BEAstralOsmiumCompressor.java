package astral_mekanism.block.blockentity.astralmachine.advanced;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.ItemChemical;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralOsmiumCompressor extends BEAstralAdvancedMachine {

    public BEAstralOsmiumCompressor(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, "mekanism:osmium_compressor");
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<ItemStackGasToItemStackRecipe, ItemChemical<Gas, GasStack, ItemStackGasToItemStackRecipe>> getRecipeType() {
        return AstralMekanismRecipeTypes.AM_COMPRESSING;
    }
    
}
