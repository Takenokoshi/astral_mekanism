package astral_mekanism.tag;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.OreTypeData;
import astral_mekanism.registries.AstralMekanismItems;
import astral_mekanism.registries.OreType;
import astral_mekanism.registries.AstralMekanismItems.IntermediateState;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

public class AstralMekanismItemTags extends ItemTagsProvider {

    public AstralMekanismItemTags(PackOutput output, CompletableFuture<Provider> providerFuture,
            CompletableFuture<TagLookup<Block>> tagLookupFuture,
            @Nullable ExistingFileHelper existingFileHelper) {
        super(output, providerFuture, tagLookupFuture, AstralMekanismID.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(Provider provider) {
        for (OreTypeData typeData : OreTypeData.values()) {
            OreType type = typeData.oreType;
            if (!type.hasMekprocessing) {
                for (IntermediateState state : IntermediateState.values()) {
                    tag(typeData.processingTags.get(state))
                            .add(AstralMekanismItems.GEM_INTERMEDIATE_ITEMS.get(type).get(state).asItem());
                }
            } else if (type == OreType.NETHERITE) {
                for (IntermediateState state : IntermediateState.values()) {
                    if (state == IntermediateState.RAW) {
                        continue;
                    }
                    tag(typeData.processingTags.get(state))
                            .add(AstralMekanismItems.GEM_INTERMEDIATE_ITEMS.get(type).get(state).asItem());
                }
            }
        }
    }

}
