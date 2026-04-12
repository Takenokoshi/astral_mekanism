package astral_mekanism.block.blockentity.elements.aekey;

import java.util.function.LongSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.storage.MEStorage;
import mekanism.api.IContentsListener;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableFluidStack;
import net.minecraftforge.fluids.FluidStack;

public class AEFluidKeySlot extends AEKeySlot<AEFluidKey> {

    public AEFluidKeySlot(Supplier<MEStorage> storageSupplier, Predicate<? super AEFluidKey> keyPredicate,
            IContentsListener listener, int slotIndex) {
        super(storageSupplier, keyPredicate, listener, AEFluidKey::fromTag, AEFluidKey::toTag,
                k -> k instanceof AEFluidKey ? (AEFluidKey) k : null, slotIndex);
    }

    public AEFluidKeySlot(Supplier<MEStorage> storageSupplier,
            IContentsListener listener, int slotIndex) {
        this(storageSupplier, alwaysTrue, listener, slotIndex);
    }

    public IInputHandler<FluidStack> getInputHandler(LongSupplier longSupplier, @Nullable IActionSource actionSource,
            RecipeError notEnoughError) {
        return new IInputHandler<FluidStack>() {
            @Override
            public FluidStack getInput() {
                return key != null ? key.toStack(1) : FluidStack.EMPTY;
            }

            @Override
            public FluidStack getRecipeInput(InputIngredient<FluidStack> recipeIngredient) {
                return recipeIngredient.getMatchingInstance(getInput());
            }

            @Override
            public void use(FluidStack recipeInput, int operations) {
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    return;
                }
                storage.extract(key, 1l * recipeInput.getAmount() * operations, Actionable.MODULATE, actionSource);
            }

            @Override
            public void calculateOperationsCanSupport(OperationTracker tracker, FluidStack recipeInput,
                    int usageMultiplier) {
                if (!FluidStack.areFluidStackTagsEqual(getInput(), recipeInput)) {
                    tracker.mismatchedRecipe();
                    return;
                }
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnoughError);
                    return;
                }
                long usaAble = storage.getAvailableStacks().get(key) - longSupplier.getAsLong();
                if (usaAble < 1) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnoughError);
                } else {
                    tracker.updateOperations(
                            (int) Math.min(0x7fffffff, usaAble / (recipeInput.getAmount() * usageMultiplier)));
                }
            }
        };
    }

    public IOutputHandler<FluidStack> getOutputHandler(LongSupplier longSupplier, @Nullable IActionSource actionSource,
            RecipeError notEnuoghSpaceError) {
        return new IOutputHandler<FluidStack>() {
            @Override
            public void handleOutput(FluidStack toOutput, int operations) {
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    return;
                }
            }

            @Override
            public void calculateOperationsCanSupport(OperationTracker tracker, FluidStack toOutput) {
                AEFluidKey outputKey = AEFluidKey.of(toOutput);
                if (outputKey.equals(key)) {
                    setKey(outputKey);
                }
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnuoghSpaceError);
                    return;
                }
                long outputAble = storage.insert(key, longSupplier.getAsLong() - getAmount(), Actionable.SIMULATE,
                        actionSource);
                if (outputAble < 1) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnuoghSpaceError);
                } else {
                    tracker.updateOperations((int) Math.min(0x7fffffff, outputAble / toOutput.getAmount()));
                }
            }
        };
    }

    @Override
    public void trackContainer(MekanismContainer container) {
        container.track(SyncableFluidStack.create(() -> key != null ? key.toStack(1) : FluidStack.EMPTY,
                stack -> {
                    if (!stack.isEmpty()) {
                        setKey(AEFluidKey.of(stack));
                    } else {
                        setKey(null);
                    }
                }));
    }
}
