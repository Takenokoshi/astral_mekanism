package astral_mekanism.block.blockentity.other;

import appeng.blockentity.networking.EnergyCellBlockEntity;
import astral_mekanism.block.block.TieredEnergyCellBlock;
import mekanism.common.registration.impl.BlockRegistryObject;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TieredEnergyCellBlockEntity extends EnergyCellBlockEntity {

    public TieredEnergyCellBlockEntity(BlockRegistryObject<TieredEnergyCellBlock,?> object, BlockPos pos, BlockState blockState) {
        super(object.getBlock().getBlockEntityType(), pos, blockState);
    }
    
}
