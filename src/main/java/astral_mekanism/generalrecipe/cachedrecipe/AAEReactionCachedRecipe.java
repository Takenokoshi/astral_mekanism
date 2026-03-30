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

public class AAEReactionCachedRecipe extends GeneralCachedRecipe<ReactionChamberRecipe> {

    private final IInputHandler<ItemStack>[] itemInputHandlers;
    private final IInputHandler<FluidStack> fluidInputHandler;
    private final IOutputHandler<ItemFluidOutput> outputHandler;
    private final ItemStackIngredient[] itemStackIngredients;
    private final FluidStackIngredient fluidStackIngredient;

    private ItemStack[] itemInputs;
    private FluidStack fluidInput;
    private ItemFluidOutput recipeOutput;

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

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
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
            for (int i = 0; i < itemStackIngredients.length; i++) {
                itemInputs[i] = itemInputHandlers[i].getRecipeInput(itemStackIngredients[i]);
                if (itemInputs[i].isEmpty()) {
                    tracker.mismatchedRecipe();
                    return;
                }
                itemInputHandlers[i].calculateOperationsCanSupport(tracker, itemInputs[i]);
            }
            recipeOutput = new ItemFluidOutput(recipe.getResultItem(), recipe.getResultFluid());
            outputHandler.calculateOperationsCanSupport(tracker, recipeOutput);
        }
    }

    @Override
    protected void finishProcessing(int operations) {
        if (itemInputs == null || fluidInput == null || recipeOutput == null) {
            return;
        }
        for (int i = 0; i < itemInputs.length; i++) {
            itemInputHandlers[i].use(itemInputs[i], operations);
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
            for (int i = 0; i < itemInputHandlers.length; i++) {
                if (i < itemStackIngredients.length) {
                    result &= !itemInputHandlers[i].getRecipeInput(itemStackIngredients[i]).isEmpty();
                } else {
                    result &= itemInputHandlers[i].getInput().isEmpty();
                }
            }
        }
        return result;
    }

}
