package astral_mekanism.mixin.mekanism;

import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;

@Mixin(value = CachedRecipe.OperationTracker.class, remap = false)
public interface OperationTrackerMixin {

    @Invoker("<init>")
    static OperationTracker invokeInit(
            Set<RecipeError> lastErrors,
            boolean checkAll,
            int startingMax) {
        throw new AssertionError();
    }

    @Invoker("capAtMaxForEnergy")
    boolean invokeCapAtMaxForEnergy();

    @Invoker("hasErrorsToCopy")
    boolean invokeHasErrorsToCopy();

    @Accessor("currentMax")
    int getCurrentMax();

    @Accessor("errors")
    Set<RecipeError> getErrors();

    @Accessor("maxForEnergy")
    void setMaxForEnergy(int value);
}
