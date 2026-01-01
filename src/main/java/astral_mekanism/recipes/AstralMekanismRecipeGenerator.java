package astral_mekanism.recipes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import appeng.recipes.handlers.ChargerRecipe;
import appeng.recipes.handlers.InscriberProcessType;
import appeng.recipes.handlers.InscriberRecipe;
import appeng.recipes.transform.TransformRecipe;
import astral_mekanism.AstralMekanismID;
import astral_mekanism.botanypots.FakeRandom;
import astral_mekanism.recipes.irecipe.CompressingAMIRecipe;
import astral_mekanism.recipes.irecipe.DissolutionAMIrecipe;
import astral_mekanism.recipes.irecipe.FormulizedSawingIRecipe;
import astral_mekanism.recipes.irecipe.GreenhouseIRecipe;
import astral_mekanism.recipes.irecipe.InjectingAMIRecipe;
import astral_mekanism.recipes.irecipe.MekanicalChagerIRecipe;
import astral_mekanism.recipes.irecipe.MekanicalInscriberIRecipe;
import astral_mekanism.recipes.irecipe.MekanicalPresserIRecipe;
import astral_mekanism.recipes.irecipe.MekanicalTransformIRecipe;
import astral_mekanism.recipes.irecipe.PurifyingAMIRecipe;
import astral_mekanism.recipes.output.ItemFluidOutput;
import astral_mekanism.recipes.output.TripleItemOutput;
import astral_mekanism.util.ItemStackUtils;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IChemicalStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.Mekanism;
import mekanism.common.recipe.impl.ChemicalDissolutionIRecipe;
import mekanism.common.recipe.impl.CompressingIRecipe;
import mekanism.common.recipe.impl.InjectingIRecipe;
import mekanism.common.recipe.impl.PressurizedReactionIRecipe;
import mekanism.common.recipe.impl.PurifyingIRecipe;
import mekanism.common.recipe.impl.SawmillIRecipe;
import mekanism.common.registries.MekanismFluids;
import net.darkhax.botanypots.data.recipes.crop.BasicCrop;
import net.darkhax.botanypots.data.recipes.soil.BasicSoil;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AstralMekanismID.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralMekanismRecipeGenerator {
    @SubscribeEvent
    public static void generateRecipe(RecipesUpdatedEvent event) {
        final FluidStackIngredient WATER = IngredientCreatorAccess.fluid()
                .from(new FluidStack(Fluids.WATER, 10000));
        final FluidStackIngredient PASTE = IngredientCreatorAccess.fluid()
                .from(MekanismFluids.NUTRITIONAL_PASTE, 100);
        IItemStackIngredientCreator itemCreator = IngredientCreatorAccess.item();
        IChemicalStackIngredientCreator<Gas, GasStack, GasStackIngredient> gasCreator = IngredientCreatorAccess.gas();

        RecipeManager manager = event.getRecipeManager();
        Collection<Recipe<?>> beforeRecipes = manager.getRecipes();
        List<Recipe<?>> forPredicate = beforeRecipes.stream()
                .filter(r -> r.getId().getNamespace().equals(AstralMekanismID.MODID)).toList();
        Predicate<ResourceLocation> idPredicate = id -> forPredicate.stream().anyMatch(r -> r.getId().equals(id));
        List<BasicSoil> basicSoils = beforeRecipes.stream()
                .filter(s -> {
                    if (s instanceof BasicSoil) {
                        return true;
                    }
                    return false;
                }).map(s -> (BasicSoil) s).toList();
        List<Recipe<?>> afterRecipes = new ArrayList<>();
        for (Recipe<?> recipe : beforeRecipes) {
            if (recipe instanceof ChargerRecipe chargerRecipe) {
                afterRecipes.add(recipe);
                ResourceLocation id = AstralMekanismID.reRL(recipe.getId(), "ae2_mekanism_styled/");
                ItemStack output = chargerRecipe.getResultItem();
                NonNullList<Ingredient> ingredients = chargerRecipe.getIngredients();
                if (idPredicate.test(id) || output.isEmpty()
                        || chargerRecipe.isSpecial() || chargerRecipe.isIncomplete() || ingredients.isEmpty()) {
                    continue;
                }
                afterRecipes.add(new MekanicalChagerIRecipe(id,
                        itemCreator.createMulti(
                                ingredients.stream().map(itemCreator::from).toArray(ItemStackIngredient[]::new)),
                        output));
            } else if (recipe instanceof InscriberRecipe inscriberRecipe) {
                afterRecipes.add(recipe);
                ResourceLocation id = AstralMekanismID.reRL(recipe.getId(), "ae2_mekanism_styled/");
                if (idPredicate.test(id)) {
                    continue;
                } else if (inscriberRecipe.getProcessType() == InscriberProcessType.INSCRIBE) {
                    Ingredient top = inscriberRecipe.getTopOptional();
                    if (!top.isEmpty() && inscriberRecipe.getBottomOptional().isEmpty()) {
                        afterRecipes.add(new MekanicalInscriberIRecipe(id,
                                itemCreator.from(inscriberRecipe.getMiddleInput()),
                                itemCreator.from(top),
                                inscriberRecipe.getResultItem()));
                    }
                } else if (inscriberRecipe.getProcessType() == InscriberProcessType.PRESS) {
                    Ingredient top = inscriberRecipe.getTopOptional();
                    Ingredient bottom = inscriberRecipe.getBottomOptional();
                    if (!top.isEmpty() && !bottom.isEmpty()) {
                        afterRecipes.add(new MekanicalPresserIRecipe(id,
                                itemCreator.from(top),
                                itemCreator.from(inscriberRecipe.getMiddleInput()),
                                itemCreator.from(bottom),
                                inscriberRecipe.getResultItem()));
                    }
                }
            } else if (recipe instanceof TransformRecipe transformRecipe && transformRecipe.circumstance.isFluid()) {
                afterRecipes.add(recipe);
                ResourceLocation id = AstralMekanismID.reRL(recipe.getId(), "ae2_mekanism_styled/");
                if (idPredicate.test(id)) {
                    continue;
                }
                ItemStackIngredient bucket = itemCreator
                        .createMulti(transformRecipe.circumstance.getFluidsForRendering().stream()
                                .map(f -> itemCreator.from(f.getBucket())).toArray(ItemStackIngredient[]::new));
                List<Ingredient> ingredients = transformRecipe.getIngredients();
                int size = ingredients.size();
                if (size == 1) {
                    afterRecipes.add(new MekanicalTransformIRecipe(id,
                            itemCreator.from(ingredients.get(0)),
                            bucket, bucket, bucket,
                            new ItemFluidOutput(transformRecipe.getResultItem(), FluidStack.EMPTY),
                            false, true, true, true));
                } else if (size == 2) {
                    afterRecipes.add(new MekanicalTransformIRecipe(id,
                            itemCreator.from(ingredients.get(0)),
                            itemCreator.from(ingredients.get(1)),
                            bucket, bucket,
                            new ItemFluidOutput(transformRecipe.getResultItem(), FluidStack.EMPTY),
                            false, false, true, true));
                } else if (size == 3) {
                    afterRecipes.add(new MekanicalTransformIRecipe(id,
                            itemCreator.from(ingredients.get(0)),
                            itemCreator.from(ingredients.get(1)),
                            itemCreator.from(ingredients.get(2)),
                            bucket,
                            new ItemFluidOutput(transformRecipe.getResultItem(), FluidStack.EMPTY),
                            false, false, false, true));
                }
            } else if (recipe instanceof PressurizedReactionIRecipe) {
                if (recipe.getId().equals(Mekanism.rl("reaction/substrate/water_ethene"))) {
                    continue;
                }
                afterRecipes.add(recipe);
            } else if (recipe instanceof SawmillIRecipe sawmillIRecipe) {
                afterRecipes.add(recipe);
                ResourceLocation id = AstralMekanismID.reRL(recipe.getId(), "formulized_sawing/");
                if (idPredicate.test(id)) {
                    continue;
                }
                double chance = sawmillIRecipe.getSecondaryChance();
                List<ItemStack> outputADef = sawmillIRecipe.getMainOutputDefinition();
                ItemStack outputA = outputADef.isEmpty() ? ItemStack.EMPTY : outputADef.get(0);
                if (chance <= 0d) {
                    afterRecipes.add(new FormulizedSawingIRecipe(id,
                            sawmillIRecipe.getInput(), outputA, ItemStack.EMPTY));
                } else {
                    List<ItemStack> outputBDef = sawmillIRecipe.getSecondaryOutputDefinition();
                    afterRecipes.add(new FormulizedSawingIRecipe(id,
                            itemCreator.createMulti(sawmillIRecipe.getInput().getRepresentations().stream()
                                    .map(stack -> itemCreator.from(
                                            stack.copyWithCount(stack.getCount() * (int) Math.ceil(1d / chance))))
                                    .toArray(ItemStackIngredient[]::new)),
                            outputA, outputBDef.isEmpty() ? ItemStack.EMPTY : outputBDef.get(0)));
                }
            } else if (recipe instanceof CompressingIRecipe compressingIRecipe) {
                afterRecipes.add(recipe);
                ResourceLocation id = AstralMekanismID.reRL(recipe.getId(), "astral_osmium_compressor/");
                if (idPredicate.test(id)) {
                    continue;
                }
                afterRecipes.add(new CompressingAMIRecipe(id, compressingIRecipe.getItemInput(),
                        gasCreator.createMulti(compressingIRecipe.getChemicalInput().getRepresentations().stream()
                                .map(stack -> gasCreator.from(new GasStack(stack, stack.getAmount() * 200)))
                                .toArray(GasStackIngredient[]::new)),
                        compressingIRecipe.getOutputDefinition().get(0)));
            } else if (recipe instanceof InjectingIRecipe injectingIRecipe) {
                afterRecipes.add(recipe);
                ResourceLocation id = AstralMekanismID.reRL(recipe.getId(),
                        "astral_chemical_injection_chamber/");
                if (idPredicate.test(id)) {
                    continue;
                }
                afterRecipes.add(new InjectingAMIRecipe(id, injectingIRecipe.getItemInput(),
                        gasCreator.createMulti(injectingIRecipe.getChemicalInput().getRepresentations().stream()
                                .map(stack -> gasCreator.from(new GasStack(stack, stack.getAmount() * 20)))
                                .toArray(GasStackIngredient[]::new)),
                        injectingIRecipe.getOutputDefinition().get(0)));
            } else if (recipe instanceof PurifyingIRecipe injectingIRecipe) {
                afterRecipes.add(recipe);
                ResourceLocation id = AstralMekanismID.reRL(recipe.getId(),
                        "astral_purifycation_chamber/");
                if (idPredicate.test(id)) {
                    continue;
                }
                afterRecipes.add(new PurifyingAMIRecipe(id, injectingIRecipe.getItemInput(),
                        gasCreator.createMulti(injectingIRecipe.getChemicalInput().getRepresentations().stream()
                                .map(stack -> gasCreator.from(new GasStack(stack, stack.getAmount() * 20)))
                                .toArray(GasStackIngredient[]::new)),
                        injectingIRecipe.getOutputDefinition().get(0)));
            } else if (recipe instanceof ChemicalDissolutionIRecipe dissolutionIRecipe) {
                afterRecipes.add(recipe);
                ResourceLocation id = AstralMekanismID.reRL(recipe.getId(), "astral_dissolution_chamber/");
                if (idPredicate.test(id)) {
                    continue;
                }
                afterRecipes.add(new DissolutionAMIrecipe(id, dissolutionIRecipe.getItemInput(),
                        gasCreator.createMulti(dissolutionIRecipe.getGasInput().getRepresentations().stream()
                                .map(g -> gasCreator.from(new GasStack(g, g.getAmount() * 100)))
                                .toArray(GasStackIngredient[]::new)),
                        dissolutionIRecipe.getOutputDefinition().get(0).getChemicalStack()));
            } else if (recipe instanceof BasicCrop basicCrop) {
                afterRecipes.add(recipe);
                ResourceLocation id1 = AstralMekanismID.reRL(recipe.getId(), "greenhouse/water/");
                ResourceLocation id2 = AstralMekanismID.reRL(recipe.getId(), "greenhouse/paste/");
                ItemStackIngredient seed = itemCreator.from(basicCrop.getSeed());
                ItemStackIngredient[] soils = basicSoils.stream()
                        .filter(s -> basicCrop.canGrowInSoil(null, null, null, s))
                        .map(s -> itemCreator.from(s.getIngredient()))
                        .toArray(ItemStackIngredient[]::new);
                List<ItemStack> outputs = basicCrop.generateDrops(new FakeRandom(), null, null, null);
                int size = outputs.size();
                if (soils.length == 0 || size == 0) {
                    continue;
                }
                ItemStackIngredient soil = itemCreator.createMulti(soils);
                TripleItemOutput output = new TripleItemOutput(outputs.get(0),
                        size > 1 ? outputs.get(size - 1) : ItemStack.EMPTY,
                        size > 2 ? outputs.get(size - 2) : ItemStack.EMPTY);
                if (!idPredicate.test(id1)) {
                    afterRecipes.add(new GreenhouseIRecipe(id1, seed, soil, WATER, output));
                }
                if (!idPredicate.test(id2)) {
                    afterRecipes.add(new GreenhouseIRecipe(id2, seed, soil, PASTE,
                            ItemStackUtils.copyWithMultiply(output, 3)));
                }
            } else {
                afterRecipes.add(recipe);
            }
        }
        manager.replaceRecipes(afterRecipes);
    }
}