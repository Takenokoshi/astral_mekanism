package astral_mekanism.block.container.prefab;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.container.slot.PagedInventoryContainerSlot;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class ContainerPagedMachine<BE extends TileEntityMekanism> extends MekanismTileContainer<BE> {

    public List<PagedInventoryContainerSlot> pagedSlots;

    public ContainerPagedMachine(ContainerTypeRegistryObject<?> type, int id, Inventory inv, @NotNull BE tile) {
        super(type, id, inv, tile);
    }

    @Override
    protected void addSlotsAndOpen() {
        pagedSlots = new ArrayList<>();
        super.addSlotsAndOpen();
    }

    @NotNull
    @Override
    protected Slot addSlot(@NotNull Slot slot) {
        if (slot instanceof PagedInventoryContainerSlot slot2) {
            slot2.setPage(0);
            pagedSlots.add(slot2);
        }
        return super.addSlot(slot);
    }

}
