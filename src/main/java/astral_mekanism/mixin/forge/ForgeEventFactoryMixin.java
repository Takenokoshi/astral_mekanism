package astral_mekanism.mixin.forge;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mekanism.common.item.gear.ItemAtomicDisassembler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;

@Mixin(value = ForgeEventFactory.class, remap = false)
public class ForgeEventFactoryMixin {

    @Inject(
        method = "onEnchantmentLevelSet",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void forceEnchantLevelForAtomicDisassembler(
            Level level,
            BlockPos pos,
            int enchantRow,
            int power,
            ItemStack itemStack,
            int enchantmentLevel,
            CallbackInfoReturnable<Integer> cir
    ) {
        if (itemStack.getItem() instanceof ItemAtomicDisassembler) {
            cir.setReturnValue(15);
        }
    }
}
