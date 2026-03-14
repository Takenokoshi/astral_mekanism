package astral_mekanism.block.gui.storage;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.style.ScreenStyle;
import astral_mekanism.block.container.storage.TieredDriveMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class TieredDriveScreen extends AEBaseScreen<TieredDriveMenu> {

    public static final int WIDTH = 590;
    public static final int HEIGHT = 253;
    public TieredDriveScreen(TieredDriveMenu menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
        imageWidth=WIDTH;
        imageHeight=HEIGHT;
        widgets.addOpenPriorityButton();
    }
}
