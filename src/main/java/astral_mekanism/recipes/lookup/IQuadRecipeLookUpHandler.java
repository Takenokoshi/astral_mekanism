package astral_mekanism.recipes.lookup;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.recipes.inputRecipeCache.QuadInputRecipeCache;
import astral_mekanism.util.AMInterface.QuadPredicate;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.common.recipe.lookup.IRecipeLookupHandler;

public interface IQuadRecipeLookUpHandler<INPUT_A, INPUT_B, INPUT_C, INPUT_D, //
        RECIPE extends MekanismRecipe & QuadPredicate<INPUT_A, INPUT_B, INPUT_C, INPUT_D>, //
        INPUT_CACHE extends QuadInputRecipeCache<INPUT_A, ?, INPUT_B, ?, INPUT_C, ?, INPUT_D, ?, RECIPE, ?, ?, ?, ?>> //
        extends IRecipeLookupHandler.IRecipeTypedLookupHandler<RECIPE, INPUT_CACHE> {
    default boolean containsRecipeABCD(INPUT_A inputA, INPUT_B inputB, INPUT_C inputC, INPUT_D inputD) {
        return getRecipeType().getInputCache().containsInputABCD(getHandlerWorld(), inputA, inputB, inputC, inputD);
    }

    default boolean containsRecipeBACD(INPUT_A inputA, INPUT_B inputB, INPUT_C inputC, INPUT_D inputD) {
        return getRecipeType().getInputCache().containsInputABCD(getHandlerWorld(), inputA, inputB, inputC, inputD);
    }

    default boolean containsRecipeCABD(INPUT_A inputA, INPUT_B inputB, INPUT_C inputC, INPUT_D inputD) {
        return getRecipeType().getInputCache().containsInputABCD(getHandlerWorld(), inputA, inputB, inputC, inputD);
    }

    default boolean containsRecipeDABC(INPUT_A inputA, INPUT_B inputB, INPUT_C inputC, INPUT_D inputD) {
        return getRecipeType().getInputCache().containsInputABCD(getHandlerWorld(), inputA, inputB, inputC, inputD);
    }

    default boolean containsRecipeA(INPUT_A input) {
        return getRecipeType().getInputCache().containsInputA(this.getHandlerWorld(), input);
    }

    default boolean containsRecipeB(INPUT_B input) {
        return getRecipeType().getInputCache().containsInputB(this.getHandlerWorld(), input);
    }

    default boolean containsRecipeC(INPUT_C input) {
        return getRecipeType().getInputCache().containsInputC(this.getHandlerWorld(), input);
    }

    default boolean containsRecipeD(INPUT_D input) {
        return getRecipeType().getInputCache().containsInputD(this.getHandlerWorld(), input);
    }

    default @Nullable RECIPE findFirstRecipe(INPUT_A inputA, INPUT_B inputB, INPUT_C inputC, INPUT_D inputD) {
        return getRecipeType().getInputCache().findFirstRecipe(getHandlerWorld(), inputA, inputB, inputC, inputD);
    }

    default @Nullable RECIPE findFirstRecipe(IInputHandler<INPUT_A> inputAHandler, IInputHandler<INPUT_B> inputBHandler,
            IInputHandler<INPUT_C> inputCHandler, IInputHandler<INPUT_D> inputDHandler) {
        return this.findFirstRecipe(inputAHandler.getInput(), inputBHandler.getInput(), inputCHandler.getInput(),
                inputDHandler.getInput());
    }
}
