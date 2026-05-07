package astral_mekanism.item.cell.pigment;

import org.jetbrains.annotations.Nullable;

import appeng.api.storage.cells.ICellHandler;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;
import net.minecraft.world.item.ItemStack;

public class InfinityPigmentCellHandler implements ICellHandler {

    @Override
    public @Nullable StorageCell getCellInventory(ItemStack arg0, @Nullable ISaveProvider arg1) {
        return isCell(arg0) ? new InfinityPigmentCellInventory() : null;
    }

    @Override
    public boolean isCell(ItemStack stack) {
        return stack.getItem() instanceof InfinityPigmentCellItem;
    }

}
