package astral_mekanism.item;

import java.util.List;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.OreTypeData;
import astral_mekanism.registries.AstralMekanismItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class AstralMekanismItemModelProvider extends ItemModelProvider {

    public AstralMekanismItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AstralMekanismID.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (OreTypeData typeData : OreTypeData.values()) {
            existingFileHelper.trackGenerated(typeData.resultTexture, TEXTURE);
            List.of(AstralMekanismItems.COMPRESSED_INGOTS_GEMS.get(typeData.oreType))
                    .forEach(item1 -> withExistingParent(item1.getRegistryName().getPath(), mcLoc("item/generated"))
                            .texture("layer0", typeData.resultTexture));
            AstralMekanismItems.COMPRESSED_PROCESSING_ITEMS.get(typeData.oreType).forEach(
                    (state, item2) -> {
                        existingFileHelper.trackGenerated(typeData.processingTexture.get(state), TEXTURE);
                        withExistingParent(item2.getRegistryName().getPath(), mcLoc("item/generated"))
                                .texture("layer0", typeData.processingTexture.get(state));
                    });
        }
    }

}
