package astral_mekanism.recipes.cachedRecipe;

import java.util.Arrays;
import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.recipes.recipe.AstralCraftingRecipe;
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
        if (!tracker.shouldContinueChecking()) {
            return;
        }
        ItemStack[] itemStacks = new ItemStack[25];
        for (int i = 0; i < 25; i++) {
            itemStacks[i] = itemInputHandlers[i].getInput();
            if (itemStacks[i].isEmpty()) {
                tracker.mismatchedRecipe();
                return;
            }
        }

        FluidStack fluidStack = fluidInputHandler.getInput();
        if (fluidStack.isEmpty()) {
            tracker.mismatchedRecipe();
            return;
        }

        GasStack gasStack = gasInputHandler.getInput();
        if (gasStack.isEmpty()) {
            tracker.mismatchedRecipe();
            return;
        }
        ItemStackIngredient[] inputItems = recipe.getInputItems();
        FluidStackIngredient inputFluid = recipe.getInputFluid();
        GasStackIngredient inputGas = recipe.getInputGas();

        itemsRecipeInput = null;
        fluidRecipeInput = null;
        gasRecipeInput = null;
        recipeOutput = null;

        AMCachedRecipeHelper.astralCraftingInputCalculateOperationsThisTick(
                tracker,
                itemInputHandlers,
                i -> inputItems[i % 25],
                fluidInputHandler,
                () -> inputFluid,
                gasInputHandler,
                () -> inputGas,
                (a, f, g) -> {
                    itemsRecipeInput = a;
                    fluidRecipeInput = f;
                    gasRecipeInput = g;
                },
                outputHandler,
                recipe::getOutput,
                o -> recipeOutput = o,
                (a, b) -> inputItems[b].test(a),
                inputFluid, inputGas
        );
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
                        gasInputHandler.getInput()
                );
    }
}
