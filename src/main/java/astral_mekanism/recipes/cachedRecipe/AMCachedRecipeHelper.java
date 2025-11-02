package astral_mekanism.recipes.cachedRecipe;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import astral_mekanism.util.AMFunctionalInterface.TriConsumer;
import astral_mekanism.util.AMFunctionalInterface.TriFunction;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class AMCachedRecipeHelper {

    public static void astralCraftingInputCalculateOperationsThisTick(
            CachedRecipe.OperationTracker tracker,

            IInputHandler<ItemStack>[] itemHandlers, // 25スロット
            Function<Integer, ItemStackIngredient> ingredientGetter, // index -> ingredient

            IInputHandler<FluidStack> fluidHandler,
            Supplier<? extends InputIngredient<FluidStack>> fluidIngredient,

            IInputHandler<GasStack> gasHandler,
            Supplier<? extends InputIngredient<GasStack>> gasIngredient,

            TriConsumer<ItemStack[], FluidStack, GasStack> inputsSetter,

            IOutputHandler<ItemStack> outputHandler,
            TriFunction<ItemStack[], FluidStack, GasStack, ItemStack> outputGetter,
            Consumer<ItemStack> outputSetter,
            BiPredicate<ItemStack, Integer> emptyCheckItems,
            Predicate<FluidStack> emptyCheckFluid,
            Predicate<GasStack> emptyCheckGas) {

        if (!tracker.shouldContinueChecking()) {
            return;
        }

        int itemSlots = itemHandlers.length;
        ItemStack[] itemInputs = new ItemStack[itemSlots];

        // --- A: ItemStack inputs ---
        for (int i = 0; i < itemSlots; i++) {
            ItemStackIngredient ingredient = ingredientGetter.apply(i);
            ItemStack stack = itemHandlers[i].getRecipeInput(ingredient);
            itemInputs[i] = stack;

            if (emptyCheckItems.test(stack, i)) {
                tracker.mismatchedRecipe();
                return;
            }
        }

        // --- B: FluidStack ---
        FluidStack fluidInput = fluidHandler.getRecipeInput(fluidIngredient.get());
        if (emptyCheckFluid.test(fluidInput)) {
            tracker.mismatchedRecipe();
            return;
        }

        // --- C: GasStack ---
        GasStack gasInput = gasHandler.getRecipeInput(gasIngredient.get());
        if (emptyCheckGas.test(gasInput)) {
            tracker.mismatchedRecipe();
            return;
        }

        // 入力セット
        inputsSetter.accept(itemInputs, fluidInput, gasInput);

        // --- 消費可能回数：アイテム ---
        for (int i = 0; i < itemSlots; i++) {
            itemHandlers[i].calculateOperationsCanSupport(tracker, itemInputs[i]);
            if (!tracker.shouldContinueChecking()) {
                return;
            }
        }

        // --- Fluid ---
        fluidHandler.calculateOperationsCanSupport(tracker, fluidInput);
        if (!tracker.shouldContinueChecking())
            return;

        // --- Gas ---
        gasHandler.calculateOperationsCanSupport(tracker, gasInput);
        if (!tracker.shouldContinueChecking())
            return;

        // --- Output calculation ---
        ItemStack output = outputGetter.apply(itemInputs, fluidInput, gasInput);
        outputSetter.accept(output);

        // 出力側が対応できる回数
        outputHandler.calculateOperationsCanSupport(tracker, output);
    }

}
