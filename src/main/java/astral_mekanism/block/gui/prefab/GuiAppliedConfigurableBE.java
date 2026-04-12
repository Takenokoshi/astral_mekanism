package astral_mekanism.block.gui.prefab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import appeng.api.stacks.AEKey;
import astral_mekanism.AstralMekanism;
import astral_mekanism.block.blockentity.base.AppliedConfigurableBlockEntity;
import astral_mekanism.block.gui.element.aeKey.GuiAEKeySlot;
import astral_mekanism.network.to_server.PacketGuiAEKeySlotSet;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiElement;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedConfigurableBE<BE extends AppliedConfigurableBlockEntity, CONTAINER extends MekanismTileContainer<BE>>
        extends GuiConfigurableTile<BE, CONTAINER> {

    private final List<GuiAEKeySlot> aeKeySlots;

    protected GuiAppliedConfigurableBE(CONTAINER container, Inventory inv, Component title) {
        super(container, inv, title);
        this.aeKeySlots = new ArrayList<>();
    }

    protected <T extends GuiElement> T addRenderableWidget(T element) {
        if (element instanceof GuiAEKeySlot aeKeySlot) {
            aeKeySlots.add(aeKeySlot);
        }
        return super.addElement(element);
    }

    public void setKey(int index, AEKey key) {
        AstralMekanism.packetHandler().sendToServer(new PacketGuiAEKeySlotSet(tile.getBlockPos(), index, key));
    }

    public List<GuiAEKeySlot> getAeKeySlots(){
        return Collections.unmodifiableList(aeKeySlots);
    }

    public BE getTile(){
        return tile;
    }

}
