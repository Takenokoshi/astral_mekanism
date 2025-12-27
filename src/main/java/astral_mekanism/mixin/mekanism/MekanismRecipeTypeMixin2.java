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
import appeng.recipes.handlers.InscriberProcessType;
import appeng.recipes.handlers.InscriberRecipe;
import appeng.recipes.transform.TransformRecipe;
import astral_mekanism.botanypots.FakeRandom;
import astral_mekanism.recipes.irecipe.DissolutionAMIrecipe;
import astral_mekanism.recipes.irecipe.FormulizedSawingIRecipe;
import astral_mekanism.recipes.irecipe.GreenhouseIRecipe;
import astral_mekanism.recipes.irecipe.InjectingAMIRecipe;
import astral_mekanism.recipes.irecipe.MekanicalChagerIRecipe;
import astral_mekanism.recipes.irecipe.MekanicalInscriberIRecipe;
import astral_mekanism.recipes.irecipe.MekanicalPresserIRecipe;
import astral_mekanism.recipes.irecipe.PurifyingAMIRecipe;
import astral_mekanism.recipes.irecipe.MekanicalTransformIRecipe;
import astral_mekanism.recipes.output.ItemFluidOutput;
import astral_mekanism.recipes.output.TripleItemOutput;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import astral_mekanism.util.ItemStackUtils;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ChemicalDissolutionRecipe;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.PressurizedReactionRecipe;
import mekanism.api.recipes.SawmillRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.Mekanism;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.registries.MekanismFluids;
import mekanism.common.registries.MekanismItems;
import net.darkhax.botanypots.BotanyPotHelper;
import net.darkhax.botanypots.data.recipes.crop.BasicCrop;
import net.darkhax.botanypots.data.recipes.soil.BasicSoil;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fluids.FluidStack;

@Mixin(value = MekanismRecipeType.class, remap = false)
public class MekanismRecipeTypeMixin2 {

    @SuppressWarnings("unchecked")
    @Inject(method = "getRecipesUncached", at = @At("RETURN"), cancellable = true)
    private <RECIPE extends MekanismRecipe> void injectGetRecipesUncached(
            RecipeManager recipeManager,
            RegistryAccess registryAccess,
            CallbackInfoReturnable<List<RECIPE>> cir) {

        if (DatagenModLoader.isRunningDataGen()) {
            return;
        }

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
            }

            cir.setReturnValue(recipes);
            return;
        }
        if (Objects.equals(type.getRegistryName(),
                AstralMekanismRecipeTypes.AM_INJECTING.get().getRegistryName())) {
            List<ItemStackGasToItemStackRecipe> beforeRecipes = recipeManager
                    .getAllRecipesFor(MekanismRecipeType.INJECTING.get());
            for (ItemStackGasToItemStackRecipe recipe : beforeRecipes) {
                recipes.add((RECIPE) new InjectingAMIRecipe(
                        recipe.getId(), recipe.getItemInput(), IngredientCreatorAccess.gas().createMulti(
                                recipe.getChemicalInput().getRepresentations().stream()
                                        .map(stack -> IngredientCreatorAccess.gas()
                                                .from(new GasStack(stack, stack.getAmount() * 20)))
                                        .toArray(GasStackIngredient[]::new)),
                        recipe.getOutputDefinition().get(0)));
            }
            cir.setReturnValue(recipes);
            return;
        }
        if (Objects.equals(type.getRegistryName(),
                AstralMekanismRecipeTypes.AM_PURIFYING.get().getRegistryName())) {
            List<ItemStackGasToItemStackRecipe> beforeRecipes = recipeManager
                    .getAllRecipesFor(MekanismRecipeType.PURIFYING.get());
            for (ItemStackGasToItemStackRecipe recipe : beforeRecipes) {
                recipes.add((RECIPE) new PurifyingAMIRecipe(
                        recipe.getId(), recipe.getItemInput(), IngredientCreatorAccess.gas().createMulti(
                                recipe.getChemicalInput().getRepresentations().stream()
                                        .map(stack -> IngredientCreatorAccess.gas()
                                                .from(new GasStack(stack, stack.getAmount() * 20)))
                                        .toArray(GasStackIngredient[]::new)),
                        recipe.getOutputDefinition().get(0)));
            }
            cir.setReturnValue(recipes);
            return;
        }
        if (Objects.equals(type.getRegistryName(),
                AstralMekanismRecipeTypes.AM_COMPRESSING.get().getRegistryName())) {
            List<ItemStackGasToItemStackRecipe> beforeRecipes = recipeManager
                    .getAllRecipesFor(MekanismRecipeType.COMPRESSING.get());
            for (ItemStackGasToItemStackRecipe recipe : beforeRecipes) {
                recipes.add((RECIPE) new PurifyingAMIRecipe(
                        recipe.getId(), recipe.getItemInput(), IngredientCreatorAccess.gas().createMulti(
                                recipe.getChemicalInput().getRepresentations().stream()
                                        .map(stack -> IngredientCreatorAccess.gas()
                                                .from(new GasStack(stack, stack.getAmount() * 200)))
                                        .toArray(GasStackIngredient[]::new)),
                        recipe.getOutputDefinition().get(0)));
            }
            cir.setReturnValue(recipes);
            return;
        }

        if (Objects.equals(type.getRegistryName(),
                AstralMekanismRecipeTypes.MEKANICAL_PRESSER_RECIPE.get().getRegistryName())) {
            for (InscriberRecipe recipe : recipeManager.getAllRecipesFor(AERecipeTypes.INSCRIBER)) {
                Ingredient top = recipe.getTopOptional();
                Ingredient bottom = recipe.getBottomOptional();
                if (recipe.getProcessType() == InscriberProcessType.PRESS && !top.isEmpty() && !bottom.isEmpty()) {
                    recipes.add((RECIPE) new MekanicalPresserIRecipe(recipe.getId(),
                            IngredientCreatorAccess.item().from(top),
                            IngredientCreatorAccess.item().from(recipe.getMiddleInput()),
                            IngredientCreatorAccess.item().from(bottom),
                            recipe.getResultItem()));
                }
            }
            cir.setReturnValue(recipes);
            return;
        }

        if (Objects.equals(type.getRegistryName(),
                AstralMekanismRecipeTypes.MEKANICAL_INSCRIBER_RECIPE.get().getRegistryName())) {
            for (InscriberRecipe recipe : recipeManager.getAllRecipesFor(AERecipeTypes.INSCRIBER)) {
                Ingredient top = recipe.getTopOptional();
                Ingredient bottom = recipe.getBottomOptional();
                if (recipe.getProcessType() == InscriberProcessType.INSCRIBE && !top.isEmpty() && bottom.isEmpty()) {
                    recipes.add((RECIPE) new MekanicalInscriberIRecipe(recipe.getId(),
                            IngredientCreatorAccess.item().from(recipe.getMiddleInput()),
                            IngredientCreatorAccess.item().from(top),
                            recipe.getResultItem()));
                }
            }
            cir.setReturnValue(recipes);
            return;
        }

        if (Objects.equals(type.getRegistryName(), AstralMekanismRecipeTypes.AM_DISSOLUTION.get().getRegistryName())) {
            for (ChemicalDissolutionRecipe recipe : recipeManager
                    .getAllRecipesFor(MekanismRecipeType.DISSOLUTION.get())) {
                recipes.add((RECIPE) (new DissolutionAMIrecipe(recipe.getId(),
                        recipe.getItemInput(),
                        IngredientCreatorAccess.gas().createMulti(
                                recipe.getGasInput().getRepresentations().stream()
                                        .map(stack -> IngredientCreatorAccess.gas()
                                                .from(new GasStack(stack, stack.getAmount() * 100)))
                                        .toArray(GasStackIngredient[]::new)),
                        recipe.getOutputDefinition().get(0).getChemicalStack())));
            }
            cir.setReturnValue(recipes);
            return;
        }

        if (Objects.equals(type.getRegistryName(),
                AstralMekanismRecipeTypes.GREENHOUSE_RECIPE.get().getRegistryName())) {
            List<BasicCrop> crops = recipeManager.getAllRecipesFor(BotanyPotHelper.CROP_TYPE.get()).stream()
                    .filter(c -> {
                        if (c instanceof BasicCrop) {
                            return true;
                        }
                        return false;
                    }).map(c -> (BasicCrop) c).toList();
            List<BasicSoil> soils = recipeManager.getAllRecipesFor(BotanyPotHelper.SOIL_TYPE.get()).stream()
                    .filter(s -> {
                        if (s instanceof BasicSoil) {
                            return true;
                        }
                        return false;
                    }).map(s -> (BasicSoil) s).toList();
            for (BasicCrop crop : crops) {
                ItemStackIngredient seedIngredient = IngredientCreatorAccess.item().from(crop.getSeed());
                ItemStackIngredient[] soilsoil = soils.stream()
                        .filter(s -> crop.canGrowInSoil(null, null, null, s))
                        .map(s -> IngredientCreatorAccess.item().from(s.getIngredient()))
                        .toArray(ItemStackIngredient[]::new);
                if (soilsoil.length == 0) {
                    continue;
                }
                ItemStackIngredient soilIngredient = IngredientCreatorAccess.item().createMulti(soilsoil);
                List<ItemStack> stacks = crop.generateDrops(new FakeRandom(), null, null, null);
                TripleItemOutput output = new TripleItemOutput(ItemStackUtils.copyWithMultiply(stacks.get(0), 2),
                        stacks.size() >= 2 ? stacks.get(1) : ItemStack.EMPTY,
                        stacks.size() >= 3 ? stacks.get(2) : ItemStack.EMPTY);
                recipes.add((RECIPE) new GreenhouseIRecipe(crop.getId(), seedIngredient, soilIngredient,
                        IngredientCreatorAccess.fluid().from(new FluidStack(Fluids.WATER, 10000)),
                        output));
                recipes.add((RECIPE) new GreenhouseIRecipe(
                        new ResourceLocation(crop.getId().getNamespace(), crop.getId().getPath() + "_plus"),
                        seedIngredient, soilIngredient,
                        IngredientCreatorAccess.fluid().from(MekanismFluids.NUTRITIONAL_PASTE, 100),
                        ItemStackUtils.copyWithMultiply(output, 3)));
            }
            cir.setReturnValue(recipes);
            return;
        }

        if (Objects.equals(type.getRegistryName(),
                AstralMekanismRecipeTypes.MEKANICAL_TRANSFORM.get().getRegistryName())) {
            for (TransformRecipe recipe : recipeManager.getAllRecipesFor(AERecipeTypes.TRANSFORM)) {
                if (recipe.circumstance.isFluid()) {
                    ItemStackIngredient bucket = IngredientCreatorAccess.item().createMulti(
                            recipe.circumstance.getFluidsForRendering().stream()
                                    .map(f -> IngredientCreatorAccess.item().from(f.getBucket()))
                                    .toArray(ItemStackIngredient[]::new));
                    List<Ingredient> ingredients = recipe.getIngredients();
                    int size = ingredients.size();
                    if (size == 1) {
                        ItemStackIngredient ingredient = IngredientCreatorAccess.item().from(ingredients.get(0));
                        recipes.add((RECIPE) new MekanicalTransformIRecipe(recipe.getId(),
                                ingredient, ingredient, ingredient, bucket,
                                new ItemFluidOutput(ItemStackUtils.copyWithMultiply(recipe.getResultItem(), 3),
                                        FluidStack.EMPTY),
                                false, false, false, true));
                    } else if (size == 2) {
                        recipes.add((RECIPE) new MekanicalTransformIRecipe(recipe.getId(),
                                IngredientCreatorAccess.item().from(ingredients.get(0)),
                                IngredientCreatorAccess.item().from(ingredients.get(1)),
                                bucket, bucket,
                                new ItemFluidOutput(recipe.getResultItem(), FluidStack.EMPTY),
                                false, false, true, true));
                    } else if (size == 3) {
                        recipes.add((RECIPE) new MekanicalTransformIRecipe(recipe.getId(),
                                IngredientCreatorAccess.item().from(ingredients.get(0)),
                                IngredientCreatorAccess.item().from(ingredients.get(1)),
                                IngredientCreatorAccess.item().from(ingredients.get(2)),
                                bucket,
                                new ItemFluidOutput(recipe.getResultItem(), FluidStack.EMPTY),
                                false, false, false, true));
                    }
                }
            }
            cir.setReturnValue(recipes);
            return;
        }
    }
}
