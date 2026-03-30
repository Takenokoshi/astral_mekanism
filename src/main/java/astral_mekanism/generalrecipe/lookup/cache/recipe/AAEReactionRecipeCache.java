package astral_mekanism.generalrecipe.lookup.cache.recipe;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import astral_mekanism.generalrecipe.IUnifiedRecipeType;
import astral_mekanism.generalrecipe.lookup.cache.type.FluidGeneralInputCache;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.pedroksl.advanced_ae.recipes.ReactionChamberRecipe;

public class AAEReactionRecipeCache extends GeneralInputRecipeCache<Container, ReactionChamberRecipe> {

    private final Set<ReactionChamberRecipe> complexRecipes;
    private final FluidGeneralInputCache<ReactionChamberRecipe> fluidInputCache;
    private final Set<ReactionChamberRecipe> emptyFluidRecipes;

    public AAEReactionRecipeCache(IUnifiedRecipeType<ReactionChamberRecipe, ?> recipeType) {
        super(recipeType);
        this.complexRecipes = new HashSet<>();
        this.fluidInputCache = new FluidGeneralInputCache<>();
        this.emptyFluidRecipes = new HashSet<>();
    }

    @Override
    public void clear() {
        super.clear();
        complexRecipes.clear();
        fluidInputCache.clear();
        emptyFluidRecipes.clear();
    }

    public boolean containsItemByIndex(Level world, ItemStack itemStack, int index) {
        if (itemStack.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return complexRecipes.stream().filter(r -> {
            if (index > r.inputs.size()) {
                return false;
            }
            return r.inputs.get(index).test(itemStack);
        }).findFirst().isPresent();
    }

    public boolean containsFluid(Level world, FluidStack fluidStack) {
        if (fluidStack.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return fluidInputCache.contains(fluidStack);
    }

    public boolean containsItemOtherByIndex(Level world, ItemStack itemStack, int index, ItemStack[] itemStacks,
            FluidStack fluidStack) {
        if (itemStack.isEmpty() || index >= itemStacks.length) {
            return false;
        }
        initCacheIfNeeded(world);
        ItemStack[] stacks = Arrays.copyOf(itemStacks, itemStacks.length);
        stacks[index] = itemStack;
        return containsItems(stacks, fluidStack);
    }

    public boolean containsFluidOther(Level world, ItemStack[] itemStacks, FluidStack fluidStack) {
        if (fluidStack.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return fluidInputCache.contains(fluidStack, getItemPredicateCanInsert(itemStacks));
    }

    private boolean containsItems(ItemStack[] itemStacks, FluidStack fluidStack) {
        Predicate<ReactionChamberRecipe> predicate = getItemPredicateCanInsert(itemStacks);
        if (!fluidStack.isEmpty()) {
            return fluidInputCache.findFirstRecipe(fluidStack, predicate) != null;
        } else {
            return complexRecipes.stream().filter(predicate).findFirst().isPresent();
        }
    }

    private static Predicate<ReactionChamberRecipe> getItemPredicateCanInsert(ItemStack[] itemStacks) {
        return r -> {
            int size = r.inputs.size();
            for (int i = 0; i < itemStacks.length; i++) {
                if (itemStacks[i].isEmpty()) {// for insert check, empty shuold be ignored.
                    continue;
                } else if (i >= size) {
                    return false;
                } else if (!r.inputs.get(i).test(itemStacks[i])) {
                    return false;
                }
            }
            return true;
        };
    }

    public ReactionChamberRecipe findFirstRecipe(Level world, ItemStack[] itemStacks, FluidStack fluidStack) {
        initCacheIfNeeded(world);
        if (fluidStack.isEmpty()) {
            return emptyFluidRecipes.stream().filter(getItemPredicateFindRecipe(itemStacks)).findFirst().orElse(null);
        } else {
            return fluidInputCache.findFirstRecipe(fluidStack, getItemPredicateFindRecipe(itemStacks));
        }
    }

    private static Predicate<ReactionChamberRecipe> getItemPredicateFindRecipe(ItemStack[] itemStacks) {
        return r -> {
            int size = r.inputs.size();
            for (int i = 0; i < itemStacks.length; i++) {
                if (i < size) {
                    if (itemStacks[i].isEmpty()) {
                        return false;
                    }
                    if (!r.inputs.get(i).test(itemStacks[i])) {
                        return false;
                    }
                } else if (!itemStacks[i].isEmpty()) {// out of recipe inputs range, input stack must be empty.
                    return false;
                }
            }
            return true;
        };
    }

    @Override
    protected void initCache(List<ReactionChamberRecipe> recipes) {
        for (ReactionChamberRecipe r : recipes) {
            complexRecipes.add(r);
            if (r.fluid.getStack().isEmpty()) {
                emptyFluidRecipes.add(r);
            } else {
                fluidInputCache.mapInputs(r, IngredientCreatorAccess.fluid().from(r.fluid.getStack()));
            }
        }
    }

}
