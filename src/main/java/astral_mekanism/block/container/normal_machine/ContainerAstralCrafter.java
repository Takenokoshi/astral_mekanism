package astral_mekanism.block.container.normal_machine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.normalmachine.BEAstralCrafter;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.world.entity.player.Inventory;

public class ContainerAstralCrafter extends MekanismTileContainer<BEAstralCrafter> {

    public ContainerAstralCrafter(ContainerTypeRegistryObject<?> type, int id, Inventory inv,
            @NotNull BEAstralCrafter tile) {
        super(type, id, inv, tile);
    }

    @Override
    protected int getInventoryYOffset() {
        return super.getInventoryYOffset() + 36;
    }

    @Override
    protected int getInventoryXOffset() {
        return super.getInventoryXOffset() + 18;
    }

}
