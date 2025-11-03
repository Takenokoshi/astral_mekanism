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

    @SuppressWarnings("unchecked")
    private final Set<AstralCraftingRecipe>[] complexIngredientItems = new Set[] {
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>(),
            new HashSet<AstralCraftingRecipe>()
    };

    private final Set<AstralCraftingRecipe> complexIngredientFluid = new HashSet<>();
    private final Set<AstralCraftingRecipe> complexIngredientGas = new HashSet<>();
    private final Set<AstralCraftingRecipe> complexRecipes = new HashSet<>();
    private final ItemInputCache<AstralCraftingRecipe>[] cacheItems;
    private final FluidInputCache<AstralCraftingRecipe> cacheFluid;
    private final ChemicalInputCache<Gas, GasStack, AstralCraftingRecipe> cacheGas;

    @SuppressWarnings("unchecked")
    public AstralCraftingRecipeCache(
            MekanismRecipeType<AstralCraftingRecipe, ?> recipeType) {
        super(recipeType);
        this.cacheItems = new ItemInputCache[25];
        for (int i = 0; i < 25; i++) {
            this.cacheItems[i] = new ItemInputCache<AstralCraftingRecipe>();
        }
        this.cacheFluid = new FluidInputCache<AstralCraftingRecipe>();
        this.cacheGas = new ChemicalInputCache<Gas, GasStack, AstralCraftingRecipe>();
    }

    @Override
    public void clear() {
        super.clear();
        for (int i = 0; i < 25; i++) {
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
        return containsInput(world, input, r -> r.getInputItem(index % 25), cacheItems[index % 25],
                complexIngredientItems[index % 25]);
    }

    public boolean containsInputFluid(@Nullable Level world, FluidStack input) {
        return containsInput(world, input, r -> r.getInputFluid(), cacheFluid, complexIngredientFluid);
    }

    public boolean containsInputGas(@Nullable Level world, GasStack input) {
        return containsInput(world, input, r -> r.getInputGas(), cacheGas, complexIngredientGas);
    }

    public boolean containsInputGas(@Nullable Level world, Gas input) {
        return containsInput(world, input.getStack(1), r -> r.getInputGas(), cacheGas, complexIngredientGas);
    }

    public boolean containsInputItemOther(@Nullable Level world, ItemStack input, int index,
            ItemStack[] inputItems, FluidStack inputFluid, GasStack inputGas) {
        if (inputItems.length != 25 || index > 25) {
            return false;
        }
        initCacheIfNeeded(world);
        return cacheItems[index].contains(input, r -> {
            boolean boo = cacheFluid.isEmpty(inputFluid) || r.getInputFluid().testType(inputFluid);
            boo &= cacheGas.isEmpty(inputGas) || r.getInputGas().testType(inputGas);
            int i = 0;
            while (boo && i < 25) {
                boo &= i == index || cacheItems[i].isEmpty(inputItems[i]) || r.getInputItem(i).testType(inputItems[i]);
                i++;
            }
            return boo;
        });
    }

    public boolean containsInputFluidOther(@Nullable Level world, FluidStack input,
            ItemStack[] inputItems, GasStack inputGas) {
        if (inputItems.length != 25) {
            return false;
        }
        initCacheIfNeeded(world);
        return cacheFluid.contains(input, r -> {
            boolean boo = cacheGas.isEmpty(inputGas) || r.getInputGas().testType(inputGas);
            int i = 0;
            while (boo && i < 25) {
                boo &= cacheItems[i].isEmpty(inputItems[i]) || r.getInputItem(i).testType(inputItems[i]);
                i++;
            }
            return boo;
        });
    }

    public boolean containsInputGasOther(@Nullable Level world, GasStack input,
            ItemStack[] inputItems, FluidStack inputFluid) {
        if (inputItems.length != 25) {
            return false;
        }
        initCacheIfNeeded(world);
        return cacheGas.contains(input, r -> {
            boolean boo = cacheFluid.isEmpty(inputFluid) || r.getInputFluid().testType(inputFluid);
            int i = 0;
            while (boo && i < 25) {
                boo &= cacheItems[i].isEmpty(inputItems[i]) || r.getInputItem(i).testType(inputItems[i]);
                i++;
            }
            return boo;
        });
    }

    @Nullable
    public AstralCraftingRecipe findFirstRecipe(@Nullable Level world, ItemStack[] inputItems, FluidStack inputFluid,
            GasStack inputGas) {
        boolean returnNull = inputItems.length != 25 || cacheFluid.isEmpty(inputFluid);
        int i = 0;
        while (!returnNull && i < 25) {
            returnNull |= cacheItems[i].isEmpty(inputItems[i]);
            i++;
        }
        if (returnNull) {
            return null;
        }
        initCacheIfNeeded(world);
        AstralCraftingRecipe recipe = cacheItems[0].findFirstRecipe(inputItems[0],
                r -> r.test(inputItems, inputFluid, inputGas));
        return recipe == null ? findFirstRecipe(complexRecipes, r -> r.test(inputItems, inputFluid, inputGas)) : recipe;
    }

    @Override
    protected void initCache(List<AstralCraftingRecipe> recipes) {
        for (AstralCraftingRecipe recipe : recipes) {
            boolean toAdd = false;
            for (int i = 0; i < 25; i++) {
                boolean complexItem = cacheItems[i].mapInputs(recipe, recipe.getInputItem(i));
                if (complexItem) {
                    complexIngredientItems[i].add(recipe);
                }
                toAdd |= complexItem;
            }
            boolean complexFluid = cacheFluid.mapInputs(recipe, recipe.getInputFluid());
            boolean complexGas = cacheGas.mapInputs(recipe, recipe.getInputGas());
            if (complexFluid) {
                complexIngredientFluid.add(recipe);
            }
            if (complexGas) {
                complexIngredientGas.add(recipe);
            }
            if (toAdd || complexFluid || complexGas) {
                complexRecipes.add(recipe);
            }
        }
    }

}
