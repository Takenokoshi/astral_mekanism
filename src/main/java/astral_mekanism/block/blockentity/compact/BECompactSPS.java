package astral_mekanism.block.blockentity.compact;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.prefab.BEGasToGasMachine;
import astral_mekanism.enumexpansion.AMEUpgrade;
import astral_mekanism.registries.AMEMachines;
import astral_mekanism.registries.AMERecipeTypes;
import mekanism.api.Upgrade;
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

public class BECompactSPS extends BEGasToGasMachine {

    public BECompactSPS(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @NotNull
    @Override
    public IMekanismRecipeTypeProvider<GasToGasRecipe, SingleChemical<Gas, GasStack, GasToGasRecipe>> getRecipeType() {
        return AMERecipeTypes.SPS_RECIPE;
    }

    @Override
    public List<ResourceLocation> getJEI() {
        return List.of(MekanismBlocks.SPS_CASING.getRegistryName(),
                AMEMachines.COMPACT_SPS.getRegistryName());
    }

    @Override
    protected long tankCapacity() {
        return 40000000;
    }

    @Override
    protected int maxOperation() {
        return 2;
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == AMEUpgrade.HYPER_SPEED.getValue()) {
            int hyperSpeed = upgradeComponent.getUpgrades(AMEUpgrade.HYPER_SPEED.getValue());
            baselineMaxOperations = (hyperSpeed % 2 + 2) << (hyperSpeed / 2);
        }
    }
}
