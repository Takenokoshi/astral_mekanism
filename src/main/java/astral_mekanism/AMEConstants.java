package astral_mekanism;

import java.util.function.BiPredicate;
import java.util.function.Function;

import appeng.recipes.transform.TransformRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.generators.common.registries.GeneratorsFluids;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class AMEConstants {
    public static final String MODID = "astral_mekanism";

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static ResourceLocation reRL(ResourceLocation location, String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path + location.getNamespace() + "/" + location.getPath());
    }

    public static final int Int1B = 1073741823;

    public static final int[] ZERO_EIGHT = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
    public static final int[] ZERO_24 = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
            19, 20, 21, 22, 23, 24 };

    public static final BiPredicate<TransformRecipe, FluidStack> transformFluidPredicate = (recipe,
            stack) -> recipe.circumstance.isFluid()
                    ? recipe.circumstance.getFluidsForRendering().stream()
                            .anyMatch(fluid -> fluid.isSame(stack.getFluid()))
                    : GeneratorsFluids.FUSION_FUEL.getFluidStack(1).isFluidEqual(stack);

    public static final Function<TransformRecipe, FluidStackIngredient> transformFluidExtractor = r -> r.circumstance
            .isFluid()
                    ? IngredientCreatorAccess.fluid()
                            .from(r.circumstance.getFluidsForRendering().stream()
                                    .map(fluid -> IngredientCreatorAccess.fluid().from(fluid, 1000)))
                    : IngredientCreatorAccess.fluid().from(GeneratorsFluids.FUSION_FUEL.getFluidStack(1000));

}
