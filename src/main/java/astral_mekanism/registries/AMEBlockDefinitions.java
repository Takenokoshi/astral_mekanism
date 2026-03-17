package astral_mekanism.registries;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import appeng.block.networking.EnergyCellBlock;
import appeng.block.networking.EnergyCellBlockItem;
import astral_mekanism.AMEConstants;
import astral_mekanism.AMETier.AMETierMap;
import astral_mekanism.block.block.TieredEnergyCellBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.pedroksl.ae2addonlib.registry.BlockRegistry;
import net.pedroksl.ae2addonlib.registry.helpers.LibBlockDefinition;

public final class AMEBlockDefinitions extends BlockRegistry {

    public static final AMEBlockDefinitions INSTANCE = new AMEBlockDefinitions();

    private AMEBlockDefinitions() {
        super(AMEConstants.MODID);
    }

    public static final AMETierMap<LibBlockDefinition<EnergyCellBlock>> ENERGY_CELLS = new AMETierMap<>(
            tier -> block(tier.nameForAE + "_energy_cell", () -> new TieredEnergyCellBlock(tier),
                    EnergyCellBlockItem::new));

    protected static <T extends Block> LibBlockDefinition<T> block(String id, Supplier<T> blockSupplier) {
        return block(AMEConstants.MODID, id, id, blockSupplier, null);
    }

    protected static <T extends Block> LibBlockDefinition<T> block(
            String id,
            Supplier<T> blockSupplier,
            @Nullable BiFunction<Block, Item.Properties, BlockItem> itemFactory) {
        return block(AMEConstants.MODID, id, id, blockSupplier, itemFactory);
    }
}
