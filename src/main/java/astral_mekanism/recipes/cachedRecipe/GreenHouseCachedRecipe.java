package astral_mekanism.recipes.cachedRecipe;

import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.recipes.output.TripleItemOutput;
import astral_mekanism.recipes.recipe.GreenhouseRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GreenhouseCachedRecipe extends CachedRecipe<GreenhouseRecipe> {

    private final IInputHandler<ItemStack> seedInputHandler;
    private final IInputHandler<ItemStack> farmlandHandler;
    private final IInputHandler<FluidStack> fluidHandler;
    private final IOutputHandler<TripleItemOutput> outputHandler;

    @Nullable
    private ItemStack seedRecipeInput;
    @Nullable
    private ItemStack farmlandRecipeInput;
    @Nullable
    private FluidStack fluidRecipeInput;
    @Nullable
    private TripleItemOutput recipeOutput;

    public GreenhouseCachedRecipe(GreenhouseRecipe recipe,
            BooleanSupplier recheckAllErrors,
            IInputHandler<ItemStack> seedInputHandler,
            IInputHandler<ItemStack> farmlandHandler,
            IInputHandler<FluidStack> fluidHandler,
            IOutputHandler<TripleItemOutput> outputHandler) {
        super(recipe, recheckAllErrors);
        this.seedInputHandler = seedInputHandler;
        this.farmlandHandler = farmlandHandler;
        this.fluidHandler = fluidHandler;
        this.outputHandler = outputHandler;
    }

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            seedRecipeInput = seedInputHandler.getRecipeInput(recipe.getInputSeed());
            farmlandRecipeInput = farmlandHandler.getRecipeInput(recipe.getFarmland());
            fluidRecipeInput = fluidHandler.getRecipeInput(recipe.getInputFluid());
            recipeOutput = recipe.getOutput();
            if (seedRecipeInput.isEmpty()
                    || farmlandRecipeInput.isEmpty()
                    || fluidRecipeInput.isEmpty()
                    || recipeOutput.isEmpty()) {
                tracker.mismatchedRecipe();
                return;
            }
            seedInputHandler.calculateOperationsCanSupport(tracker, seedRecipeInput);
            farmlandHandler.calculateOperationsCanSupport(tracker, farmlandRecipeInput);
            fluidHandler.calculateOperationsCanSupport(tracker, fluidRecipeInput);
            outputHandler.calculateOperationsCanSupport(tracker, recipeOutput);
        }
    }

    @Override
    protected void finishProcessing(int arg0) {
        if (seedRecipeInput == null || farmlandRecipeInput == null || fluidRecipeInput == null || recipeOutput == null
                || recipeOutput.isEmpty()) {
            return;
        }
        seedInputHandler.use(seedRecipeInput, arg0);
        farmlandHandler.use(farmlandRecipeInput, arg0);
        fluidHandler.use(fluidRecipeInput, arg0);
        outputHandler.handleOutput(recipeOutput, arg0);
    }

    @Override
    public boolean isInputValid() {
        return !seedInputHandler.getInput().isEmpty()
                && !farmlandHandler.getInput().isEmpty()
                && !fluidHandler.getInput().isEmpty()
                && recipe.test(seedInputHandler.getInput(), farmlandHandler.getInput(), fluidHandler.getInput());
    }

}
