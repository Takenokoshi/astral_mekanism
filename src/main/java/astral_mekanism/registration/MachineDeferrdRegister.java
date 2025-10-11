package astral_mekanism.registration;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import astral_mekanism.registration.RegistrationInterfaces.BlockEntityConstructor;
import astral_mekanism.registration.RegistrationInterfaces.ContainerConstructor;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;

public class MachineDeferrdRegister {
    private final String modId;
    public final BlockDeferredRegister blockRegister;
    private final TileEntityTypeDeferredRegister tileRegister;
    private final ExtendedContainerDeferredRegister containerRegister;

    public MachineDeferrdRegister(String modId) {
        this.modId = modId;
        this.blockRegister = new BlockDeferredRegister(this.modId);
        this.tileRegister = new TileEntityTypeDeferredRegister(this.modId);
        this.containerRegister = new ExtendedContainerDeferredRegister(this.modId);
    }

    public <BE extends TileEntityMekanism, BLOCKTYPE extends BlockTypeTile<BE>, BLOCK extends BlockTileModel<BE, BLOCKTYPE>, CONTAINER extends MekanismTileContainer<BE>, ITEM extends ItemBlockMachine> MachineRegistryObject<BE, BLOCKTYPE, BLOCK, CONTAINER, ITEM> register(
            String name, BLOCKTYPE blocktype, Function<BLOCKTYPE, BLOCK> blockCreator,
            Function<BLOCK, ITEM> itemCreator, BlockEntityConstructor<BE, BLOCKTYPE, BLOCK> beConstructor,
            Class<BE> beClass, ContainerConstructor<BE, CONTAINER> contConstructor) {
        BlockRegistryObject<BLOCK, ITEM> blockObject = blockRegister.register(name,
                () -> blockCreator.apply(blocktype),
                itemCreator);
        TileEntityTypeRegistryObject<BE> tileObject = tileRegister.register(blockObject,
                (p, s) -> beConstructor.create(blockObject, p, s), BE::tickServer, BE::tickClient);
        ExtendedContainerRegistryObject<CONTAINER> containerObject = containerRegister.register(name, beClass,
                contConstructor);
        return new MachineRegistryObject<>(blockObject, tileObject, containerObject);
    }
    public <BE extends TileEntityMekanism> MachineRegistryObject<BE, BlockTypeMachine<BE>, BlockTileModel<BE, BlockTypeMachine<BE>>, MekanismTileContainer<BE>, ItemBlockMachine> register(
            String name, BlockTypeMachine<BE> blocktype,
            Class<BE> beClass,
            BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTileModel<BE, BlockTypeMachine<BE>>> beConstructor) {
        BlockRegistryObject<BlockTileModel<BE, BlockTypeMachine<BE>>, ItemBlockMachine> blockObject = blockRegister
                .register(name,
                        () -> new BlockTileModel<>(blocktype,
                                p -> p.strength(1.5f, 6.0f).sound(SoundType.STONE).mapColor(MapColor.STONE)),
                        ItemBlockMachine::new);
        TileEntityTypeRegistryObject<BE> tileObject = tileRegister.register(blockObject,
                (p, s) -> beConstructor.create(blockObject, p, s), BE::tickServer, BE::tickClient);
        ExtendedContainerRegistryObject<MekanismTileContainer<BE>> containerObject = containerRegister.register(name,
                beClass,
                MekanismTileContainer<BE>::new);
        return new MachineRegistryObject<>(blockObject, tileObject, containerObject);
    }

    public <BE extends TileEntityMekanism> MachineRegistryObject<BE, BlockTypeTile<BE>, BlockTileModel<BE, BlockTypeTile<BE>>, MekanismTileContainer<BE>, ItemBlockMachine> register(
            String name, BlockTypeTile<BE> blocktype,
            BlockEntityConstructor<BE, BlockTypeTile<BE>, BlockTileModel<BE, BlockTypeTile<BE>>> beConstructor,
            Class<BE> beClass) {
        BlockRegistryObject<BlockTileModel<BE, BlockTypeTile<BE>>, ItemBlockMachine> blockObject = blockRegister
                .register(name,
                        () -> new BlockTileModel<>(blocktype,
                                p -> p.strength(1.5f, 6.0f).sound(SoundType.STONE).mapColor(MapColor.STONE)),
                        ItemBlockMachine::new);
        TileEntityTypeRegistryObject<BE> tileObject = tileRegister.register(blockObject,
                (p, s) -> beConstructor.create(blockObject, p, s), BE::tickServer, BE::tickClient);
        ExtendedContainerRegistryObject<MekanismTileContainer<BE>> containerObject = containerRegister.register(name,
                beClass,
                MekanismTileContainer<BE>::new);
        return new MachineRegistryObject<>(blockObject, tileObject, containerObject);
    }


    public <KEY extends Enum<KEY>, BE extends TileEntityMekanism, BLOCKTYPE extends BlockTypeTile<BE>, BLOCK extends BlockTileModel<BE, BLOCKTYPE>, CONTAINER extends MekanismTileContainer<BE>, ITEM extends ItemBlockMachine> Map<KEY, MachineRegistryObject<BE, BLOCKTYPE, BLOCK, CONTAINER, ITEM>> mapRegister(
            Class<KEY> keyClass, Function<KEY, String> nameFunction, Map<KEY, BLOCKTYPE> blocktypeMap,
            Function<BLOCKTYPE, BLOCK> blockCreater,
            Function<BLOCK, ITEM> itemCreator,
            Function<KEY, BlockEntityConstructor<BE, BLOCKTYPE, BLOCK>> beConstructorFunction,
            Class<BE> beClass, ContainerConstructor<BE, CONTAINER> contConstructor) {
        Map<KEY, MachineRegistryObject<BE, BLOCKTYPE, BLOCK, CONTAINER, ITEM>> result = new EnumMap<>(keyClass);
        for (KEY key : keyClass.getEnumConstants()) {
            result.put(key, this.register(nameFunction.apply(key), blocktypeMap.get(key), blockCreater,
                    itemCreator, beConstructorFunction.apply(key), beClass, contConstructor));
        }
        return result;
    }

    public void register(IEventBus bus) {
        blockRegister.register(bus);
        tileRegister.register(bus);
        containerRegister.register(bus);
    }
}
