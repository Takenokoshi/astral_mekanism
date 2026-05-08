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

public class BEAstralChemicalInjectionChamber extends BEAMEAdvancedMachine {

    public BEAstralChemicalInjectionChamber(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, "mekanism:chemical_injection_chamber",20);
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<ItemStackGasToItemStackRecipe, ItemChemical<Gas, GasStack, ItemStackGasToItemStackRecipe>> getRecipeType() {
        return MekanismRecipeType.INJECTING;
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
