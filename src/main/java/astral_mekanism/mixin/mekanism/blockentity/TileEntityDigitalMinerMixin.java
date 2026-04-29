package astral_mekanism.mixin.mekanism.blockentity;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.SoftOverride;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import astral_mekanism.enums.AMEUpgrade;
import mekanism.api.Upgrade;
import mekanism.common.content.miner.MinerFilter;
import mekanism.common.tile.machine.TileEntityDigitalMiner;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(value = TileEntityDigitalMiner.class, remap = false)
public class TileEntityDigitalMinerMixin extends TileEntityMekanismMixin {

    @Unique
    private boolean astral_mekanism$isAdvancedStoneGeneratorUpgradeInstalled;

    @WrapOperation(// 0
            method = "setReplace", // 1
            at = @At(// 2
                    value = "INVOKE", // 3
                    target = "Lmekanism/common/tile/machine/TileEntityDigitalMiner;"
                            + "getReplace("
                            + "  Lnet/minecraft/world/item/Item;"
                            + "Ljava/util/function/Predicate;"
                            + ")Lnet/minecraft/world/item/ItemStack;"// 4
            ))
    private ItemStack astral_mekanism$modifyReplaceStack(
            TileEntityDigitalMiner miner,
            Item replaceTarget,
            Predicate<Item> matcher,
            Operation<ItemStack> original,
            BlockState state,
            BlockPos pos,
            MinerFilter<?> filter) {
        if (astral_mekanism$isAdvancedStoneGeneratorUpgradeInstalled) {
            return pos.getY() > 4 ? Items.STONE.getDefaultInstance() : Items.DEEPSLATE.getDefaultInstance();
        }
        return original.call(miner, replaceTarget, matcher);
    }

    @Override
    @SoftOverride
    protected void astral_mekanism$recalculateUpgradesInject(Upgrade upgrade, CallbackInfo ci) {
        if (upgrade == AMEUpgrade.ADVANCED_STONE_GENERATOR.getValue()) {
            astral_mekanism$isAdvancedStoneGeneratorUpgradeInstalled = ((TileEntityDigitalMiner) (Object) this)
                    .getComponent().isUpgradeInstalled(AMEUpgrade.ADVANCED_STONE_GENERATOR.getValue());
        }
    }
}
