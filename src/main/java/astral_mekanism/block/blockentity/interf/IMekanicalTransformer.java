package astral_mekanism.block.blockentity.interf;

import astral_mekanism.recipes.lookup.AMIRecipeLookUpHandler.QuadrupleItemRecipeLookUpHandler;
import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;

public interface IMekanicalTransformer<BE extends TileEntityRecipeMachine<MekanicalTransformRecipe> & IMekanicalTransformer<BE>>
        extends QuadrupleItemRecipeLookUpHandler<MekanicalTransformRecipe> {
    public BasicFluidTank getFluidTank();

    public MachineEnergyContainer<BE> getEnergyContainer();

    public abstract double getProgressScaled();
}
