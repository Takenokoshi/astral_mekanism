package astral_mekanism.mixin.mekanism.recipe;

import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import astral_mekanism.generalrecipe.IUnifiedRecipeType;
import astral_mekanism.generalrecipe.IUnifiedRecipeTypeProvider;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

@Mixin(value = MekanismRecipeType.class, remap = false)
public abstract class MekanismRecipeTypeMixin<RECIPE extends MekanismRecipe, INPUT_CACHE extends IInputRecipeCache>
        implements IUnifiedRecipeType<RECIPE, INPUT_CACHE>,
        IUnifiedRecipeTypeProvider<RECIPE, INPUT_CACHE> {
    @Invoker("<init>")
    public static <RECIPE extends MekanismRecipe, INPUT_CACHE extends IInputRecipeCache> MekanismRecipeType<RECIPE, INPUT_CACHE> invokeNew(
            String name, Function<MekanismRecipeType<RECIPE, INPUT_CACHE>, INPUT_CACHE> inputCacheCreator) {
        return null;
    }

    @Mutable
    @Accessor("registryName")
    public abstract void setRegistryName(ResourceLocation resourceLocation);

    @Mutable
    @Accessor("inputCache")
    public abstract void setInputCache(INPUT_CACHE inputCache);

    @Override
    public IUnifiedRecipeType<RECIPE, INPUT_CACHE> getRecipeType() {
        return (IUnifiedRecipeType<RECIPE, INPUT_CACHE>) this;
    }

    @Shadow
    @Override
    public abstract ResourceLocation getRegistryName();

    @Shadow
    @Override
    public abstract INPUT_CACHE getInputCache();

    @Shadow
    @Override
    public abstract List<RECIPE> getRecipes(@Nullable Level world);
}
