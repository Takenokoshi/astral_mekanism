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
    static OperationTracker astral_mekanism$invokeInit(
            Set<RecipeError> lastErrors,
            boolean checkAll,
            int startingMax) {
        throw new AssertionError();
    }

    @Invoker("capAtMaxForEnergy")
    boolean astral_mekanism$invokeCapAtMaxForEnergy();

    @Invoker("hasErrorsToCopy")
    boolean astral_mekanism$invokeHasErrorsToCopy();

    @Accessor("currentMax")
    int astral_mekanism$getCurrentMax();

    @Accessor("errors")
    Set<RecipeError> astral_mekanism$getErrors();

    @Accessor("maxForEnergy")
    void astral_mekanism$setMaxForEnergy(int value);
}
