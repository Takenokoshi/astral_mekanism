package astral_mekanism.block.blockentity.recipemachine;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public interface IGeneralRecipeTypeProvider<RECIPE extends Recipe<?>,INPUT_CACHE extends IInputRecipeCache> {
    
    default ResourceLocation getRegistryName() {
        return new ResourceLocation(getRecipeType().toString());
    }

    public RecipeType<RECIPE> getRecipeType();
    public INPUT_CACHE getInputCache();
    public List<RECIPE> getRecipes(@Nullable Level world);
}
