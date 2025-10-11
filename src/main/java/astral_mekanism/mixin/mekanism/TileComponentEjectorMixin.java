package astral_mekanism.mixin.mekanism;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;

@Mixin(value = TileComponentEjector.class, remap = false)
public class TileComponentEjectorMixin {
    @Shadow
    @Final
    @Mutable
    private int tickDelay;

    @Inject(method = "outputItems", at = @At("RETURN"))
    private void outputItemsInject(ConfigInfo info, CallbackInfo ci) {
        tickDelay = 0;
    }

}
