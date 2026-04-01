package astral_mekanism.mixin.mekanism.blockentity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import astral_mekanism.integration.AMEEmpowered;
import mekanism.api.Upgrade;
import mekanism.common.tile.interfaces.IUpgradeTile;
import mekanism.common.tile.machine.TileEntityRotaryCondensentrator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(value = TileEntityRotaryCondensentrator.class, remap = false)
public abstract class TileEntityRotaryCondensentratorMixin {

    @Mutable
    @Accessor("baselineMaxOperations")
    abstract void astral_mekanism$setBaselineMaxOperations(int value);

    @Inject(method = "<init>", at = @At("TAIL"))
    void astral_mekanism$injectInit(BlockPos pos, BlockState state, CallbackInfo ci) {
        astral_mekanism$setBaselineMaxOperations(200);
    }

    @Inject(method = "recalculateUpgrades", at = @At("TAIL"))
    void astral_mekanism$injectRecalculateUpgrades(Upgrade upgrade, CallbackInfo ci) {
        if (AMEEmpowered.empoweredIsLoaded()) {
            astral_mekanism$setBaselineMaxOperations(200 << AMEEmpowered.getAllSpeeds((IUpgradeTile) (Object) this));
        } else if (upgrade == Upgrade.SPEED) {
            astral_mekanism$setBaselineMaxOperations(200 << ((IUpgradeTile) (Object) this).getComponent().getUpgrades(Upgrade.SPEED));
        }
    }
}
