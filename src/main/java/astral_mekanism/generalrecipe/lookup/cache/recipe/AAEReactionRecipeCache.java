package astral_mekanism.generalrecipe.lookup.cache.recipe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import astral_mekanism.generalrecipe.IUnifiedRecipeType;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.pedroksl.advanced_ae.recipes.ReactionChamberRecipe;
// NOTE:
// This method assumes no duplicate ingredients in recipes.
// Does NOT validate counts, only existence.

public class AAEReactionRecipeCache extends GeneralInputRecipeCache<Container, ReactionChamberRecipe> {

    private final Map<Item, List<ReactionChamberRecipe>> itemRecipesMap;
    private final List<ReactionChamberRecipe> nonItemRecipes;
    private final Map<Fluid, List<ReactionChamberRecipe>> fluidRecipesMap;
    private final List<ReactionChamberRecipe> nonFluidRecipes;

    public AAEReactionRecipeCache(IUnifiedRecipeType<ReactionChamberRecipe, ?> recipeType) {
        super(recipeType);
        this.itemRecipesMap = new HashMap<>();
        this.nonItemRecipes = new ArrayList<>();
        this.fluidRecipesMap = new HashMap<>();
        this.nonFluidRecipes = new ArrayList<>();
    }

    @Override
    public void clear() {
        super.clear();
        itemRecipesMap.clear();
        nonItemRecipes.clear();
        fluidRecipesMap.clear();
        nonFluidRecipes.clear();
    }

    public boolean containsItem(Level world, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return itemRecipesMap.containsKey(stack.getItem());
    }

    public boolean containsFluid(Level world, FluidStack fluidStack) {
        if (fluidStack.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        return fluidRecipesMap.containsKey(fluidStack.getFluid());
    }

    public boolean containsItemOther(Level world, ItemStack itemStack, List<ItemStack> itemStacks,
            FluidStack fluidStack) {
        if (itemStack.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        if (itemRecipesMap.containsKey(itemStack.getItem())) {
            return itemRecipesMap.get(itemStack.getItem()).stream().anyMatch(r -> {
                if (!checkByFluid(r, fluidStack)) {
                    return false;
                }
                for (ItemStack stack : itemStacks) {
                    if (!checkByItem(r, stack)) {
                        return false;
                    }
                }
                return true;
            });
        }
        return false;
    }

    public boolean containsFluidOther(Level world, List<ItemStack> itemStacks,
            FluidStack fluidStack) {
        if (fluidStack.isEmpty()) {
            return false;
        }
        initCacheIfNeeded(world);
        if (fluidRecipesMap.containsKey(fluidStack.getFluid())) {
            return fluidRecipesMap.get(fluidStack.getFluid()).stream()
                    .anyMatch(r -> {
                        for (ItemStack stack : itemStacks) {
                            if (!checkByItem(r, stack)) {
                                return false;
                            }
                        }
                        return true;
                    });
        }
        return false;
    }

    public ReactionChamberRecipe findFirstRecipe(Level world, List<ItemStack> itemStacks, FluidStack fluidStack) {
        initCacheIfNeeded(world);
        if (itemStacks.isEmpty()) {
            return nonItemRecipes.stream().filter(r -> checkByFluid(r, fluidStack)).findFirst().orElse(null);
        }
        if (fluidStack.isEmpty()) {
            return nonFluidRecipes.stream().filter(r -> checkByRecipe(r, itemStacks)).findFirst().orElse(null);
        }
        if (fluidRecipesMap.containsKey(fluidStack.getFluid())) {
            return fluidRecipesMap.get(fluidStack.getFluid()).stream().filter(r -> checkByRecipe(r, itemStacks))
                    .findFirst().orElse(null);
        }
        return null;
    }

    private boolean checkByFluid(ReactionChamberRecipe r, FluidStack fluidStack) {
        if (fluidStack.isEmpty()) {
            return r.fluid.getStack().isEmpty();
        } else {
            return r.fluid.test(fluidStack);
        }
    }

    private boolean checkByItem(ReactionChamberRecipe r, ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return false;
        }
        return r.inputs.stream().filter(item -> item.getIngredient().test(itemStack)).findFirst().isPresent();
    }

    private boolean checkByRecipe(ReactionChamberRecipe r, List<ItemStack> itemStacks) {
        if (itemStacks.isEmpty()) {
            return false;
        }
        for (var item : r.inputs) {// Ingredient implements Predicate<ItemStack>
            if (!itemStacks.stream().filter(item.getIngredient()).findFirst().isPresent()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void initCache(List<ReactionChamberRecipe> recipes) {
        Map<Fluid, Set<ReactionChamberRecipe>> fluidBaseMap = new HashMap<>();
        Map<Item, Set<ReactionChamberRecipe>> itemBaseMap = new HashMap<>();
        Set<ReactionChamberRecipe> nonFluidSet = new HashSet<>();
        Set<ReactionChamberRecipe> nonItemSet = new HashSet<>();
        for (ReactionChamberRecipe r : recipes) {
            if (r.fluid.getStack().isEmpty()) {
                nonFluidSet.add(r);
            } else {
                fluidBaseMap.putIfAbsent(r.fluid.getStack().getFluid(), new HashSet<>());
                fluidBaseMap.get(r.fluid.getStack().getFluid()).add(r);
            }
            if (r.inputs.isEmpty()) {
                nonItemSet.add(r);
            } else {
                for (var item : r.inputs) {
                    for (ItemStack stack : item.getIngredient().getItems()) {
                        itemBaseMap.putIfAbsent(stack.getItem(), new HashSet<>());
                        itemBaseMap.get(stack.getItem()).add(r);
                    }
                }
            }
        }
        fluidBaseMap.forEach((fluid, set) -> {
            fluidRecipesMap.put(fluid, set.stream()
                    .sorted(Comparator.<ReactionChamberRecipe>comparingInt(r -> r.inputs.size()).reversed())
                    .toList());
        });
        itemBaseMap.forEach((item, set) -> {
            itemRecipesMap.put(item, set.stream()
                    .sorted(Comparator.<ReactionChamberRecipe>comparingLong(r -> {
                        long result = r.inputs.size();
                        if (!r.fluid.getStack().isEmpty()) {
                            result += 0x7fffffff;// 液体ありを優先
                        }
                        return result;
                    }).reversed())
                    .toList());
        });
        for (ReactionChamberRecipe reactionChamberRecipe : nonFluidSet.stream()
                .sorted(Comparator.<ReactionChamberRecipe>comparingInt(r -> r.inputs.size()).reversed()).toList()) {
            nonFluidRecipes.add(reactionChamberRecipe);
        }
        for (ReactionChamberRecipe reactionChamberRecipe : nonItemSet.stream()
                .sorted(Comparator.<ReactionChamberRecipe>comparingInt(r -> r.inputs.size()).reversed()).toList()) {
            nonItemRecipes.add(reactionChamberRecipe);
        }
    }
}
