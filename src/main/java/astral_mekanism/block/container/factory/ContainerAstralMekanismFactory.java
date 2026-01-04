package astral_mekanism.block.container.factory;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.base.BlockEntityRecipeFactory;
import astral_mekanism.block.blockentity.base.FactoryGuiHelper;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.world.entity.player.Inventory;

public class ContainerAstralMekanismFactory<BE extends BlockEntityRecipeFactory<?, BE>>
        extends MekanismTileContainer<BE> {

    public ContainerAstralMekanismFactory(ContainerTypeRegistryObject<?> type, int id, Inventory inv,
            @NotNull BE tile) {
        super(type, id, inv, tile);
    }

    @Override
    protected int getInventoryYOffset() {
        return FactoryGuiHelper.getALLHeight(tile.tier, tile.getHeightPerProcess()) - 94;
    }

    @Override
    protected int getInventoryXOffset() {
        return FactoryGuiHelper.getALLWidth(tile.tier, tile.getWidthPerProcess(), tile.getSideSpaceWidth()) / 2 - 81;
    }

}
