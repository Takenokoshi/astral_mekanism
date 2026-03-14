package astral_mekanism.mixin.astral_mekanism;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.SoftOverride;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import appeng.client.Point;
import appeng.client.gui.layout.SlotGridLayout;
import appeng.client.gui.style.SlotPosition;
import astral_mekanism.block.gui.storage.TieredDriveScreen;
import astral_mekanism.mixin.ae2.AEBaseScreenMixin;
import net.minecraft.client.renderer.Rect2i;

@Mixin(value = TieredDriveScreen.class, remap = false)
public class TieredDriveScreenMixin extends AEBaseScreenMixin {
    @Override
    @SoftOverride
    protected void getSlotPositionInject(SlotPosition position, int semanticIndex, CallbackInfoReturnable<Point> cir) {
        SlotGridLayout grid = position.getGrid();
        if (grid == SlotGridLayout.BREAK_AFTER_2COLS) {
            Point pos = position.resolve(new Rect2i(0, 0, TieredDriveScreen.WIDTH, TieredDriveScreen.HEIGHT));
            pos = new Point(pos.getX(), pos.getY()).move(semanticIndex % 32, semanticIndex / 32);
            cir.setReturnValue(pos);
        }
    }
}
