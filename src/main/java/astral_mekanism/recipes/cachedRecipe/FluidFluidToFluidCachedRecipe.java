package astral_mekanism.recipes.cachedRecipe;

import java.util.function.BooleanSupplier;

import javax.annotation.Nullable;

import astral_mekanism.recipes.recipe.FluidFluidToFluidRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipeHelper;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraftforge.fluids.FluidStack;

public class FluidFluidToFluidCachedRecipe extends CachedRecipe<FluidFluidToFluidRecipe> {

	private final IOutputHandler<FluidStack> outputHandler;
	private final IInputHandler<FluidStack> inputHandlerA;
	private final IInputHandler<FluidStack> inputHandlerB;
	@Nullable
	private FluidStack recipeInputA;
	@Nullable
	private FluidStack recipeInputB;
	@Nullable
	private FluidStack recipeOutput;

	public FluidFluidToFluidCachedRecipe(FluidFluidToFluidRecipe recipe, BooleanSupplier recheckAllErrors,
			IInputHandler<FluidStack> inputHandlerA, IInputHandler<FluidStack> inputHandlerB,
			IOutputHandler<FluidStack> outputHandler) {
		super(recipe, recheckAllErrors);
		this.outputHandler = outputHandler;
		this.inputHandlerA = inputHandlerA;
		this.inputHandlerB = inputHandlerB;
	}

	@Override
	protected void calculateOperationsThisTick(OperationTracker tracker) {
		super.calculateOperationsThisTick(tracker);
		if (tracker.shouldContinueChecking()) {
			FluidStack inputFluidA = inputHandlerA.getInput();
			FluidStack inputFluidB = inputHandlerB.getInput();
			if (inputFluidA.isEmpty() || inputFluidB.isEmpty()) {
				tracker.mismatchedRecipe();
			} else {
				FluidStackIngredient inputA = recipe.getInputA();
				FluidStackIngredient inputB = recipe.getInputB();
				CachedRecipeHelper.twoInputCalculateOperationsThisTick(tracker, inputHandlerA,
						() -> inputA, inputHandlerB, () -> inputB, (A, B) -> {
							recipeInputA = A;
							recipeInputB = B;
						}, outputHandler, recipe::getOutput,
						output -> this.recipeOutput = output, FluidStack::isEmpty,
						FluidStack::isEmpty);
			}

		}
	}

	@Override
	protected void finishProcessing(int arg0) {
		if (recipeInputA != null && recipeInputB != null && recipeOutput != null && !recipeInputA.isEmpty()
				&& !recipeInputB.isEmpty() && !recipeOutput.isEmpty()) {
			inputHandlerA.use(recipeInputA, arg0);
			inputHandlerB.use(recipeInputB, arg0);
			outputHandler.handleOutput(recipeOutput, arg0);
		}
	}

	@Override
	public boolean isInputValid() {
		return !inputHandlerA.getInput().isEmpty() && !inputHandlerB.getInput().isEmpty()
				&& recipe.test(inputHandlerA.getInput(), inputHandlerB.getInput());
	}

}
