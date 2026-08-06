package astral_mekanism.item;

import astral_mekanism.AMEConstants;
import astral_mekanism.registries.AMEItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class AstralMekanismItemModelProvider extends ItemModelProvider {

    public AstralMekanismItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AMEConstants.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(AMEItems.ESSENTIAL_TIER_INSTALLER.getRegistryName());
        basicItem(AMEItems.BASIC_STANDARD_TIER_INSTALLER.getRegistryName());
        basicItem(AMEItems.ADVANCED_TIER_INSTALLER.getRegistryName());
        basicItem(AMEItems.ELITE_TIER_INSTALLER.getRegistryName());
        basicItem(AMEItems.ENCHANTED_ULTIMATE_TIER_INSTALLER.getRegistryName());
        basicItem(AMEItems.ABSOLUTE_OVERCLOCKED_TIER_INSTALLER.getRegistryName());
        basicItem(AMEItems.SUPREME_QUANTUM_TIER_INSTALLER.getRegistryName());
        basicItem(AMEItems.COSMIC_DENSE_TIER_INSTALLER.getRegistryName());
        basicItem(AMEItems.INFINITE_MULTIVERSAL_TIER_INSTALLER.getRegistryName());
        basicItem(AMEItems.ASTRONOMICAL_TIER_INSTALLER.getRegistryName());
        basicItem(AMEItems.ASTRONOMICAL_MAX_TIER_INSTALLER.getRegistryName());
    }

}
