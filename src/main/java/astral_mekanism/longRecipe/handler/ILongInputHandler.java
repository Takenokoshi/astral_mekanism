package astral_mekanism.longRecipe.handler;

import astral_mekanism.longRecipe.LongOperationTracker;
import mekanism.api.recipes.ingredients.InputIngredient;

public interface ILongInputHandler<INPUT> {
    INPUT getInput();

    INPUT getRecipeInput(InputIngredient<INPUT> recipeIngredient);

    void use(INPUT recipeInput, long operations);

    void calculateOperationsCanSupport(LongOperationTracker tracker, INPUT recipeInput, int usageMultiplier);

    default void calculateOperationsCanSupport(LongOperationTracker tracker, INPUT recipeInput) {
        calculateOperationsCanSupport(tracker, recipeInput, 1);
    }
}
