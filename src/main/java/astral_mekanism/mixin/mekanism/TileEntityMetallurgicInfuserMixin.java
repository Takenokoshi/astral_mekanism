package astral_mekanism.mixin.mekanism;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import mekanism.common.tile.machine.TileEntityMetallurgicInfuser;

@Mixin(value = TileEntityMetallurgicInfuser.class, remap = false)
public abstract class TileEntityMetallurgicInfuserMixin {

    @ModifyConstant(method = "<init>", constant = @Constant(longValue = 1_000L))
    private long astral_mekanism$increaseMaxInfuse(long original) {
        return 1_000_000L;
    }
}
