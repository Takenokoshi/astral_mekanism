package astral_mekanism.recipes.cachedRecipe;

import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.recipes.ingredient.ArrayItemStackIngredient;
import astral_mekanism.recipes.recipe.ExpandedCrafterRecipe;
import astral_mekanism.util.ArrayItemStackUtils;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ExpandedCrafterCachedRecipe extends CachedRecipe<ExpandedCrafterRecipe> {

    private final IInputHandler<ItemStack[]> itemsInputHandler;
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

    public ExpandedCrafterCachedRecipe(
            ExpandedCrafterRecipe recipe,
            BooleanSupplier recheckAllErrors,
            IInputHandler<ItemStack[]> itemsInputHandler,
            IInputHandler<FluidStack> fluidInputHandler,
            IInputHandler<GasStack> gasInputHandler,
            IOutputHandler<ItemStack> outputHandler) {
        super(recipe, recheckAllErrors);
        this.itemsInputHandler = itemsInputHandler;
        this.fluidInputHandler = fluidInputHandler;
        this.gasInputHandler = gasInputHandler;
        this.outputHandler = outputHandler;
    }

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            ItemStack[] itemStacks = itemsInputHandler.getInput();
            FluidStack fluidStack = fluidInputHandler.getInput();
            GasStack gasStack = gasInputHandler.getInput();
            if (ArrayItemStackUtils.includeNullOrEmpty(itemStacks) || fluidStack.isEmpty() || gasStack.isEmpty()) {
                tracker.mismatchedRecipe();
            } else {
                ArrayItemStackIngredient inputItems = recipe.getInputItems();
                FluidStackIngredient inputFluid = recipe.getInputFluid();
                GasStackIngredient inputGas = recipe.getInputGas();
                AMCachedRecipeHelper.threeInputCalculateOperationsThisTick(tracker,
                        itemsInputHandler, () -> inputItems,
                        fluidInputHandler, () -> inputFluid,
                        gasInputHandler, () -> inputGas,
                        (I, F, G) -> {
                            itemsRecipeInput = I;
                            fluidRecipeInput = F;
                            gasRecipeInput = G;
                        }, outputHandler, recipe::getOutput, o -> recipeOutput = o,
                        ArrayItemStackUtils::includeNullOrEmpty, FluidStack::isEmpty, GasStack::isEmpty);
            }
        }
    }

    @Override
    protected void finishProcessing(int arg0) {
        if (fluidRecipeInput != null && !fluidRecipeInput.isEmpty() && gasRecipeInput != null
                && !gasRecipeInput.isEmpty() && recipeOutput != null && !recipeOutput.isEmpty()
                && !ArrayItemStackUtils.includeNullOrEmpty(itemsRecipeInput)) {
            itemsInputHandler.use(itemsRecipeInput, arg0);
            fluidInputHandler.use(fluidRecipeInput, arg0);
            gasInputHandler.use(gasRecipeInput, arg0);
            outputHandler.handleOutput(recipeOutput, arg0);
        }
    }

    @Override
    public boolean isInputValid() {
        ItemStack[] itemStacks = itemsInputHandler.getInput();
        FluidStack fluidStack = fluidInputHandler.getInput();
        GasStack gasStack = gasInputHandler.getInput();
        return !ArrayItemStackUtils.includeNullOrEmpty(itemStacks)
                && !fluidStack.isEmpty()
                && !gasStack.isEmpty()
                && recipe.test(itemStacks, fluidStack, gasStack);
    }

}
