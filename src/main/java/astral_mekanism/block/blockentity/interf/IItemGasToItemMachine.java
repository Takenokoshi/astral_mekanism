package astral_mekanism.block.blockentity.interf;

import java.util.List;

import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.common.lib.Color;
import mekanism.common.recipe.lookup.IDoubleRecipeLookupHandler.ItemChemicalRecipeLookupHandler;
import mekanism.common.tile.base.TileEntityMekanism;

public interface IItemGasToItemMachine<BE extends TileEntityMekanism & IItemGasToItemMachine<BE, RECIPE>, RECIPE extends ItemStackGasToItemStackRecipe>
        extends IEnergizedMachine<BE>, ItemChemicalRecipeLookupHandler<Gas, GasStack, RECIPE> {

    public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    public IGasTank getInputTank();

    public default Color getColor() {
        return getInputTank().isEmpty() ? Color.WHITE : Color.rgb(getInputTank().getStack().getChemicalTint());
    }

    public FloatingLong getEnergyUsage();
}
