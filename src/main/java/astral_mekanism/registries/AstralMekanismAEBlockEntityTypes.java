package astral_mekanism.registries;

import java.util.EnumMap;
import java.util.function.Supplier;

import appeng.block.AEBaseEntityBlock;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.blockentity.networking.EnergyCellBlockEntity;
import astral_mekanism.AstralMekanismID;
import astral_mekanism.AstralMekanismTier;
import astral_mekanism.block.blockentity.storage.TieredDriveBlockEntities;
import astral_mekanism.block.blockentity.storage.TieredDriveBlockEntities.TieredDriveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.pedroksl.ae2addonlib.registry.BlockEntityRegistry;
import net.pedroksl.ae2addonlib.registry.helpers.LibBlockDefinition;

public final class AstralMekanismAEBlockEntityTypes extends BlockEntityRegistry {

    public static final AstralMekanismAEBlockEntityTypes INSTANCE = new AstralMekanismAEBlockEntityTypes();

    private AstralMekanismAEBlockEntityTypes() {
        super(AstralMekanismID.MODID);
    }

    public static final EnumMap<AstralMekanismTier, Supplier<BlockEntityType<EnergyCellBlockEntity>>> ENERGY_CELLS = new EnumMap<>(
            AstralMekanismTier.class);

    public static final EnumMap<AstralMekanismTier, Supplier<BlockEntityType<TieredDriveBlockEntity>>> DRIVES = new EnumMap<>(
            AstralMekanismTier.class);

    public static final Supplier<BlockEntityType<EnergyCellBlockEntity>> LOGIC_ENERGY_CELL = createEnergyCell(AstralMekanismTier.ESSENTIAL);

    public static final Supplier<BlockEntityType<EnergyCellBlockEntity>> CALCULATION_ENERGY_CELL =  createEnergyCell(AstralMekanismTier.BASIC);

    public static final Supplier<BlockEntityType<EnergyCellBlockEntity>> ENGINEERING_ENERGY_CELL = createEnergyCell(AstralMekanismTier.ADVANCED);

    public static final Supplier<BlockEntityType<EnergyCellBlockEntity>> ACCUMULATION_ENERGY_CELL =  createEnergyCell(AstralMekanismTier.ELITE);

    public static final Supplier<BlockEntityType<EnergyCellBlockEntity>> PHOTON_ENERGY_CELL = createEnergyCell(AstralMekanismTier.ULTIMATE);

    public static final Supplier<BlockEntityType<EnergyCellBlockEntity>> QUANTUM_ENERGY_CELL =  createEnergyCell(AstralMekanismTier.ABSOLUTE);

    public static final Supplier<BlockEntityType<EnergyCellBlockEntity>> COMPOSITE_ENERGY_CELL =createEnergyCell(AstralMekanismTier.SUPREME);

    public static final Supplier<BlockEntityType<EnergyCellBlockEntity>> ORIGIN_ENERGY_CELL = createEnergyCell(AstralMekanismTier.COSMIC);

    public static final Supplier<BlockEntityType<EnergyCellBlockEntity>> AUTONOMY_ENERGY_CELL = createEnergyCell(AstralMekanismTier.INFINITE);

    public static final Supplier<BlockEntityType<EnergyCellBlockEntity>> FIRMAMENT_ENERGY_CELL = createEnergyCell(AstralMekanismTier.ASTRAL);

    public static final Supplier<BlockEntityType<TieredDriveBlockEntity>> LOGIC_DRIVE = createDrive(AstralMekanismTier.ESSENTIAL);

    public static final Supplier<BlockEntityType<TieredDriveBlockEntity>> CALCULATION_DRIVE = createDrive(AstralMekanismTier.BASIC);

    public static final Supplier<BlockEntityType<TieredDriveBlockEntity>> ENGINEERING_DRIVE =createDrive(AstralMekanismTier.ADVANCED);

    public static final Supplier<BlockEntityType<TieredDriveBlockEntity>> ACCUMULATION_DRIVE =createDrive(AstralMekanismTier.ELITE);

    public static final Supplier<BlockEntityType<TieredDriveBlockEntity>> PHOTON_DRIVE = createDrive(AstralMekanismTier.ULTIMATE);

    public static final Supplier<BlockEntityType<TieredDriveBlockEntity>> QUANTUM_DRIVE =createDrive(AstralMekanismTier.ABSOLUTE);

    public static final Supplier<BlockEntityType<TieredDriveBlockEntity>> COMPOSITE_DRIVE = createDrive(AstralMekanismTier.SUPREME);

    public static final Supplier<BlockEntityType<TieredDriveBlockEntity>> ORIGIN_DRIVE =  createDrive(AstralMekanismTier.COSMIC);

    public static final Supplier<BlockEntityType<TieredDriveBlockEntity>> AUTONOMY_DRIVE =createDrive(AstralMekanismTier.INFINITE);

    public static final Supplier<BlockEntityType<TieredDriveBlockEntity>> FIRMAMENT_DRIVE = createDrive(AstralMekanismTier.ASTRAL);

    @SafeVarargs
    private static <T extends AEBaseBlockEntity> Supplier<BlockEntityType<T>> create(
            String id,
            Class<T> entityClass,
            BlockEntityFactory<T> factory,
            LibBlockDefinition<? extends AEBaseEntityBlock<?>>... blockDefs) {
        return create(AstralMekanismID.MODID, id, entityClass, factory, blockDefs);
    }

    private static Supplier<BlockEntityType<EnergyCellBlockEntity>> createEnergyCell(AstralMekanismTier tier) {
        ENERGY_CELLS.put(tier, create(AstralMekanismBlockDefinitions.ENERGY_CELLS.get(tier).id().getPath(),
                EnergyCellBlockEntity.class, EnergyCellBlockEntity::new,
                AstralMekanismBlockDefinitions.ENERGY_CELLS.get(tier)));
        return ENERGY_CELLS.get(tier);
    }

    private static Supplier<BlockEntityType<TieredDriveBlockEntity>> createDrive(AstralMekanismTier tier) {
        DRIVES.put(tier, create(AstralMekanismBlockDefinitions.DRIVES.get(tier).id().getPath(),
                TieredDriveBlockEntity.class, TieredDriveBlockEntities.get(tier)::create,
                AstralMekanismBlockDefinitions.DRIVES.get(tier)));
        return DRIVES.get(tier);
    }
}
