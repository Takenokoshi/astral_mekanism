package astral_mekanism.registries;

import java.util.EnumMap;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import appeng.block.networking.EnergyCellBlock;
import appeng.block.networking.EnergyCellBlockItem;
import astral_mekanism.AstralMekanismID;
import astral_mekanism.AstralMekanismTier;
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

    public static final EnumMap<AstralMekanismTier, LibBlockDefinition<EnergyCellBlock>> ENERGY_CELLS = new EnumMap<>(
            AstralMekanismTier.class);

    public static final EnumMap<AstralMekanismTier, LibBlockDefinition<TieredDriveBlock>> DRIVES = new EnumMap<>(
            AstralMekanismTier.class);

    public static final LibBlockDefinition<EnergyCellBlock> LOGIC_ENERGY_CELL = energyCellBlock(
            AstralMekanismTier.ESSENTIAL);
    public static final LibBlockDefinition<EnergyCellBlock> CALCULATION_ENERGY_CELL = energyCellBlock(
            AstralMekanismTier.BASIC);
    public static final LibBlockDefinition<EnergyCellBlock> ENGINEERING_ENERGY_CELL = energyCellBlock(
            AstralMekanismTier.ADVANCED);
    public static final LibBlockDefinition<EnergyCellBlock> ACCUMULATION_ENERGY_CELL = energyCellBlock(
            AstralMekanismTier.ELITE);
    public static final LibBlockDefinition<EnergyCellBlock> PHOTON_ENERGY_CELL = energyCellBlock(
            AstralMekanismTier.ULTIMATE);
    public static final LibBlockDefinition<EnergyCellBlock> QUANTUM_ENERGY_CELL = energyCellBlock(
            AstralMekanismTier.ABSOLUTE);
    public static final LibBlockDefinition<EnergyCellBlock> COMPOSITE_ENERGY_CELL = energyCellBlock(
            AstralMekanismTier.SUPREME);
    public static final LibBlockDefinition<EnergyCellBlock> ORIGIN_ENERGY_CELL = energyCellBlock(
            AstralMekanismTier.COSMIC);
    public static final LibBlockDefinition<EnergyCellBlock> AUTONOMY_ENERGY_CELL = energyCellBlock(
            AstralMekanismTier.INFINITE);
    public static final LibBlockDefinition<EnergyCellBlock> FIRMAMENT_ENERGY_CELL = energyCellBlock(
            AstralMekanismTier.ASTRAL);
    public static final LibBlockDefinition<TieredDriveBlock> LOGIC_DRIVE = driveBlock(AstralMekanismTier.ESSENTIAL);
    public static final LibBlockDefinition<TieredDriveBlock> CALCULATION_DRIVE = driveBlock(AstralMekanismTier.BASIC);
    public static final LibBlockDefinition<TieredDriveBlock> ENGINEERING_DRIVE = driveBlock(
            AstralMekanismTier.ADVANCED);
    public static final LibBlockDefinition<TieredDriveBlock> ACCUMULATION_DRIVE = driveBlock(AstralMekanismTier.ELITE);
    public static final LibBlockDefinition<TieredDriveBlock> PHOTON_DRIVE = driveBlock(AstralMekanismTier.ULTIMATE);
    public static final LibBlockDefinition<TieredDriveBlock> QUANTUM_DRIVE = driveBlock(AstralMekanismTier.ABSOLUTE);
    public static final LibBlockDefinition<TieredDriveBlock> COMPOSITE_DRIVE = driveBlock(AstralMekanismTier.SUPREME);
    public static final LibBlockDefinition<TieredDriveBlock> ORIGIN_DRIVE = driveBlock(AstralMekanismTier.COSMIC);
    public static final LibBlockDefinition<TieredDriveBlock> AUTONOMY_DRIVE = driveBlock(AstralMekanismTier.INFINITE);
    public static final LibBlockDefinition<TieredDriveBlock> FIRMAMENT_DRIVE = driveBlock(AstralMekanismTier.ASTRAL);

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
        ENERGY_CELLS.put(tier, block(tier.nameForAE + "_energy_cell", () -> new TieredEnergyCellBlock(tier),
                EnergyCellBlockItem::new));
        return ENERGY_CELLS.get(tier);
    }

    private static LibBlockDefinition<TieredDriveBlock> driveBlock(AstralMekanismTier tier) {
        DRIVES.put(tier, block(tier.nameForAE + "_drive", TieredDriveBlock::new));
        return DRIVES.get(tier);
    }

}
