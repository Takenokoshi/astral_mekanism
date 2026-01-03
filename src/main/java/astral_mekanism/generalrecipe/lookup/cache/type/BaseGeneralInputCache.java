package astral_mekanism.generalrecipe.lookup.cache.type;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import mekanism.api.recipes.ingredients.InputIngredient;
import net.minecraft.world.item.crafting.Recipe;

public abstract class BaseGeneralInputCache<KEY, INPUT, INGREDIENT extends InputIngredient<INPUT>, RECIPE extends Recipe<?>>
        implements
        IGeneralInputCache<INPUT, INGREDIENT, RECIPE> {

    private final Map<KEY, Set<RECIPE>> inputCache = new HashMap<>();

    @Override
    public void clear() {
        inputCache.clear();
    }

    @Override
    public boolean contains(INPUT input) {
        return inputCache.containsKey(createKey(input));
    }

    @Override
    public boolean contains(INPUT input, Predicate<RECIPE> matchCriteria) {
        Set<RECIPE> recipes = inputCache.get(createKey(input));
        return recipes != null && recipes.stream().anyMatch(matchCriteria);
    }

    @Nullable
    @Override
    public RECIPE findFirstRecipe(INPUT input, Predicate<RECIPE> matchCriteria) {
        return findFirstRecipe(inputCache.get(createKey(input)), matchCriteria);
    }

    @Nullable
    protected RECIPE findFirstRecipe(@Nullable Collection<RECIPE> recipes, Predicate<RECIPE> matchCriteria) {
        return recipes == null ? null : recipes.stream().filter(matchCriteria).findFirst().orElse(null);
    }

    protected abstract KEY createKey(INPUT input);

    protected void addInputCache(KEY input, RECIPE recipe) {
        inputCache.computeIfAbsent(input, i -> new HashSet<>()).add(recipe);
    }
}
