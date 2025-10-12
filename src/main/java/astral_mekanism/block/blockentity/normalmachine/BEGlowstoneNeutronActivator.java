package astral_mekanism.block.blockentity.normalmachine;

import org.jetbrains.annotations.NotNull;
import astral_mekanism.block.blockentity.prefab.BEGasToGasBlock;
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
		return 10000;
	}

	@Override
	protected int maxOperation() {
		return 128;
	}

	@Override
	public String getJEI() {
		return "mekanism:solar_neutron_activator";
	}
}