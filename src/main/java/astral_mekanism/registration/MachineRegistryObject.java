package astral_mekanism.registration;

import mekanism.api.providers.IBlockProvider;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.registration.INamedEntry;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class MachineRegistryObject<BE extends TileEntityMekanism, BLOCKTYPE extends BlockTypeTile<BE>, BLOCK extends BlockTileModel<BE, BLOCKTYPE>, CONTAINER extends MekanismTileContainer<BE>, ITEM extends ItemBlockMachine>
        implements INamedEntry, IBlockProvider {
    private final BlockRegistryObject<BLOCK, ITEM> block;
    private final TileEntityTypeRegistryObject<BE> tile;
    private final ExtendedContainerRegistryObject<CONTAINER> container;

    public MachineRegistryObject(BlockRegistryObject<BLOCK, ITEM> block, TileEntityTypeRegistryObject<BE> tile,
            ExtendedContainerRegistryObject<CONTAINER> container) {
        this.block = block;
        this.tile = tile;
        this.container = container;
    }

    public BlockRegistryObject<BLOCK, ITEM> getBlockRO() {
        return block;
    };

    public TileEntityTypeRegistryObject<BE> getTileRO() {
        return tile;
    };

    public ContainerTypeRegistryObject<CONTAINER> getContainerRO() {
        return (ContainerTypeRegistryObject<CONTAINER>) container;
    }

    @Override
    public Item asItem() {
        return block.asItem();
    }

    @Override
    public Block getBlock() {
        return block.getBlock();
    }

    @Override
    public String getInternalRegistryName() {
        return block.getInternalRegistryName();
    }
}
