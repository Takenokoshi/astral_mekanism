package astral_mekanism.recipes.cachedRecipe;

import java.util.function.BooleanSupplier;

import org.checkerframework.checker.nullness.qual.Nullable;

import astral_mekanism.recipes.output.ItemFluidOutput;
import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class MekanicalTransformCachedRecipe extends CachedRecipe<MekanicalTransformRecipe> {

    private final IInputHandler<ItemStack> inputHandlerIA;
    private final IInputHandler<ItemStack> inputHandlerIB;
    private final IInputHandler<ItemStack> inputHandlerIC;
    private final IInputHandler<FluidStack> inputHandlerFA;
    private final IInputHandler<FluidStack> inputHandlerFB;
    private final IOutputHandler<ItemFluidOutput> outputHandler;
    @Nullable
    private ItemStack recipeInputIA;
    @Nullable
    private ItemStack recipeInputIB;
    @Nullable
    private ItemStack recipeInputIC;
    @Nullable
    private FluidStack recipeInputFA;
    @Nullable
    private FluidStack recipeInputFB;
    @Nullable
    private ItemFluidOutput recipeOutput;

    public MekanicalTransformCachedRecipe(MekanicalTransformRecipe recipe, BooleanSupplier recheckAllErrors,
            IInputHandler<ItemStack> inputHandlerIA, IInputHandler<ItemStack> inputHandlerIB,
            IInputHandler<ItemStack> inputHandlerIC,
            IInputHandler<FluidStack> inputHandlerFA, IInputHandler<FluidStack> inputHandlerFB,
            IOutputHandler<ItemFluidOutput> outputHandler) {
        super(recipe, recheckAllErrors);
        this.inputHandlerIA = inputHandlerIA;
        this.inputHandlerIB = inputHandlerIB;
        this.inputHandlerIC = inputHandlerIC;
        this.inputHandlerFA = inputHandlerFA;
        this.inputHandlerFB = inputHandlerFB;
        this.outputHandler = outputHandler;
    }

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (!tracker.shouldContinueChecking()) {
            return;
        }
        recipeInputIA = inputHandlerIA.getRecipeInput(recipe.getInputItemA());
        recipeInputIB = inputHandlerIB.getRecipeInput(recipe.getInputItemB());
        recipeInputIC = inputHandlerIC.getRecipeInput(recipe.getInputItemC());
        recipeInputFA = inputHandlerFA.getRecipeInput(recipe.getInputFluidA());
        recipeInputFB = inputHandlerFB.getRecipeInput(recipe.getInputFluidB());
        recipeOutput = recipe.getOutput();
        if (recipeInputIA.isEmpty() || recipeInputIB.isEmpty() || recipeInputIC.isEmpty()
                || recipeInputFA.isEmpty() || recipeInputFB.isEmpty() || recipeOutput.isEmpty()) {
            tracker.mismatchedRecipe();
            return;
        }
        if (!recipe.isItemACatalyst()) {
            inputHandlerIA.calculateOperationsCanSupport(tracker, recipeInputIA);
        }
        if (!recipe.isItemBCatalyst()) {
            inputHandlerIB.calculateOperationsCanSupport(tracker, recipeInputIB);
        }
        if (!recipe.isItemCCatalyst()) {
            inputHandlerIC.calculateOperationsCanSupport(tracker, recipeInputIC);
        }
        if (!recipe.isFluidACatalyst()) {
            inputHandlerFA.calculateOperationsCanSupport(tracker, recipeInputFA);
        }
        if (!recipe.isFluidBCatalyst()) {
            inputHandlerFB.calculateOperationsCanSupport(tracker, recipeInputFB);
        }
        outputHandler.calculateOperationsCanSupport(tracker, recipeOutput);
    }

    @Override
    protected void finishProcessing(int arg0) {
        if (recipeInputIA == null || recipeInputIB == null || recipeInputIC == null
                || recipeInputFA == null || recipeInputFB == null || recipeOutput.isEmpty()) {
            return;
        }
        if (!recipe.isItemACatalyst()) {
            inputHandlerIA.use(recipeInputIA, arg0);
        }
        if (!recipe.isItemBCatalyst()) {
            inputHandlerIB.use(recipeInputIB, arg0);
        }
        if (!recipe.isItemCCatalyst()) {
            inputHandlerIC.use(recipeInputIC, arg0);
        }
        if (!recipe.isFluidACatalyst()) {
            inputHandlerFA.use(recipeInputFA, arg0);
        }
        if (!recipe.isFluidBCatalyst()) {
            inputHandlerFB.use(recipeInputFB, arg0);
        }
        outputHandler.handleOutput(recipeOutput, arg0);
    }

    @Override
    public boolean isInputValid() {
        return !inputHandlerIA.getInput().isEmpty()
                && !inputHandlerIB.getInput().isEmpty()
                && !inputHandlerIC.getInput().isEmpty()
                && !inputHandlerFA.getInput().isEmpty()
                && !inputHandlerFB.getInput().isEmpty()
                && recipe.test(inputHandlerIA.getInput(), inputHandlerIB.getInput(), inputHandlerIC.getInput(),
                        inputHandlerFA.getInput(), inputHandlerFB.getInput());
    }

}
