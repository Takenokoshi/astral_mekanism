package astral_mekanism.recipes.irecipe;

import astral_mekanism.recipes.output.ItemFluidOutput;
import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
import astral_mekanism.registries.AstralMekanismRecipeSerializers;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class MekanicalTransformIRecipe extends MekanicalTransformRecipe {

    public MekanicalTransformIRecipe(ResourceLocation id, ItemStackIngredient inputItemA,
            ItemStackIngredient inputItemB, ItemStackIngredient inputItemC, FluidStackIngredient inputFluidA,
            FluidStackIngredient inputFluidB, ItemFluidOutput output, boolean itemAIsCatalyst, boolean itemBIsCatalyst,
            boolean itemCIsCatalyst, boolean fluidAIsCatalyst, boolean fluidBIsCatalyst) {
        super(id, inputItemA, inputItemB, inputItemC, inputFluidA, inputFluidB, output, itemAIsCatalyst,
                itemBIsCatalyst,
                itemCIsCatalyst, fluidAIsCatalyst, fluidBIsCatalyst);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AstralMekanismRecipeSerializers.MEKANICAL_TRANSFORM.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AstralMekanismRecipeTypes.MEKANICAL_TRAMSFORM.get();
    }

}
