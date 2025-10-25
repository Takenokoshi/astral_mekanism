package astral_mekanism.mixin.mekanism;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import appeng.recipes.AERecipeTypes;
import appeng.recipes.handlers.ChargerRecipe;
import astral_mekanism.recipes.Irecipe.FormulizedSawingIRecipe;
import astral_mekanism.recipes.Irecipe.MekanicalChagerIRecipe;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.PressurizedReactionRecipe;
import mekanism.api.recipes.SawmillRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.Mekanism;
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

        MekanismRecipeType<?, ?> type = (MekanismRecipeType<?, ?>) (Object) this;
        List<RECIPE> recipes = new ArrayList<>(cir.getReturnValue());

        if (type == AstralMekanismRecipeTypes.MEKANICAL_CHARGER_RECIPE.get()) {
            for (ChargerRecipe chargerRecipe : recipeManager.getAllRecipesFor(AERecipeTypes.CHARGER)) {
                ItemStack recipeOutput = chargerRecipe.getResultItem();
                if (chargerRecipe.isSpecial() || chargerRecipe.isIncomplete() || recipeOutput.isEmpty())
                    continue;

                NonNullList<Ingredient> ingredients = chargerRecipe.getIngredients();
                if (ingredients.isEmpty())
                    continue;

                IItemStackIngredientCreator ingredientCreator = IngredientCreatorAccess.item();
                ItemStackIngredient input = ingredientCreator.from(
                        ingredients.stream().map(ingredientCreator::from));

                recipes.add((RECIPE) new MekanicalChagerIRecipe(chargerRecipe.getId(), input, recipeOutput));
            }
            cir.setReturnValue(recipes);
            return;
        }

        if (type == MekanismRecipeType.REACTION.get()) {
            List<RECIPE> results = new ArrayList<>();
            for (RECIPE recipe : recipes) {
                PressurizedReactionRecipe reaction = (PressurizedReactionRecipe) recipe;
                ItemStack result = reaction.getOutputDefinition().get(0).item();
                if (ItemStack.isSameItem(result, MekanismItems.SUBSTRATE.getItemStack()) && result.getCount() > 1) {
                    continue;
                }
                results.add(recipe);
            }
            cir.setReturnValue(results);
            return;
        }

        if (Objects.equals(type.getRegistryName(),
                AstralMekanismRecipeTypes.FORMULIZED_SAWING_RECIPE.get().getRegistryName())) {

            List<SawmillRecipe> hugv = recipeManager.getAllRecipesFor(MekanismRecipeType.SAWING.get());
            Mekanism.logger.info("" + hugv.size());
            for (SawmillRecipe sawmillRecipe : hugv) {
                double chance = sawmillRecipe.getSecondaryChance();
                List<ItemStack> outputADef = sawmillRecipe.getMainOutputDefinition();
                ItemStack outputA = outputADef.isEmpty() ? ItemStack.EMPTY : outputADef.get(0);

                if (chance == 0d) {
                    recipes.add((RECIPE) new FormulizedSawingIRecipe(
                            sawmillRecipe.getId(),
                            sawmillRecipe.getInput(),
                            outputA,
                            ItemStack.EMPTY));
                } else {
                    int multiplier = (int) Math.ceil(1d / chance);
                    List<ItemStack> outputBDef = sawmillRecipe.getSecondaryOutputDefinition();

                    ItemStackIngredient multipliedInput = IngredientCreatorAccess.item().createMulti(
                            sawmillRecipe.getInput().getRepresentations().stream()
                                    .map(stack -> IngredientCreatorAccess.item()
                                            .from(stack.copyWithCount(stack.getCount() * multiplier)))
                                    .toArray(ItemStackIngredient[]::new));

                    ItemStack outputB = outputBDef.isEmpty() ? ItemStack.EMPTY : outputBDef.get(0);
                    recipes.add((RECIPE) new FormulizedSawingIRecipe(
                            sawmillRecipe.getId(),
                            multipliedInput,
                            outputA.copyWithCount(outputA.getCount() * multiplier),
                            outputB));
                }

                Mekanism.logger.info("[AstralMek] Added sawmill recipe: {}", sawmillRecipe.getId());
            }

            cir.setReturnValue(recipes);
        }
    }
}
