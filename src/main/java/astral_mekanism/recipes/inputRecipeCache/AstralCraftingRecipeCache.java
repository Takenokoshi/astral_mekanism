package astral_mekanism.recipes.inputRecipeCache;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.recipes.recipe.AstralCraftingRecipe;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.AbstractInputRecipeCache;
import mekanism.common.recipe.lookup.cache.type.ChemicalInputCache;
import mekanism.common.recipe.lookup.cache.type.FluidInputCache;
import mekanism.common.recipe.lookup.cache.type.ItemInputCache;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
public class AstralCraftingRecipeCache extends AbstractInputRecipeCache<AstralCraftingRecipe> {

    private static final int SLOT_COUNT = 25;

    @SuppressWarnings("unchecked")
    private final Set<AstralCraftingRecipe>[] complexIngredientItems = new Set[SLOT_COUNT];

    private final Set<AstralCraftingRecipe> complexIngredientFluid = new HashSet<>();
    private final Set<AstralCraftingRecipe> complexIngredientGas = new HashSet<>();
    private final Set<AstralCraftingRecipe> complexRecipes = new HashSet<>();

    @SuppressWarnings("unchecked")
    private final ItemInputCache<AstralCraftingRecipe>[] cacheItems = new ItemInputCache[SLOT_COUNT];

    private final FluidInputCache<AstralCraftingRecipe> cacheFluid = new FluidInputCache<>();
    private final ChemicalInputCache<Gas, GasStack, AstralCraftingRecipe> cacheGas = new ChemicalInputCache<>();

    public AstralCraftingRecipeCache(MekanismRecipeType<AstralCraftingRecipe, ?> recipeType) {
        super(recipeType);

        for (int i = 0; i < SLOT_COUNT; i++) {
            cacheItems[i] = new ItemInputCache<>();
            complexIngredientItems[i] = new HashSet<>();
        }
    }

    @Override
    public void clear() {
        super.clear();
        for (int i = 0; i < SLOT_COUNT; i++) {
            cacheItems[i].clear();
            complexIngredientItems[i].clear();
        }
        cacheFluid.clear();
        cacheGas.clear();
        complexIngredientFluid.clear();
        complexIngredientGas.clear();
        complexRecipes.clear();
    }

    public boolean containsInputItem(@Nullable Level world, ItemStack input, int index) {
        if (index < 0 || index >= SLOT_COUNT) {
            return false;
        }
        return containsInput(world, input, r -> r.getInputItem(index), cacheItems[index], complexIngredientItems[index]);
    }

    public boolean containsInputFluid(@Nullable Level world, FluidStack input) {
        return containsInput(world, input, AstralCraftingRecipe::getInputFluid, cacheFluid, complexIngredientFluid);
    }

    public boolean containsInputGas(@Nullable Level world, GasStack input) {
        return containsInput(world, input, AstralCraftingRecipe::getInputGas, cacheGas, complexIngredientGas);
    }

    public boolean containsInputGas(@Nullable Level world, Gas input) {
        return containsInput(world, input.getStack(1), AstralCraftingRecipe::getInputGas, cacheGas, complexIngredientGas);
    }

    public boolean containsInputItemOther(@Nullable Level world, ItemStack input, int index,
                                          ItemStack[] inputItems, FluidStack inputFluid, GasStack inputGas) {
        if (index < 0 || index >= SLOT_COUNT || inputItems.length != SLOT_COUNT) {
            return false;
        }
        initCacheIfNeeded(world);

        return cacheItems[index].contains(input, r -> {
            // type match only, not amounts
            if (!cacheFluid.isEmpty(inputFluid) && !r.getInputFluid().testType(inputFluid)) return false;
            if (!cacheGas.isEmpty(inputGas) && !r.getInputGas().testType(inputGas)) return false;

            for (int i = 0; i < SLOT_COUNT; i++) {
                if (i == index) continue;
                if (!cacheItems[i].isEmpty(inputItems[i]) && !r.getInputItem(i).testType(inputItems[i])) {
                    return false;
                }
            }
            return true;
        });
    }

    public boolean containsInputFluidOther(@Nullable Level world, FluidStack input,
                                           ItemStack[] inputItems, GasStack inputGas) {
        if (inputItems.length != SLOT_COUNT) {
            return false;
        }
        initCacheIfNeeded(world);

        return cacheFluid.contains(input, r -> {
            if (!cacheGas.isEmpty(inputGas) && !r.getInputGas().testType(inputGas)) return false;
            for (int i = 0; i < SLOT_COUNT; i++) {
                if (!cacheItems[i].isEmpty(inputItems[i]) && !r.getInputItem(i).testType(inputItems[i])) {
                    return false;
                }
            }
            return true;
        });
    }

    public boolean containsInputGasOther(@Nullable Level world, GasStack input,
                                         ItemStack[] inputItems, FluidStack inputFluid) {
        if (inputItems.length != SLOT_COUNT) {
            return false;
        }
        initCacheIfNeeded(world);

        return cacheGas.contains(input, r -> {
            if (!cacheFluid.isEmpty(inputFluid) && !r.getInputFluid().testType(inputFluid)) return false;
            for (int i = 0; i < SLOT_COUNT; i++) {
                if (!cacheItems[i].isEmpty(inputItems[i]) && !r.getInputItem(i).testType(inputItems[i])) {
                    return false;
                }
            }
            return true;
        });
    }

    @Nullable
    public AstralCraftingRecipe findFirstRecipe(@Nullable Level world, ItemStack[] inputItems,
                                                FluidStack inputFluid, GasStack inputGas) {
        if (inputItems.length != SLOT_COUNT || cacheFluid.isEmpty(inputFluid)) {
            return null;
        }

        for (int i = 0; i < SLOT_COUNT; i++) {
            if (cacheItems[i].isEmpty(inputItems[i])) {
                return null;
            }
        }

        initCacheIfNeeded(world);

        // Use slot 0 as primary
        AstralCraftingRecipe recipe = cacheItems[0].findFirstRecipe(
                inputItems[0],
                r -> r.test(inputItems, inputFluid, inputGas)
        );

        return recipe != null
                ? recipe
                : findFirstRecipe(complexRecipes, r -> r.test(inputItems, inputFluid, inputGas));
    }

    @Override
    protected void initCache(List<AstralCraftingRecipe> recipes) {
        for (AstralCraftingRecipe recipe : recipes) {
            boolean complex = false;

            for (int i = 0; i < SLOT_COUNT; i++) {
                boolean complexItem = cacheItems[i].mapInputs(recipe, recipe.getInputItem(i));
                if (complexItem) {
                    complexIngredientItems[i].add(recipe);
                }
                complex |= complexItem;
            }

            boolean complexFluid = cacheFluid.mapInputs(recipe, recipe.getInputFluid());
            boolean complexGas = cacheGas.mapInputs(recipe, recipe.getInputGas());

            if (complexFluid) complexIngredientFluid.add(recipe);
            if (complexGas) complexIngredientGas.add(recipe);

            if (complex || complexFluid || complexGas) {
                complexRecipes.add(recipe);
            }
        }
    }
}
