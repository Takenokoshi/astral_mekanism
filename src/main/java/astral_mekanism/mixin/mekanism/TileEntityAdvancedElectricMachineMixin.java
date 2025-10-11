package astral_mekanism.mixin.mekanism;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import mekanism.common.tile.prefab.TileEntityAdvancedElectricMachine;

@Mixin(value = TileEntityAdvancedElectricMachine.class, remap = false)
public abstract class TileEntityAdvancedElectricMachineMixin {
    @Shadow
    @Final
    @Mutable
    public static long MAX_GAS;

    static {
        MAX_GAS = 20000L;
    }

}
