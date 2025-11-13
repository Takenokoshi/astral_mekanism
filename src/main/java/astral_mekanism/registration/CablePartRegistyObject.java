package astral_mekanism.registration;

import appeng.api.parts.IPart;
import appeng.api.parts.IPartItem;
import appeng.api.parts.PartModels;
import appeng.items.parts.PartModelsHelper;
import mekanism.api.providers.IItemProvider;
import mekanism.common.registration.INamedEntry;
import mekanism.common.registration.impl.ItemRegistryObject;
import net.minecraft.world.item.Item;

public class CablePartRegistyObject<PART extends IPart, ITEM extends Item & IPartItem<PART>> implements INamedEntry, IItemProvider {
    private final ItemRegistryObject<ITEM> item;

    public CablePartRegistyObject(ItemRegistryObject<ITEM> item, Class<PART> partClass) {
        this.item = item;
        PartModels.registerModels(PartModelsHelper.createModels(partClass));
    }

    @Override
    public Item asItem() {
        return this.item.asItem();
    }

    @Override
    public String getInternalRegistryName() {
        return this.item.getInternalRegistryName();
    }
}
