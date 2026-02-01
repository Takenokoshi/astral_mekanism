package astral_mekanism.recipe;

import java.util.function.Consumer;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.OreTypeData;
import astral_mekanism.recipe.builder.AstralMekanismRecipeBuilder.ItemCompressingRecipeBuilder;
import astral_mekanism.recipe.builder.AstralMekanismRecipeBuilder.ItemUnzippingRecipeBuilder;
import astral_mekanism.registries.AstralMekanismGases;
import astral_mekanism.registries.AstralMekanismItems;
import astral_mekanism.registries.AstralMekanismSlurries;
import astral_mekanism.registries.OreType;
import astral_mekanism.registries.AstralMekanismItems.IntermediateState;
import mekanism.api.datagen.recipe.builder.ChemicalCrystallizerRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ChemicalDissolutionRecipeBuilder;
import mekanism.api.datagen.recipe.builder.CombinerRecipeBuilder;
import mekanism.api.datagen.recipe.builder.FluidSlurryToSlurryRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ItemStackChemicalToItemStackRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ItemStackToItemStackRecipeBuilder;
import mekanism.api.datagen.recipe.builder.PressurizedReactionRecipeBuilder;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.registries.MekanismGases;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

public class AstralMekanismRecipeProvider extends RecipeProvider {

    public AstralMekanismRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        for (OreTypeData typeData : OreTypeData.values()) {
            String base = "processing/" + typeData.oreType.type;
            ChemicalDissolutionRecipeBuilder.dissolution(
                    IngredientCreatorAccess.item()
                            .from(ItemTags.create(new ResourceLocation("forge", "ores/"+ typeData.oreType.type))),
                    IngredientCreatorAccess.gas().from(AstralMekanismGases.OLEUM.getStack(1)),
                    AstralMekanismSlurries.COMPRESSED_SLURRIES.get(typeData.oreType).getDirtySlurry()
                            .getStack(1))
                    .build(consumer, AstralMekanismID.rl(base + "/dirty_compressed_slurry"));
            FluidSlurryToSlurryRecipeBuilder.washing(
                    IngredientCreatorAccess.fluid().from(new FluidStack(Fluids.WATER, 1)),
                    IngredientCreatorAccess.slurry()
                            .from(AstralMekanismSlurries.COMPRESSED_SLURRIES.get(typeData.oreType).getDirtySlurry()
                                    .getStack(0x1000000000000000L)),
                    AstralMekanismSlurries.COMPRESSED_SLURRIES.get(typeData.oreType).getCleanSlurry()
                            .getStack(1))
                    .build(consumer, AstralMekanismID.rl(base + "/clean_compressed_slurry"));
            ChemicalCrystallizerRecipeBuilder.crystallizing(
                    IngredientCreatorAccess.slurry()
                            .from(AstralMekanismSlurries.COMPRESSED_SLURRIES.get(typeData.oreType).getCleanSlurry()
                                    .getStack(0x1000000000000000L)),
                    AstralMekanismItems.COMPRESSED_PROCESSING_ITEMS.get(typeData.oreType)
                            .get(IntermediateState.CRYSTAL).getItemStack(1))
                    .build(consumer, AstralMekanismID.rl(base + "/crystal_compressed"));
            ItemStackChemicalToItemStackRecipeBuilder
                    .injecting(IngredientCreatorAccess.item()
                            .from(AstralMekanismItems.COMPRESSED_PROCESSING_ITEMS.get(typeData.oreType)
                                    .get(IntermediateState.CRYSTAL).getItemStack(1)),
                            IngredientCreatorAccess.gas().from(MekanismGases.HYDROGEN_CHLORIDE.getStack(50)),
                            AstralMekanismItems.COMPRESSED_PROCESSING_ITEMS.get(typeData.oreType)
                                    .get(IntermediateState.SHARD).getItemStack(1))
                    .build(consumer, AstralMekanismID.rl(base + "/shard_compressed_1"));
            ItemStackChemicalToItemStackRecipeBuilder
                    .injecting(IngredientCreatorAccess.item()
                            .from(AstralMekanismItems.COMPRESSED_PROCESSING_ITEMS.get(typeData.oreType)
                                    .get(IntermediateState.CRYSTAL).getItemStack(1)),
                            IngredientCreatorAccess.gas().from(AstralMekanismGases.AQUA_REGIA.getStack(200)),
                            AstralMekanismItems.COMPRESSED_PROCESSING_ITEMS.get(typeData.oreType)
                                    .get(IntermediateState.SHARD).getItemStack(2))
                    .build(consumer, AstralMekanismID.rl(base + "/shard_compressed_2"));
            ItemStackChemicalToItemStackRecipeBuilder
                    .purifying(IngredientCreatorAccess.item()
                            .from(AstralMekanismItems.COMPRESSED_PROCESSING_ITEMS.get(typeData.oreType)
                                    .get(IntermediateState.SHARD).getItemStack(1)),
                            IngredientCreatorAccess.gas().from(MekanismGases.OXYGEN.getStack(50)),
                            AstralMekanismItems.COMPRESSED_PROCESSING_ITEMS.get(typeData.oreType)
                                    .get(IntermediateState.CLUMP).getItemStack(1))
                    .build(consumer, AstralMekanismID.rl(base + "/clump_compressed_1"));
            ItemStackChemicalToItemStackRecipeBuilder
                    .purifying(IngredientCreatorAccess.item()
                            .from(AstralMekanismItems.COMPRESSED_PROCESSING_ITEMS.get(typeData.oreType)
                                    .get(IntermediateState.SHARD).getItemStack(1)),
                            IngredientCreatorAccess.gas().from(AstralMekanismGases.NITRIC_ACID.getStack(200)),
                            AstralMekanismItems.COMPRESSED_PROCESSING_ITEMS.get(typeData.oreType)
                                    .get(IntermediateState.CLUMP).getItemStack(2))
                    .build(consumer, AstralMekanismID.rl(base + "/clump_compressed_2"));
            String drtyDustOrDebris = typeData.oreType.hasMekprocessing ? "dirty_dust" : "debris";
            ItemStackToItemStackRecipeBuilder.crushing(
                    IngredientCreatorAccess.item()
                            .from(AstralMekanismItems.COMPRESSED_PROCESSING_ITEMS.get(typeData.oreType)
                                    .get(IntermediateState.CLUMP).getItemStack(1)),
                    AstralMekanismItems.COMPRESSED_PROCESSING_ITEMS.get(typeData.oreType).get(IntermediateState.CRUSHED)
                            .getItemStack(1))
                    .build(consumer, AstralMekanismID.rl(base + "/" + drtyDustOrDebris + "_compressed"));
            String dustOrRaw = typeData.oreType.hasMekprocessing ? "dust" : "raw";
            ItemStackToItemStackRecipeBuilder.enriching(
                    IngredientCreatorAccess.item()
                            .from(AstralMekanismItems.COMPRESSED_PROCESSING_ITEMS.get(typeData.oreType)
                                    .get(IntermediateState.CRUSHED).getItemStack(1)),
                    AstralMekanismItems.COMPRESSED_PROCESSING_ITEMS.get(typeData.oreType).get(IntermediateState.RAW)
                            .getItemStack(typeData.recipeMultiply))
                    .build(consumer, AstralMekanismID.rl(base + "/" + dustOrRaw + "_compressed"));
            SimpleCookingRecipeBuilder
                    .smelting(
                            Ingredient.of(AstralMekanismItems.COMPRESSED_PROCESSING_ITEMS.get(typeData.oreType)
                                    .get(IntermediateState.RAW)),
                            RecipeCategory.COMBAT,
                            AstralMekanismItems.COMPRESSED_INGOTS_GEMS.get(typeData.oreType)[19],
                            0.5f,
                            200)
                    .unlockedBy("unlock", has(AstralMekanismItems.COMPRESSED_PROCESSING_ITEMS.get(typeData.oreType)
                            .get(IntermediateState.RAW)))
                    .save(consumer, AstralMekanismID.rl(base + "/compressed_" + typeData.oreType.type));
            for (int i = 0; i < 19; i++) {
                new ItemCompressingRecipeBuilder(
                        IngredientCreatorAccess.item().from(
                                AstralMekanismItems.COMPRESSED_INGOTS_GEMS.get(typeData.oreType)[i].getItemStack(64)),
                        AstralMekanismItems.COMPRESSED_INGOTS_GEMS.get(typeData.oreType)[i + 1].getItemStack(1))
                        .build(consumer, AstralMekanismID.rl(base + "/compressing_" + (i + 2)));
                new ItemUnzippingRecipeBuilder(
                        IngredientCreatorAccess.item().from(
                                AstralMekanismItems.COMPRESSED_INGOTS_GEMS.get(typeData.oreType)[i + 1]
                                        .getItemStack(1)),
                        AstralMekanismItems.COMPRESSED_INGOTS_GEMS.get(typeData.oreType)[i].getItemStack(64))
                        .build(consumer, AstralMekanismID.rl(base + "/unzipping_" + (i + 2)));
            }
            new ItemCompressingRecipeBuilder(
                    IngredientCreatorAccess.item().from(typeData.finalItem),
                    AstralMekanismItems.COMPRESSED_INGOTS_GEMS.get(typeData.oreType)[0].getItemStack(1))
                    .build(consumer, AstralMekanismID.rl(base + "/compressing_" + 1));
            new ItemUnzippingRecipeBuilder(
                    IngredientCreatorAccess.item().from(
                            AstralMekanismItems.COMPRESSED_INGOTS_GEMS.get(typeData.oreType)[0]
                                    .getItemStack(1)),
                    typeData.finalItem)
                    .build(consumer, AstralMekanismID.rl(base + "/unzipping_" + 1));

            if (typeData.oreType != OreType.NETHERITE) {
                PressurizedReactionRecipeBuilder.reaction(
                        IngredientCreatorAccess.item()
                                .from(ItemTags.create(new ResourceLocation("forge", "dusts/" + typeData.oreType.type))),
                        IngredientCreatorAccess.fluid().from(Fluids.LAVA, 1000),
                        IngredientCreatorAccess.gas().from(AstralMekanismGases.ASTRAL_ETHER.getStack(1)),
                        100,
                        AstralMekanismItems.STARLIGHTS.get(typeData.oreType).getItemStack())
                        .build(consumer, AstralMekanismID.rl(base + "/starlight"));
                String oreOrCluster = typeData == OreTypeData.AMETHYST || typeData == OreTypeData.CERTUS_QUARTZ
                        ? "cluster"
                        : "ore";
                CombinerRecipeBuilder.combining(
                        IngredientCreatorAccess.item().from(AstralMekanismItems.STARLIGHTS.get(typeData.oreType)),
                        IngredientCreatorAccess.item().from(Items.COBBLESTONE),
                        typeData.oreItem.copyWithCount(64))
                        .build(consumer, AstralMekanismID.rl(base + "/" + oreOrCluster));
            }

            if (!typeData.oreType.hasMekprocessing) {
                ChemicalDissolutionRecipeBuilder.dissolution(
                        IngredientCreatorAccess.item()
                                .from(ItemTags.create(new ResourceLocation("forge","ores/"+ typeData.oreType.type))),
                        IngredientCreatorAccess.gas().from(MekanismGases.SULFURIC_ACID.getStack(1)),
                        AstralMekanismSlurries.GEM_SLURRIES.get(typeData.oreType).getDirtySlurry()
                                .getStack(1000))
                        .build(consumer, AstralMekanismID.rl(base + "/dirty_slurry"));
                FluidSlurryToSlurryRecipeBuilder.washing(
                        IngredientCreatorAccess.fluid().from(new FluidStack(Fluids.WATER, 1)),
                        IngredientCreatorAccess.slurry()
                                .from(AstralMekanismSlurries.GEM_SLURRIES.get(typeData.oreType).getDirtySlurry()
                                        .getStack(1)),
                        AstralMekanismSlurries.GEM_SLURRIES.get(typeData.oreType).getCleanSlurry()
                                .getStack(1))
                        .build(consumer, AstralMekanismID.rl(base + "/clean_slurry"));
                ChemicalCrystallizerRecipeBuilder.crystallizing(
                        IngredientCreatorAccess.slurry()
                                .from(AstralMekanismSlurries.GEM_SLURRIES.get(typeData.oreType).getCleanSlurry()
                                        .getStack(200)),
                        AstralMekanismItems.GEM_INTERMEDIATE_ITEMS.get(typeData.oreType)
                                .get(IntermediateState.CRYSTAL).getItemStack(1))
                        .build(consumer, AstralMekanismID.rl(base + "/crystal"));
                ItemStackChemicalToItemStackRecipeBuilder
                        .injecting(IngredientCreatorAccess.item()
                                .from(AstralMekanismItems.GEM_INTERMEDIATE_ITEMS.get(typeData.oreType)
                                        .get(IntermediateState.CRYSTAL).getItemStack(1)),
                                IngredientCreatorAccess.gas().from(MekanismGases.HYDROGEN_CHLORIDE.getStack(50)),
                                AstralMekanismItems.GEM_INTERMEDIATE_ITEMS.get(typeData.oreType)
                                        .get(IntermediateState.SHARD).getItemStack(1))
                        .build(consumer, AstralMekanismID.rl(base + "/shard_1"));
                ItemStackChemicalToItemStackRecipeBuilder
                        .injecting(IngredientCreatorAccess.item()
                                .from(AstralMekanismItems.GEM_INTERMEDIATE_ITEMS.get(typeData.oreType)
                                        .get(IntermediateState.CRYSTAL).getItemStack(1)),
                                IngredientCreatorAccess.gas().from(AstralMekanismGases.AQUA_REGIA.getStack(200)),
                                AstralMekanismItems.GEM_INTERMEDIATE_ITEMS.get(typeData.oreType)
                                        .get(IntermediateState.SHARD).getItemStack(2))
                        .build(consumer, AstralMekanismID.rl(base + "/shard_2"));
                ItemStackChemicalToItemStackRecipeBuilder
                        .purifying(IngredientCreatorAccess.item()
                                .from(AstralMekanismItems.GEM_INTERMEDIATE_ITEMS.get(typeData.oreType)
                                        .get(IntermediateState.SHARD).getItemStack(1)),
                                IngredientCreatorAccess.gas().from(MekanismGases.OXYGEN.getStack(50)),
                                AstralMekanismItems.GEM_INTERMEDIATE_ITEMS.get(typeData.oreType)
                                        .get(IntermediateState.CLUMP).getItemStack(1))
                        .build(consumer, AstralMekanismID.rl(base + "/clump_1"));
                ItemStackChemicalToItemStackRecipeBuilder
                        .purifying(IngredientCreatorAccess.item()
                                .from(AstralMekanismItems.GEM_INTERMEDIATE_ITEMS.get(typeData.oreType)
                                        .get(IntermediateState.SHARD).getItemStack(1)),
                                IngredientCreatorAccess.gas().from(AstralMekanismGases.NITRIC_ACID.getStack(200)),
                                AstralMekanismItems.GEM_INTERMEDIATE_ITEMS.get(typeData.oreType)
                                        .get(IntermediateState.CLUMP).getItemStack(2))
                        .build(consumer, AstralMekanismID.rl(base + "/clump_2"));
                ItemStackToItemStackRecipeBuilder.crushing(
                        IngredientCreatorAccess.item()
                                .from(AstralMekanismItems.GEM_INTERMEDIATE_ITEMS.get(typeData.oreType)
                                        .get(IntermediateState.CLUMP).getItemStack(1)),
                        AstralMekanismItems.GEM_INTERMEDIATE_ITEMS.get(typeData.oreType).get(IntermediateState.CRUSHED)
                                .getItemStack(1))
                        .build(consumer, AstralMekanismID.rl(base + "/debris"));
                ItemStackToItemStackRecipeBuilder.enriching(
                        IngredientCreatorAccess.item()
                                .from(AstralMekanismItems.GEM_INTERMEDIATE_ITEMS.get(typeData.oreType)
                                        .get(IntermediateState.CRUSHED).getItemStack(1)),
                        AstralMekanismItems.GEM_INTERMEDIATE_ITEMS.get(typeData.oreType).get(IntermediateState.RAW)
                                .getItemStack(typeData.recipeMultiply))
                        .build(consumer, AstralMekanismID.rl(base + "/raw"));
                SimpleCookingRecipeBuilder
                        .smelting(Ingredient.of(typeData.processingTags.get(IntermediateState.RAW)),
                                RecipeCategory.COMBAT,
                                typeData.finalItem.getItem(),
                                0.5f,
                                200)
                        .unlockedBy("unlock", has(typeData.processingTags.get(IntermediateState.RAW)))
                        .save(consumer, AstralMekanismID.rl(base + "/" + typeData.oreType.type));
            }
        }
    }

}
