package astral_mekanism.block.blockentity.astralmachine;

import appeng.recipes.transform.TransformRecipe;
import astral_mekanism.block.blockentity.prefab.BEAbstractTransformer;
import astral_mekanism.generalrecipe.cachedrecipe.GeneralCachedRecipe;
import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralTransformer extends BEAbstractTransformer {

    public BEAstralTransformer(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int fluidTankCapacity() {
        return 0x7fffffff;
    }

    @Override
    protected GeneralCachedRecipe<TransformRecipe> operateCachedRecipe(
            GeneralCachedRecipe<TransformRecipe> cachedRecipe) {
        return cachedRecipe.setBaselineMaxOperations(() -> 0x7fffffff);
    }

    @Override
    protected CachedRecipe<MekanicalTransformRecipe> operateCachedRecipe(
            CachedRecipe<MekanicalTransformRecipe> cachedRecipe) {
        return cachedRecipe.setBaselineMaxOperations(() -> 0x7fffffff);
    }

}
