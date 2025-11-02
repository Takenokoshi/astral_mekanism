package astral_mekanism.recipes.cachedRecipe;

import java.util.Arrays;
import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.recipes.recipe.AstralCraftingRecipe;
import astral_mekanism.util.ArrayItemStackUtils;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
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
        if (tracker.shouldContinueChecking()) {
            ItemStack[] itemStacks = Arrays.stream(itemInputHandlers).map(h -> h.getInput()).toArray(ItemStack[]::new);
            FluidStack fluidStack = fluidInputHandler.getInput();
            GasStack gasStack = gasInputHandler.getInput();
            if (ArrayItemStackUtils.includeNullOrEmpty(itemStacks) || fluidStack.isEmpty() || gasStack.isEmpty()) {
                tracker.mismatchedRecipe();
            } else {
                ItemStackIngredient[] inputItems = recipe.getInputItems();
                FluidStackIngredient inputFluid = recipe.getInputFluid();
                GasStackIngredient inputGas = recipe.getInputGas();
                AMCachedRecipeHelper.astralCraftingInputCalculateOperationsThisTick(tracker,
                        itemInputHandlers, i -> inputItems[i % 25],
                        fluidInputHandler, () -> inputFluid,
                        gasInputHandler, () -> inputGas,
                        (a, f, g) -> {
                            itemsRecipeInput = a;
                            fluidRecipeInput = f;
                            gasRecipeInput = g;
                        }, outputHandler, recipe::getOutput, o -> recipeOutput = o,
                        (a, b) -> inputItems[b].test(a), inputFluid, inputGas);
            }
        }
    }

    @Override
    protected void finishProcessing(int arg0) {
        if (fluidRecipeInput != null && !fluidRecipeInput.isEmpty() && gasRecipeInput != null
                && !gasRecipeInput.isEmpty() && recipeOutput != null && !recipeOutput.isEmpty()
                && !ArrayItemStackUtils.includeNullOrEmpty(itemsRecipeInput)) {
            for (int i = 0; i < 25; i++) {
                itemInputHandlers[i].use(itemsRecipeInput[i], arg0);
            }
            fluidInputHandler.use(fluidRecipeInput, arg0);
            gasInputHandler.use(gasRecipeInput, arg0);
            outputHandler.handleOutput(recipeOutput, arg0);
        }
    }

    @Override
    public boolean isInputValid() {
        ItemStack[] itemStacks = Arrays.stream(itemInputHandlers).map(h -> h.getInput()).toArray(ItemStack[]::new);
        FluidStack fluidStack = fluidInputHandler.getInput();
        GasStack gasStack = gasInputHandler.getInput();
        return !ArrayItemStackUtils.includeNullOrEmpty(itemStacks)
                && !fluidStack.isEmpty()
                && !gasStack.isEmpty()
                && recipe.test(itemStacks, fluidStack, gasStack);
    }

}
