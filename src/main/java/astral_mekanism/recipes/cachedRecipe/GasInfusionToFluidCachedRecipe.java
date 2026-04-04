package astral_mekanism.recipes.cachedRecipe;

import java.util.function.BooleanSupplier;

import astral_mekanism.recipes.recipe.GasInfusionToFluidRecipe;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraftforge.fluids.FluidStack;

public class GasInfusionToFluidCachedRecipe extends CachedRecipe<GasInfusionToFluidRecipe> {

    private final IInputHandler<GasStack> gasInputHandler;
    private final IInputHandler<InfusionStack> infusionInputHandler;
    private final IOutputHandler<FluidStack> outputHandler;

    private GasStack gasRecipeInput = GasStack.EMPTY;
    private InfusionStack infusionRecipeInput = InfusionStack.EMPTY;
    private FluidStack recipeOutput = FluidStack.EMPTY;

    public GasInfusionToFluidCachedRecipe(GasInfusionToFluidRecipe recipe, BooleanSupplier recheckAllErrors,
            IInputHandler<GasStack> gasInputHandler, IInputHandler<InfusionStack> infusionInputHandler,
            IOutputHandler<FluidStack> outputHandler) {
        super(recipe, recheckAllErrors);
        this.gasInputHandler = gasInputHandler;
        this.infusionInputHandler = infusionInputHandler;
        this.outputHandler = outputHandler;
    }

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (!tracker.shouldContinueChecking()) {
            return;
        }
        gasRecipeInput = gasInputHandler.getRecipeInput(recipe.getGasInput());
        infusionRecipeInput = infusionInputHandler.getRecipeInput(recipe.getInfusionInput());
        recipeOutput = recipe.getOutput();
        if (gasRecipeInput.isEmpty() || infusionRecipeInput.isEmpty() || recipeOutput.isEmpty()) {
            tracker.mismatchedRecipe();
            return;
        }
        gasInputHandler.calculateOperationsCanSupport(tracker, gasRecipeInput);
        infusionInputHandler.calculateOperationsCanSupport(tracker, infusionRecipeInput);
        outputHandler.calculateOperationsCanSupport(tracker, recipeOutput);
    }

    @Override
    public boolean isInputValid() {
        return !gasInputHandler.getInput().isEmpty() && !infusionInputHandler.getInput().isEmpty()
                && recipe.test(gasInputHandler.getInput(), infusionInputHandler.getInput());
    }

    @Override
    protected void finishProcessing(int operations) {
        if (gasRecipeInput.isEmpty() || infusionRecipeInput.isEmpty() || recipeOutput.isEmpty()) {
            return;
        }
        gasInputHandler.use(gasRecipeInput, operations);
        infusionInputHandler.use(infusionRecipeInput, operations);
        outputHandler.handleOutput(recipeOutput, operations);
    }

}
