package astral_mekanism.registration;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import astral_mekanism.registration.BlockTypeMachine.BlockMachineBuilder;
import astral_mekanism.registration.RegistrationInterfaces.BlockEntityConstructor;
import astral_mekanism.registration.RegistrationInterfaces.ContainerConstructor;
import mekanism.api.text.ILangEntry;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;

public class MachineDeferredRegister {
    private final String modId;
    public final BlockDeferredRegister blockRegister;
    private final TileEntityTypeDeferredRegister tileRegister;
    private final ExtendedContainerDeferredRegister containerRegister;

    private final List<MachineRegistryObject<?, ?, ?, ?>> machines;

    public MachineDeferredRegister(String modId) {
        this.modId = modId;
        this.blockRegister = new BlockDeferredRegister(this.modId);
        this.tileRegister = new TileEntityTypeDeferredRegister(this.modId);
        this.containerRegister = new ExtendedContainerDeferredRegister(this.modId);
        machines = new ArrayList<MachineRegistryObject<?, ?, ?, ?>>();
    }

    public <BE extends TileEntityMekanism, BLOCK extends BlockTileModel<BE, BlockTypeMachine<BE>>, CONTAINER extends MekanismTileContainer<BE>, ITEM extends ItemBlockMachine> MachineRegistryObject<BE, BLOCK, CONTAINER, ITEM> registerFull(
            String name,
            Function<BlockTypeMachine<BE>, BLOCK> blockCreator,
            Function<BLOCK, ITEM> itemCreator,
            BlockEntityConstructor<BE, BlockTypeMachine<BE>, BLOCK> beConstructor,
            Class<BE> beClass,
            ContainerConstructor<BE, CONTAINER> contConstructor,
            ILangEntry entry,
            UnaryOperator<BlockMachineBuilder<BlockTypeMachine<BE>, BE>> operator) {
        MachineRegistryObject<BE, BLOCK, CONTAINER, ITEM> object = new MachineRegistryObject<>(name, blockRegister,
                tileRegister, containerRegister, blockCreator,
                itemCreator, beConstructor, beClass, contConstructor, entry, operator);
        machines.add(object);
        return object;
    }

    public <BE extends TileEntityMekanism, BLOCK extends BlockTileModel<BE, BlockTypeMachine<BE>>, ITEM extends ItemBlockMachine> MachineRegistryObject<BE, BLOCK, MekanismTileContainer<BE>, ITEM> registerDefaultContainer(
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

    public <BE extends TileEntityMekanism, BLOCK extends BlockTileModel<BE, BlockTypeMachine<BE>>, CONTAINER extends MekanismTileContainer<BE>> MachineRegistryObject<BE, BLOCK, CONTAINER, ItemBlockMachine> registerDefaultItem(
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

    public <BE extends TileEntityMekanism, BLOCK extends BlockTileModel<BE, BlockTypeMachine<BE>>> MachineRegistryObject<BE, BLOCK, MekanismTileContainer<BE>, ItemBlockMachine> registerDefaultContainerItem(
            String name,
            Function<BlockTypeMachine<BE>, BLOCK> blockCreator,
            BlockEntityConstructor<BE, BlockTypeMachine<BE>, BLOCK> beConstructor,
            Class<BE> beClass,
            ILangEntry entry,
            UnaryOperator<BlockMachineBuilder<BlockTypeMachine<BE>, BE>> operator) {
        return registerFull(name, blockCreator, ItemBlockMachine::new, beConstructor, beClass,
                MekanismTileContainer<BE>::new, entry, operator);
    }

    public <BE extends TileEntityMekanism, CONTAINER extends MekanismTileContainer<BE>, ITEM extends ItemBlockMachine> MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, CONTAINER, ITEM> registerDefaultBlock(
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

    public <BE extends TileEntityMekanism, ITEM extends ItemBlockMachine> MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, MekanismTileContainer<BE>, ITEM> registerDefaultBlockContainer(
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

    public <BE extends TileEntityMekanism, CONTAINER extends MekanismTileContainer<BE>> MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, CONTAINER, ItemBlockMachine> registerDefaultBlockItem(
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

    public <BE extends TileEntityMekanism> MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, MekanismTileContainer<BE>, ItemBlockMachine> registerSimple(
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

    public <KEY extends Enum<KEY>, BE extends TileEntityMekanism> EnumMap<KEY, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, MekanismTileContainer<BE>, ItemBlockMachine>> registerSimpleMap(
            Function<KEY, String> nameFunction,
            BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTileModel<BE, BlockTypeMachine<BE>>> beConstructor,
            Class<BE> beClass,
            ILangEntry entry,
            BiFunction<KEY, BlockMachineBuilder<BlockTypeMachine<BE>, BE>, BlockMachineBuilder<BlockTypeMachine<BE>, BE>> biFunction,
            Class<KEY> keyClass) {
        EnumMap<KEY, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, MekanismTileContainer<BE>, ItemBlockMachine>> map = new EnumMap<>(
                keyClass);
        for (KEY key : keyClass.getEnumConstants()) {
            map.put(key, registerSimple(nameFunction.apply(key), beConstructor, beClass, entry,
                    btm -> biFunction.apply(key, btm)));
        }
        return map;
    }

    public void register(IEventBus bus) {
        blockRegister.register(bus);
        tileRegister.register(bus);
        containerRegister.register(bus);
    }

    public List<MachineRegistryObject<?,?,?,?>> getAllMachines(){
        return machines;
    }
}
