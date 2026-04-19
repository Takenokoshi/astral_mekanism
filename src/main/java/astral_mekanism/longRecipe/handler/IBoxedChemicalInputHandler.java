package astral_mekanism.longRecipe.handler;

import astral_mekanism.longRecipe.LongOperationTracker;
import mekanism.api.chemical.merged.BoxedChemicalStack;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;

public interface IBoxedChemicalInputHandler {
    BoxedChemicalStack getInput();

    BoxedChemicalStack getRecipeInput(ChemicalStackIngredient<?, ?> recipeIngredient);

    void use(BoxedChemicalStack recipeInput, long operations);

    void calculateOperationsCanSupport(LongOperationTracker tracker, BoxedChemicalStack recipeInput, long usageMultiplier);
}
