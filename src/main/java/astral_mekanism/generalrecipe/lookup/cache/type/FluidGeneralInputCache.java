package astral_mekanism.generalrecipe.lookup.cache.type;

import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.common.recipe.ingredient.creator.FluidStackIngredientCreator.MultiFluidStackIngredient;
import mekanism.common.recipe.ingredient.creator.FluidStackIngredientCreator.SingleFluidStackIngredient;
import mekanism.common.recipe.ingredient.creator.FluidStackIngredientCreator.TaggedFluidStackIngredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidGeneralInputCache<RECIPE extends Recipe<?>> extends BaseGeneralInputCache<Fluid,FluidStack,FluidStackIngredient,RECIPE> {

    @Override
    public boolean mapInputs(RECIPE recipe, FluidStackIngredient inputIngredient) {
        if (inputIngredient instanceof SingleFluidStackIngredient single) {
            addInputCache(single.getInputRaw().getFluid(), recipe);
        } else if (inputIngredient instanceof TaggedFluidStackIngredient tagged) {
            for (Fluid input : tagged.getRawInput()) {
                addInputCache(input, recipe);
            }
        } else if (inputIngredient instanceof MultiFluidStackIngredient multi) {
            return mapMultiInputs(recipe, multi);
        } else {
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty(FluidStack input) {
        return input.isEmpty();
    }

    @Override
    protected Fluid createKey(FluidStack stack) {
        return stack.getFluid();
    }
    
}
