package astral_mekanism.block.blockentity.astralmachine.electric;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.basemachine.BEAMEElectricMachine;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralCrusher extends BEAMEElectricMachine {

    public BEAstralCrusher(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state,"mekanism:crusher");
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<ItemStackToItemStackRecipe, SingleItem<ItemStackToItemStackRecipe>> getRecipeType() {
        return MekanismRecipeType.CRUSHING;
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }
    
}
