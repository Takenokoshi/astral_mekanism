package astral_mekanism.mixin.mekanism.recipe.lookup.cache.recipe;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;

import astral_mekanism.generalrecipe.lookup.cache.recipe.ISingleInputRecipeCache;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleItem;
import net.minecraft.world.item.ItemStack;

@Mixin(value = SingleItem.class, remap = false)
public abstract class SingleItemMixin<RECIPE extends MekanismRecipe & Predicate<ItemStack>>
        implements ISingleInputRecipeCache<ItemStack, RECIPE> {

}
