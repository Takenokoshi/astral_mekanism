package astral_mekanism.block.blockentity.astralmachine;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.prefab.BEGasToGasMachine;
import astral_mekanism.registries.AMEMachines;
import astral_mekanism.registries.AMERecipeTypes;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
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
    protected long tankCapacity() {
        return Long.MAX_VALUE;
    }

    @Override
    protected int maxOperation() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<ResourceLocation> getJEI() {
        return List.of(MekanismBlocks.SPS_CASING.getRegistryName(),
                AMEMachines.COMPACT_SPS.getRegistryName());
    }

}
