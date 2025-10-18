package astral_mekanism.recipes.cachedRecipe;

import java.util.Objects;
import java.util.function.BooleanSupplier;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.recipes.output.DoubleItemStackOutput;
import astral_mekanism.recipes.recipe.GreenHouseRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GreenHouseCachedRecipe extends CachedRecipe<GreenHouseRecipe> {

    private final IOutputHandler<DoubleItemStackOutput> outputHandler;
    private final IInputHandler<@NotNull ItemStack> itemInputHandler;
    private final IInputHandler<@NotNull FluidStack> fluidInputHandler;
    private ItemStack recipeItem;
    private FluidStack recipeFluid;
    @Nullable
    private DoubleItemStackOutput output;

    public GreenHouseCachedRecipe(GreenHouseRecipe recipe, BooleanSupplier recheckAllErrors,
            IInputHandler<@NotNull ItemStack> itemInputHandler, IInputHandler<@NotNull FluidStack> fluidInputHandler,
            IOutputHandler<DoubleItemStackOutput> outputHandler) {
        super(recipe, recheckAllErrors);
        this.recipeItem = ItemStack.EMPTY;
        this.recipeFluid = FluidStack.EMPTY;
        this.itemInputHandler = Objects.requireNonNull(itemInputHandler);
        this.fluidInputHandler = Objects.requireNonNull(fluidInputHandler);
        this.outputHandler = Objects.requireNonNull(outputHandler);
    }

    protected void calculateOperationsThisTick(CachedRecipe.OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            this.recipeItem = this.itemInputHandler.getRecipeInput(this.recipe.getInputItem());
            this.recipeFluid = this.fluidInputHandler.getRecipeInput(this.recipe.getInputFluid());
            if (recipeItem.isEmpty() || recipeFluid.isEmpty()) {
                tracker.mismatchedRecipe();
            } else {
                this.itemInputHandler.calculateOperationsCanSupport(tracker, recipeItem);
                this.fluidInputHandler.calculateOperationsCanSupport(tracker, recipeFluid);
                if (tracker.shouldContinueChecking()) {
                    this.output = this.recipe.getOutput(recipeItem, recipeFluid);
                    this.outputHandler.calculateOperationsCanSupport(tracker, output);
                }
            }
        }
    }

    @Override
    protected void finishProcessing(int operations) {
        if (this.output != null && !this.recipeItem.isEmpty() && !this.recipeFluid.isEmpty()) {
            this.itemInputHandler.use(recipeItem, operations);
            this.fluidInputHandler.use(recipeFluid, operations);
            this.outputHandler.handleOutput(output, operations);
        }
    }

    @Override
    public boolean isInputValid() {
        ItemStack item = (ItemStack) this.itemInputHandler.getInput();
        FluidStack fluid = (FluidStack) this.fluidInputHandler.getInput();
        return !item.isEmpty() && !fluid.isEmpty() && ((GreenHouseRecipe) this.recipe).test(item, fluid);
    }

}
