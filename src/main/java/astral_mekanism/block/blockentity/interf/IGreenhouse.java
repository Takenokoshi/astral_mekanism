package astral_mekanism.block.blockentity.interf;

import astral_mekanism.recipes.lookup.AMIRecipeLookUpHandler.ItemItemFluidRecipeLookUpHandler;
import astral_mekanism.recipes.recipe.GreenhouseRecipe;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;

public interface IGreenhouse<BE extends TileEntityRecipeMachine<GreenhouseRecipe> & IGreenhouse<BE>>
        extends ItemItemFluidRecipeLookUpHandler<GreenhouseRecipe> {

    public abstract BasicFluidTank getFluidTank();

    public abstract MachineEnergyContainer<BE> getEnergyContainer();

    public abstract double getProgressScaled();
}
