package astral_mekanism.mixin.mekanism;

import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import java.util.function.Function;

@Mixin(value = MekanismRecipeType.class, remap = false)
public interface MekanismRecipeTypeMixin {
    @Invoker("<init>")
    static <MR extends MekanismRecipe, IIRC extends IInputRecipeCache> MekanismRecipeType<MR, IIRC> invokeNew(
            String name, Function<MekanismRecipeType<MR, IIRC>, IIRC> inputCacheCreator) {
        return null;
    }

    @Mutable
    @Accessor("registryName")
    void setRegistryName(ResourceLocation resourceLocation);

    @Mutable
    @Accessor("inputCache")
    <IIRC extends IInputRecipeCache> void setInputCache(IIRC inputCache);
}