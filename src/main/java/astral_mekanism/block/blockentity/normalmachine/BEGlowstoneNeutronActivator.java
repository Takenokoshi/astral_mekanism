package astral_mekanism.block.blockentity.normalmachine;

import org.jetbrains.annotations.NotNull;
import astral_mekanism.block.blockentity.prefab.BEGasToGasBlock;
import astral_mekanism.integration.AMEEmpowered;
import mekanism.api.Upgrade;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleChemical;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEGlowstoneNeutronActivator extends BEGasToGasBlock {

    public BEGlowstoneNeutronActivator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @NotNull
    @Override
    public IMekanismRecipeTypeProvider<GasToGasRecipe, SingleChemical<Gas, GasStack, GasToGasRecipe>> getRecipeType() {
        return MekanismRecipeType.ACTIVATING;
    }

    @Override
    protected long tankCapacity() {
        return 0x100000000l;
    }

    @Override
    protected int maxOperation() {
        return 128;
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (AMEEmpowered.empoweredIsLoaded()) {
            baselineMaxOperations = 128 << AMEEmpowered.getAllSpeeds(this);
        } else if (upgrade == Upgrade.SPEED) {
            baselineMaxOperations = 128 << upgradeComponent.getUpgrades(Upgrade.SPEED);
        }
    }

    @Override
    public String getJEI() {
        return "mekanism:solar_neutron_activator";
    }
}