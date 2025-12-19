package astral_mekanism.registration;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import astral_mekanism.registration.BlockTypeMachine.BlockMachineBuilder;
import astral_mekanism.registration.RegistrationInterfaces.BlockEntityConstructor;
import astral_mekanism.registration.RegistrationInterfaces.ContainerConstructor;
import mekanism.api.text.ILangEntry;
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

public class MachineDeferredRegister {
    private final String modId;
    public final BlockDeferredRegister blockRegister;
    private final TileEntityTypeDeferredRegister tileRegister;
    private final ExtendedContainerDeferredRegister containerRegister;

    public MachineDeferredRegister(String modId) {
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

    // MachineRegistryObject2
    public <BE extends TileEntityMekanism, BLOCK extends BlockTileModel<BE, BlockTypeMachine<BE>>, CONTAINER extends MekanismTileContainer<BE>, ITEM extends ItemBlockMachine> MachineRegistryObject2<BE, BLOCK, CONTAINER, ITEM> registerFull(
            String name,
            Function<BlockTypeMachine<BE>, BLOCK> blockCreator,
            Function<BLOCK, ITEM> itemCreator,
            BlockEntityConstructor<BE, BlockTypeMachine<BE>, BLOCK> beConstructor,
            Class<BE> beClass,
            ContainerConstructor<BE, CONTAINER> contConstructor,
            ILangEntry entry,
            UnaryOperator<BlockMachineBuilder<BlockTypeMachine<BE>, BE>> operator) {
        return new MachineRegistryObject2<>(name, blockRegister, tileRegister, containerRegister, blockCreator,
                itemCreator, beConstructor, beClass, contConstructor, entry, operator);
    }

    public <BE extends TileEntityMekanism, BLOCK extends BlockTileModel<BE, BlockTypeMachine<BE>>, ITEM extends ItemBlockMachine> MachineRegistryObject2<BE, BLOCK, MekanismTileContainer<BE>, ITEM> registerDefaultContainer(
            String name,
            Function<BlockTypeMachine<BE>, BLOCK> blockCreator,
            Function<BLOCK, ITEM> itemCreator,
            BlockEntityConstructor<BE, BlockTypeMachine<BE>, BLOCK> beConstructor,
            Class<BE> beClass,
            ILangEntry entry,
            UnaryOperator<BlockMachineBuilder<BlockTypeMachine<BE>, BE>> operator) {
        return registerFull(name, blockCreator, itemCreator, beConstructor, beClass, MekanismTileContainer<BE>::new,
                entry,
                operator);
    }

    public <BE extends TileEntityMekanism, BLOCK extends BlockTileModel<BE, BlockTypeMachine<BE>>, CONTAINER extends MekanismTileContainer<BE>> MachineRegistryObject2<BE, BLOCK, CONTAINER, ItemBlockMachine> registerDefaultItem(
            String name,
            Function<BlockTypeMachine<BE>, BLOCK> blockCreator,
            BlockEntityConstructor<BE, BlockTypeMachine<BE>, BLOCK> beConstructor,
            Class<BE> beClass,
            ContainerConstructor<BE, CONTAINER> contConstructor,
            ILangEntry entry,
            UnaryOperator<BlockMachineBuilder<BlockTypeMachine<BE>, BE>> operator) {
        return registerFull(name, blockCreator, ItemBlockMachine::new, beConstructor, beClass, contConstructor, entry,
                operator);
    }

    public <BE extends TileEntityMekanism, BLOCK extends BlockTileModel<BE, BlockTypeMachine<BE>>> MachineRegistryObject2<BE, BLOCK, MekanismTileContainer<BE>, ItemBlockMachine> registerDefaultContainerItem(
            String name,
            Function<BlockTypeMachine<BE>, BLOCK> blockCreator,
            BlockEntityConstructor<BE, BlockTypeMachine<BE>, BLOCK> beConstructor,
            Class<BE> beClass,
            ILangEntry entry,
            UnaryOperator<BlockMachineBuilder<BlockTypeMachine<BE>, BE>> operator) {
        return registerFull(name, blockCreator, ItemBlockMachine::new, beConstructor, beClass,
                MekanismTileContainer<BE>::new, entry, operator);
    }

    public <BE extends TileEntityMekanism, CONTAINER extends MekanismTileContainer<BE>, ITEM extends ItemBlockMachine> MachineRegistryObject2<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, CONTAINER, ITEM> registerDefaultBlock(
            String name,
            Function<BlockTypeMachine<BE>, BlockTileModel<BE, BlockTypeMachine<BE>>> blockCreator,
            Function<BlockTileModel<BE, BlockTypeMachine<BE>>, ITEM> itemCreator,
            BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTileModel<BE, BlockTypeMachine<BE>>> beConstructor,
            Class<BE> beClass,
            ContainerConstructor<BE, CONTAINER> contConstructor,
            ILangEntry entry,
            UnaryOperator<BlockMachineBuilder<BlockTypeMachine<BE>, BE>> operator) {
        return registerFull(name,
                bt -> new BlockTileModel<>(bt,
                        p -> p.strength(1.5f, 6.0f).sound(SoundType.STONE).mapColor(MapColor.STONE)),
                itemCreator, beConstructor, beClass, contConstructor, entry, operator);
    }

    public <BE extends TileEntityMekanism, ITEM extends ItemBlockMachine> MachineRegistryObject2<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, MekanismTileContainer<BE>, ITEM> registerDefaultBlockContainer(
            String name,
            Function<BlockTileModel<BE, BlockTypeMachine<BE>>, ITEM> itemCreator,
            BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTileModel<BE, BlockTypeMachine<BE>>> beConstructor,
            Class<BE> beClass,
            ILangEntry entry,
            UnaryOperator<BlockMachineBuilder<BlockTypeMachine<BE>, BE>> operator) {
        return registerFull(name,
                bt -> new BlockTileModel<>(bt,
                        p -> p.strength(1.5f, 6.0f).sound(SoundType.STONE).mapColor(MapColor.STONE)),
                itemCreator, beConstructor, beClass, MekanismTileContainer<BE>::new, entry,
                operator);
    }

    public <BE extends TileEntityMekanism, CONTAINER extends MekanismTileContainer<BE>> MachineRegistryObject2<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, CONTAINER, ItemBlockMachine> registerDefaultBlockItem(
            String name,
            BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTileModel<BE, BlockTypeMachine<BE>>> beConstructor,
            Class<BE> beClass,
            ContainerConstructor<BE, CONTAINER> contConstructor,
            ILangEntry entry,
            UnaryOperator<BlockMachineBuilder<BlockTypeMachine<BE>, BE>> operator) {
        return registerFull(name,
                bt -> new BlockTileModel<>(bt,
                        p -> p.strength(1.5f, 6.0f).sound(SoundType.STONE).mapColor(MapColor.STONE)),
                ItemBlockMachine::new, beConstructor, beClass, contConstructor, entry,
                operator);
    }

    public <BE extends TileEntityMekanism> MachineRegistryObject2<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, MekanismTileContainer<BE>, ItemBlockMachine> regSimple(
            String name,
            BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTileModel<BE, BlockTypeMachine<BE>>> beConstructor,
            Class<BE> beClass,
            ILangEntry entry,
            UnaryOperator<BlockMachineBuilder<BlockTypeMachine<BE>, BE>> operator) {
        return registerFull(name,
                bt -> new BlockTileModel<>(bt,
                        p -> p.strength(1.5f, 6.0f).sound(SoundType.STONE).mapColor(MapColor.STONE)),
                ItemBlockMachine::new, beConstructor, beClass,
                MekanismTileContainer<BE>::new, entry, operator);
    }

    public <KEY extends Enum<KEY>, BE extends TileEntityMekanism> Map<KEY, MachineRegistryObject2<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, MekanismTileContainer<BE>, ItemBlockMachine>> regSimpleMap(
            Function<KEY, String> nameFunction,
            BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTileModel<BE, BlockTypeMachine<BE>>> beConstructor,
            Class<BE> beClass,
            ILangEntry entry,
            BiFunction<KEY, BlockMachineBuilder<BlockTypeMachine<BE>, BE>, BlockMachineBuilder<BlockTypeMachine<BE>, BE>> biFunction,
            Class<KEY> keyClass) {
        Map<KEY, MachineRegistryObject2<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, MekanismTileContainer<BE>, ItemBlockMachine>> map = new EnumMap<>(
                keyClass);
        for (KEY key : keyClass.getEnumConstants()) {
            map.put(key, regSimple(nameFunction.apply(key), beConstructor, beClass, entry,
                    btm -> biFunction.apply(key, btm)));
        }
        return map;
    }

    public void register(IEventBus bus) {
        blockRegister.register(bus);
        tileRegister.register(bus);
        containerRegister.register(bus);
    }
}
