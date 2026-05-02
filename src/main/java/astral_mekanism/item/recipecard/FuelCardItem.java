package astral_mekanism.item.recipecard;

import appeng.items.contents.CellConfig;
import appeng.util.ConfigInventory;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.attribute.GasAttributes.Fuel;
import net.minecraft.world.item.ItemStack;

public class FuelCardItem extends AbstractCardItem {

    public FuelCardItem(Properties properties) {
        super(properties);
    }

    public ConfigInventory getConfigInventory(ItemStack is) {
        return CellConfig.create(key -> key instanceof MekanismKey mekanismKey
                && mekanismKey.getForm() == MekanismKey.GAS
                && mekanismKey.withAmount(1) instanceof GasStack gasStack
                && gasStack.has(Fuel.class), is, 1);
    }
}
