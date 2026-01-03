package astral_mekanism.mixin.mekanism;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import mekanism.common.tile.prefab.TileEntityAdvancedElectricMachine;

@Mixin(value = TileEntityAdvancedElectricMachine.class, remap = false)
public abstract class TileEntityAdvancedElectricMachineMixin {

    @ModifyConstant(method = "<getInitialGasTanks>", constant = @Constant(longValue = 210L))
    private long astral_mekanism$increaseMaxInfuse(long original) {
        return 1_000_000L;
    }
}
