package astral_mekanism.generalrecipe.cachedrecipe;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import astral_mekanism.mixin.mekanism.OperationTrackerMixin;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.math.FloatingLong;
import mekanism.api.math.FloatingLongConsumer;
import mekanism.api.math.FloatingLongSupplier;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import net.minecraft.world.item.crafting.Recipe;

@NothingNullByDefault
public abstract class GeneralCachedRecipe<RECIPE extends Recipe<?>> {
    protected final RECIPE recipe;
    private Set<RecipeError> errors = Collections.emptySet();
    private final BooleanSupplier recheckAllErrors;
    private BooleanSupplier canHolderFunction = () -> true;
    private BooleanConsumer setActive = active -> {
    };
    private IntSupplier requiredTicks = () -> 1;
    private Runnable onFinish = () -> {
    };
    private FloatingLongSupplier perTickEnergy = () -> FloatingLong.ZERO;
    private FloatingLongSupplier storedEnergy = () -> FloatingLong.ZERO;
    private FloatingLongConsumer useEnergy = energy -> {
    };
    private IntSupplier baselineMaxOperations = () -> 1;
    private Consumer<OperationTracker> postProcessOperations = tracker -> {
    };
    private Consumer<Set<RecipeError>> onErrorsChange = errors -> {
    };
    private int operatingTicks;
    private IntConsumer operatingTicksChanged = ticks -> {
    };

    protected GeneralCachedRecipe(RECIPE recipe, BooleanSupplier recheckAllErrors) {
        this.recipe = Objects.requireNonNull(recipe, "Recipe cannot be null.");
        this.recheckAllErrors = Objects.requireNonNull(recheckAllErrors, "Recheck all errors supplier cannot be null.");
    }

    public GeneralCachedRecipe<RECIPE> setCanHolderFunction(BooleanSupplier canHolderFunction) {
        this.canHolderFunction = Objects.requireNonNull(canHolderFunction, "Can holder function cannot be null.");
        return this;
    }

    public GeneralCachedRecipe<RECIPE> setActive(BooleanConsumer setActive) {
        this.setActive = Objects.requireNonNull(setActive, "Set active consumer cannot be null.");
        return this;
    }

    public GeneralCachedRecipe<RECIPE> setEnergyRequirements(FloatingLongSupplier perTickEnergy,
            IEnergyContainer energyContainer) {
        this.perTickEnergy = Objects.requireNonNull(perTickEnergy, "The per tick energy cannot be null.");
        Objects.requireNonNull(energyContainer, "Energy container cannot be null.");
        this.storedEnergy = energyContainer::getEnergy;
        this.useEnergy = energy -> energyContainer.extract(energy, Action.EXECUTE, AutomationType.INTERNAL);
        return this;
    }

    public GeneralCachedRecipe<RECIPE> setRequiredTicks(IntSupplier requiredTicks) {
        this.requiredTicks = Objects.requireNonNull(requiredTicks, "Required ticks cannot be null.");
        return this;
    }

    public GeneralCachedRecipe<RECIPE> setOperatingTicksChanged(IntConsumer operatingTicksChanged) {
        this.operatingTicksChanged = Objects.requireNonNull(operatingTicksChanged,
                "Operating ticks changed handler cannot be null.");
        return this;
    }

    public GeneralCachedRecipe<RECIPE> setOnFinish(Runnable onFinish) {
        this.onFinish = Objects.requireNonNull(onFinish, "On finish handling cannot be null.");
        return this;
    }

    public GeneralCachedRecipe<RECIPE> setBaselineMaxOperations(IntSupplier baselineMaxOperations) {
        this.baselineMaxOperations = Objects.requireNonNull(baselineMaxOperations,
                "Baseline max operations cannot be null.");
        return this;
    }

    public GeneralCachedRecipe<RECIPE> setPostProcessOperations(Consumer<OperationTracker> postProcessOperations) {
        this.postProcessOperations = Objects.requireNonNull(postProcessOperations,
                "Post processing of the operation count cannot be null.");
        return this;
    }

    public GeneralCachedRecipe<RECIPE> setErrorsChanged(Consumer<Set<RecipeError>> onErrorsChange) {
        this.onErrorsChange = Objects.requireNonNull(onErrorsChange, "On errors change consumer cannot be null.");
        return this;
    }

    private void updateErrors(Set<RecipeError> errors) {
        if (!this.errors.equals(errors)) {
            this.errors = errors;
            onErrorsChange.accept(errors);
        }
    }

    public void loadSavedOperatingTicks(int operatingTicks) {
        if (operatingTicks > 0 && operatingTicks < requiredTicks.getAsInt()) {
            this.operatingTicks = operatingTicks;
        }
    }

    public void process() {
        int operations;
        if (canHolderFunction.getAsBoolean()) {
            setupVariableValues();
            OperationTracker tracker = OperationTrackerMixin.invokeInit(errors, recheckAllErrors.getAsBoolean(),
                    baselineMaxOperations.getAsInt());
            calculateOperationsThisTick(tracker);
            if (tracker.shouldContinueChecking()) {
                postProcessOperations.accept(tracker);
                if (tracker.shouldContinueChecking()
                        && ((OperationTrackerMixin) (Object) tracker).invokeCapAtMaxForEnergy()) {
                    tracker.addError(RecipeError.NOT_ENOUGH_ENERGY_REDUCED_RATE);
                }
            }
            operations = ((OperationTrackerMixin) (Object) tracker).getCurrentMax();
            if (((OperationTrackerMixin) (Object) tracker).invokeHasErrorsToCopy()) {
                updateErrors(((OperationTrackerMixin) (Object) tracker).getErrors());
            }
        } else {
            operations = 0;
            if (!errors.isEmpty()) {
                updateErrors(Collections.emptySet());
            }
        }
        if (operations > 0) {
            setActive.accept(true);
            useEnergy(operations);
            operatingTicks++;
            int ticksRequired = requiredTicks.getAsInt();
            if (operatingTicks >= ticksRequired) {
                operatingTicks = 0;
                finishProcessing(operations);
                onFinish.run();
                resetCache();
            } else {
                useResources(operations);
            }
            if (ticksRequired > 1) {
                operatingTicksChanged.accept(operatingTicks);
            }
        } else {
            setActive.accept(false);
            if (operations < 0) {
                operatingTicks = 0;
                operatingTicksChanged.accept(operatingTicks);
                resetCache();
            }
        }
    }

    protected void setupVariableValues() {
    }

    protected int getOperatingTicks() {
        return operatingTicks;
    }

    protected void useResources(int operations) {
    }

    protected void resetCache() {
    }

    protected void useEnergy(int operations) {
        useEnergy.accept(perTickEnergy.get().multiply(operations));
    }

    protected void calculateOperationsThisTick(OperationTracker tracker) {
        if (tracker.shouldContinueChecking()) {
            FloatingLong energyPerTick = perTickEnergy.get();
            if (!energyPerTick.isZero()) {
                int operations = storedEnergy.get().divideToInt(energyPerTick);
                ((OperationTrackerMixin) (Object) tracker).setMaxForEnergy(operations);
                if (operations == 0) {
                    tracker.updateOperations(operations);
                    tracker.addError(RecipeError.NOT_ENOUGH_ENERGY);
                }
            }
        }
    }

    public abstract boolean isInputValid();

    protected abstract void finishProcessing(int operations);

    public RECIPE getRecipe() {
        return recipe;
    }
}