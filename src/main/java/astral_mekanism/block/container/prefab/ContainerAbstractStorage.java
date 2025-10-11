package astral_mekanism.block.container.prefab;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.prefab.BEAbstractStorage;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.world.entity.player.Inventory;

public class ContainerAbstractStorage<STORAGE extends BEAbstractStorage>
        extends MekanismTileContainer<STORAGE> {

    public ContainerAbstractStorage(ContainerTypeRegistryObject<?> type, int id, Inventory inv, @NotNull STORAGE tile) {
        super(type, id, inv, tile);
    }

    @Override
    protected int getInventoryYOffset() {
        return 192;
    }

    @Override
    protected int getInventoryXOffset() {
        return 71;
    }

}
