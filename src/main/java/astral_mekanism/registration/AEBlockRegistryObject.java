package astral_mekanism.registration;

import java.util.function.Function;
import java.util.function.Supplier;

import appeng.core.definitions.BlockDefinition;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.registration.INamedEntry;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class AEBlockRegistryObject<BLOCK extends Block, ITEM extends BlockItem> implements INamedEntry, IBlockProvider {

    public final BlockRegistryObject<BLOCK, ITEM> registryObject;
    public final BlockDefinition<BLOCK> definition;

    public AEBlockRegistryObject(
            String name,
            Function<String, ResourceLocation> idCreator,
            BlockDeferredRegister deferredRegister,
            Supplier<BLOCK> supplier,
            Function<BLOCK, ITEM> itemCreator) {
        registryObject = deferredRegister.register(name, supplier, itemCreator);
        BLOCK block = registryObject.getBlock();
        definition = new BlockDefinition<>(getEnglishName(name), idCreator.apply(name), block, itemCreator.apply(block));
    }

    @Override
    public Item asItem() {
        return registryObject.asItem();
    }

    @Override
    public Block getBlock() {
        return registryObject.getBlock();
    }

    @Override
    public String getInternalRegistryName() {
        return registryObject.getInternalRegistryName();
    }

    public static String getEnglishName(String value) {
        String[] parts = value.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            sb.append(parts[i].toUpperCase());
        }
        return sb.toString();
    }
}
