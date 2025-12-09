package astral_mekanism.block.blockentity.astralmachine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.prefab.BEDoubleItemToItemRecipeMachine;
import astral_mekanism.recipes.handler.CatalystHelper;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.CombinerRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.DoubleItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralMekanicalInscriber extends BEDoubleItemToItemRecipeMachine {

    public BEAstralMekanicalInscriber(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<CombinerRecipe, DoubleItem<CombinerRecipe>> getRecipeType() {
        return AstralMekanismRecipeTypes.MEKANICAL_INSCRIBER_RECIPE;
    }

    @Override
    protected IInputHandler<ItemStack> createMainInputHandler(IInventorySlot slot, RecipeError notEnoughError) {
        return InputHelper.getInputHandler(slot, notEnoughError);
    }

    @Override
    protected IInputHandler<ItemStack> createExtraInputHandler(IInventorySlot slot, RecipeError notEnoughError) {
        return CatalystHelper.getCatalystHandler(slot, notEnoughError);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String getJEI() {
        return "astral_mekanism:mekanical_inscriber";
    }

}
