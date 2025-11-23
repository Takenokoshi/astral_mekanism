package astral_mekanism.recipes.irecipe;

import astral_mekanism.recipes.recipe.GreenHouseRecipe;
import astral_mekanism.registries.AstralMekanismRecipeSerializers;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

@NothingNullByDefault

public class GreenHouseIRecipe  extends GreenHouseRecipe{

    public GreenHouseIRecipe(ResourceLocation id, ItemStackIngredient inputItem, FluidStackIngredient inputFluid,
            FloatingLong energyRequired, int duration, ItemStack outputItemA, ItemStack outputItemB) {
        super(id, inputItem, inputFluid, energyRequired, duration, outputItemA, outputItemB);
    }

    @Override
    public RecipeSerializer<GreenHouseRecipe> getSerializer() {
        return AstralMekanismRecipeSerializers.Greenhouse_recipe.get();
    }

    @Override
    public RecipeType<GreenHouseRecipe> getType() {
        return AstralMekanismRecipeTypes.Greenhouse_recipe.get();
    }
    
}
