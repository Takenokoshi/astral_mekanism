package astral_mekanism.block.blockentity.enchantedmachine.electric;

import org.jetbrains.annotations.NotNull;

import com.jerry.mekanism_extras.api.ExtraUpgrade;

import astral_mekanism.block.blockentity.basemachine.BEAMEElectricMachine;
import astral_mekanism.integration.AMEEmpowered;
import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEEnchantedCrusher extends BEAMEElectricMachine {
    private int baselineMaxOperations = 200;

    public BEEnchantedCrusher(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, "mekanism:crusher");
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<ItemStackToItemStackRecipe, SingleItem<ItemStackToItemStackRecipe>> getRecipeType() {
        return MekanismRecipeType.CRUSHING;
    }

    @Override
    protected int getBaselineMaxOperations() {
        return baselineMaxOperations;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableInt.create(this::getBaselineMaxOperations, v -> baselineMaxOperations = v));
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (AMEEmpowered.empoweredIsLoaded()) {
            if (AMEEmpowered.isEmpoweredSpeed(upgrade) || upgrade == Upgrade.SPEED || upgrade == ExtraUpgrade.STACK) {
                baselineMaxOperations = 200 * ((1 << upgradeComponent.getUpgrades(Upgrade.SPEED)) + (2 << AMEEmpowered
                        .getEmpoweredSpeeds(this))) << upgradeComponent.getUpgrades(ExtraUpgrade.STACK);
            }
        } else if (upgrade == Upgrade.SPEED || upgrade == ExtraUpgrade.STACK) {
            baselineMaxOperations = 200 << (upgradeComponent.getUpgrades(Upgrade.SPEED)
                    + upgradeComponent.getUpgrades(ExtraUpgrade.STACK));
        }
    }
    
}
