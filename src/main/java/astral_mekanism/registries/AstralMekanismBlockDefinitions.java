package astral_mekanism.registries;

import java.util.EnumMap;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import appeng.block.networking.EnergyCellBlock;
import appeng.block.networking.EnergyCellBlockItem;
import astral_mekanism.AstralMekanismID;
import astral_mekanism.AstralMekanismTier;
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

    public static final EnumMap<AstralMekanismTier, LibBlockDefinition<EnergyCellBlock>> ENERGY_CELLS = new EnumMap<>(
            AstralMekanismTier.class);

    public static final LibBlockDefinition<EnergyCellBlock> LOGIC_ENERGY_CELL = ENERGY_CELLS
            .put(AstralMekanismTier.ESSENTIAL, energyCellBlock(AstralMekanismTier.ESSENTIAL));

    public static final LibBlockDefinition<EnergyCellBlock> CALCULATION_ENERGY_CELL = ENERGY_CELLS
            .put(AstralMekanismTier.BASIC, energyCellBlock(AstralMekanismTier.BASIC));

    public static final LibBlockDefinition<EnergyCellBlock> ENGINEERING_ENERGY_CELL = ENERGY_CELLS
            .put(AstralMekanismTier.ADVANCED, energyCellBlock(AstralMekanismTier.ADVANCED));

    public static final LibBlockDefinition<EnergyCellBlock> ACCUMULATION_ENERGY_CELL = ENERGY_CELLS
            .put(AstralMekanismTier.ELITE, energyCellBlock(AstralMekanismTier.ELITE));

    public static final LibBlockDefinition<EnergyCellBlock> PHOTON_ENERGY_CELL = ENERGY_CELLS
            .put(AstralMekanismTier.ULTIMATE, energyCellBlock(AstralMekanismTier.ULTIMATE));

    public static final LibBlockDefinition<EnergyCellBlock> QUANTUM_ENERGY_CELL = ENERGY_CELLS
            .put(AstralMekanismTier.ABSOLUTE, energyCellBlock(AstralMekanismTier.ABSOLUTE));

    public static final LibBlockDefinition<EnergyCellBlock> COMPOSITE_ENERGY_CELL = ENERGY_CELLS
            .put(AstralMekanismTier.SUPREME, energyCellBlock(AstralMekanismTier.SUPREME));

    public static final LibBlockDefinition<EnergyCellBlock> ORIGIN_ENERGY_CELL = ENERGY_CELLS
            .put(AstralMekanismTier.COSMIC, energyCellBlock(AstralMekanismTier.COSMIC));

    public static final LibBlockDefinition<EnergyCellBlock> AUTONOMY_ENERGY_CELL = ENERGY_CELLS
            .put(AstralMekanismTier.INFINITE, energyCellBlock(AstralMekanismTier.INFINITE));

    public static final LibBlockDefinition<EnergyCellBlock> FIRMAMENT_ENERGY_CELL = ENERGY_CELLS
            .put(AstralMekanismTier.ASTRAL, energyCellBlock(AstralMekanismTier.ASTRAL));

    protected static <T extends Block> LibBlockDefinition<T> block(String id, Supplier<T> blockSupplier) {
        return block(AstralMekanismID.MODID, id, id, blockSupplier, null);
    }

    protected static <T extends Block> LibBlockDefinition<T> block(
            String id,
            Supplier<T> blockSupplier,
            @Nullable BiFunction<Block, Item.Properties, BlockItem> itemFactory) {
        return block(AstralMekanismID.MODID, id, id, blockSupplier, itemFactory);
    }

    private static LibBlockDefinition<EnergyCellBlock> energyCellBlock(AstralMekanismTier tier) {
        return block(tier.nameForAE + "_energy_cell", () -> new TieredEnergyCellBlock(tier), EnergyCellBlockItem::new);
    }

}
