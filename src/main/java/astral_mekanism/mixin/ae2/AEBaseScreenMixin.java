package astral_mekanism.mixin.ae2;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import appeng.client.Point;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.style.SlotPosition;

@Mixin(value = AEBaseScreen.class, remap = false)
public class AEBaseScreenMixin {
    @Inject(method = "getSlotPosition", at = @At("HEAD"), cancellable = true)
    protected void getSlotPositionInject(SlotPosition position, int semanticIndex, CallbackInfoReturnable<Point> cir) {
    }
}
