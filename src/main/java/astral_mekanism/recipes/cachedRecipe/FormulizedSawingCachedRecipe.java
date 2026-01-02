package astral_mekanism.recipes.cachedRecipe;

import java.util.List;
import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.recipes.output.DoubleItemOutput;
import mekanism.api.recipes.SawmillRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipeHelper;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;

public class FormulizedSawingCachedRecipe extends CachedRecipe<SawmillRecipe> {

    private final IInputHandler<ItemStack> inputHandler;
    private final IOutputHandler<DoubleItemOutput> outputHandler;
    private final int multiply;
    private final ItemStackIngredient inputIngredient;
    @Nullable
    private ItemStack recipeInput;
    @Nullable
    private DoubleItemOutput recipeOutput;

    public FormulizedSawingCachedRecipe(SawmillRecipe recipe, BooleanSupplier recheckAllErrors,
            IInputHandler<ItemStack> inputHandler, IOutputHandler<DoubleItemOutput> outputHandler) {
        super(recipe, recheckAllErrors);
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
        double chance = this.recipe.getSecondaryChance();
        this.multiply = chance > 0 ? (int) Math.ceil(1 / this.recipe.getSecondaryChance()) : 1;
        this.inputIngredient = IngredientCreatorAccess.item()
                .createMulti(this.recipe.getInput().getRepresentations().stream()
                        .map(stack -> IngredientCreatorAccess.item()
                                .from(stack.copyWithCount(stack.getCount() * multiply)))
                        .toArray(ItemStackIngredient[]::new));
    }

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            ItemStack inputStack = inputHandler.getInput();
            if (inputStack.isEmpty()) {
                tracker.mismatchedRecipe();
            } else {
                List<ItemStack> extraOutputDef = recipe.getSecondaryOutputDefinition();
                CachedRecipeHelper.oneInputCalculateOperationsThisTick(tracker,
                        inputHandler, () -> inputIngredient, i -> recipeInput = i,
                        outputHandler, stack -> {
                            ItemStack main = recipe.getOutput(stack).getMainOutput();
                            return new DoubleItemOutput(main.copyWithCount(main.getCount() * multiply),
                                    extraOutputDef.isEmpty() ? ItemStack.EMPTY : extraOutputDef.get(0));
                        }, o -> recipeOutput = o, ItemStack::isEmpty);
            }
        }
    }

    @Override
    public boolean isInputValid() {
        return !inputHandler.getInput().isEmpty() && recipe.test(inputHandler.getInput());
    }

    @Override
    protected void finishProcessing(int arg0) {
        if (recipeInput != null && !recipeInput.isEmpty() && recipeOutput != null) {
            inputHandler.use(recipeInput, arg0);
            outputHandler.handleOutput(recipeOutput, arg0);
        }
    }

}
