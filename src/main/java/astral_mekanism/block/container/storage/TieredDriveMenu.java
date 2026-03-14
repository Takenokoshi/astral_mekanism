package astral_mekanism.block.container.storage;

import appeng.menu.AEBaseMenu;
import appeng.menu.SlotSemantics;
import appeng.menu.slot.RestrictedInputSlot;
import appeng.menu.slot.RestrictedInputSlot.PlacableItemType;
import astral_mekanism.block.blockentity.storage.TieredDriveBlockEntities.TieredDriveBlockEntity;
import astral_mekanism.registries.AstralMekanismMenus;
import net.minecraft.world.entity.player.Inventory;

public class TieredDriveMenu extends AEBaseMenu {
    public TieredDriveMenu(int id, Inventory playerInventory, TieredDriveBlockEntity drive) {
        super(AstralMekanismMenus.DRIVES.get(drive.getTier()).get(), id, playerInventory, drive);

        for (int i = 0; i < drive.getCellCount(); ++i) {
            this.addSlot(new RestrictedInputSlot(PlacableItemType.STORAGE_CELLS, drive.getInternalInventory(), i),
                    SlotSemantics.STORAGE_CELL);
        }
        createPlayerInventorySlots(playerInventory);
    }

}
