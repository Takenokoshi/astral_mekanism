package astral_mekanism.generalrecipe.cachedrecipe;

import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.recipes.output.ItemInfuseOutput;
import astral_mekanism.registries.AMEInfuseTypes;
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
    private final IntSupplier xpUpgrade;
    @Nullable
    private ItemStack recipeInput;
    @Nullable
    private ItemInfuseOutput recipeOutput;

    public EssentialSmeltingCachedRecipe(SmeltingRecipe recipe, BooleanSupplier recheckAllErrors,
            IInputHandler<ItemStack> inputHandler, IOutputHandler<ItemInfuseOutput> outputHandler,
            IntSupplier xpUpgrade) {
        super(recipe, recheckAllErrors);
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
        IItemStackIngredientCreator creator = IngredientCreatorAccess.item();
        this.inputIngredient = creator.createMulti(
                recipe.getIngredients().stream()
                        .map(creator::from)
                        .toArray(ItemStackIngredient[]::new));
        this.xpUpgrade = xpUpgrade;
    }

    @Override
    public void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            ItemStack inputStack = inputHandler.getInput();
            if (inputStack.isEmpty()) {
                tracker.mismatchedRecipe();
            } else {
                long xp = (long) (recipe.getExperience() * 100 * (1l << xpUpgrade.getAsInt() * 2));
                recipeInput = inputHandler.getRecipeInput(inputIngredient);
                recipeOutput = new ItemInfuseOutput(recipe.getResultItem(null),
                        xp <= 0 ? InfusionStack.EMPTY : AMEInfuseTypes.XP.getStack(xp));
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
    public void finishProcessing(int operations) {
        if (recipeInput != null && !recipeInput.isEmpty() && recipeOutput != null) {
            inputHandler.use(recipeInput, operations);
            outputHandler.handleOutput(recipeOutput, operations);
        }
    }

    public static IOutputHandler<ItemInfuseOutput> merge(IOutputHandler<ItemStack> itemOutputHandler,
            IOutputHandler<InfusionStack> infusionOutputHandler) {
        return new IOutputHandler<ItemInfuseOutput>() {

            @Override
            public void handleOutput(ItemInfuseOutput toOutput, int operations) {
                itemOutputHandler.handleOutput(toOutput.itemStack(), operations);
                infusionOutputHandler.handleOutput(toOutput.infusionStack(), operations);
            }

            @Override
            public void calculateOperationsCanSupport(OperationTracker tracker, ItemInfuseOutput toOutput) {
                itemOutputHandler.calculateOperationsCanSupport(tracker, toOutput.itemStack());
                if (!toOutput.infusionStack().isEmpty()) {
                    infusionOutputHandler.calculateOperationsCanSupport(tracker, toOutput.infusionStack());
                }
            }

        };
    }

}
