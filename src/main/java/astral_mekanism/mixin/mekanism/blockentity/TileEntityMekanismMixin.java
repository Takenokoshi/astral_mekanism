package astral_mekanism.mixin.mekanism.blockentity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import astral_mekanism.enumexpansion.AMEUpgrade;
import astral_mekanism.registries.AstralMekanismGases;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.Upgrade;
import mekanism.api.math.MathUtils;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.component.TileComponentUpgrade;
import mekanism.common.tile.interfaces.IUpgradeTile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

@Mixin(value = TileEntityMekanism.class, remap = false)
public class TileEntityMekanismMixin {

    @Unique
    int astral_mekanism$cobblestoneTick = 0;

    @Unique
    int astral_mekanism$cobblestoneBaseTick = 200;

    @Unique
    int astral_mekanism$cobblestoneUpgrades = 0;

    @Unique
    int astral_mekanism$cobblestoneCount = 1;

    @Shadow
    TileComponentUpgrade upgradeComponent;

    @Inject(method = "onUpdateServer", at = @At("HEAD"))
    private void astral_mekanism$onUpdateServerInject(CallbackInfo ci) {
        if (((IUpgradeTile) (Object) this).supportsUpgrades()) {
            if (upgradeComponent.isUpgradeInstalled(AMEUpgrade.WATER_SUPPLY.getValue())) {
                ((TileEntityMekanism) (Object) this).getFluidTanks(null).forEach(tank -> {
                    tank.insert(new FluidStack(Fluids.WATER, 0x7fffffff), Action.EXECUTE, AutomationType.EXTERNAL);
                });
            }
            if (upgradeComponent.isUpgradeInstalled(AMEUpgrade.AIR_INTAKE.getValue())) {
                ((TileEntityMekanism) (Object) this).getGasTanks(null).forEach(tank -> {
                    tank.insert(AstralMekanismGases.AIR.getStack(Long.MAX_VALUE), Action.EXECUTE,
                            AutomationType.EXTERNAL);
                });
            }
            if (astral_mekanism$cobblestoneUpgrades > 0) {
                astral_mekanism$cobblestoneTick++;
                if (astral_mekanism$cobblestoneTick < astral_mekanism$cobblestoneBaseTick) {
                    return;
                }
                astral_mekanism$cobblestoneTick = 0;
                ItemStack toInsert = Items.COBBLESTONE.getDefaultInstance();
                toInsert.setCount(
                        Math.min(Items.COBBLESTONE.getMaxStackSize(toInsert), astral_mekanism$cobblestoneCount));
                ((TileEntityMekanism) (Object) this).getInventorySlots(null).forEach(slot -> {
                    slot.insertItem(toInsert, Action.EXECUTE, AutomationType.EXTERNAL);
                });
            }
        }
    }

    @Inject(method = "recalculateUpgrades", at = @At("HEAD"))
    protected void astral_mekanism$recalculateUpgradesInject(Upgrade upgrade, CallbackInfo ci) {
        if (upgrade == AMEUpgrade.COBBLESTONE_SUPPLY.getValue()) {
            astral_mekanism$cobblestoneUpgrades = upgradeComponent
                    .getUpgrades(AMEUpgrade.COBBLESTONE_SUPPLY.getValue());// 0~32
            astral_mekanism$cobblestoneBaseTick = astral_mekanism$cobblestoneUpgrades < 16
                    ? MathUtils.clampToInt(200d / 15 * (16 - astral_mekanism$cobblestoneUpgrades))
                    : 1;
            astral_mekanism$cobblestoneCount = astral_mekanism$cobblestoneUpgrades < 17 ? 1
                    : (1 << (astral_mekanism$cobblestoneUpgrades * 2 - 33)) - 1;

        }
    }

    @Inject(method = "shouldDumpRadiation", at = @At("HEAD"), cancellable = true)
    private void astral_mekanism$shouldDumpRadiationInject(CallbackInfoReturnable<Boolean> cir) {
        if (((IUpgradeTile) (Object) this).supportsUpgrades()) {
            cir.setReturnValue(!upgradeComponent.isUpgradeInstalled(AMEUpgrade.RADIOACTIVE_SEALING.getValue()));
            cir.cancel();
        }
    }
}
