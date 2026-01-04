package astral_mekanism.block.blockentity.base;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.common.inventory.container.MekanismContainer;

public class ErrorTracker {

    public final List<RecipeError> errorTypes;
    public final IntSet globalTypes;
    public final boolean[][] trackedErrors;
    public final int processes;

    public ErrorTracker(List<RecipeError> errorTypes, Set<RecipeError> globalErrorTypes, int processes) {
        this.errorTypes = List.copyOf(errorTypes);
        globalTypes = new IntArraySet(globalErrorTypes.size());
        for (int i = 0; i < this.errorTypes.size(); i++) {
            RecipeError error = this.errorTypes.get(i);
            if (globalErrorTypes.contains(error)) {
                globalTypes.add(i);
            }
        }
        this.processes = processes;
        trackedErrors = new boolean[this.processes][];
        int errors = this.errorTypes.size();
        for (int i = 0; i < trackedErrors.length; i++) {
            trackedErrors[i] = new boolean[errors];
        }
    }

    public void track(MekanismContainer container) {
        container.trackArray(trackedErrors);
    }

    public void onErrorsChanged(Set<RecipeError> errors, int processIndex) {
        boolean[] processTrackedErrors = trackedErrors[processIndex];
        for (int i = 0; i < processTrackedErrors.length; i++) {
            processTrackedErrors[i] = errors.contains(errorTypes.get(i));
        }
    }

    BooleanSupplier getWarningCheck(RecipeError error, int processIndex) {
        if (processIndex >= 0 && processIndex < processes) {
            int errorIndex = errorTypes.indexOf(error);
            if (errorIndex >= 0) {
                if (globalTypes.contains(errorIndex)) {
                    return () -> Arrays.stream(trackedErrors)
                            .anyMatch(processTrackedErrors -> processTrackedErrors[errorIndex]);
                }
                return () -> trackedErrors[processIndex][errorIndex];
            }
        }
        // Something went wrong
        return () -> false;
    }
}