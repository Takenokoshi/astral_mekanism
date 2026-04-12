package astral_mekanism.recipes.handler;

import mekanism.api.chemical.merged.BoxedChemicalStack;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;

public interface IBoxedChemicalInputHandler {
    BoxedChemicalStack getInput();

    BoxedChemicalStack getRecipeInput(ChemicalStackIngredient<?, ?> recipeIngredient);

    void use(BoxedChemicalStack recipeInput, long operations);

    void calculateOperationsCanSupport(OperationTracker tracker, BoxedChemicalStack recipeInput, long usageMultiplier);
}
