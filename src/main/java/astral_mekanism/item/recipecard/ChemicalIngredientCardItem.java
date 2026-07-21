package astral_mekanism.item.recipecard;

import appeng.items.contents.CellConfig;
import appeng.util.ConfigInventory;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import net.minecraft.world.item.ItemStack;

public class ChemicalIngredientCardItem extends AbstractCardItem {

    public ChemicalIngredientCardItem(Properties properties) {
        super(properties);
    }

    public ConfigInventory getConfigInventory(ItemStack is) {
        return CellConfig.create(key -> key instanceof MekanismKey, is, 1);
    }

    public MekanismKey getKey(ItemStack is) {
        ConfigInventory inv = getConfigInventory(is);
        if (inv == null || inv.isEmpty()) {
            return null;
        }
        return inv.getKey(0) instanceof MekanismKey mekanismKey
                ? mekanismKey
                : null;
    }

}
