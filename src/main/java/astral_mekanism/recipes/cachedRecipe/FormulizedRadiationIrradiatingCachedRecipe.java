package astral_mekanism.recipes.cachedRecipe;

import java.util.function.BooleanSupplier;
import fixdol.mekanismelements.api.recipes.RadiationIrradiatingRecipe;

import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.cache.TwoInputCachedRecipe;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;

public class FormulizedRadiationIrradiatingCachedRecipe
        extends TwoInputCachedRecipe<ItemStack, GasStack, GasStack, RadiationIrradiatingRecipe> {

    public FormulizedRadiationIrradiatingCachedRecipe(RadiationIrradiatingRecipe recipe,
            BooleanSupplier recheckAllErrors, IInputHandler<ItemStack> inputHandler,
            IInputHandler<GasStack> secondaryInputHandler,
            IOutputHandler<GasStack> outputHandler) {
        super(recipe, recheckAllErrors, inputHandler, secondaryInputHandler, outputHandler,
                recipe::getItemInput,
                () -> IngredientCreatorAccess.gas()
                        .from(recipe.getGasInput().getRepresentations().stream().map(stack -> {
                            GasStack v = stack.copy();
                            v.setAmount(stack.getAmount() * 25);
                            return v;
                        }).map(IngredientCreatorAccess.gas()::from)),
                recipe::getOutput,
                ItemStack::isEmpty,
                GasStack::isEmpty,
                GasStack::isEmpty);
    }
}
