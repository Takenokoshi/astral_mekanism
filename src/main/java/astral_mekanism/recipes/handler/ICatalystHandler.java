package astral_mekanism.recipes.handler;

import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.api.recipes.inputs.IInputHandler;

public interface ICatalystHandler<CATALYST> extends IInputHandler<CATALYST> {

    public abstract CATALYST getCatalyst();

    public abstract CATALYST getRecipeCatalyst(InputIngredient<CATALYST> ingredient);

    @Override
    default CATALYST getInput() {
        return getCatalyst();
    }

    @Override
    default CATALYST getRecipeInput(InputIngredient<CATALYST> arg0) {
        return getRecipeCatalyst(arg0);
    }

    @Override
    default void use(CATALYST catalyst, int multiply) {
    }
}
