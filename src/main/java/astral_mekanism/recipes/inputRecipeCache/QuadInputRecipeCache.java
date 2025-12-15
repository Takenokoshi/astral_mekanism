package astral_mekanism.recipes.inputRecipeCache;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.util.AMInterface.QuadPredicate;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.AbstractInputRecipeCache;
import mekanism.common.recipe.lookup.cache.type.IInputCache;
import net.minecraft.world.level.Level;

public abstract class QuadInputRecipeCache<INPUT_A, INGREDIENT_A extends InputIngredient<INPUT_A>, INPUT_B, INGREDIENT_B extends InputIngredient<INPUT_B>, INPUT_C, INGREDIENT_C extends InputIngredient<INPUT_C>, INPUT_D, INGREDIENT_D extends InputIngredient<INPUT_D>, RECIPE extends MekanismRecipe & QuadPredicate<INPUT_A, INPUT_B, INPUT_C, INPUT_D>, CACHE_A extends IInputCache<INPUT_A, INGREDIENT_A, RECIPE>, CACHE_B extends IInputCache<INPUT_B, INGREDIENT_B, RECIPE>, CACHE_C extends IInputCache<INPUT_C, INGREDIENT_C, RECIPE>, CACHE_D extends IInputCache<INPUT_D, INGREDIENT_D, RECIPE>>
        extends AbstractInputRecipeCache<RECIPE> {
    private final Set<RECIPE> complexIngredientA = new HashSet<>();
    private final Set<RECIPE> complexIngredientB = new HashSet<>();
    private final Set<RECIPE> complexIngredientC = new HashSet<>();
    private final Set<RECIPE> complexIngredientD = new HashSet<>();
    private final Set<RECIPE> complexRecipes = new HashSet<>();
    private final Function<RECIPE, INGREDIENT_A> inputAExtractor;
    private final Function<RECIPE, INGREDIENT_B> inputBExtractor;
    private final Function<RECIPE, INGREDIENT_C> inputCExtractor;
    private final Function<RECIPE, INGREDIENT_D> inputDExtractor;
    private final CACHE_A cacheA;
    private final CACHE_B cacheB;
    private final CACHE_C cacheC;
    private final CACHE_D cacheD;

    protected QuadInputRecipeCache(MekanismRecipeType<RECIPE, ?> recipeType,
            Function<RECIPE, INGREDIENT_A> inputAExtractor,
            Function<RECIPE, INGREDIENT_B> inputBExtractor,
            Function<RECIPE, INGREDIENT_C> inputCExtractor,
            Function<RECIPE, INGREDIENT_D> inputDExtractor,
            CACHE_A cacheA, CACHE_B cacheB, CACHE_C cacheC, CACHE_D cacheD) {
        super(recipeType);
        this.inputAExtractor = inputAExtractor;
        this.inputBExtractor = inputBExtractor;
        this.inputCExtractor = inputCExtractor;
        this.inputDExtractor = inputDExtractor;
        this.cacheA = cacheA;
        this.cacheB = cacheB;
        this.cacheC = cacheC;
        this.cacheD = cacheD;
    }

    public void clear() {
        super.clear();
        this.cacheA.clear();
        this.cacheB.clear();
        this.cacheC.clear();
        this.cacheD.clear();
        this.complexIngredientA.clear();
        this.complexIngredientB.clear();
        this.complexIngredientC.clear();
        this.complexIngredientD.clear();
        this.complexRecipes.clear();
    }

    public boolean containsInputA(@Nullable Level world, INPUT_A input) {
        return this.containsInput(world, input, this.inputAExtractor, this.cacheA, this.complexIngredientA);
    }

    public boolean containsInputB(@Nullable Level world, INPUT_B input) {
        return this.containsInput(world, input, this.inputBExtractor, this.cacheB, this.complexIngredientB);
    }

    public boolean containsInputC(@Nullable Level world, INPUT_C input) {
        return this.containsInput(world, input, this.inputCExtractor, this.cacheC, this.complexIngredientC);
    }

    public boolean containsInputD(@Nullable Level world, INPUT_D input) {
        return this.containsInput(world, input, this.inputDExtractor, this.cacheD, this.complexIngredientD);
    }

    public boolean containsInputABCD(@Nullable Level world, INPUT_A inputA, INPUT_B inputB, INPUT_C inputC,
            INPUT_D inputD) {
        return containsGrouping(world,
                inputA, inputAExtractor, cacheA, complexIngredientA,
                inputB, inputBExtractor, cacheB, complexIngredientB,
                inputC, inputCExtractor, cacheC, complexIngredientC,
                inputD, inputDExtractor, cacheD, complexIngredientD);
    }

    public boolean containsInputBACD(@Nullable Level world, INPUT_A inputA, INPUT_B inputB, INPUT_C inputC,
            INPUT_D inputD) {
        return containsGrouping(world,
                inputB, inputBExtractor, cacheB, complexIngredientB,
                inputA, inputAExtractor, cacheA, complexIngredientA,
                inputC, inputCExtractor, cacheC, complexIngredientC,
                inputD, inputDExtractor, cacheD, complexIngredientD);
    }

    public boolean containsInputCABD(@Nullable Level world, INPUT_A inputA, INPUT_B inputB, INPUT_C inputC,
            INPUT_D inputD) {
        return containsGrouping(world,
                inputC, inputCExtractor, cacheC, complexIngredientC,
                inputA, inputAExtractor, cacheA, complexIngredientA,
                inputB, inputBExtractor, cacheB, complexIngredientB,
                inputD, inputDExtractor, cacheD, complexIngredientD);
    }

    public boolean containsInputDABC(@Nullable Level world, INPUT_A inputA, INPUT_B inputB, INPUT_C inputC,
            INPUT_D inputD) {
        return containsGrouping(world,
                inputD, inputDExtractor, cacheD, complexIngredientD,
                inputA, inputAExtractor, cacheA, complexIngredientA,
                inputB, inputBExtractor, cacheB, complexIngredientB,
                inputC, inputCExtractor, cacheC, complexIngredientC);
    }

    public @Nullable RECIPE findFirstRecipe(@Nullable Level world, INPUT_A inputA, INPUT_B inputB, INPUT_C inputC,
            INPUT_D inputD) {
        if (cacheA.isEmpty(inputA) || cacheB.isEmpty(inputB) || cacheC.isEmpty(inputC)) {
            return null;
        }
        initCacheIfNeeded(world);
        Predicate<RECIPE> matchPredicate = r -> r.test(inputA, inputB, inputC, inputD);
        RECIPE recipe = cacheA.findFirstRecipe(inputA, matchPredicate);
        return recipe == null ? findFirstRecipe(complexRecipes, matchPredicate) : recipe;
    }

    @Override
    protected void initCache(List<RECIPE> recipes) {
        for (RECIPE recipe : recipes) {
            boolean complexA = cacheA.mapInputs(recipe, inputAExtractor.apply(recipe));
            boolean complexB = cacheB.mapInputs(recipe, inputBExtractor.apply(recipe));
            boolean complexC = cacheC.mapInputs(recipe, inputCExtractor.apply(recipe));
            boolean complexD = cacheD.mapInputs(recipe, inputDExtractor.apply(recipe));
            if (complexA) {
                complexIngredientA.add(recipe);
            }
            if (complexB) {
                complexIngredientB.add(recipe);
            }
            if (complexC) {
                complexIngredientC.add(recipe);
            }
            if (complexD) {
                complexIngredientD.add(recipe);
            }
            if (complexA || complexB || complexC || complexD) {
                complexRecipes.add(recipe);
            }
        }
    }

    private <INPUT_1, INGREDIENT_1 extends InputIngredient<INPUT_1>, CACHE_1 extends IInputCache<INPUT_1, INGREDIENT_1, RECIPE>, INPUT_2, INGREDIENT_2 extends InputIngredient<INPUT_2>, CACHE_2 extends IInputCache<INPUT_2, INGREDIENT_2, RECIPE>, INPUT_3, INGREDIENT_3 extends InputIngredient<INPUT_3>, CACHE_3 extends IInputCache<INPUT_3, INGREDIENT_3, RECIPE>, INPUT_4, INGREDIENT_4 extends InputIngredient<INPUT_4>, CACHE_4 extends IInputCache<INPUT_4, INGREDIENT_4, RECIPE>> boolean containsGrouping(
            @Nullable Level world,
            INPUT_1 input1, Function<RECIPE, INGREDIENT_1> input1Extractor, CACHE_1 cache1,
            Set<RECIPE> complexIngredients1,
            INPUT_2 input2, Function<RECIPE, INGREDIENT_2> input2Extractor, CACHE_2 cache2,
            Set<RECIPE> complexIngredients2,
            INPUT_3 input3, Function<RECIPE, INGREDIENT_3> input3Extractor, CACHE_3 cache3,
            Set<RECIPE> complexIngredients3,
            INPUT_4 input4, Function<RECIPE, INGREDIENT_4> input4Extractor, CACHE_4 cache4,
            Set<RECIPE> complexIngredients4) {
        if (cache1.isEmpty(input1)) {
            if (cache2.isEmpty(input2)) {
                if (cache3.isEmpty(input3)) {
                    return containsInput(world, input4, input4Extractor, cache4, complexIngredients4);
                } else {
                    return containsPairing(world, input3, input3Extractor, cache3, complexIngredients3,
                            input4, input4Extractor, cache4, complexIngredients4);
                }
            } else {
                if (cache3.isEmpty(input3)) {
                    return containsPairing(world, input2, input2Extractor, cache2, complexIngredients2,
                            input4, input4Extractor, cache4, complexIngredients4);

                } else if (cache4.isEmpty(input4)) {
                    return containsPairing(world, input2, input2Extractor, cache2, complexIngredients2,
                            input3, input3Extractor, cache3, complexIngredients3);
                } else {
                    initCacheIfNeeded(world);
                    return cache2.contains(input2,
                            recipe -> input3Extractor.apply(recipe).testType(input3)
                                    && input4Extractor.apply(recipe).testType(input4))
                                            ? true
                                            : complexIngredients2.stream()
                                                    .anyMatch(recipe -> input2Extractor.apply(recipe).testType(input2)
                                                            && input3Extractor.apply(recipe).testType(input3)
                                                            && input4Extractor.apply(recipe).testType(input4));
                }
            }
        } else {
            if (cache2.isEmpty(input2)) {
                if (cache3.isEmpty(input3)) {
                    return containsPairing(world, input1, input1Extractor, cache1, complexIngredients1,
                            input4, input4Extractor, cache4, complexIngredients4);
                } else if (cache4.isEmpty(input4)) {
                    return containsPairing(world, input1, input1Extractor, cache1, complexIngredients1,
                            input3, input3Extractor, cache3, complexIngredients3);
                } else {
                    initCacheIfNeeded(world);
                    return cache1.contains(input1,
                            recipe -> input3Extractor.apply(recipe).testType(input3)
                                    && input4Extractor.apply(recipe).testType(input4))
                                            ? true
                                            : complexIngredients2.stream()
                                                    .anyMatch(recipe -> input1Extractor.apply(recipe).testType(input1)
                                                            && input3Extractor.apply(recipe).testType(input3)
                                                            && input4Extractor.apply(recipe).testType(input4));
                }
            } else {
                if (cache3.isEmpty(input3)) {
                    return cache4.isEmpty(input4)
                            ? containsPairing(world, input1, input1Extractor, cache1, complexIngredients1,
                                    input2, input2Extractor, cache2, complexIngredients2)
                            : cache1.contains(input1,
                                    recipe -> input2Extractor.apply(recipe).testType(input2)
                                            && input4Extractor.apply(recipe).testType(input4))
                                                    ? true
                                                    : complexIngredients2.stream()
                                                            .anyMatch(recipe -> input1Extractor.apply(recipe)
                                                                    .testType(input1)
                                                                    && input2Extractor.apply(recipe).testType(input2)
                                                                    && input4Extractor.apply(recipe).testType(input4));
                } else if (cache4.isEmpty(input4)) {
                    return cache1.contains(input1,
                            recipe -> input2Extractor.apply(recipe).testType(input2)
                                    && input3Extractor.apply(recipe).testType(input3))
                                            ? true
                                            : complexIngredients2.stream()
                                                    .anyMatch(recipe -> input1Extractor.apply(recipe)
                                                            .testType(input1)
                                                            && input2Extractor.apply(recipe).testType(input2)
                                                            && input3Extractor.apply(recipe).testType(input3));
                } else {
                    return cache1.contains(input1,
                            recipe -> input2Extractor.apply(recipe).testType(input2)
                                    && input3Extractor.apply(recipe).testType(input3)
                                    && input4Extractor.apply(recipe).testType(input4))
                                            ? true
                                            : complexIngredients2.stream()
                                                    .anyMatch(recipe -> input1Extractor.apply(recipe)
                                                            .testType(input1)
                                                            && input2Extractor.apply(recipe).testType(input2)
                                                            && input3Extractor.apply(recipe).testType(input3)
                                                            && input4Extractor.apply(recipe).testType(input4));
                }
            }
        }
    }

}
