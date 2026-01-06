package astral_mekanism.mixin.mekanism.recipe.lookup.cache.recipe;

import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import astral_mekanism.generalrecipe.lookup.cache.recipe.ISingleInputRecipeCache;
import astral_mekanism.generalrecipe.lookup.cache.type.IUnifiedInputCache;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.common.recipe.lookup.cache.SingleInputRecipeCache;
import mekanism.common.recipe.lookup.cache.type.IInputCache;
import net.minecraft.world.level.Level;

@Mixin(value = SingleInputRecipeCache.class, remap = false)
public abstract class SingleInputRecipeCacheMixin<INPUT, INGREDIENT extends InputIngredient<INPUT>, RECIPE extends MekanismRecipe & Predicate<INPUT>, CACHE extends IUnifiedInputCache<INPUT, INGREDIENT, RECIPE> & IInputCache<INPUT, INGREDIENT, RECIPE>>
        implements ISingleInputRecipeCache<INPUT, RECIPE> {

    @Shadow
    public abstract void clear();

    @Shadow
    public abstract boolean containsInput(@Nullable Level world, INPUT input);

    @Shadow
    @Nullable
    public abstract RECIPE findFirstRecipe(@Nullable Level world, INPUT input);

    @Shadow
    @Nullable
    public abstract RECIPE findTypeBasedRecipe(@Nullable Level world, INPUT input);

    @Shadow
    @Nullable
    public abstract RECIPE findTypeBasedRecipe(
            @Nullable Level world,
            INPUT input,
            Predicate<RECIPE> matchCriteria);

}
