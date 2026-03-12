package astral_mekanism.registration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import appeng.core.definitions.BlockDefinition;
import mekanism.common.registration.impl.BlockDeferredRegister;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class AEBlockDeferredRegister {

    private final String modId;
    private final Function<String, ResourceLocation> idCreator;
    public final BlockDeferredRegister blockRegister;

    private final List<AEBlockRegistryObject<?, ?>> aeBlocks;
    private final List<BlockDefinition<?>> definitions;

    public AEBlockDeferredRegister(String modId, Function<String, ResourceLocation> idCreator) {
        this.modId = modId;
        this.idCreator = idCreator;
        this.blockRegister = new BlockDeferredRegister(modId);
        this.aeBlocks = new ArrayList<>();
        this.definitions = new ArrayList<>();
    }

    public <BLOCK extends Block, ITEM extends BlockItem> AEBlockRegistryObject<BLOCK, ITEM> register(
            String name, Supplier<BLOCK> supplier, Function<BLOCK, ITEM> itemCreator) {
        AEBlockRegistryObject<BLOCK, ITEM> result = new AEBlockRegistryObject<>(name, idCreator, blockRegister,
                supplier, itemCreator);
        aeBlocks.add(result);
        definitions.add(result.definition);
        return result;
    }

    public List<AEBlockRegistryObject<?, ?>> getAEBlocks(){
        return Collections.unmodifiableList(aeBlocks);
    }

    public List<BlockDefinition<?>> getDefinitions(){
        return Collections.unmodifiableList(definitions);
    }
}
