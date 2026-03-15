package astral_mekanism.registries;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import appeng.block.networking.EnergyCellBlock;
import appeng.block.networking.EnergyCellBlockItem;
import astral_mekanism.AstralMekanismID;
import astral_mekanism.AstralMekanismTier.AstralMekanismTierMap;
import astral_mekanism.block.block.TieredDriveBlock;
import astral_mekanism.block.block.TieredEnergyCellBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.pedroksl.ae2addonlib.registry.BlockRegistry;
import net.pedroksl.ae2addonlib.registry.helpers.LibBlockDefinition;

public final class AstralMekanismBlockDefinitions extends BlockRegistry {

    public static final AstralMekanismBlockDefinitions INSTANCE = new AstralMekanismBlockDefinitions();

    private AstralMekanismBlockDefinitions() {
        super(AstralMekanismID.MODID);
    }

    public static final AstralMekanismTierMap<LibBlockDefinition<EnergyCellBlock>> ENERGY_CELLS = new AstralMekanismTierMap<>(
            tier -> block(tier.nameForAE + "_energy_cell", () -> new TieredEnergyCellBlock(tier),
                    EnergyCellBlockItem::new));
    public static final AstralMekanismTierMap<LibBlockDefinition<TieredDriveBlock>> DRIVES = new AstralMekanismTierMap<>(
            tier -> block(tier.nameForAE + "_drive", TieredDriveBlock::new));

    protected static <T extends Block> LibBlockDefinition<T> block(String id, Supplier<T> blockSupplier) {
        return block(AstralMekanismID.MODID, id, id, blockSupplier, null);
    }

    protected static <T extends Block> LibBlockDefinition<T> block(
            String id,
            Supplier<T> blockSupplier,
            @Nullable BiFunction<Block, Item.Properties, BlockItem> itemFactory) {
        return block(AstralMekanismID.MODID, id, id, blockSupplier, itemFactory);
    }
}
