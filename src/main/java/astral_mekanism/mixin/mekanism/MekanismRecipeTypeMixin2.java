package astral_mekanism.mixin.mekanism;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import appeng.recipes.AERecipeTypes;
import appeng.recipes.handlers.ChargerRecipe;
import astral_mekanism.recipes.Irecipe.MekanicalChagerIRecipe;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.PressurizedReactionRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.registries.MekanismItems;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;

@Mixin(value = MekanismRecipeType.class, remap = false)
public class MekanismRecipeTypeMixin2 {

    @SuppressWarnings("unchecked")
    @Inject(method = "getRecipesUncached", at = @At("RETURN"), cancellable = true)
    private <RECIPE extends MekanismRecipe> void injectGetRecipesUncached(
            RecipeManager recipeManager,
            RegistryAccess registryAccess,
            CallbackInfoReturnable<List<RECIPE>> cir) {
        if ((MekanismRecipeType<?, ?>) (Object) this == AstralMekanismRecipeTypes.MEKANICAL_CHARGER_RECIPE.get()) {
            List<RECIPE> recipes = cir.getReturnValue();
            recipes = new ArrayList<>(recipes);
            for (ChargerRecipe chargerRecipe : recipeManager.getAllRecipesFor(AERecipeTypes.CHARGER)) {
                ItemStack recipeOutput = chargerRecipe.getResultItem();
                if (!chargerRecipe.isSpecial() && !chargerRecipe.isIncomplete() && !recipeOutput.isEmpty()) {
                    NonNullList<Ingredient> ingredients = chargerRecipe.getIngredients();
                    ItemStackIngredient input;
                    if (ingredients.isEmpty()) {
                        continue;
                    } else {
                        IItemStackIngredientCreator ingredientCreator = IngredientCreatorAccess.item();
                        input = ingredientCreator.from(ingredients.stream().map(ingredientCreator::from));
                    }
                    recipes.add((RECIPE) new MekanicalChagerIRecipe(chargerRecipe.getId(), input, recipeOutput));
                }
            }
            cir.setReturnValue(recipes);
        } else if ((MekanismRecipeType<?, ?>) (Object) this == MekanismRecipeType.REACTION.get()) {
            List<RECIPE> recipes = cir.getReturnValue();
            for (int i = 0; i < recipes.size(); i++) {
                PressurizedReactionRecipe recipe = (PressurizedReactionRecipe) recipes.get(i);
                ItemStack result = recipe.getOutputDefinition().get(0).item();
                if (ItemStack.isSameItem(result, MekanismItems.SUBSTRATE.getItemStack()) && result.getCount() > 1) {
                    recipes.remove(i);
                }
            }
            cir.setReturnValue(recipes);
        }
    }
}
