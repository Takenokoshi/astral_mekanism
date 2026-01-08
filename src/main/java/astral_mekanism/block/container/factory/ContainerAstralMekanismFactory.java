package astral_mekanism.block.container.factory;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.base.IAstralMekanismFactory;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.entity.player.Inventory;

public class ContainerAstralMekanismFactory<BE extends TileEntityMekanism & IAstralMekanismFactory<BE>>
        extends MekanismTileContainer<BE> {

    public ContainerAstralMekanismFactory(ContainerTypeRegistryObject<?> type, int id, Inventory inv,
            @NotNull BE tile) {
        super(type, id, inv, tile);
    }

    @Override
    protected int getInventoryYOffset() {
        return tile.getPageHeight() - 78;
    }

    @Override
    protected int getInventoryXOffset() {
        return tile.getSideSpaceWidth() + 71;
    }

}
