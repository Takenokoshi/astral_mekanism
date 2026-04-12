package astral_mekanism.item;

import astral_mekanism.AMEConstants;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class AstralMekanismItemModelProvider extends ItemModelProvider {

    public AstralMekanismItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AMEConstants.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
    }

}
