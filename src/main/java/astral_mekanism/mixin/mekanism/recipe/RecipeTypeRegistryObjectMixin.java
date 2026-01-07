package astral_mekanism.mixin.mekanism.recipe;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import astral_mekanism.generalrecipe.IUnifiedRecipeTypeProvider;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

@Mixin(value = RecipeTypeRegistryObject.class, remap = false)
public abstract class RecipeTypeRegistryObjectMixin<RECIPE extends MekanismRecipe, INPUT_CACHE extends IInputRecipeCache>
        implements IUnifiedRecipeTypeProvider<RECIPE, INPUT_CACHE>, IMekanismRecipeTypeProvider<RECIPE, INPUT_CACHE> {

    @SuppressWarnings("unchecked")
    @Override
    public MekanismRecipeTypeMixin2<RECIPE, INPUT_CACHE> getUnifiedRecipeType() {
        return (MekanismRecipeTypeMixin2<RECIPE, INPUT_CACHE>) (Object) ((RecipeTypeRegistryObject<RECIPE, INPUT_CACHE>) (Object) this)
                .getRecipeType();
    }

    @Override
    public INPUT_CACHE getInputCache() {
        return IMekanismRecipeTypeProvider.super.getInputCache();
    }

    @Override
    public boolean contains(@Nullable Level world, Predicate<RECIPE> matchCriteria) {
        return IMekanismRecipeTypeProvider.super.contains(world, matchCriteria);
    }

    @Override
    public ResourceLocation getRegistryName() {
        return IMekanismRecipeTypeProvider.super.getRegistryName();
    }

    @Override
    public @NotNull List<RECIPE> getRecipes(@Nullable Level world) {
        return IMekanismRecipeTypeProvider.super.getRecipes(world);
    }

    @Override
    public Stream<RECIPE> stream(@Nullable Level world) {
        return IMekanismRecipeTypeProvider.super.stream(world);
    };
}
