package astral_mekanism.block.blockentity.interf;

import astral_mekanism.recipes.lookup.AMIRecipeLookUpHandler.QuadItemRecipeLookUpHandler;
import astral_mekanism.recipes.recipe.TransitionRecipe;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;

public interface IMekanicalTransformer<BE extends TileEntityRecipeMachine<TransitionRecipe> & IMekanicalTransformer<BE>>
        extends QuadItemRecipeLookUpHandler<TransitionRecipe> {
    public BasicFluidTank getFluidTank();

    public MachineEnergyContainer<BE> getEnergyContainer();

    public abstract double getProgressScaled();
}
