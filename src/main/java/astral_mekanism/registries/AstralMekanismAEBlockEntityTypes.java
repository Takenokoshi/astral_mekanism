package astral_mekanism.registries;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import appeng.block.networking.EnergyCellBlock;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.blockentity.networking.EnergyCellBlockEntity;
import astral_mekanism.AstralMekanismTier;
import mekanism.common.registration.impl.BlockRegistryObject;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class AstralMekanismAEBlockEntityTypes {

    public static void init() {
    }

    public static final BlockEntityType<EnergyCellBlockEntity> ENERGY_CELL = ((Supplier<BlockEntityType<EnergyCellBlockEntity>>) () -> {
        AtomicReference<BlockEntityType<EnergyCellBlockEntity>> reference = new AtomicReference<>();
        BlockEntityType.BlockEntitySupplier<EnergyCellBlockEntity> supplier = (pos,
                state) -> new EnergyCellBlockEntity(reference.get(), pos, state);
        BlockEntityType<EnergyCellBlockEntity> type = BlockEntityType.Builder
                .of(supplier, AstralMekanismBlocks.ENERGY_CELLS.values().stream()
                        .map(BlockRegistryObject::getBlock)
                        .toArray(EnergyCellBlock[]::new))
                .build(null);
        reference.set(type);
        AEBaseBlockEntity.registerBlockEntityItem(type,
                AstralMekanismBlocks.ENERGY_CELLS.get(AstralMekanismTier.ASTRAL).asItem());
        AstralMekanismBlocks.ENERGY_CELLS.forEach((tier, object) -> {
            object.getBlock().setBlockEntity(EnergyCellBlockEntity.class, type, null, null);
        });
        return type;
    }).get();
}
