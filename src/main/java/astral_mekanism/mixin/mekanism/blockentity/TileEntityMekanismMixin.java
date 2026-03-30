package astral_mekanism.mixin.mekanism.blockentity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import astral_mekanism.enumexpansion.AMEUpgrade;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.component.TileComponentUpgrade;
import mekanism.common.tile.interfaces.IUpgradeTile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

@Mixin(value = TileEntityMekanism.class, remap = false)
public class TileEntityMekanismMixin {

    @Shadow
    TileComponentUpgrade upgradeComponent;

    @Inject(method = "onUpdateServer", at = @At("HEAD"))
    private void onUpdateServerInject(CallbackInfo ci) {
        if (((IUpgradeTile) (Object) this).supportsUpgrades()) {
            if (upgradeComponent.isUpgradeInstalled(AMEUpgrade.COBBLESTONE_SUPPLY)) {
                ItemStack toInsert = Items.COBBLESTONE.getDefaultInstance();
                toInsert.setCount(Math.min(Items.COBBLESTONE.getMaxStackSize(toInsert),
                        (1 << upgradeComponent.getUpgrades(AMEUpgrade.COBBLESTONE_SUPPLY) * 2 - 1) - 1));
                ((TileEntityMekanism) (Object) this).getInventorySlots(null).forEach(slot -> {
                    slot.insertItem(toInsert, Action.EXECUTE, AutomationType.EXTERNAL);
                });
            }
            if (upgradeComponent.isUpgradeInstalled(AMEUpgrade.WATER_SUPPLY)) {
                ((TileEntityMekanism) (Object) this).getFluidTanks(null).forEach(tank -> {
                    tank.insert(new FluidStack(Fluids.WATER, 0x7fffffff), Action.EXECUTE, AutomationType.EXTERNAL);
                });
            }
        }
    }
}
