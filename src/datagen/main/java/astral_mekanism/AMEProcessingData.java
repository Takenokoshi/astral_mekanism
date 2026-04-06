package astral_mekanism;

import java.util.function.Consumer;

import com.fxd927.mekanismelements.common.registries.MSGases;
import com.jerry.mekanism_extras.common.registry.ExtraItem;

import appeng.core.definitions.AEItems;
import astral_mekanism.registries.AstralMekanismFluids;
import astral_mekanism.registries.AstralMekanismGases;
import astral_mekanism.registries.AstralMekanismItems;
import astral_mekanism.registries.AstralMekanismSlurries;
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
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.SlurryStackIngredient;
import mekanism.api.recipes.ingredients.creator.IChemicalStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IFluidStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.registries.MekanismItems;
import mekanism.common.resource.PrimaryResource;
import mekanism.common.resource.ResourceType;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public enum AMEProcessingData {
    IRON(AMEProcessableMaterialType.IRON, PrimaryResource.IRON, Items.IRON_INGOT),
    GOLD(AMEProcessableMaterialType.GOLD, PrimaryResource.GOLD, Items.GOLD_INGOT),
    COPPER(AMEProcessableMaterialType.COPPER, PrimaryResource.COPPER, Items.COPPER_INGOT),
    NETHERITE(AMEProcessableMaterialType.NETHERITE, MekanismItems.NETHERITE_DUST, Items.NETHERITE_INGOT),
    OSMIUM(AMEProcessableMaterialType.OSMIUM, PrimaryResource.OSMIUM),
    TIN(AMEProcessableMaterialType.TIN, PrimaryResource.TIN),
    LEAD(AMEProcessableMaterialType.LEAD, PrimaryResource.LEAD),
    URANIUM(AMEProcessableMaterialType.URANIUM, PrimaryResource.URANIUM),
    NAQUADAH(AMEProcessableMaterialType.NAQUADAH, ExtraItem.NAQUADAH_DUST, ExtraItem.INGOT_NAQUADAH),
    COAL(AMEProcessableMaterialType.COAL, MekanismItems.COAL_DUST, Items.COAL),
    DIAMOND(AMEProcessableMaterialType.DIAMOND, MekanismItems.DIAMOND_DUST, Items.DIAMOND),
    EMERALD(AMEProcessableMaterialType.EMERALD, MekanismItems.EMERALD_DUST, Items.EMERALD),
    REDSTONE(AMEProcessableMaterialType.REDSTONE, Items.REDSTONE, Items.REDSTONE),
    LAPIS_LAZULI(AMEProcessableMaterialType.LAPIS_LAZULI, MekanismItems.LAPIS_LAZULI_DUST, Items.LAPIS_LAZULI),
    QUARTZ(AMEProcessableMaterialType.QUARTZ, MekanismItems.QUARTZ_DUST, Items.QUARTZ),
    FLUORITE(AMEProcessableMaterialType.FLUORITE, MekanismItems.FLUORITE_DUST, MekanismItems.FLUORITE_GEM),
    AMETHYST(AMEProcessableMaterialType.AMETHYST, AstralMekanismItems.AMETHYST_DUST, Items.AMETHYST_SHARD),
    CERTUS_QUARTZ(AMEProcessableMaterialType.CERTUS_QUARTZ, AEItems.CERTUS_QUARTZ_DUST, AEItems.CERTUS_QUARTZ_CRYSTAL),
    ;

    public final AMEProcessableMaterialType type;
    public final ItemLike dustItem;
    public final ItemLike finalItem;

    private AMEProcessingData(AMEProcessableMaterialType type, ItemLike dustItem, ItemLike finalItem) {
        this.type = type;
        this.dustItem = dustItem;
        this.finalItem = finalItem;
    }

    private AMEProcessingData(AMEProcessableMaterialType type, PrimaryResource primaryResource,
            ItemLike finalItem) {
        this.type = type;
        this.dustItem = MekanismItems.PROCESSED_RESOURCES.get(ResourceType.DUST, primaryResource);
        this.finalItem = finalItem;
    }

    private AMEProcessingData(AMEProcessableMaterialType type, PrimaryResource primaryResource) {
        this.type = type;
        this.dustItem = MekanismItems.PROCESSED_RESOURCES.get(ResourceType.DUST, primaryResource);
        this.finalItem = MekanismItems.PROCESSED_RESOURCES.get(ResourceType.INGOT, primaryResource);
    }

    public static void buildRecipes(Consumer<FinishedRecipe> consumer) {
        IItemStackIngredientCreator iCreator = IngredientCreatorAccess.item();
        IChemicalStackIngredientCreator<Gas, GasStack, GasStackIngredient> gCreator = IngredientCreatorAccess.gas();
        IChemicalStackIngredientCreator<Slurry, SlurryStack, SlurryStackIngredient> sCreator = IngredientCreatorAccess
                .slurry();
        IFluidStackIngredientCreator fCreator = IngredientCreatorAccess.fluid();

        final GasStackIngredient SINGULARITY_ACID = gCreator.from(AstralMekanismGases.SINGULARITY_ACID.getStack(1));
        final FluidStackIngredient WISDOM_RIVULET = fCreator.from(AstralMekanismFluids.WISDOM_RIVULET.getFluidStack(1));
        final GasStackIngredient AQUA_REGIA = gCreator.createMulti(
                gCreator.from(MSGases.AQUA_REGIA.getStack(1)),
                gCreator.from(AstralMekanismGases.AQUA_REGIA.getStack(1)));
        final GasStackIngredient NITRIC_ACID = gCreator.createMulti(
                gCreator.from(MSGases.NITRIC_ACID.getStack(1)),
                gCreator.from(AstralMekanismGases.NITRIC_ACID.getStack(1)));
        for (AMEProcessingData pData : values()) {
            String loc = "unique_processing/" + pData.type.name;
            ItemStackIngredient feedstock = iCreator
                    .from(ItemTags.create(AMEConstants.rl("feedstocks/" + pData.type.name)));
            ChemicalDissolutionRecipeBuilder.dissolution(
                    feedstock,
                    SINGULARITY_ACID,
                    AstralMekanismSlurries.SPECIFIC_SLURRIES.get(pData.type).get().getStack(160))
                    .build(consumer, AMEConstants.rl(loc + "/dissolution"));
            FluidSlurryToSlurryRecipeBuilder.washing(
                    WISDOM_RIVULET,
                    sCreator.from(AstralMekanismSlurries.SPECIFIC_SLURRIES.get(pData.type), 1),
                    AstralMekanismSlurries.SHINING_SLURRIES.get(pData.type).getStack(10))
                    .build(consumer, AMEConstants.rl(loc + "/washing"));
            ChemicalCrystallizerRecipeBuilder.crystallizing(
                    sCreator.from(AstralMekanismSlurries.SHINING_SLURRIES.get(pData.type), 100),
                    AstralMekanismItems.AME_MATERIAL_PROCESSING_ITEMS.get(pData.type)
                            .get(AMEProcessingItemStates.SHINING_CRYSTAL).getItemStack())
                    .build(consumer, AMEConstants.rl(loc + "/crystallizing"));
            ItemStackChemicalToItemStackRecipeBuilder.injecting(iCreator.createMulti(
                    feedstock,
                    iCreator.from(ItemTags.create(AMEConstants.rl("shining_crystals/" + pData.type.name)))),
                    AQUA_REGIA,
                    AstralMekanismItems.AME_MATERIAL_PROCESSING_ITEMS.get(pData.type)
                            .get(AMEProcessingItemStates.SHINING_SHARD).getItemStack(10))
                    .build(consumer, AMEConstants.rl(loc + "/injecting"));
            ItemStackChemicalToItemStackRecipeBuilder.purifying(iCreator.createMulti(
                    feedstock,
                    iCreator.from(ItemTags.create(AMEConstants.rl("shining_shards/" + pData.type.name)))),
                    NITRIC_ACID,
                    AstralMekanismItems.AME_MATERIAL_PROCESSING_ITEMS.get(pData.type)
                            .get(AMEProcessingItemStates.SHINING_CLUMP_GEM)
                            .getItemStack(pData.type.additionalMultiply))
                    .build(consumer, AMEConstants.rl(loc + "/purifying"));
            ItemStackToItemStackRecipeBuilder.crushing(iCreator.createMulti(
                    feedstock,
                    iCreator.from(ItemTags.create(AMEConstants
                            .rl(pData.type.isMetal || pData.type == AMEProcessableMaterialType.REDSTONE
                                    ? "shining_clumps/" + pData.type.name
                                    : "shining_gems/" + pData.type.name)))),
                    AstralMekanismItems.AME_MATERIAL_PROCESSING_ITEMS.get(pData.type)
                            .get(AMEProcessingItemStates.SHINING_DUST).getItemStack(8))
                    .build(consumer, AMEConstants.rl(loc + "/crushing"));
            ItemStackToItemStackRecipeBuilder.enriching(
                    iCreator.from(ItemTags.create(AMEConstants.rl("shining_dusts/" + pData.type.name))),
                    pData.dustItem.asItem().getDefaultInstance().copyWithCount(6))
                    .build(consumer, AMEConstants.rl(loc + "/enriching"));
            if (!pData.type.isMetal && pData.type != AMEProcessableMaterialType.REDSTONE) {
                ItemStackToItemStackRecipeBuilder.enriching(
                        iCreator.from(ItemTags.create(AMEConstants.rl("shining_gems/" + pData.type.name))),
                        pData.finalItem.asItem().getDefaultInstance().copyWithCount(48))
                        .build(consumer, AMEConstants.rl(loc + "/final"));
            }
        }
    }
}
