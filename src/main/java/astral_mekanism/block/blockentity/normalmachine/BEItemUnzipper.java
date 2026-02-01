package astral_mekanism.block.blockentity.normalmachine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleItem;
import mekanism.common.tile.prefab.TileEntityElectricMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEItemUnzipper extends TileEntityElectricMachine {

    public BEItemUnzipper(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, 200);
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<ItemStackToItemStackRecipe, SingleItem<ItemStackToItemStackRecipe>> getRecipeType() {
        return AstralMekanismRecipeTypes.ITEM_UNZIPPING;
    }
    
}
