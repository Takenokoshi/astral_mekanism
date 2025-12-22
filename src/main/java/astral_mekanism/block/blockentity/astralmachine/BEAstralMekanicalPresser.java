package astral_mekanism.block.blockentity.astralmachine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.prefab.BETripleItemToItemRecipeMachine;
import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.TripleItem;
import astral_mekanism.recipes.recipe.TripleItemToItemRecipe;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralMekanicalPresser extends BETripleItemToItemRecipeMachine<BEAstralMekanicalPresser> {

    public BEAstralMekanicalPresser(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    public String getJEI() {
        return "astral_mekanism:mekanical_presser";
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<TripleItemToItemRecipe, TripleItem<TripleItemToItemRecipe>> getRecipeType() {
        return AstralMekanismRecipeTypes.MEKANICAL_PRESSER_RECIPE;
    }

    @Override
    public int getBaselineMaxOperations() {
        return 0x7fffffff;
    }
    
}
