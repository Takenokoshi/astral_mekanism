package astral_mekanism.recipes.irecipe;

import astral_mekanism.registries.AstralMekanismRecipeSerializers;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.ChemicalDissolutionRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class DissolutionAMIrecipe extends ChemicalDissolutionRecipe {

    public DissolutionAMIrecipe(ResourceLocation id, ItemStackIngredient itemInput, GasStackIngredient gasInput,
            ChemicalStack<?> output) {
        super(id, itemInput, gasInput, output);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AstralMekanismRecipeSerializers.AM_DISSOLUTION.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AstralMekanismRecipeTypes.AM_DISSOLUTION.get();
    }
    
}
