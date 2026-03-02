package astral_mekanism.block.container.normalmachine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.prefab.BEAbstractTransformer;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.world.entity.player.Inventory;

public class ContainerTransformer<BE extends BEAbstractTransformer> extends MekanismTileContainer<BE> {

    public ContainerTransformer(ContainerTypeRegistryObject<?> type, int id, Inventory inv, @NotNull BE tile) {
        super(type, id, inv, tile);
    }

    @Override
    protected int getInventoryXOffset() {
        return super.getInventoryXOffset() + 18;
    }
    
}
