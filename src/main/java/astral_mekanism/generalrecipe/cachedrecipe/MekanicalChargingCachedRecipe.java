package astral_mekanism.generalrecipe.cachedrecipe;

import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.Nullable;

import appeng.recipes.handlers.ChargerRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;

public class MekanicalChargingCachedRecipe extends GeneralCachedRecipe<ChargerRecipe> {

    private final IInputHandler<ItemStack> inputHandler;
    private final IOutputHandler<ItemStack> outputHandler;
    private final ItemStackIngredient inputIngredient;
    @Nullable
    private ItemStack recipeInput;
    @Nullable
    private ItemStack recipeOutput;

    public MekanicalChargingCachedRecipe(ChargerRecipe recipe, BooleanSupplier recheckAllErrors,
            IInputHandler<ItemStack> inputHandler, IOutputHandler<ItemStack> outputHandler) {
        super(recipe, recheckAllErrors);
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
        IItemStackIngredientCreator creator = IngredientCreatorAccess.item();
        this.inputIngredient = creator
                .createMulti(recipe.getIngredients().stream().map(creator::from).toArray(ItemStackIngredient[]::new));
    }

    @Override
    public void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            ItemStack inputStack = inputHandler.getInput();
            if (inputStack.isEmpty()) {
                tracker.mismatchedRecipe();
            } else {
                recipeInput = inputHandler.getRecipeInput(inputIngredient);
                recipeOutput = recipe.getResultItem();
                if (recipeInput.isEmpty()||recipeOutput.isEmpty()) {
                    tracker.mismatchedRecipe();
                    return;
                }
                inputHandler.calculateOperationsCanSupport(tracker, recipeInput);
                outputHandler.calculateOperationsCanSupport(tracker, recipeOutput);
            }
        }
    }

    @Override
    public boolean isInputValid() {
        return !inputHandler.getInput().isEmpty() && inputIngredient.test(inputHandler.getInput());
    }

    @Override
    public void finishProcessing(int operations) {
        if (recipeInput != null && !recipeInput.isEmpty() && recipeOutput != null) {
            inputHandler.use(recipeInput, operations);
            outputHandler.handleOutput(recipeOutput, operations);
        }
    }

}
