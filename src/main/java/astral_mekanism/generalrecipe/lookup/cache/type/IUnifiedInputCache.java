package astral_mekanism.generalrecipe.lookup.cache.type;

import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.common.recipe.ingredient.IMultiIngredient;
import net.minecraft.world.item.crafting.Recipe;

public interface IUnifiedInputCache<INPUT, INGREDIENT extends InputIngredient<INPUT>, RECIPE extends Recipe<?>> {

    boolean contains(INPUT input);

    boolean contains(INPUT input, Predicate<RECIPE> matchCriteria);

    @Nullable
    RECIPE findFirstRecipe(INPUT input, Predicate<RECIPE> matchCriteria);

    boolean mapInputs(RECIPE recipe, INGREDIENT inputIngredient);

    default boolean mapMultiInputs(RECIPE recipe, IMultiIngredient<INPUT, ? extends INGREDIENT> multi) {
        return multi.forEachIngredient(ingredient -> mapInputs(recipe, ingredient));
    }

    void clear();

    boolean isEmpty(INPUT input);
}
