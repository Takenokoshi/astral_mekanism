package astral_mekanism.recipes.cachedRecipe;

import java.util.function.BooleanSupplier;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.cache.TwoInputCachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.world.item.ItemStack;

public class FormulizedItemGasToItemCachedRecipe
        extends TwoInputCachedRecipe<ItemStack, GasStack, ItemStack, ItemStackGasToItemStackRecipe> {

    public FormulizedItemGasToItemCachedRecipe(ItemStackGasToItemStackRecipe recipe,
            BooleanSupplier recheckAllErrors, IInputHandler<ItemStack> inputHandler,
            IInputHandler<GasStack> secondaryInputHandler, IOutputHandler<ItemStack> outputHandler, int multiply) {
        super(recipe, recheckAllErrors, inputHandler, secondaryInputHandler, outputHandler, recipe::getItemInput,
                () -> {
                    return IngredientCreatorAccess.gas()
                            .createMulti(recipe.getChemicalInput().getRepresentations().stream()
                                    .map(stack -> IngredientCreatorAccess.gas()
                                            .from(new GasStack(stack, stack.getAmount() * multiply)))
                                    .toArray(GasStackIngredient[]::new));
                }, recipe::getOutput, ItemStack::isEmpty, GasStack::isEmpty, ItemStack::isEmpty);
    }

}
