package astral_mekanism.item.recipecard;

import appeng.items.contents.CellConfig;
import appeng.util.ConfigInventory;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import net.minecraft.world.item.ItemStack;

public class GasIngredientCardItem extends ChemicalIngredientCardItem {

    public GasIngredientCardItem(Properties properties) {
        super(properties);
    }

    public ConfigInventory getConfigInventory(ItemStack is) {
        return CellConfig.create(
                key -> key instanceof MekanismKey mekanismKey && mekanismKey.getForm() == MekanismKey.GAS, is, 1);
    }

    public MekanismKey getKey(ItemStack is) {
        ConfigInventory inv = getConfigInventory(is);
        if (inv == null || inv.isEmpty()) {
            return null;
        }
        return inv.getKey(0) instanceof MekanismKey mekanismKey && mekanismKey.getForm() == MekanismKey.GAS
                ? mekanismKey
                : null;
    }
}
