package astral_mekanism.mixin.mekanism;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import mekanism.common.tile.prefab.TileEntityRecipeMachine;

@Mixin(value = TileEntityRecipeMachine.class, remap = false)
public abstract class TileEntityrecipeMachineMixin {
    @ModifyConstant(method = "getInitialInfusionTanks", constant = @Constant(longValue = 1_000L))
    private long astral_mekanism$maxInfuse(long original) {
        return 1_000_000L;
    }

}
