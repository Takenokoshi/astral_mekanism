package astral_mekanism.recipes.cachedRecipe;

import java.util.Objects;
import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.recipes.recipe.ReconstructionRecipe;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.PressurizedReactionRecipe.PressurizedReactionRecipeOutput;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ReconstructionCachedRecipe extends CachedRecipe<ReconstructionRecipe> {

    private final IOutputHandler<@NotNull PressurizedReactionRecipeOutput> outputHandler;
    private final IInputHandler<@NotNull ItemStack> itemInputHandler;
    private final IInputHandler<@NotNull FluidStack> fluidInputHandler;
    private final IInputHandler<@NotNull GasStack> gasInputHandler;

    private ItemStack recipeItem = ItemStack.EMPTY;
    private FluidStack recipeFluid = FluidStack.EMPTY;
    private GasStack recipeGas = GasStack.EMPTY;
    @Nullable
    private PressurizedReactionRecipeOutput output;

    public ReconstructionCachedRecipe(ReconstructionRecipe recipe, BooleanSupplier recheckAllErrors,
            IInputHandler<@NotNull ItemStack> itemInputHandler,
            IInputHandler<@NotNull FluidStack> fluidInputHandler,
            IInputHandler<@NotNull GasStack> gasInputHandler,
            IOutputHandler<@NotNull PressurizedReactionRecipeOutput> outputHandler) {
        super(recipe, recheckAllErrors);
        this.itemInputHandler = Objects.requireNonNull(itemInputHandler, "Item input handler cannot be null.");
        this.fluidInputHandler = Objects.requireNonNull(fluidInputHandler, "Fluid input handler cannot be null.");
        this.gasInputHandler = Objects.requireNonNull(gasInputHandler, "Gas input handler cannot be null.");
        this.outputHandler = Objects.requireNonNull(outputHandler, "Output handler cannot be null.");
    }

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            recipeItem = itemInputHandler.getRecipeInput(recipe.getInputSolid());
            if (recipeItem.isEmpty()) {
                tracker.mismatchedRecipe();
            } else {
                recipeFluid = fluidInputHandler.getRecipeInput(recipe.getInputFluid());
                if (recipeFluid.isEmpty()) {
                    tracker.mismatchedRecipe();
                } else {
                    recipeGas = gasInputHandler.getRecipeInput(recipe.getInputGas());
                    if (recipeGas.isEmpty()) {
                        tracker.mismatchedRecipe();
                    } else {
                        if (!recipe.getItemNotConsumed()) {
                            itemInputHandler.calculateOperationsCanSupport(tracker, recipeItem);
                        }
                        if (tracker.shouldContinueChecking()) {
                            fluidInputHandler.calculateOperationsCanSupport(tracker, recipeFluid);
                            if (tracker.shouldContinueChecking()) {
                                gasInputHandler.calculateOperationsCanSupport(tracker, recipeGas);
                                if (tracker.shouldContinueChecking()) {
                                    output = recipe.getOutput(recipeItem, recipeFluid, recipeGas);
                                    outputHandler.calculateOperationsCanSupport(tracker, output);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isInputValid() {
        ItemStack item = itemInputHandler.getInput();
        if (item.isEmpty()) {
            return false;
        }
        GasStack gas = gasInputHandler.getInput();
        if (gas.isEmpty()) {
            return false;
        }
        FluidStack fluid = fluidInputHandler.getInput();
        return !fluid.isEmpty() && recipe.test(item, fluid, gas);
    }

    @Override
    protected void finishProcessing(int operations) {
        if (output != null && !recipeItem.isEmpty() && !recipeFluid.isEmpty() && !recipeGas.isEmpty()) {
            if (!recipe.getItemNotConsumed()) {
                itemInputHandler.use(recipeItem, operations);
            }
            fluidInputHandler.use(recipeFluid, operations);
            gasInputHandler.use(recipeGas, operations);
            outputHandler.handleOutput(output, operations);
        }
    }

}
