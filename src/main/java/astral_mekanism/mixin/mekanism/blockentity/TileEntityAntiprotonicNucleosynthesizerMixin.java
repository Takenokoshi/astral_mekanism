package astral_mekanism.mixin.mekanism.blockentity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.SoftOverride;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import astral_mekanism.enums.AMEUpgrade;
import mekanism.api.Upgrade;
import mekanism.api.recipes.NucleosynthesizingRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.tile.machine.TileEntityAntiprotonicNucleosynthesizer;

@Mixin(value = TileEntityAntiprotonicNucleosynthesizer.class, remap = false)
public class TileEntityAntiprotonicNucleosynthesizerMixin extends TileEntityMekanismMixin {

    @Unique
    int astral_mekanism$antiprotonic$recipeTicksRequired = 400;

    @Inject(method = "onCachedRecipeChanged", at = @At("RETURN"))
    private void astral_mekanism$antiprotonic$onCachedRecipeChangedInject(
            CachedRecipe<NucleosynthesizingRecipe> cachedRecipe,
            int cacheIndex, CallbackInfo ci) {
        astral_mekanism$antiprotonic$recipeTicksRequired = cachedRecipe.getRecipe().getDuration();
        TileEntityAntiprotonicNucleosynthesizer self = (TileEntityAntiprotonicNucleosynthesizer) (Object) this;
        int hyperSpeed = self.getComponent().getUpgrades(AMEUpgrade.HYPER_SPEED.getValue());
        self.ticksRequired = (int) (astral_mekanism$antiprotonic$recipeTicksRequired
                * Math.pow(MekanismConfig.general.maxUpgradeMultiplier.get(), -hyperSpeed / 8d));
    }

    @Override
    @SoftOverride
    protected void astral_mekanism$recalculateUpgradesInject(Upgrade upgrade, CallbackInfo ci) {
        super.astral_mekanism$recalculateUpgradesInject(upgrade, ci);
        if (upgrade == AMEUpgrade.HYPER_SPEED.getValue()) {
            TileEntityAntiprotonicNucleosynthesizer self = (TileEntityAntiprotonicNucleosynthesizer) (Object) this;
            int hyperSpeed = self.getComponent().getUpgrades(AMEUpgrade.HYPER_SPEED.getValue());
            self.ticksRequired = (int) (astral_mekanism$antiprotonic$recipeTicksRequired
                    * Math.pow(MekanismConfig.general.maxUpgradeMultiplier.get(), -hyperSpeed / 8d));
        }
    }

    @Inject(method = "addContainerTrackers", at = @At("HEAD"))
    private void astral_mekanism$antiprotonic$addContainerTrackersInject(MekanismContainer container, CallbackInfo ci) {
        container.track(SyncableInt.create(() -> astral_mekanism$antiprotonic$recipeTicksRequired,
                v -> astral_mekanism$antiprotonic$recipeTicksRequired = v));
    }
}
