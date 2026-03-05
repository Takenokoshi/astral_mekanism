package astral_mekanism.block.blockentity.interf;

import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.common.lib.Color;
import mekanism.common.recipe.lookup.IDoubleRecipeLookupHandler.ItemChemicalRecipeLookupHandler;
import mekanism.common.tile.base.TileEntityMekanism;

public interface ICompactAPT<BE extends TileEntityMekanism & ICompactAPT<BE>>
        extends IEnergizedMachine<BE>, ItemChemicalRecipeLookupHandler<Gas, GasStack, ItemStackGasToItemStackRecipe> {

    public IGasTank getInputTank();
    

    public default Color getColor(){
        return getInputTank().isEmpty() ? Color.WHITE : Color.rgb(getInputTank().getStack().getChemicalTint());
    }

    public FloatingLong getEnergyUsage();
}
