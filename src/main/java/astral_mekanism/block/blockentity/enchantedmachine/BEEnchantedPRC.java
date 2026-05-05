package astral_mekanism.block.blockentity.enchantedmachine;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.jerry.mekanism_extras.api.ExtraUpgrade;

import astral_mekanism.block.blockentity.basemachine.BEAMEPressurizedReactionChamber;
import astral_mekanism.integration.AMEEmpowered;
import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.PressurizedReactionRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEEnchantedPRC extends BEAMEPressurizedReactionChamber {
    private int baselineMaxOperations = 600;
    private int recipeTicksRequired = 200;
    private int ticksRequired = 200;
    private int operatingTicks = 0;

    public BEEnchantedPRC(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return baselineMaxOperations;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableInt.create(this::getBaselineMaxOperations, v -> baselineMaxOperations = v));
        container.track(SyncableInt.create(this::getTicksRequired, v -> ticksRequired = v));
        container.track(SyncableInt.create(() -> this.operatingTicks, this::setOperatingTicks));
        container.track(SyncableInt.create(() -> this.recipeTicksRequired, v -> recipeTicksRequired = v));
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == Upgrade.SPEED) {
            recalculateTicks();
        } else if (upgrade == ExtraUpgrade.STACK) {
            baselineMaxOperations = 600 << upgradeComponent.getUpgrades(ExtraUpgrade.STACK);
        } else if (AMEEmpowered.empoweredIsLoaded() && AMEEmpowered.isEmpoweredEnergy(upgrade)) {
            recalculateTicks();
        }
    }

    @Override
    public @NotNull CachedRecipe<PressurizedReactionRecipe> createNewCachedRecipe(
            @NotNull PressurizedReactionRecipe recipe, int index) {
        return super.createNewCachedRecipe(recipe, index)
                .setRequiredTicks(this::getTicksRequired)
                .setOperatingTicksChanged(this::setOperatingTicks);
    }

    @Override
    public void onCachedRecipeChanged(@Nullable CachedRecipe<PressurizedReactionRecipe> cachedRecipe, int cacheIndex) {
        super.onCachedRecipeChanged(cachedRecipe, cacheIndex);
        recipeTicksRequired = cachedRecipe.getRecipe().getDuration();
        recalculateTicks();
    }

    private void recalculateTicks() {
        ticksRequired = AMEEmpowered.empoweredIsLoaded()
                ? AMEEmpowered.getTicks(recipeTicksRequired, this)
                : MekanismUtils.getTicks(this, recipeTicksRequired);
    }

    @Override
    public double getScaledProgress() {
        return getActive() ? operatingTicks / (double) recipeTicksRequired : 0;
    }

    private void setOperatingTicks(int v) {
        operatingTicks = v;
    }

    private int getTicksRequired() {
        return ticksRequired;
    }

}
