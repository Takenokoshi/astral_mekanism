package astral_mekanism.recipes.cachedRecipe;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import astral_mekanism.util.AMFunctionalInterface.TriConsumer;
import astral_mekanism.util.AMFunctionalInterface.TriFunction;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;

public class AMCachedRecipeHelper {
    public static <INPUT_A, INPUT_B, INPUT_C, OUTPUT> void threeInputCalculateOperationsThisTick(
        CachedRecipe.OperationTracker tracker,
        IInputHandler<INPUT_A> inputAHandler,
        Supplier<? extends InputIngredient<INPUT_A>> inputAIngredient,
        IInputHandler<INPUT_B> inputBHandler,
        Supplier<? extends InputIngredient<INPUT_B>> inputBIngredient,
        IInputHandler<INPUT_C> inputCHandler,
        Supplier<? extends InputIngredient<INPUT_C>> inputCIngredient,
        TriConsumer<INPUT_A, INPUT_B, INPUT_C> inputsSetter,
        IOutputHandler<OUTPUT> outputHandler,
        TriFunction<INPUT_A, INPUT_B, INPUT_C, OUTPUT> outputGetter,
        Consumer<OUTPUT> outputSetter,
        Predicate<INPUT_A> emptyCheckA,
        Predicate<INPUT_B> emptyCheckB,
        Predicate<INPUT_C> emptyCheckC
) {

    if (tracker.shouldContinueChecking()) {
        // === Input A ===
        INPUT_A inputA = inputAHandler.getRecipeInput(inputAIngredient.get());
        if (emptyCheckA.test(inputA)) {
            tracker.mismatchedRecipe();
            return;
        }

        // === Input B ===
        INPUT_B inputB = inputBHandler.getRecipeInput(inputBIngredient.get());
        if (emptyCheckB.test(inputB)) {
            tracker.mismatchedRecipe();
            return;
        }

        // === Input C ===
        INPUT_C inputC = inputCHandler.getRecipeInput(inputCIngredient.get());
        if (emptyCheckC.test(inputC)) {
            tracker.mismatchedRecipe();
            return;
        }

        // 入力セット
        inputsSetter.accept(inputA, inputB, inputC);

        // A がサポートできる回数
        inputAHandler.calculateOperationsCanSupport(tracker, inputA);
        if (!tracker.shouldContinueChecking()) return;

        // B がサポートできる回数
        inputBHandler.calculateOperationsCanSupport(tracker, inputB);
        if (!tracker.shouldContinueChecking()) return;

        // C がサポートできる回数
        inputCHandler.calculateOperationsCanSupport(tracker, inputC);
        if (!tracker.shouldContinueChecking()) return;

        // 出力を試算
        OUTPUT output = outputGetter.apply(inputA, inputB, inputC);
        outputSetter.accept(output);

        // 出力スロットが対応可能な回数
        outputHandler.calculateOperationsCanSupport(tracker, output);
    }
}

}
