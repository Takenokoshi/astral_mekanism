package astral_mekanism.generalrecipe;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public interface IUnifiedRecipeType<RECIPE extends Recipe<?>, INPUT_CACHE extends IInputRecipeCache> extends RecipeType<RECIPE> {

    ResourceLocation getRegistryName();

    INPUT_CACHE getInputCache();

    @NotNull
    List<RECIPE> getRecipes(@Nullable Level world);
}
