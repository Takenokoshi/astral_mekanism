package astral_mekanism.block.shape;

import net.minecraft.world.phys.shapes.VoxelShape;

public final class AMBlockShapes {
    public static final VoxelShape[] GAS_BURNING_GENERATOR = VoxelShapeBuilder.create()
            .addShape(2, 4, 2, 14, 5, 14) // base_platform
            .addShape(1, 5, 4, 2, 5, 12) // bottom_connector1
            .addShape(4, 5, 14, 12, 5, 15) // bottom_connector2
            .addShape(14, 5, 4, 15, 5, 12) // bottom_connector3
            .addShape(4, 5, 1, 12, 5, 2) // bottom_connector4
            .addShape(4, 12, 2, 12, 13, 3) // port_connector_north1
            .addShape(4, 12, 13, 12, 13, 14) // port_connector_south1
            .addShape(0.5, 12, 4, 2, 13, 12) // port_connector_west1 (近似)
            .addShape(2, 12, 4, 3, 13, 12) // port_connector_west2
            .addShape(13, 12, 4, 14, 13, 12) // port_connector_east2
            .addShape(4, 13, 12, 12, 16, 13) // chamber_south
            .addShape(4, 13, 3, 12, 16, 4) // chamber_north
            .addShape(3, 13, 3, 4, 16, 13) // chamber_west
            .addShape(12, 13, 3, 13, 16, 13) // chamber_east
            .addShape(12, 5, 1, 15, 14, 4) // tank1
            .addShape(1, 5, 1, 4, 14, 4) // tank4
            .addShape(1, 5, 12, 4, 14, 15) // tank3
            .addShape(12, 5, 12, 15, 14, 15) // tank2
            .addShape(0, 0, 0, 16, 4, 16) // base
            .addShape(4, 4, 0, 12, 12, 1) // port_north
            .addShape(15, 4, 4, 16, 12, 12) // port_east
            .addShape(4, 16, 4, 12, 16, 12) // port_up
            .addShape(4, 4, 15, 12, 12, 16) // port_south
            .addShape(0, 4, 4, 1, 12, 12) // port_west
            .addShape(3, 4, 0, 4, 13, 1) // c
            .addShape(0, 4, 12, 1, 13, 13) // c
            .addShape(12, 4, 15, 13, 13, 16)// c
            .addShape(15, 4, 3, 16, 13, 4) // c
            .addShape(12, 4, 0, 13, 13, 1) // a
            .addShape(0, 4, 3, 1, 13, 4) // a
            .addShape(3, 4, 15, 4, 13, 16) // a
            .addShape(15, 4, 12, 16, 13, 13)// a
            .addShape(4, 12, 0, 12, 13, 1) // b
            .addShape(15, 12, 4, 16, 13, 12)// b
            .addShape(4, 12, 15, 12, 13, 16)// b
            .addShape(0, 12, 4, 1, 13, 12) // b
            .addShape(3, 4, 1, 13, 5, 2) // d
            .addShape(1, 4, 3, 2, 5, 13) // d
            .addShape(3, 4, 14, 13, 5, 15) // d
            .addShape(14, 4, 3, 15, 5, 13) // d
            .build4();

    public static final VoxelShape[] HEAT_GENERATOR = VoxelShapeBuilder.create()
            .addShape(mekanism.generators.common.content.blocktype.BlockShapes.HEAT_GENERATOR[0])
            .addShape(0, 6, 0, 1, 15, 1)
            .addShape(15, 6, 0, 16, 15, 1)
            .addShape(0, 15, 5, 1, 16, 6)
            .addShape(15, 15, 5, 16, 16, 6)
            .addShape(0, 6, 15, 1, 15, 16)
            .addShape(15, 6, 15, 16, 15, 16)
            .addShape(0, 15, 15, 16, 16, 16)
            .addShape(0, 4, 4, 16, 12, 12)
            .addShape(4, 4, 14, 12, 12, 16)
            .addShape(4, 14, 4, 12, 16, 12)
            .build4();
}
