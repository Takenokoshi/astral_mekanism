package astral_mekanism.block.shape;

import mekanism.common.util.VoxelShapeUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelShapeBuilder {
    private VoxelShape shape;

    private VoxelShapeBuilder() {
        shape = Shapes.empty();
    }

    public static VoxelShapeBuilder create() {
        return new VoxelShapeBuilder();
    }

    public VoxelShapeBuilder addShape(VoxelShape add) {
        shape = Shapes.or(shape, add);
        return this;
    }

    public VoxelShapeBuilder addShape(VoxelShape[] add) {
        for (VoxelShape adding : add) {
            shape = Shapes.or(shape, adding);
        }
        return this;
    }

    public VoxelShapeBuilder addShape(double fx, double fy, double fz, double tx, double ty, double tz) {
        shape = Shapes.or(shape, Block.box(fx, fy, fz, tx, ty, tz));
        return this;
    }

    public VoxelShape build() {
        return shape.optimize();
    }

    public VoxelShape[] build4() {
        VoxelShape[] result = new VoxelShape[4];
        VoxelShapeUtils.setShape(shape, result);
        return result;
    }
}
