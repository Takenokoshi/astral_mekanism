package astral_mekanism.registries;

import java.util.EnumMap;
import java.util.function.Supplier;

import appeng.block.AEBaseEntityBlock;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.blockentity.networking.EnergyCellBlockEntity;
import astral_mekanism.AMEConstants;
import astral_mekanism.AMETier;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.pedroksl.ae2addonlib.registry.BlockEntityRegistry;
import net.pedroksl.ae2addonlib.registry.helpers.LibBlockDefinition;

public final class AMEBlockEntityRegistry extends BlockEntityRegistry {

    public static final AMEBlockEntityRegistry INSTANCE = new AMEBlockEntityRegistry();

    private AMEBlockEntityRegistry() {
        super(AMEConstants.MODID);
    }

    public static final EnumMap<AMETier, Supplier<BlockEntityType<EnergyCellBlockEntity>>> ENERGY_CELLS = new EnumMap<>(
            AMETier.class);

    public static final Supplier<BlockEntityType<EnergyCellBlockEntity>> LOGIC_ENERGY_CELL = createEnergyCell(
            AMETier.ESSENTIAL);

    public static final Supplier<BlockEntityType<EnergyCellBlockEntity>> CALCULATION_ENERGY_CELL = createEnergyCell(
            AMETier.BASIC);

    public static final Supplier<BlockEntityType<EnergyCellBlockEntity>> ENGINEERING_ENERGY_CELL = createEnergyCell(
            AMETier.ADVANCED);

    public static final Supplier<BlockEntityType<EnergyCellBlockEntity>> ACCUMULATION_ENERGY_CELL = createEnergyCell(
            AMETier.ELITE);

    public static final Supplier<BlockEntityType<EnergyCellBlockEntity>> PHOTON_ENERGY_CELL = createEnergyCell(
            AMETier.ULTIMATE);

    public static final Supplier<BlockEntityType<EnergyCellBlockEntity>> QUANTUM_ENERGY_CELL = createEnergyCell(
            AMETier.ABSOLUTE);

    public static final Supplier<BlockEntityType<EnergyCellBlockEntity>> COMPOSITE_ENERGY_CELL = createEnergyCell(
            AMETier.SUPREME);

    public static final Supplier<BlockEntityType<EnergyCellBlockEntity>> ORIGIN_ENERGY_CELL = createEnergyCell(
            AMETier.COSMIC);

    public static final Supplier<BlockEntityType<EnergyCellBlockEntity>> AUTONOMY_ENERGY_CELL = createEnergyCell(
            AMETier.INFINITE);

    public static final Supplier<BlockEntityType<EnergyCellBlockEntity>> FIRMAMENT_ENERGY_CELL = createEnergyCell(
            AMETier.ASTRAL);

    @SafeVarargs
    private static <T extends AEBaseBlockEntity> Supplier<BlockEntityType<T>> create(
            String id,
            Class<T> entityClass,
            BlockEntityFactory<T> factory,
            LibBlockDefinition<? extends AEBaseEntityBlock<?>>... blockDefs) {
        return create(AMEConstants.MODID, id, entityClass, factory, blockDefs);
    }

    private static Supplier<BlockEntityType<EnergyCellBlockEntity>> createEnergyCell(AMETier tier) {
        ENERGY_CELLS.put(tier, create(AMEBlockDefinitions.ENERGY_CELLS.get(tier).id().getPath(),
                EnergyCellBlockEntity.class, EnergyCellBlockEntity::new,
                AMEBlockDefinitions.ENERGY_CELLS.get(tier)));
        return ENERGY_CELLS.get(tier);
    }
}
