package astral_mekanism.mixin.mekanism.recipe;

import java.util.function.Function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import net.minecraft.resources.ResourceLocation;

@Mixin(value = MekanismRecipeType.class, remap = false)
public interface MekanismRecipeTypeMixin<RECIPE extends MekanismRecipe, INPUT_CACHE extends IInputRecipeCache> {
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
}
