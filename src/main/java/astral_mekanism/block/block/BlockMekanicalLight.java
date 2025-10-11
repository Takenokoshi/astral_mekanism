package astral_mekanism.block.block;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.shape.VoxelShapeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockMekanicalLight extends Block {

    public static final VoxelShape SHAPE = VoxelShapeBuilder.create()
            .addShape(4, 0, 0, 12, 2, 2)
            .addShape(4, 0, 14, 12, 2, 16)
            .addShape(4, 14, 14, 12, 16, 16)
            .addShape(4, 14, 0, 12, 16, 2)
            .addShape(0, 4, 0, 2, 12, 2)
            .addShape(14, 4, 0, 16, 12, 2)
            .addShape(14, 4, 14, 16, 12, 16)
            .addShape(0, 4, 14, 2, 12, 16)
            .addShape(0, 0, 4, 2, 2, 12)
            .addShape(14, 0, 4, 16, 2, 12)
            .addShape(0, 14, 4, 2, 16, 12)
            .addShape(14, 14, 4, 16, 16, 12)
            .addShape(0, 0, 0, 4, 4, 4)
            .addShape(12, 0, 0, 16, 4, 4)
            .addShape(0, 0, 12, 4, 4, 16)
            .addShape(12, 0, 12, 16, 4, 16)
            .addShape(0, 12, 0, 4, 16, 4)
            .addShape(12, 12, 0, 16, 16, 4)
            .addShape(0, 12, 12, 4, 16, 16)
            .addShape(12, 12, 12, 16, 16, 16)
            .addShape(1, 1, 1, 15, 15, 15)
            .build();

    public BlockMekanicalLight(Properties p) {
        super(p.lightLevel((s) -> 15)
                .strength(1.5f, 6.0f)
                .sound(SoundType.STONE)
                .mapColor(MapColor.STONE));
    }

    public static BlockMekanicalLight create() {
        return new BlockMekanicalLight(BlockBehaviour.Properties.of());
    }

    @Override
    @NotNull
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos,
            @NotNull CollisionContext context) {
        return SHAPE;
    }

}
