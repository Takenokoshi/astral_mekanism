package astral_mekanism.block.blockentity.enchantedmachine.advanced;

import org.jetbrains.annotations.NotNull;

import com.jerry.mekanism_extras.api.ExtraUpgrade;

import astral_mekanism.block.blockentity.basemachine.BEAMEAdvancedMachine;
import astral_mekanism.integration.AMEEmpowered;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.Upgrade;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.common.capabilities.chemical.variable.VariableCapacityChemicalTankBuilder;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.ItemChemical;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEEnchantedPurificationChamber extends BEAMEAdvancedMachine {
    private int baselineMaxOperations = 200;
    private long gasTankCapacity = 200 * 5000;

    public BEEnchantedPurificationChamber(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, "mekanism:purification_chamber", 20);
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<ItemStackGasToItemStackRecipe, ItemChemical<Gas, GasStack, ItemStackGasToItemStackRecipe>> getRecipeType() {
        return MekanismRecipeType.INJECTING;
    }

    @Override
    protected int getBaselineMaxOperations() {
        return baselineMaxOperations;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableInt.create(this::getBaselineMaxOperations, v -> baselineMaxOperations = v));
        container.track(SyncableLong.create(this::getGasTankCapacity, v -> gasTankCapacity = v));
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
        gasTankCapacity = 5000l * baselineMaxOperations;
    }

    private long getGasTankCapacity() {
        return gasTankCapacity;
    }

    @Override
    protected IGasTank createGasTank(IContentsListener recipeCacheListener) {
        return VariableCapacityChemicalTankBuilder.GAS.create(this::getGasTankCapacity,
                (gas, type) -> type == AutomationType.MANUAL,
                (gas, type) -> containsRecipeBA(inputInventorySlot.getStack(), gas),
                this::containsRecipeB, ChemicalAttributeValidator.ALWAYS_ALLOW, recipeCacheListener);
    }

}
