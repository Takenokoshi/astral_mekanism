package astral_mekanism;

import java.util.function.Consumer;

import com.fxd927.mekanismelements.common.registries.MSGases;
import com.jerry.mekanism_extras.common.registry.ExtraBlock;
import com.jerry.mekanism_extras.common.registry.ExtraItem;
import com.jerry.mekanism_extras.common.resource.ore.ExtraOreType;

import appeng.core.definitions.AEItems;
import astral_mekanism.recipe.builder.ReconstructionRecipeBuilder;
import astral_mekanism.registries.AMEBlocks;
import astral_mekanism.registries.AMEFluids;
import astral_mekanism.registries.AMEGases;
import astral_mekanism.registries.AMEInfuseTypes;
import astral_mekanism.registries.AMEItems;
import astral_mekanism.registries.AMESlurries;
import astral_mekanism.registryenum.AMEProcessableMaterialType;
import astral_mekanism.registryenum.AMEProcessingItemStates;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.api.datagen.recipe.builder.ChemicalCrystallizerRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ChemicalDissolutionRecipeBuilder;
import mekanism.api.datagen.recipe.builder.FluidSlurryToSlurryRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ItemStackChemicalToItemStackRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ItemStackToItemStackRecipeBuilder;
import mekanism.api.datagen.recipe.builder.NucleosynthesizingRecipeBuilder;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.SlurryStackIngredient;
import mekanism.api.recipes.ingredients.creator.IChemicalStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IFluidStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.registries.MekanismGases;
import mekanism.common.registries.MekanismItems;
import mekanism.common.resource.PrimaryResource;
import mekanism.common.resource.ResourceType;
import mekanism.common.resource.ore.OreType;
import mekanism.generators.common.registries.GeneratorsGases;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

public enum AMEProcessingData {
    IRON(AMEProcessableMaterialType.IRON, PrimaryResource.IRON, Items.IRON_INGOT, Items.IRON_ORE),
    GOLD(AMEProcessableMaterialType.GOLD, PrimaryResource.GOLD, Items.GOLD_INGOT, Items.GOLD_ORE),
    COPPER(AMEProcessableMaterialType.COPPER, PrimaryResource.COPPER, Items.COPPER_INGOT, Items.COPPER_ORE),
    NETHERITE(AMEProcessableMaterialType.NETHERITE, MekanismItems.NETHERITE_DUST, Items.NETHERITE_INGOT,
            AMEBlocks.NETHERITE_ORE),
    OSMIUM(AMEProcessableMaterialType.OSMIUM, PrimaryResource.OSMIUM),
    TIN(AMEProcessableMaterialType.TIN, PrimaryResource.TIN),
    LEAD(AMEProcessableMaterialType.LEAD, PrimaryResource.LEAD),
    URANIUM(AMEProcessableMaterialType.URANIUM, PrimaryResource.URANIUM),
    NAQUADAH(AMEProcessableMaterialType.NAQUADAH, ExtraItem.NAQUADAH_DUST, ExtraItem.INGOT_NAQUADAH,
            ExtraBlock.ORES.get(ExtraOreType.NAQUADAH).stone()),
    COAL(AMEProcessableMaterialType.COAL, MekanismItems.COAL_DUST, Items.COAL, Items.COAL_ORE),
    DIAMOND(AMEProcessableMaterialType.DIAMOND, MekanismItems.DIAMOND_DUST, Items.DIAMOND, Items.DIAMOND_ORE),
    EMERALD(AMEProcessableMaterialType.EMERALD, MekanismItems.EMERALD_DUST, Items.EMERALD, Items.EMERALD_ORE),
    REDSTONE(AMEProcessableMaterialType.REDSTONE, Items.REDSTONE, Items.REDSTONE, Items.REDSTONE_ORE),
    LAPIS_LAZULI(AMEProcessableMaterialType.LAPIS_LAZULI, MekanismItems.LAPIS_LAZULI_DUST, Items.LAPIS_LAZULI,
            Items.LAPIS_ORE),
    QUARTZ(AMEProcessableMaterialType.QUARTZ, MekanismItems.QUARTZ_DUST, Items.QUARTZ, Items.NETHER_QUARTZ_ORE),
    FLUORITE(AMEProcessableMaterialType.FLUORITE, MekanismItems.FLUORITE_DUST, MekanismItems.FLUORITE_GEM,
            MekanismBlocks.ORES.get(OreType.FLUORITE).stoneBlock()),
    AMETHYST(AMEProcessableMaterialType.AMETHYST, AMEItems.AMETHYST_DUST, Items.AMETHYST_SHARD, AMEBlocks.AMETHYST_ORE),
    CERTUS_QUARTZ(AMEProcessableMaterialType.CERTUS_QUARTZ, AEItems.CERTUS_QUARTZ_DUST, AEItems.CERTUS_QUARTZ_CRYSTAL,
            AMEBlocks.CERTUS_QUARTS_ORE),
            ;

    public final AMEProcessableMaterialType type;
    public final ItemLike dustItem;
    public final ItemLike finalItem;
    public final ItemLike ore;

    private AMEProcessingData(AMEProcessableMaterialType type, ItemLike dustItem, ItemLike finalItem, ItemLike ore) {
        this.type = type;
        this.dustItem = dustItem;
        this.finalItem = finalItem;
        this.ore = ore;
    }

    private AMEProcessingData(AMEProcessableMaterialType type, PrimaryResource primaryResource,
            ItemLike finalItem, ItemLike ore) {
        this.type = type;
        this.dustItem = MekanismItems.PROCESSED_RESOURCES.get(ResourceType.DUST, primaryResource);
        this.finalItem = finalItem;
        this.ore = ore;
    }

    private AMEProcessingData(AMEProcessableMaterialType type, PrimaryResource primaryResource) {
        this.type = type;
        this.dustItem = MekanismItems.PROCESSED_RESOURCES.get(ResourceType.DUST, primaryResource);
        this.finalItem = MekanismItems.PROCESSED_RESOURCES.get(ResourceType.INGOT, primaryResource);
        this.ore = MekanismBlocks.ORES.get(OreType.get(primaryResource)).stoneBlock();
    }

    public static void buildRecipes(Consumer<FinishedRecipe> consumer) {
        IItemStackIngredientCreator iCreator = IngredientCreatorAccess.item();
        IChemicalStackIngredientCreator<Gas, GasStack, GasStackIngredient> gCreator = IngredientCreatorAccess.gas();
        IChemicalStackIngredientCreator<Slurry, SlurryStack, SlurryStackIngredient> sCreator = IngredientCreatorAccess
                .slurry();
        IFluidStackIngredientCreator fCreator = IngredientCreatorAccess.fluid();
        final GasStackIngredient SINGULARITY_ACID = gCreator.from(AMEGases.SINGULARITY_ACID.getStack(1));
        final FluidStackIngredient WISDOM_RIVULET = fCreator.from(AMEFluids.WISDOM_RIVULET.getFluidStack(1));
        final FluidStackIngredient REFINED_ASTRAL_ETHER = fCreator
                .from(AMEFluids.REFINED_ASTRAL_ETHER.getFluidStack(1));
        final GasStackIngredient AQUA_REGIA = gCreator.createMulti(
                gCreator.from(MSGases.AQUA_REGIA.getStack(1)),
                gCreator.from(AMEGases.AQUA_REGIA.getStack(1)));
        final GasStackIngredient NITRIC_ACID = gCreator.createMulti(
                gCreator.from(MSGases.NITRIC_ACID.getStack(1)),
                gCreator.from(AMEGases.NITRIC_ACID.getStack(1)));
        final GasStackIngredient SPARKLING_SINGULARITY_RIVULET = gCreator
                .from(AMEGases.SPARKLING_SINGULARITY_RIVULET.getStack(5));
        final GasStackIngredient ANTIMATTER = gCreator.from(MekanismGases.ANTIMATTER.getStack(5));
        final GasStackIngredient INTERSTELLER_ANTIMATTER = gCreator.from(AMEGases.INTERSTELLAR_ANTIMATTER.getStack(1));
        for (AMEProcessingData pData : values()) {
            String processingLoc = "unique_processing/" + pData.type.name;
            String starlightLoc = "starlight/" + pData.type.name;
            ItemStackIngredient feedstock = iCreator
                    .from(ItemTags.create(AMEConstants.rl("feedstocks/" + pData.type.name)));
            ReconstructionRecipeBuilder.reconstruction(feedstock,
                    REFINED_ASTRAL_ETHER,
                    INTERSTELLER_ANTIMATTER,
                    1,
                    AMEBlocks.RECONSTRUCTED_ORE.get(pData.type).getItemStack(50),
                    GeneratorsGases.FUSION_FUEL.getStack(1000000))
                    .setItemNotConsumed(false)
                    .build(consumer, AMEConstants.rl(processingLoc + "/reconstruction"));
            NucleosynthesizingRecipeBuilder.nucleosynthesizing(iCreator.createMulti(
                    feedstock,
                    iCreator.from(ItemTags.create(AMEConstants.rl("reconstructed_ores/" + pData.type.name)))),
                    ANTIMATTER,
                    AMEBlocks.ENRICHED_ORE.get(pData.type).getItemStack(25),
                    2)
                    .build(consumer, AMEConstants.rl(processingLoc + "/nucleosynthesizing"));
            ItemStackChemicalToItemStackRecipeBuilder.compressing(iCreator.createMulti(
                    feedstock,
                    iCreator.from(ItemTags.create(AMEConstants.rl("enriched_ores/" + pData.type.name)))),
                    SPARKLING_SINGULARITY_RIVULET,
                    AMEBlocks.SPARKLING_ORE.get(pData.type).getItemStack(20))
                    .build(consumer, AMEConstants.rl(processingLoc + "/compressing"));
            ChemicalDissolutionRecipeBuilder.dissolution(iCreator.createMulti(
                    feedstock,
                    iCreator.from(ItemTags.create(AMEConstants.rl("sparkling_ores/" + pData.type.name)))),
                    SINGULARITY_ACID,
                    AMESlurries.SPECIFIC_SLURRIES.get(pData.type).get().getStack(160))
                    .build(consumer, AMEConstants.rl(processingLoc + "/dissolution"));
            FluidSlurryToSlurryRecipeBuilder.washing(
                    WISDOM_RIVULET,
                    sCreator.from(AMESlurries.SPECIFIC_SLURRIES.get(pData.type), 1),
                    AMESlurries.SHINING_SLURRIES.get(pData.type).getStack(10))
                    .build(consumer, AMEConstants.rl(processingLoc + "/washing"));
            ChemicalCrystallizerRecipeBuilder.crystallizing(
                    sCreator.from(AMESlurries.SHINING_SLURRIES.get(pData.type), 100),
                    AMEItems.AME_MATERIAL_PROCESSING_ITEMS.get(pData.type)
                            .get(AMEProcessingItemStates.SHINING_CRYSTAL).getItemStack())
                    .build(consumer, AMEConstants.rl(processingLoc + "/crystallizing"));
            ItemStackChemicalToItemStackRecipeBuilder.injecting(iCreator.createMulti(
                    feedstock,
                    iCreator.from(ItemTags.create(AMEConstants.rl("shining_crystals/" + pData.type.name)))),
                    AQUA_REGIA,
                    AMEItems.AME_MATERIAL_PROCESSING_ITEMS.get(pData.type)
                            .get(AMEProcessingItemStates.SHINING_SHARD).getItemStack(10))
                    .build(consumer, AMEConstants.rl(processingLoc + "/injecting"));
            ItemStackChemicalToItemStackRecipeBuilder.purifying(iCreator.createMulti(
                    feedstock,
                    iCreator.from(ItemTags.create(AMEConstants.rl("shining_shards/" + pData.type.name)))),
                    NITRIC_ACID,
                    AMEItems.AME_MATERIAL_PROCESSING_ITEMS.get(pData.type)
                            .get(AMEProcessingItemStates.SHINING_CLUMP_GEM)
                            .getItemStack(pData.type.additionalMultiply))
                    .build(consumer, AMEConstants.rl(processingLoc + "/purifying"));
            ItemStackToItemStackRecipeBuilder.crushing(iCreator.createMulti(
                    feedstock,
                    iCreator.from(ItemTags.create(AMEConstants
                            .rl(pData.type.isMetal || pData.type == AMEProcessableMaterialType.REDSTONE
                                    ? "shining_clumps/" + pData.type.name
                                    : "shining_gems/" + pData.type.name)))),
                    AMEItems.AME_MATERIAL_PROCESSING_ITEMS.get(pData.type)
                            .get(AMEProcessingItemStates.SHINING_DUST).getItemStack(8))
                    .build(consumer, AMEConstants.rl(processingLoc + "/crushing"));
            ItemStackToItemStackRecipeBuilder.enriching(
                    iCreator.from(ItemTags.create(AMEConstants.rl("shining_dusts/" + pData.type.name))),
                    pData.dustItem.asItem().getDefaultInstance().copyWithCount(6))
                    .build(consumer, AMEConstants.rl(processingLoc + "/enriching"));
            if (!pData.type.isMetal && pData.type != AMEProcessableMaterialType.REDSTONE) {
                ItemStackToItemStackRecipeBuilder.enriching(
                        iCreator.from(ItemTags.create(AMEConstants.rl("shining_gems/" + pData.type.name))),
                        pData.finalItem.asItem().getDefaultInstance().copyWithCount(48))
                        .build(consumer, AMEConstants.rl(processingLoc + "/final"));
            }

            ReconstructionRecipeBuilder.reconstruction(
                    iCreator.from(ItemTags.create(AMEConstants.rl("final_materials/" + pData.type.name))),
                    REFINED_ASTRAL_ETHER,
                    INTERSTELLER_ANTIMATTER,
                    1,
                    ItemStack.EMPTY,
                    AMEGases.RECONSTRUCTED_MATERIALS.get(pData.type).getStack(1))
                    .setItemNotConsumed(false)
                    .build(consumer, AMEConstants.rl(starlightLoc + "/reconstructed"));
            ReconstructionRecipeBuilder.reconstruction(
                    iCreator.from(ItemTags.create(AMEConstants.rl("shining_crystals/" + pData.type.name))),
                    fCreator.from(AMEFluids.REFINED_ASTRAL_ETHER.getFluidStack(480 * pData.type.additionalMultiply)),
                    gCreator.from(AMEGases.INTERSTELLAR_ANTIMATTER.getStack(480 * pData.type.additionalMultiply)),
                    1,
                    ItemStack.EMPTY,
                    AMEGases.RECONSTRUCTED_MATERIALS.get(pData.type).getStack(480 * pData.type.additionalMultiply))
                    .setItemNotConsumed(false)
                    .build(consumer, AMEConstants.rl(starlightLoc + "/reconstructed_from_crystal"));
            ReconstructionRecipeBuilder.reconstruction(
                    iCreator.from(ItemTags.create(AMEConstants.rl("sparkling_ores/" + pData.type.name))),
                    fCreator.from(AMEFluids.REFINED_ASTRAL_ETHER.getFluidStack(7680 * pData.type.additionalMultiply)),
                    gCreator.from(AMEGases.INTERSTELLAR_ANTIMATTER.getStack(7680 * pData.type.additionalMultiply)),
                    1,
                    ItemStack.EMPTY,
                    AMEGases.RECONSTRUCTED_MATERIALS.get(pData.type).getStack(7680 * pData.type.additionalMultiply))
                    .setItemNotConsumed(false)
                    .build(consumer, AMEConstants.rl(starlightLoc + "/reconstructed_from_sparkling"));
            ReconstructionRecipeBuilder.reconstruction(
                    iCreator.from(AMEItems.SPARKLING_NOVA),
                    fCreator.from(AMEFluids.REFINED_ASTRAL_ETHER.getFluidStack(2000000000)),
                    gCreator.from(AMEGases.RECONSTRUCTED_MATERIALS.get(pData.type).getStack(1000000000000000l)),
                    4000,
                    AMEItems.STARLIGHTS.get(pData.type).getItemStack(1),
                    MSGases.BERYLLIUM.getStack(1000000000))
                    .setItemNotConsumed(false)
                    .build(consumer, AMEConstants.rl(starlightLoc + "/starlight"));
            ReconstructionRecipeBuilder.reconstruction(
                    iCreator.from(AMEItems.STARLIGHTS.get(pData.type).getItemStack(1)),
                    pData != AMEProcessingData.NETHERITE
                            ? fCreator.from(new FluidStack(Fluids.LAVA, 2000000000))
                            : fCreator.from(AMEFluids.MIXED_LAVA.getFluidStack(2000000)),
                    gCreator.from(AMEGases.ASTRAL_ETHER.getStack(100)),
                    1,
                    AMEBlocks.COMPRESSED_ORE.get(pData.type).getItemStack(64),
                    MSGases.AMERICIUM.getStack(100000))
                    .setItemNotConsumed(true)
                    .build(consumer, AMEConstants.rl(starlightLoc + "/compressed_ore"));
            ItemStackChemicalToItemStackRecipeBuilder.metallurgicInfusing(
                    iCreator.from(AMEBlocks.COMPRESSED_ORE.get(pData.type)),
                    IngredientCreatorAccess.infusion().from(AMEInfuseTypes.XP.getStack(100)),
                    new ItemStack(pData.ore, 64))
                    .build(consumer, AMEConstants.rl(starlightLoc + "/ore"));

        }
    }
}
