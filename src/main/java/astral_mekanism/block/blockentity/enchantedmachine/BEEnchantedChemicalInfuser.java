package astral_mekanism.block.blockentity.enchantedmachine;

import java.util.function.Predicate;

import com.jerry.mekanism_extras.api.ExtraUpgrade;

import astral_mekanism.block.blockentity.basemachine.BEAMEChemicalInfuser;
import astral_mekanism.integration.AMEEmpowered;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.Upgrade;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.chemical.variable.VariableCapacityChemicalTankBuilder;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.inventory.container.sync.SyncableLong;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEEnchantedChemicalInfuser extends BEAMEChemicalInfuser {
    private int baselineMaxOperations = 200;
    private long gasTankCapacity = 200 * 5000;

    public BEEnchantedChemicalInfuser(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
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
    protected IGasTank createGasTank(Predicate<Gas> canInsert, Predicate<Gas> validator,
            IContentsListener recipeCacheListener) {
        return VariableCapacityChemicalTankBuilder.GAS.create(this::getGasTankCapacity,
                (gas, type) -> type == AutomationType.MANUAL,
                (gas, type) -> canInsert.test(gas), validator,
                ChemicalAttributeValidator.ALWAYS_ALLOW, recipeCacheListener);
    }
}
