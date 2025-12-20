package astral_mekanism.recipes.cachedRecipe;

import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.recipes.output.ItemFluidOutput;
import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;

public class MekanicalTransformCachedRecipe extends CachedRecipe<MekanicalTransformRecipe> {

    private final IInputHandler<ItemStack> itemAInputHandler;
    private final IInputHandler<ItemStack> itemBInputHandler;
    private final IInputHandler<ItemStack> itemCInputHandler;
    private final IInputHandler<ItemStack> itemDInputHandler;
    private final IOutputHandler<ItemFluidOutput> outputHandler;

    @Nullable
    private ItemStack itemARecipeInput;
    @Nullable
    private ItemStack itemBRecipeInput;
    @Nullable
    private ItemStack itemCRecipeInput;
    @Nullable
    private ItemStack itemDRecipeInput;
    @Nullable
    private ItemFluidOutput recipeOutput;

    public MekanicalTransformCachedRecipe(MekanicalTransformRecipe recipe,
            BooleanSupplier recheckAllErrors,
            IInputHandler<ItemStack> itemAInputHandler,
            IInputHandler<ItemStack> itemBInputHandler,
            IInputHandler<ItemStack> itemCInputHandler,
            IInputHandler<ItemStack> itemDInputHandler,
            IOutputHandler<ItemFluidOutput> outputHandler) {
        super(recipe, recheckAllErrors);
        this.itemAInputHandler = itemAInputHandler;
        this.itemBInputHandler = itemBInputHandler;
        this.itemCInputHandler = itemCInputHandler;
        this.itemDInputHandler = itemDInputHandler;
        this.outputHandler = outputHandler;
    }

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            itemARecipeInput = itemAInputHandler.getRecipeInput(recipe.getInputItemA());
            itemBRecipeInput = itemBInputHandler.getRecipeInput(recipe.getInputItemB());
            itemCRecipeInput = itemCInputHandler.getRecipeInput(recipe.getInputItemC());
            itemDRecipeInput = itemDInputHandler.getRecipeInput(recipe.getInputItemD());
            recipeOutput = recipe.getOutput();

            if (itemARecipeInput.isEmpty()
                    || itemBRecipeInput.isEmpty()
                    || itemCRecipeInput.isEmpty()
                    || itemDRecipeInput.isEmpty()
                    || recipeOutput.isEmpty()) {
                tracker.mismatchedRecipe();
                return;
            }
            if (!recipe.isACatalyst()) {
                itemAInputHandler.calculateOperationsCanSupport(tracker, itemARecipeInput);
            }
            if (!recipe.isBCatalyst()) {
                itemBInputHandler.calculateOperationsCanSupport(tracker, itemBRecipeInput);
            }
            if (!recipe.isCCatalyst()) {
                itemCInputHandler.calculateOperationsCanSupport(tracker, itemCRecipeInput);
            }
            if (!recipe.isDCatalyst()) {
                itemDInputHandler.calculateOperationsCanSupport(tracker, itemDRecipeInput);
            }
            outputHandler.calculateOperationsCanSupport(tracker, recipeOutput);
        }
    }

    @Override
    protected void finishProcessing(int arg0) {
        if (itemARecipeInput == null || itemBRecipeInput == null || itemCRecipeInput == null || itemDRecipeInput == null
                || recipeOutput == null || recipeOutput.isEmpty()) {
            return;
        }
        if (!recipe.isACatalyst()) {
            itemAInputHandler.use(itemARecipeInput, arg0);
        }
        if (!recipe.isBCatalyst()) {
            itemBInputHandler.use(itemBRecipeInput, arg0);
        }
        if (!recipe.isCCatalyst()) {
            itemCInputHandler.use(itemCRecipeInput, arg0);
        }
        if (!recipe.isDCatalyst()) {
            itemDInputHandler.use(itemDRecipeInput, arg0);
        }

    }

    @Override
    public boolean isInputValid() {
        return !itemAInputHandler.getInput().isEmpty()
                && !itemBInputHandler.getInput().isEmpty()
                && !itemCInputHandler.getInput().isEmpty()
                && !itemDInputHandler.getInput().isEmpty()
                && recipe.test(itemAInputHandler.getInput(), itemBInputHandler.getInput(),
                        itemCInputHandler.getInput(), itemDInputHandler.getInput());
    }

}
