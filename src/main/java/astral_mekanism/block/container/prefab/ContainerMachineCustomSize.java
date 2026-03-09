package astral_mekanism.block.container.prefab;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.interf.IHasCustomSizeContainer;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.entity.player.Inventory;

public class ContainerMachineCustomSize<BE extends TileEntityMekanism & IHasCustomSizeContainer>
        extends MekanismTileContainer<BE> {

    public ContainerMachineCustomSize(ContainerTypeRegistryObject<?> type, int id, Inventory inv, @NotNull BE tile) {
        super(type, id, inv, tile);
    }

    @Override
    protected int getInventoryXOffset() {
        return tile.getInventoryXOffset();
    }

    @Override
    protected int getInventoryYOffset() {
        return tile.getInventoryYOffset();
    }

}
