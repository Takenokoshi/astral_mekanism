package astral_mekanism.generalrecipe.cachedrecipe;

import java.util.function.BooleanSupplier;
import appeng.blockentity.qnb.QuantumBridgeBlockEntity;
import appeng.core.definitions.AEItems;
import appeng.recipes.transform.TransformRecipe;
import astral_mekanism.generalrecipe.AMERecipeConstants;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class TransformCachedRecipe extends GeneralCachedRecipe<TransformRecipe> {

    private final IInputHandler<ItemStack> inputHandlerIA;
    private final IInputHandler<ItemStack> inputHandlerIB;
    private final IInputHandler<ItemStack> inputHandlerIC;
    private final IInputHandler<FluidStack> inputHandlerF;
    private final TransformItemOutputHandler outputHandler;
    private final ItemStackIngredient iAIngredient;
    private final ItemStackIngredient iBIngredient;
    private final ItemStackIngredient iCIngredient;
    private final FluidStackIngredient fIngredient;

    public TransformCachedRecipe(TransformRecipe recipe,
            BooleanSupplier recheckAllErrors,
            IInputHandler<ItemStack> inputHandlerIA,
            IInputHandler<ItemStack> inputHandlerIB,
            IInputHandler<ItemStack> inputHandlerIC,
            IInputHandler<FluidStack> inputHandlerF,
            TransformItemOutputHandler outputHandler) {
        super(recipe, recheckAllErrors);
        this.inputHandlerIA = inputHandlerIA;
        this.inputHandlerIB = inputHandlerIB;
        this.inputHandlerIC = inputHandlerIC;
        this.inputHandlerF = inputHandlerF;
        this.outputHandler = outputHandler;
        this.fIngredient = AMERecipeConstants.TRANSFORM_FLUID_EXTRACTOR.apply(recipe);
        IItemStackIngredientCreator creatorI = IngredientCreatorAccess.item();
        int size = this.recipe.ingredients.size();
        this.iAIngredient = 0 < size && size < 4 ? creatorI.from(this.recipe.ingredients.get(0)) : null;
        this.iBIngredient = 1 < size && size < 4 ? creatorI.from(this.recipe.ingredients.get(1)) : null;
        this.iCIngredient = 2 < size && size < 4 ? creatorI.from(this.recipe.ingredients.get(2)) : null;
    }

    @Override
    public void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (!tracker.shouldContinueChecking()) {
            tracker.mismatchedRecipe();
            return;
        }
        if (iAIngredient == null) {
            tracker.mismatchedRecipe();
            return;
        } else {
            ItemStack input = inputHandlerIA.getRecipeInput(iAIngredient);
            if (input.isEmpty()) {
                tracker.mismatchedRecipe();
                return;
            } else {
                inputHandlerIA.calculateOperationsCanSupport(tracker, input);
            }
        }
        if (iBIngredient == null) {
            if (!inputHandlerIB.getInput().isEmpty()) {
                tracker.mismatchedRecipe();
                return;
            }
        } else {
            ItemStack input = inputHandlerIB.getRecipeInput(iBIngredient);
            if (input.isEmpty()) {
                tracker.mismatchedRecipe();
                return;
            } else {
                inputHandlerIB.calculateOperationsCanSupport(tracker, input);
            }
        }
        if (iCIngredient == null) {
            if (!inputHandlerIC.getInput().isEmpty()) {
                tracker.mismatchedRecipe();
                return;
            }
        } else {
            ItemStack input = inputHandlerIC.getRecipeInput(iCIngredient);
            if (input.isEmpty()) {
                tracker.mismatchedRecipe();
                return;
            } else {
                inputHandlerIC.calculateOperationsCanSupport(tracker, input);
            }
        }
        FluidStack inputFluid = inputHandlerF.getRecipeInput(fIngredient);
        if (inputFluid.isEmpty()) {
            tracker.mismatchedRecipe();
            return;
        } else {
            inputHandlerF.calculateOperationsCanSupport(tracker, inputFluid);
        }
        outputHandler.calculateOperationsCanSupport(tracker, recipe.getResultItem());
    }

    @Override
    protected void finishProcessing(int operations) {
        if (iAIngredient != null) {
            inputHandlerIA.use(inputHandlerIA.getRecipeInput(iAIngredient), operations);
        }
        if (iBIngredient != null) {
            inputHandlerIB.use(inputHandlerIB.getRecipeInput(iBIngredient), operations);
        }
        if (iCIngredient != null) {
            inputHandlerIC.use(inputHandlerIC.getRecipeInput(iCIngredient), operations);
        }
        outputHandler.handleOutput(recipe.output, operations);
    }

    @Override
    public boolean isInputValid() {
        boolean result = fIngredient.test(inputHandlerF.getInput());
        result &= iAIngredient == null ? inputHandlerIA.getInput().isEmpty()
                : iAIngredient.test(inputHandlerIA.getInput());
        result &= iBIngredient == null ? inputHandlerIB.getInput().isEmpty()
                : iBIngredient.test(inputHandlerIB.getInput());
        result &= iCIngredient == null ? inputHandlerIC.getInput().isEmpty()
                : iCIngredient.test(inputHandlerIC.getInput());
        return result;
    }

    public static class TransformItemOutputHandler implements IOutputHandler<ItemStack> {

        private final IInventorySlot slot;
        private final RecipeError notEnoughSpaceError;

        public TransformItemOutputHandler(IInventorySlot slot, RecipeError notEnoughSpaceError) {
            this.slot = slot;
            this.notEnoughSpaceError = notEnoughSpaceError;
        }

        @Override
        public void handleOutput(ItemStack toOutput, int operations) {
            if (AEItems.QUANTUM_ENTANGLED_SINGULARITY.isSameAs(toOutput)) {
                QuantumBridgeBlockEntity.assignFrequency(toOutput);
            }
            slot.insertItem(toOutput.copyWithCount(toOutput.getCount() * operations), Action.EXECUTE,
                    AutomationType.INTERNAL);
        }

        @Override
        public void calculateOperationsCanSupport(OperationTracker tracker, ItemStack toOutput) {
            if (AEItems.QUANTUM_ENTANGLED_SINGULARITY.isSameAs(toOutput)) {
                if (slot.isEmpty()) {
                    tracker.updateOperations(1);
                } else {
                    tracker.addError(notEnoughSpaceError);
                    tracker.mismatchedRecipe();
                }
            } else if (slot.isEmpty() || ItemStack.isSameItem(slot.getStack(), toOutput)) {
                int operations = (toOutput.getMaxStackSize() - slot.getCount()) / toOutput.getCount();
                if (operations < 1) {
                    tracker.addError(notEnoughSpaceError);
                    tracker.mismatchedRecipe();
                } else {
                    tracker.updateOperations(operations);
                }
            } else {
                tracker.addError(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);
                tracker.mismatchedRecipe();
            }
        }

    }

}
