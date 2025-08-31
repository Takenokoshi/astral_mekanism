package astral_mekanism.blocks.universal_storage;

import astral_mekanism.registries.AstralMekanismContainers;
import astral_mekanism.registries.AstralMekanismItems;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.common.registration.impl.ItemRegistryObject;
import net.minecraft.world.item.Item;

public class DataCacheUniversalStorage {
	public static ContainerTypeRegistryObject<ContainerUniversalStorage> getContainerTypeRegistryObject() {
		return AstralMekanismContainers.Universal_storage;
	}

	public static ItemRegistryObject<Item> getInsertUpgrade() {
		return AstralMekanismItems.ITEM_INSERT_UPGRADE;
	}
}
