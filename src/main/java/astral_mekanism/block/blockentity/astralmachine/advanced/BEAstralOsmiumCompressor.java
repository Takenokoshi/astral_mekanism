package astral_mekanism.block.blockentity.astralmachine.advanced;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.basemachine.BEAMEAdvancedMachine;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.ItemChemical;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralOsmiumCompressor extends BEAMEAdvancedMachine {

    public BEAstralOsmiumCompressor(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, "mekanism:osmium_compressor",200);
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<ItemStackGasToItemStackRecipe, ItemChemical<Gas, GasStack, ItemStackGasToItemStackRecipe>> getRecipeType() {
        return MekanismRecipeType.COMPRESSING;
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

    @Override
    protected IGasTank createGasTank(IContentsListener recipeCacheListener) {
        return ChemicalTankBuilder.GAS.create(Long.MAX_VALUE,
                ChemicalTankBuilder.GAS.notExternal,
                (gas, automationType) -> containsRecipeBA(inputInventorySlot.getStack(), gas),
                this::containsRecipeB, ChemicalAttributeValidator.ALWAYS_ALLOW, recipeCacheListener);
    }
    
}
