package astral_mekanism.tag;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.AMEConstants;
import astral_mekanism.AMEProcessingData;
import astral_mekanism.registries.AMEBlocks;
import astral_mekanism.registries.AMEItems;
import astral_mekanism.registryenum.AMEProcessableMaterialType;
import astral_mekanism.registryenum.AMEProcessingItemStates;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

public class AstralMekanismItemTags extends ItemTagsProvider {

    public AstralMekanismItemTags(PackOutput output, CompletableFuture<Provider> providerFuture,
            CompletableFuture<TagLookup<Block>> tagLookupFuture,
            @Nullable ExistingFileHelper existingFileHelper) {
        super(output, providerFuture, tagLookupFuture, AMEConstants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(Provider provider) {
        addProcessingTags();
    }

    private void addProcessingTags() {
        for (AMEProcessingData pData : AMEProcessingData.values()) {
            tag(ItemTags.create(AMEConstants.rl("shining_crystals/" + pData.type.name)))
                    .add(AMEItems.AME_MATERIAL_PROCESSING_ITEMS.get(pData.type)
                            .get(AMEProcessingItemStates.SHINING_CRYSTAL).asItem());
            tag(ItemTags.create(AMEConstants.rl("shining_shards/" + pData.type.name)))
                    .add(AMEItems.AME_MATERIAL_PROCESSING_ITEMS.get(pData.type)
                            .get(AMEProcessingItemStates.SHINING_SHARD).asItem());
            tag(ItemTags.create(AMEConstants.rl("shining_dusts/" + pData.type.name)))
                    .add(AMEItems.AME_MATERIAL_PROCESSING_ITEMS.get(pData.type)
                            .get(AMEProcessingItemStates.SHINING_DUST).asItem());
            tag(ItemTags.create(AMEConstants.rl(pData.type.isMetal || pData.type == AMEProcessableMaterialType.REDSTONE
                    ? "shining_clumps/" + pData.type.name
                    : "shining_gems/" + pData.type.name)))
                    .add(AMEItems.AME_MATERIAL_PROCESSING_ITEMS.get(pData.type)
                            .get(AMEProcessingItemStates.SHINING_CLUMP_GEM).asItem());
            tag(ItemTags.create(AMEConstants.rl("compressed_ores/" + pData.type.name)))
                    .add(AMEBlocks.COMPRESSED_ORE.get(pData.type).asItem());
            tag(ItemTags.create(AMEConstants.rl("reconstructed_ores/" + pData.type.name)))
                    .add(AMEBlocks.RECONSTRUCTED_ORE.get(pData.type).asItem());
            tag(ItemTags.create(AMEConstants.rl("enriched_ores/" + pData.type.name)))
                    .add(AMEBlocks.ENRICHED_ORE.get(pData.type).asItem());
            tag(ItemTags.create(AMEConstants.rl("sparkling_ores/" + pData.type.name)))
                    .add(AMEBlocks.SPARKLING_ORE.get(pData.type).asItem());
            tag(ItemTags.create(AMEConstants.rl("final_materials/" + pData.type.name)))
                    .add(pData.finalItem.asItem());
        }
    }

}
