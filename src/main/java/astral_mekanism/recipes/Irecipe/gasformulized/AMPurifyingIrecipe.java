package astral_mekanism.recipes.Irecipe.gasformulized;

import astral_mekanism.registries.AstralMekanismRecipeSerializers;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.common.recipe.serializer.ItemStackChemicalToItemStackRecipeSerializer.IFactory;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class AMPurifyingIrecipe extends ItemStackGasToItemStackRecipe {

    private final int gasUp;

    public AMPurifyingIrecipe(ResourceLocation id, ItemStackIngredient itemInput, GasStackIngredient gasInput,
            ItemStack output, int num) {
        super(id, itemInput, gasInput, output);
        gasUp = num % 9;
    }

    public static IFactory<Gas, GasStack, GasStackIngredient, ItemStackGasToItemStackRecipe> getFac(int num) {
        return (a, b, c, d) -> new AMPurifyingIrecipe(a, b, c, d, num);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AstralMekanismRecipeSerializers.AM_PURIFYING[gasUp].get();
    }

    @Override
    public RecipeType<?> getType() {
        return AstralMekanismRecipeTypes.AM_PURIFYING[gasUp].get();
    }

}
