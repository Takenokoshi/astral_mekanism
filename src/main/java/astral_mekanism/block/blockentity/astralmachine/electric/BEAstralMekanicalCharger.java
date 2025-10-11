package astral_mekanism.block.blockentity.astralmachine.electric;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralMekanicalCharger extends BEAstralElectricMachine {

    public BEAstralMekanicalCharger(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state,"astral_nekanism:mekanical_charger");
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<ItemStackToItemStackRecipe, SingleItem<ItemStackToItemStackRecipe>> getRecipeType() {
        return AstralMekanismRecipeTypes.MEKANICAL_CHARGER_RECIPE;
    }
    
}
