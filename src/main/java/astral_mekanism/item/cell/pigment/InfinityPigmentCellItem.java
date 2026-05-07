package astral_mekanism.item.cell.pigment;

import appeng.items.AEBaseItem;
import net.minecraft.world.item.ItemStack;

public class InfinityPigmentCellItem extends AEBaseItem {

    public InfinityPigmentCellItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
    
}
