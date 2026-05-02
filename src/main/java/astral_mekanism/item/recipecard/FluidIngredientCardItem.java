package astral_mekanism.item.recipecard;

import appeng.api.stacks.AEFluidKey;
import appeng.items.contents.CellConfig;
import appeng.util.ConfigInventory;
import net.minecraft.world.item.ItemStack;

public class FluidIngredientCardItem extends AbstractCardItem {

    public FluidIngredientCardItem(Properties properties) {
        super(properties);
    }

    public ConfigInventory getConfigInventory(ItemStack is) {
        return CellConfig.create(AEFluidKey::is, is, 1);
    }

    public AEFluidKey getKey(ItemStack is) {
        ConfigInventory inv = getConfigInventory(is);
        if (inv == null || inv.isEmpty()) {
            return null;
        }
        return inv.getKey(0) instanceof AEFluidKey itemKey ? itemKey : null;
    }
    
}
