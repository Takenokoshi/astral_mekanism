package astral_mekanism.block.block;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import appeng.block.networking.EnergyCellBlock;
import appeng.blockentity.networking.EnergyCellBlockEntity;
import astral_mekanism.AstralMekanismTier;
import astral_mekanism.block.shape.VoxelShapeBuilder;
import astral_mekanism.registries.AstralMekanismAEBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TieredEnergyCellBlock extends EnergyCellBlock {

    private final AstralMekanismTier tier;

    public TieredEnergyCellBlock(double maxPower, double chargeRate, int priority, AstralMekanismTier tier) {
        super(maxPower, chargeRate, priority);
        this.tier = tier;
    }

    public TieredEnergyCellBlock(AstralMekanismTier tier) {
        this(Math.pow(8, tier.ordinal() + 1) * 12800000d, Math.pow(2, tier.ordinal() + 1) * 3200d,
                20001 + tier.ordinal(), tier);
    }

    public static final VoxelShape SHAPE = VoxelShapeBuilder.create()
            .addShape(1, 1, 1, 15, 15, 15)
            .addShape(0, 0, 0, 16, 1, 1)
            .addShape(0, 0, 15, 16, 1, 16)
            .addShape(0, 15, 0, 16, 16, 1)
            .addShape(0, 15, 15, 16, 16, 16)
            .addShape(0, 0, 0, 1, 16, 1)
            .addShape(0, 0, 15, 1, 16, 16)
            .addShape(15, 0, 0, 16, 16, 1)
            .addShape(15, 0, 15, 16, 16, 16)
            .addShape(0, 0, 0, 1, 1, 16)
            .addShape(0, 15, 0, 1, 16, 16)
            .addShape(15, 0, 0, 16, 1, 16)
            .addShape(15, 15, 0, 16, 16, 16)
            .build();

    @Override
    @NotNull
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos,
            @NotNull CollisionContext context) {
        return SHAPE;
    }
/*
    public BlockEntityType<EnergyCellBlockEntity> getBlockEntityType() {
        return AstralMekanismAEBlockEntityTypes.ENERGY_CELL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state){
        return AstralMekanismAEBlockEntityTypes.ENERGY_CELL.create(pos, state);
    } */

}
