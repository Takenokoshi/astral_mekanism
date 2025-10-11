package astral_mekanism.registration;

import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.state.BlockState;

public class RegistrationInterfaces {
    @FunctionalInterface
    public interface BlockEntityConstructor<BE extends TileEntityMekanism, BLOCKTYPE extends BlockTypeTile<BE>, BLOCK extends BlockTileModel<BE, BLOCKTYPE>> {
        public BE create(
                BlockRegistryObject<BLOCK, ? extends ItemBlockMachine> registryObject,
                BlockPos pos, BlockState state);
    }

    @FunctionalInterface
    public interface ContainerConstructor<TILE extends TileEntityMekanism, CONTAINER extends MekanismTileContainer<TILE>> {
        CONTAINER create(ContainerTypeRegistryObject<CONTAINER> type, int id, Inventory inv, TILE tile);
    }
}
