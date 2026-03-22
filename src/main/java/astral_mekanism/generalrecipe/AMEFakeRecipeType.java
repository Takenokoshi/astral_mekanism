package astral_mekanism.generalrecipe;

import astral_mekanism.AMEConstants;
import astral_mekanism.generalrecipe.recipe.CropSoilRecipe;
import net.minecraft.world.item.crafting.RecipeType;

public class AMEFakeRecipeType {
    public static final RecipeType<CropSoilRecipe> CROP_SOIL_FLUID = RecipeType
            .simple(AMEConstants.rl("crop_soil_fluid"));
}
