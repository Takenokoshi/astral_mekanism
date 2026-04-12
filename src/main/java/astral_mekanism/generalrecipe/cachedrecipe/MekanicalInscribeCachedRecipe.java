package astral_mekanism.generalrecipe.cachedrecipe;

import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.Nullable;

import appeng.recipes.handlers.InscriberProcessType;
import appeng.recipes.handlers.InscriberRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class MekanicalInscribeCachedRecipe extends GeneralCachedRecipe<InscriberRecipe> {

    private final IInputHandler<ItemStack> topInputHandler;
    private final IInputHandler<ItemStack> middleInputHandler;
    private final IInputHandler<ItemStack> bottomInputHandler;
    private final IOutputHandler<ItemStack> outputHandler;
    private final Ingredient topIngredient;
    private final Ingredient middleIngredient;
    private final Ingredient bottomIngredient;
    private final ItemStackIngredient topStackIngredient;
    private final ItemStackIngredient middleStackIngredient;
    private final ItemStackIngredient bottomStackIngredient;
    @Nullable
    private ItemStack topRecipeInput;
    @Nullable
    private ItemStack middleRecipeInput;
    @Nullable
    private ItemStack bottomRecipeInput;
    @Nullable
    private ItemStack recipeOutput;

    public MekanicalInscribeCachedRecipe(InscriberRecipe recipe, BooleanSupplier recheckAllErrors,
            IInputHandler<ItemStack> topInputHandler,
            IInputHandler<ItemStack> middleInputHandler,
            IInputHandler<ItemStack> bottomInputHandler,
            IOutputHandler<ItemStack> outputHandler) {
        super(recipe, recheckAllErrors);
        this.topInputHandler = topInputHandler;
        this.middleInputHandler = middleInputHandler;
        this.bottomInputHandler = bottomInputHandler;
        this.outputHandler = outputHandler;
        IItemStackIngredientCreator creator = IngredientCreatorAccess.item();
        this.topIngredient = recipe.getTopOptional();
        this.middleIngredient = recipe.getMiddleInput();
        this.bottomIngredient = recipe.getBottomOptional();
        this.topStackIngredient = topIngredient.isEmpty() ? null : creator.from(topIngredient);
        this.middleStackIngredient = middleIngredient.isEmpty() ? null : creator.from(middleIngredient);
        this.bottomStackIngredient = bottomIngredient.isEmpty() ? null : creator.from(bottomIngredient);
    }

    @Override
    public void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            topRecipeInput = topIngredient.isEmpty() ? ItemStack.EMPTY
                    : topInputHandler.getRecipeInput(topStackIngredient);
            middleRecipeInput = middleIngredient.isEmpty() ? ItemStack.EMPTY
                    : middleInputHandler.getRecipeInput(middleStackIngredient);
            bottomRecipeInput = bottomIngredient.isEmpty() ? ItemStack.EMPTY
                    : bottomInputHandler.getRecipeInput(bottomStackIngredient);
            recipeOutput = recipe.getResultItem();
            if ((topRecipeInput.isEmpty() != topIngredient.isEmpty())
                    || (middleRecipeInput.isEmpty() != middleIngredient.isEmpty())
                    || (bottomRecipeInput.isEmpty() != bottomIngredient.isEmpty())
                    || recipeOutput.isEmpty()) {
                tracker.mismatchedRecipe();
                return;
            }
            middleInputHandler.calculateOperationsCanSupport(tracker, middleRecipeInput);
            outputHandler.calculateOperationsCanSupport(tracker, recipeOutput);
            if (!topRecipeInput.isEmpty() && recipe.getProcessType() == InscriberProcessType.PRESS) {
                topInputHandler.calculateOperationsCanSupport(tracker, topRecipeInput);
            }
            if (!bottomRecipeInput.isEmpty() && recipe.getProcessType() == InscriberProcessType.PRESS) {
                bottomInputHandler.calculateOperationsCanSupport(tracker, bottomRecipeInput);
            }
        }
    }

    @Override
    protected void finishProcessing(int operations) {
        middleInputHandler.use(middleRecipeInput, operations);
        outputHandler.handleOutput(recipeOutput, operations);
        if (!topRecipeInput.isEmpty() && recipe.getProcessType() == InscriberProcessType.PRESS) {
            topInputHandler.use(topRecipeInput, operations);
        }
        if (!bottomRecipeInput.isEmpty() && recipe.getProcessType() == InscriberProcessType.PRESS) {
            bottomInputHandler.use(bottomRecipeInput, operations);
        }
    }

    @Override
    public boolean isInputValid() {
        boolean result = middleStackIngredient.test(middleInputHandler.getInput());
        result &= (topIngredient.isEmpty()
                ? topInputHandler.getInput().isEmpty()
                : topStackIngredient.test(topInputHandler.getInput()));
        result &= (bottomIngredient.isEmpty()
                ? bottomInputHandler.getInput().isEmpty()
                : bottomStackIngredient.test(bottomInputHandler.getInput()));
        return result;
    }

}
