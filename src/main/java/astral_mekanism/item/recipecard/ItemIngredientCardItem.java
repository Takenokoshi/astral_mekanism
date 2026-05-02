package astral_mekanism.item.recipecard;

import appeng.api.stacks.AEItemKey;
import appeng.items.contents.CellConfig;
import appeng.util.ConfigInventory;
import net.minecraft.world.item.ItemStack;

public class ItemIngredientCardItem extends AbstractCardItem {

    public ItemIngredientCardItem(Properties properties) {
        super(properties);
    }

    public ConfigInventory getConfigInventory(ItemStack is) {
        return CellConfig.create(AEItemKey::is, is, 1);
    }

    public AEItemKey getKey(ItemStack is) {
        ConfigInventory inv = getConfigInventory(is);
        if (inv == null || inv.isEmpty()) {
            return null;
        }
        return inv.getKey(0) instanceof AEItemKey itemKey ? itemKey : null;
    }
}
