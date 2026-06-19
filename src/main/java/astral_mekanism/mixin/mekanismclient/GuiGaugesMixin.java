package astral_mekanism.mixin.mekanismclient;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.gauge.GuiEnergyGauge;
import mekanism.client.gui.element.gauge.GuiFluidGauge;

@Mixin(value = { GuiChemicalGauge.class, GuiFluidGauge.class, GuiEnergyGauge.class }, remap = false)
public class GuiGaugesMixin {
    @ModifyReturnValue(method = "getScaledLevel",at = @At("RETURN"))
    private int astral_mekanism$modifyScaledLevel(int original) {
        return original < 0 ? 1000 : original;
    }
}
