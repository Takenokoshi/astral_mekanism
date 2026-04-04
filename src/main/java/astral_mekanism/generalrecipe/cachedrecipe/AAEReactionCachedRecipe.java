package astral_mekanism.generalrecipe.cachedrecipe;

import java.util.function.BooleanSupplier;

import astral_mekanism.recipes.output.ItemFluidOutput;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.pedroksl.advanced_ae.recipes.ReactionChamberRecipe;

// NOTE:
// This matching uses a greedy algorithm assuming:
// - No duplicate ingredients
// - No overlapping ingredient conditions
// If these assumptions break, matching may fail.

public class AAEReactionCachedRecipe extends GeneralCachedRecipe<ReactionChamberRecipe> {

    private final IInputHandler<ItemStack>[] itemInputHandlers;
    private final IInputHandler<FluidStack> fluidInputHandler;
    private final IOutputHandler<ItemFluidOutput> outputHandler;
    private final ItemStackIngredient[] itemStackIngredients;
    private final FluidStackIngredient fluidStackIngredient;

    private ItemStack[] itemInputs;
    private FluidStack fluidInput = FluidStack.EMPTY;
    private ItemFluidOutput recipeOutput = ItemFluidOutput.EMPTY;
    private IInputHandler<ItemStack>[] arrangedHandlers;

    public AAEReactionCachedRecipe(ReactionChamberRecipe recipe, BooleanSupplier recheckAllErrors,
            IInputHandler<ItemStack>[] itemInputHandlers, IInputHandler<FluidStack> fluidInputHandler,
            IOutputHandler<ItemFluidOutput> outputHandler) {
        super(recipe, recheckAllErrors);
        this.itemInputHandlers = itemInputHandlers;
        this.fluidInputHandler = fluidInputHandler;
        this.outputHandler = outputHandler;
        this.itemStackIngredients = new ItemStackIngredient[recipe.inputs.size()];
        for (int i = 0; i < itemStackIngredients.length; i++) {
            itemStackIngredients[i] = IngredientCreatorAccess.item().from(recipe.inputs.get(i).getIngredient(),
                    recipe.inputs.get(i).getAmount());
        }
        FluidStack inputFluid = recipe.fluid.getStack();
        this.fluidStackIngredient = inputFluid.isEmpty() ? null : IngredientCreatorAccess.fluid().from(inputFluid);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            itemInputs = null;
            arrangedHandlers = null;
            recipeOutput = ItemFluidOutput.EMPTY;
            fluidInput = FluidStack.EMPTY;
            if (itemInputHandlers.length < itemStackIngredients.length) {
                tracker.mismatchedRecipe();
                return;
            }
            if (fluidStackIngredient == null) {
                fluidInput = FluidStack.EMPTY;
            } else {
                fluidInput = fluidInputHandler.getRecipeInput(fluidStackIngredient);
                if (fluidInput.isEmpty()) {
                    tracker.mismatchedRecipe();
                    return;
                }
                fluidInputHandler.calculateOperationsCanSupport(tracker, fluidInput);
            }
            itemInputs = new ItemStack[itemStackIngredients.length];
            boolean[] used = new boolean[itemInputHandlers.length];
            arrangedHandlers = new IInputHandler[itemStackIngredients.length];
            for (int ingIndex = 0; ingIndex < itemStackIngredients.length; ingIndex++) {
                boolean found = false;
                for (int hanIndex = 0; hanIndex < itemInputHandlers.length; hanIndex++) {
                    if (used[hanIndex]) {
                        continue;
                    }
                    ItemStack input = itemInputHandlers[hanIndex].getRecipeInput(itemStackIngredients[ingIndex]);
                    if (input.isEmpty()) {
                        continue;
                    }
                    found = true;
                    used[hanIndex] = true;
                    arrangedHandlers[ingIndex] = itemInputHandlers[hanIndex];
                    itemInputs[ingIndex] = input;
                    arrangedHandlers[ingIndex].calculateOperationsCanSupport(tracker, itemInputs[ingIndex]);
                    break;
                }
                if (!found) {
                    tracker.mismatchedRecipe();
                    return;
                }
            }
            recipeOutput = new ItemFluidOutput(recipe.getResultItem(), recipe.getResultFluid());
            outputHandler.calculateOperationsCanSupport(tracker, recipeOutput);
        }
    }

    @Override
    protected void finishProcessing(int operations) {
        if (itemInputs == null || recipeOutput == ItemFluidOutput.EMPTY || arrangedHandlers == null) {
            return;
        }
        for (int i = 0; i < itemInputs.length; i++) {
            arrangedHandlers[i].use(itemInputs[i], operations);
        }
        if (!fluidInput.isEmpty()) {
            fluidInputHandler.use(fluidInput, operations);
        }
        outputHandler.handleOutput(recipeOutput, operations);
    }

    @Override
    public boolean isInputValid() {
        boolean result = fluidStackIngredient == null
                ? fluidInputHandler.getInput().isEmpty()
                : !fluidInputHandler.getRecipeInput(fluidStackIngredient).isEmpty();
        result &= itemInputHandlers.length >= itemStackIngredients.length;
        if (result) {
            boolean[] used = new boolean[itemInputHandlers.length];
            for (int i = 0; i < itemStackIngredients.length; i++) {
                boolean found = false;
                for (int j = 0; j < itemInputHandlers.length; j++) {
                    if (used[j]) {
                        continue;
                    }
                    if (itemStackIngredients[i].test(itemInputHandlers[j].getInput())) {
                        found = true;
                        used[j] = true;
                        break;
                    }
                }
                result &= found;
            }
        }
        return result;
    }

}
