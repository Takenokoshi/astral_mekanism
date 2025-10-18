package astral_mekanism.recipes.cachedRecipe;

import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.recipes.output.DoubleItemStackOutput;
import astral_mekanism.recipes.recipe.ItemToItemItemRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipeHelper;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;

public class ItemToItemItemCachedRecipe extends CachedRecipe<ItemToItemItemRecipe> {

    private final IInputHandler<ItemStack> inputHandler;
    private final IOutputHandler<DoubleItemStackOutput> outputHandler;
    @Nullable
    private ItemStack recipeInput;
    @Nullable
    private DoubleItemStackOutput recipeOutput;

    public ItemToItemItemCachedRecipe(ItemToItemItemRecipe recipe, BooleanSupplier recheckAllErrors,
            IInputHandler<ItemStack> inputHandler, IOutputHandler<DoubleItemStackOutput> outputHandlerA) {
        super(recipe, recheckAllErrors);
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandlerA;
    }

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            ItemStack inputStack = inputHandler.getInput();
            if (inputStack.isEmpty()) {
                tracker.mismatchedRecipe();
            } else {
                ItemStackIngredient inputIngredient = recipe.getInput();
                CachedRecipeHelper.oneInputCalculateOperationsThisTick(tracker,
                        inputHandler, () -> inputIngredient, i -> recipeInput = i,
                        outputHandler, recipe::getOutput, o -> recipeOutput = o, ItemStack::isEmpty);
            }
        }
    }

    @Override
    protected void finishProcessing(int arg0) {
        if (recipeInput != null && !recipeInput.isEmpty() && recipeOutput != null) {
            inputHandler.use(recipeInput, arg0);
            outputHandler.handleOutput(recipeOutput, arg0);
        }
    }

    @Override
    public boolean isInputValid() {
        return !inputHandler.getInput().isEmpty() && recipe.test(inputHandler.getInput());
    }

}
