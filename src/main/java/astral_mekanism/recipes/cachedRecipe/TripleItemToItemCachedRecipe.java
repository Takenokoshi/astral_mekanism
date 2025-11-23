package astral_mekanism.recipes.cachedRecipe;

import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.recipes.recipe.TripleItemToItemRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;

public class TripleItemToItemCachedRecipe extends CachedRecipe<TripleItemToItemRecipe> {

    private final IInputHandler<ItemStack> itemAInputHandler;
    private final IInputHandler<ItemStack> itemBInputHandler;
    private final IInputHandler<ItemStack> itemCInputHandler;
    private final IOutputHandler<ItemStack> outputHandler;

    @Nullable
    private ItemStack itemARecipeInput;
    @Nullable
    private ItemStack itemBRecipeInput;
    @Nullable
    private ItemStack itemCRecipeInput;
    @Nullable
    private ItemStack recipeOutput;

    public TripleItemToItemCachedRecipe(
            TripleItemToItemRecipe recipe,
            BooleanSupplier recheckAllErrors,
            IInputHandler<ItemStack> itemAInputHandler,
            IInputHandler<ItemStack> itemBInputHandler,
            IInputHandler<ItemStack> itemCInputHandler,
            IOutputHandler<ItemStack> outputHandler) {
        super(recipe, recheckAllErrors);
        this.itemAInputHandler = itemAInputHandler;
        this.itemBInputHandler = itemBInputHandler;
        this.itemCInputHandler = itemCInputHandler;
        this.outputHandler = outputHandler;
    }

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            itemARecipeInput = itemAInputHandler.getRecipeInput(recipe.getInputItemA());
            itemBRecipeInput = itemBInputHandler.getRecipeInput(recipe.getInputItemB());
            itemCRecipeInput = itemCInputHandler.getRecipeInput(recipe.getInputItemC());
            recipeOutput = recipe.getOutput(itemARecipeInput, itemBRecipeInput, itemARecipeInput);

            if (itemARecipeInput.isEmpty() || itemBRecipeInput.isEmpty() || itemCRecipeInput.isEmpty()
                    || recipeOutput.isEmpty()) {
                tracker.mismatchedRecipe();
                return;
            }

            itemAInputHandler.calculateOperationsCanSupport(tracker, itemARecipeInput);
            itemBInputHandler.calculateOperationsCanSupport(tracker, itemBRecipeInput);
            itemCInputHandler.calculateOperationsCanSupport(tracker, itemCRecipeInput);
            outputHandler.calculateOperationsCanSupport(tracker, recipeOutput);
        }
    }

    @Override
    protected void finishProcessing(int arg0) {
        if (itemARecipeInput == null || itemBRecipeInput == null || itemCRecipeInput == null || recipeOutput == null
                || recipeOutput.isEmpty()) {
            return;
        }
        itemAInputHandler.use(itemARecipeInput, arg0);
        itemBInputHandler.use(itemBRecipeInput, arg0);
        itemCInputHandler.use(itemCRecipeInput, arg0);
        outputHandler.handleOutput(recipeOutput, arg0);
    }

    @Override
    public boolean isInputValid() {
        return !itemAInputHandler.getInput().isEmpty()
                && !itemBInputHandler.getInput().isEmpty()
                && !itemCInputHandler.getInput().isEmpty()
                && recipe.test(itemAInputHandler.getInput(), itemBInputHandler.getInput(),
                        itemCInputHandler.getInput());
    }

}
