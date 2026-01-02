package astral_mekanism.recipes.cachedRecipe;

import java.util.function.BooleanSupplier;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.merged.BoxedChemicalStack;
import mekanism.api.recipes.ChemicalDissolutionRecipe;
import mekanism.api.recipes.cache.TwoInputCachedRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;

public class FormulizedDissolutionCachedRecipe
        extends TwoInputCachedRecipe<ItemStack, GasStack, BoxedChemicalStack, ChemicalDissolutionRecipe> {

    public FormulizedDissolutionCachedRecipe(ChemicalDissolutionRecipe recipe, BooleanSupplier recheckAllErrors,
            IInputHandler<ItemStack> inputHandler, IInputHandler<GasStack> secondaryInputHandler,
            IOutputHandler<BoxedChemicalStack> outputHandler, int multiply) {
        super(recipe, recheckAllErrors, inputHandler, secondaryInputHandler, outputHandler, recipe::getItemInput,
                () -> {
                    return IngredientCreatorAccess.gas()
                            .createMulti(recipe.getGasInput().getRepresentations().stream()
                                    .map(stack -> IngredientCreatorAccess.gas()
                                            .from(new GasStack(stack, stack.getAmount() * multiply)))
                                    .toArray(GasStackIngredient[]::new));
                }, recipe::getOutput, ItemStack::isEmpty, GasStack::isEmpty, BoxedChemicalStack::isEmpty);
    }

}
