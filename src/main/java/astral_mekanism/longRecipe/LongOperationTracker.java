package astral_mekanism.longRecipe;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;

public class LongOperationTracker {

    private static final int RESET_PROGRESS = -1;
    private static final int MISMATCHED_RECIPE = -2;

    private final Set<RecipeError> lastErrors;
    private Set<RecipeError> errors = Collections.emptySet();
    private boolean checkAll;
    private boolean checkedErrors = true;
    private long currentMax;
    private long maxForEnergy;

    public LongOperationTracker(Set<RecipeError> lastErrors, boolean checkAll, long startingMax) {
        this.lastErrors = lastErrors;
        this.checkAll = checkAll;
        this.currentMax = startingMax;
        this.maxForEnergy = currentMax;
    }

    public boolean hasErrorsToCopy() {
        if (currentMax == MISMATCHED_RECIPE) {
            errors = Collections.emptySet();
            return true;
        } else if (checkAll || currentMax > 0) {
            return true;
        }
        return !checkedErrors && !lastErrors.containsAll(errors);
    }

    public boolean shouldContinueChecking() {
        if (currentMax > 0) {
            return true;
        } else if (currentMax == 0) {
            if (checkAll) {
                return true;
            } else if (!checkedErrors) {
                if (!lastErrors.containsAll(errors)) {
                    checkAll = true;
                    return true;
                }
                checkedErrors = true;
            }
        }
        return false;
    }

    public boolean updateOperations(long max) {
        if (max < currentMax) {
            currentMax = max;
            return true;
        }
        return false;
    }

    public boolean capAtMaxForEnergy() {
        return updateOperations(maxForEnergy);
    }

    public void mismatchedRecipe() {
        updateOperations(MISMATCHED_RECIPE);
    }

    public void resetProgress(RecipeError error) {
        updateOperations(RESET_PROGRESS);
        addError(error);
    }

    public void addError(RecipeError error) {
        Objects.requireNonNull(error, "Error cannot be null.");
        if (errors.isEmpty()) {
            errors = new ObjectArraySet<>();
        }
        if (errors.add(error)) {
            checkedErrors = false;
        }
    }

    public long getMaxForEnergy(){
        return maxForEnergy;
    }

    public long getCurrentMax(){
        return currentMax;
    }
    public void setNaxForEnergy(long value){
        maxForEnergy = value;
    }

    public Set<RecipeError> getErrors(){
        return errors;
    }
}
