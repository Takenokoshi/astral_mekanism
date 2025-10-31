package astral_mekanism.block.container.normal_machine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.normalmachine.BEExpandedCrafter;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.world.entity.player.Inventory;

public class ContainerExpandedCrafter extends MekanismTileContainer<BEExpandedCrafter> {

    public ContainerExpandedCrafter(ContainerTypeRegistryObject<?> type, int id, Inventory inv,
            @NotNull BEExpandedCrafter tile) {
        super(type, id, inv, tile);
    }

    @Override
    protected int getInventoryYOffset() {
        return 120;
    }

    @Override
    protected int getInventoryXOffset() {
        return 17;
    }

}
