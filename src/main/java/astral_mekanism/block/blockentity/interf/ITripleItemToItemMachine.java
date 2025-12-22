package astral_mekanism.block.blockentity.interf;

import astral_mekanism.recipes.lookup.AMIRecipeLookUpHandler.TripleItemRecipeLookUpHandler;
import astral_mekanism.recipes.recipe.TripleItemToItemRecipe;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;

public interface ITripleItemToItemMachine<BE extends TileEntityRecipeMachine<TripleItemToItemRecipe> & ITripleItemToItemMachine<BE>>
        extends TripleItemRecipeLookUpHandler<TripleItemToItemRecipe> {

    public abstract MachineEnergyContainer<BE> getEnergyContainer();

    public abstract double getProgressScaled();

    public abstract String getJEI();
}
