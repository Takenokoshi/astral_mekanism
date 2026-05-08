package astral_mekanism.block.blockentity.astralmachine;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.prefab.BEGasToGasMachine;
import astral_mekanism.registries.AMEMachines;
import astral_mekanism.registries.AMERecipeTypes;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleChemical;
import mekanism.common.registries.MekanismBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class BEAstralSPS extends BEGasToGasMachine {

    public BEAstralSPS(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<GasToGasRecipe, SingleChemical<Gas, GasStack, GasToGasRecipe>> getRecipeType() {
        return AMERecipeTypes.SPS_RECIPE;
    }

    @Override
    public List<ResourceLocation> getJEI() {
        return List.of(MekanismBlocks.SPS_CASING.getRegistryName(),
                AMEMachines.COMPACT_SPS.getRegistryName());
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

    @Override
    protected IGasTank createInputTank(BiPredicate<Gas, AutomationType> canExtract,
            BiPredicate<Gas, AutomationType> canInsert, Predicate<Gas> validator,
            @Nullable ChemicalAttributeValidator attributeValidator, @Nullable IContentsListener listener) {
        return ChemicalTankBuilder.GAS.create(Long.MAX_VALUE, canExtract, canInsert, validator, attributeValidator, listener);
    }

}
