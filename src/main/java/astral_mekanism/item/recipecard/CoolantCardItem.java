package astral_mekanism.item.recipecard;

import appeng.api.stacks.AEFluidKey;
import appeng.items.contents.CellConfig;
import appeng.util.ConfigInventory;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.chemical.gas.attribute.GasAttributes.CooledCoolant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;

public class CoolantCardItem extends AbstractCardItem {

    public CoolantCardItem(Properties properties) {
        super(properties);
    }

    public ConfigInventory getConfigInventory(ItemStack is) {
        return CellConfig.create(key -> {
            if (key instanceof AEFluidKey fluidKey && fluidKey.getFluid().equals(Fluids.WATER)) {
                return true;
            }
            if (key instanceof MekanismKey mekanismKey && mekanismKey.getForm() == MekanismKey.GAS) {
                return mekanismKey.getStack().has(CooledCoolant.class);
            }
            return false;
        }, is, 1);
    }
}
