package astral_mekanism.registration;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import astral_mekanism.registration.BlockTypeMachine.BlockMachineBuilder;
import astral_mekanism.registration.RegistrationInterfaces.BlockEntityConstructor;
import astral_mekanism.registration.RegistrationInterfaces.ContainerConstructor;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.text.ILangEntry;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.registration.INamedEntry;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class MachineRegistryObject<BE extends TileEntityMekanism, BLOCK extends BlockTileModel<BE, BlockTypeMachine<BE>>, CONTAINER extends MekanismTileContainer<BE>, ITEM extends ItemBlockMachine>
        implements INamedEntry, IBlockProvider {

    private final BlockRegistryObject<BLOCK, ITEM> blockRegistryObject;
    private final TileEntityTypeRegistryObject<BE> tileRegistryObject;
    private final ExtendedContainerRegistryObject<CONTAINER> containerRegistryObject;
    private final BlockTypeMachine<BE> blockType;

    public MachineRegistryObject(
            String name,
            BlockDeferredRegister blockRegister,
            TileEntityTypeDeferredRegister tileRegister,
            ExtendedContainerDeferredRegister containerRegister,
            Function<BlockTypeMachine<BE>, BLOCK> blockCreator,
            Function<BLOCK, ITEM> itemCreator,
            BlockEntityConstructor<BE, BlockTypeMachine<BE>, BLOCK> beConstructor,
            Class<BE> beClass,
            ContainerConstructor<BE, CONTAINER> contConstructor,
            ILangEntry entry,
            UnaryOperator<BlockMachineBuilder<BlockTypeMachine<BE>, BE>> operator) {
        blockType = operator.apply(BlockMachineBuilder.createMachine(this::getTile, entry))
                .withGui(this::getContainer)
                .build();
        blockRegistryObject = blockRegister.register(name, () -> blockCreator.apply(blockType), itemCreator);
        tileRegistryObject = tileRegister.register(blockRegistryObject,
                (p, s) -> beConstructor.create(blockRegistryObject, p, s),
                BE::tickServer, BE::tickClient);
        containerRegistryObject = containerRegister.register(name, beClass, contConstructor);
    }

    public BlockRegistryObject<BLOCK, ITEM> getBlockObject() {
        return blockRegistryObject;
    }

    public TileEntityTypeRegistryObject<BE> getTile() {
        return tileRegistryObject;
    }

    public ExtendedContainerRegistryObject<CONTAINER> getContainer() {
        return containerRegistryObject;
    }

    public BlockTypeMachine<BE> getBlockType(){
        return blockType;
    }

    @Override
    public Item asItem() {
        return blockRegistryObject.asItem();
    }

    @Override
    public Block getBlock() {
        return blockRegistryObject.getBlock();
    }

    @Override
    public String getInternalRegistryName() {
        return blockRegistryObject.getInternalRegistryName();
    }

}
