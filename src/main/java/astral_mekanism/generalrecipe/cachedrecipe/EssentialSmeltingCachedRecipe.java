package astral_mekanism.generalrecipe.cachedrecipe;

import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.recipes.output.ItemInfuseOutput;
import astral_mekanism.registries.AstralMekanismInfuseTypes;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;

public class EssentialSmeltingCachedRecipe extends GeneralCachedRecipe<SmeltingRecipe> {

    private final IInputHandler<ItemStack> inputHandler;
    private final IOutputHandler<ItemInfuseOutput> outputHandler;
    private final ItemStackIngredient inputIngredient;
    @Nullable
    private ItemStack recipeInput;
    @Nullable
    private ItemInfuseOutput recipeOutput;

    public EssentialSmeltingCachedRecipe(SmeltingRecipe recipe, BooleanSupplier recheckAllErrors,
            IInputHandler<ItemStack> inputHandler, IOutputHandler<ItemInfuseOutput> outputHandler) {
        super(recipe, recheckAllErrors);
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
        IItemStackIngredientCreator creator = IngredientCreatorAccess.item();
        this.inputIngredient = creator.createMulti(
                recipe.getIngredients().stream()
                        .map(creator::from)
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
                long xp = (long) (recipe.getExperience() * 100);
                recipeInput = inputHandler.getRecipeInput(inputIngredient);
                recipeOutput = new ItemInfuseOutput(recipe.getResultItem(null),
                        xp <= 0 ? InfusionStack.EMPTY : AstralMekanismInfuseTypes.XP.getStack(xp));
                if (recipeInput.isEmpty() || recipeOutput.itemStack().isEmpty()) {
                    tracker.mismatchedRecipe();
                    return;
                }
                inputHandler.calculateOperationsCanSupport(tracker, recipeInput);
                outputHandler.calculateOperationsCanSupport(tracker, recipeOutput);
            }

        }
    }

    @Override
    public boolean isInputValid() {
        return !inputHandler.getInput().isEmpty() && inputIngredient.test(inputHandler.getInput());
    }

    @Override
    protected void finishProcessing(int operations) {
        if (recipeInput != null && !recipeInput.isEmpty() && recipeOutput != null) {
            inputHandler.use(recipeInput, operations);
            outputHandler.handleOutput(recipeOutput, operations);
        }
    }

}
