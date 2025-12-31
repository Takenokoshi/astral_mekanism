package astral_mekanism.mixin.mekanism;

import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mekanism.common.item.gear.ItemAtomicDisassembler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

@Mixin(value = ItemAtomicDisassembler.class, remap = false)
public class ItemAtomicDisassemblerMixin {

    private static final Set<Enchantment> DISALLOWED = Set.of(
            Enchantments.MENDING,
            Enchantments.UNBREAKING,
            Enchantments.BLOCK_EFFICIENCY,
            Enchantments.SHARPNESS,
            Enchantments.BANE_OF_ARTHROPODS,
            Enchantments.SMITE,
            Enchantments.SWEEPING_EDGE);

    @Inject(method = "isEnchantable", at = @At("RETURN"), cancellable = true)
    private void isEnchantableInject(@NotNull ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
        return;
    }

    @Inject(method = "isBookEnchantable", at = @At("RETURN"), cancellable = true)
    private void isBookEnchantableInject(ItemStack stack, ItemStack book, CallbackInfoReturnable<Boolean> cir) {
        boolean result = book.getAllEnchantments().keySet().stream()
                .allMatch(enchantment -> ((ItemAtomicDisassembler) (Object) (this))
                        .canApplyAtEnchantingTable(stack, enchantment));
        cir.setReturnValue(result);
        return;
    }

    @Inject(method = "canApplyAtEnchantingTable", at = @At("RETURN"), cancellable = true)
    private void canApplyAtEnchantingTableInject(ItemStack stack, Enchantment enchantment,
            CallbackInfoReturnable<Boolean> cir) {
        if (DISALLOWED.contains(enchantment)) {
            cir.setReturnValue(false);
            return;
        } else {
            boolean allowed = enchantment.category.canEnchant(Items.NETHERITE_PICKAXE)
                    || enchantment.category.canEnchant(Items.NETHERITE_SWORD);
            cir.setReturnValue(allowed);
            return;
        }
    }

    @Inject(method = "getEnchantmentValue", at = @At("RETURN"), cancellable = true)
    private void getEnchantmentValueInject(CallbackInfoReturnable<Integer> cir){
        cir.setReturnValue(15);
    }
}
