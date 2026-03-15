package astral_mekanism.block.block;

import org.jetbrains.annotations.Nullable;

import appeng.api.orientation.IOrientationStrategy;
import appeng.api.orientation.OrientationStrategies;
import appeng.block.AEBaseEntityBlock;
import appeng.util.InteractionUtil;
import astral_mekanism.block.blockentity.storage.TieredDriveBlockEntities.TieredDriveBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

public class TieredDriveBlock extends AEBaseEntityBlock<TieredDriveBlockEntity> {

    public TieredDriveBlock() {
        super(metalProps());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }

    @Override
    public InteractionResult onActivated(Level level, BlockPos pos, Player p, InteractionHand hand,
            @Nullable ItemStack heldItem, BlockHitResult hit) {
        if (InteractionUtil.isInAlternateUseMode(p)) {
            return InteractionResult.PASS;
        } else {
            TieredDriveBlockEntity be = this.getBlockEntity(level, pos);
            if (be != null) {
                if (!level.isClientSide()) {
                    be.openMenu(p);
                }
                return InteractionResult.sidedSuccess(level.isClientSide());
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    public IOrientationStrategy getOrientationStrategy() {
        return OrientationStrategies.full();
    }
}
