package astral_mekanism.recipes.cachedRecipe;

import java.util.Arrays;
import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.recipes.recipe.AstralCraftingRecipe;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class AstralCraftingCachedRecipe extends CachedRecipe<AstralCraftingRecipe> {

    private final IInputHandler<ItemStack>[] itemInputHandlers;
    private final IInputHandler<FluidStack> fluidInputHandler;
    private final IInputHandler<GasStack> gasInputHandler;
    private final IOutputHandler<ItemStack> outputHandler;

    @Nullable
    private ItemStack[] itemsRecipeInput;
    @Nullable
    private FluidStack fluidRecipeInput;
    @Nullable
    private GasStack gasRecipeInput;
    @Nullable
    private ItemStack recipeOutput;

    @SuppressWarnings("unchecked")
    public AstralCraftingCachedRecipe(AstralCraftingRecipe recipe, BooleanSupplier recheckAllErrors,
            IInputHandler<ItemStack>[] itemInputHandlers, IInputHandler<FluidStack> fluidInputHandler,
            IInputHandler<GasStack> gasInputHandler, IOutputHandler<ItemStack> outputHandler) {
        super(recipe, recheckAllErrors);

        this.itemInputHandlers = new IInputHandler[25];
        for (int i = 0; i < 25; i++) {
            this.itemInputHandlers[i] = itemInputHandlers[i % itemInputHandlers.length];
        }

        this.fluidInputHandler = fluidInputHandler;
        this.gasInputHandler = gasInputHandler;
        this.outputHandler = outputHandler;
    }

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (!tracker.shouldContinueChecking()) {
            return;
        }
        for (int i = 0; i < 25; i++) {
            itemsRecipeInput[i] = itemInputHandlers[i].getInput();
        }

        fluidRecipeInput = fluidInputHandler.getInput();
        gasRecipeInput = gasInputHandler.getInput();
        recipeOutput = recipe.getOutput(itemsRecipeInput, fluidRecipeInput, gasRecipeInput);
        if (fluidRecipeInput.isEmpty() || gasRecipeInput.isEmpty()) {
            tracker.mismatchedRecipe();
            return;
        }
        for (int i = 0; i < 25; i++) {
            if (itemsRecipeInput[i].isEmpty()) {
                tracker.mismatchedRecipe();
                return;
            }
        }
        for (int i = 0; i < 25; i++) {
            itemInputHandlers[i].calculateOperationsCanSupport(tracker, itemsRecipeInput[i]);
            if (!tracker.shouldContinueChecking()) {
                return;
            }
        }
        fluidInputHandler.calculateOperationsCanSupport(tracker, fluidRecipeInput);
        gasInputHandler.calculateOperationsCanSupport(tracker, gasRecipeInput);
        outputHandler.calculateOperationsCanSupport(tracker, recipeOutput);
    }

    @Override
    protected void finishProcessing(int operations) {
        if (itemsRecipeInput == null || fluidRecipeInput == null || gasRecipeInput == null || recipeOutput == null) {
            return;
        }
        if (recipeOutput.isEmpty()) {
            return;
        }
        for (int i = 0; i < 25; i++) {
            if (itemsRecipeInput[i].isEmpty()) {
                return;
            }
        }
        for (int i = 0; i < 25; i++) {
            itemInputHandlers[i].use(itemsRecipeInput[i], operations);
        }
        fluidInputHandler.use(fluidRecipeInput, operations);
        gasInputHandler.use(gasRecipeInput, operations);
        outputHandler.handleOutput(recipeOutput, operations);
    }

    @Override
    public boolean isInputValid() {
        for (IInputHandler<ItemStack> handler : itemInputHandlers) {
            if (handler.getInput().isEmpty()) {
                return false;
            }
        }
        return !fluidInputHandler.getInput().isEmpty()
                && !gasInputHandler.getInput().isEmpty()
                && recipe.test(
                        Arrays.stream(itemInputHandlers).map(IInputHandler::getInput).toArray(ItemStack[]::new),
                        fluidInputHandler.getInput(),
                        gasInputHandler.getInput());
    }
}
