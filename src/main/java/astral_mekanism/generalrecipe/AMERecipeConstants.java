package astral_mekanism.generalrecipe;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import appeng.recipes.transform.TransformRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.generators.common.registries.GeneratorsFluids;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.fluids.FluidStack;

public class AMERecipeConstants {

    public static final BiFunction<CraftingRecipe, Integer, ItemStackIngredient> CRAFTING_ITEM_EXTRACTOR = (
            recipe, index) -> {
        int listIndex = index;
        if (recipe instanceof ShapedRecipe shapedRecipe) {
            int x = index % 3;
            int width = shapedRecipe.getWidth();
            listIndex = x < width ? x + index / 3 * width : 9;
        }
        Ingredient ingredient = listIndex < recipe.getIngredients().size()
                ? recipe.getIngredients().get(listIndex)
                : Ingredient.EMPTY;
        return ingredient.isEmpty() ? null : IngredientCreatorAccess.item().from(ingredient);
    };

    public static final TriPredicate<CraftingRecipe, Integer, ItemStack> CRAFTING_ITEM_PREDICATE = (recipe, index,
            stack) -> {
        int listIndex = index;
        if (recipe instanceof ShapedRecipe shapedRecipe) {
            int x = index % 3;
            int width = shapedRecipe.getWidth();
            listIndex = x < width ? x + index / 3 * width : 9;
        }
        Ingredient ingredient = listIndex < recipe.getIngredients().size()
                ? recipe.getIngredients().get(listIndex)
                : Ingredient.EMPTY;
        return ingredient.isEmpty() ? stack.isEmpty() : ingredient.test(stack);
    };

    public static final Function<TransformRecipe, FluidStackIngredient> TRANSFORM_FLUID_EXTRACTOR = r -> r.circumstance
            .isFluid()
                    ? IngredientCreatorAccess.fluid()
                            .from(r.circumstance.getFluidsForRendering().stream()
                                    .map(fluid -> IngredientCreatorAccess.fluid().from(fluid, 1000)))
                    : IngredientCreatorAccess.fluid().from(GeneratorsFluids.FUSION_FUEL.getFluidStack(1000));

    public static final BiPredicate<TransformRecipe, FluidStack> TRANSFORM_FLUID_PREDICATE = (recipe,
            stack) -> recipe.circumstance.isFluid()
                    ? recipe.circumstance.getFluidsForRendering().stream()
                            .anyMatch(fluid -> fluid.isSame(stack.getFluid()))
                    : GeneratorsFluids.FUSION_FUEL.getFluidStack(1).isFluidEqual(stack);
}
