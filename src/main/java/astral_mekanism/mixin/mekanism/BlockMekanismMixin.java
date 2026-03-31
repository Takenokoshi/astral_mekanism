package astral_mekanism.mixin.mekanism;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import astral_mekanism.enumexpansion.AMEUpgrade;
import mekanism.common.block.BlockMekanism;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

@Mixin(value = BlockMekanism.class, remap = false)
public class BlockMekanismMixin {
    @ModifyExpressionValue(method = "getDrops", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"))
    private boolean astral_mekanism$modifyCondition(boolean original,
            BlockState state,
            LootParams.Builder builder) {
        return original || astral_mekanism$hasRadioActiveSealingUpgrade(state, builder);
    }

    @Unique
    private static boolean astral_mekanism$hasRadioActiveSealingUpgrade(BlockState state,
            LootParams.Builder builder) {
        BlockEntity be = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (be != null && be instanceof TileEntityMekanism tileMek && tileMek.supportsUpgrades()) {
            return tileMek.getComponent().isUpgradeInstalled(AMEUpgrade.RADIOACTIVE_SEALING.getValue());
        }
        return false;
    }
}
