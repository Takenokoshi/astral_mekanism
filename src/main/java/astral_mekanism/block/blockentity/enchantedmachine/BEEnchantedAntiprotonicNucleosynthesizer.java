package astral_mekanism.block.blockentity.enchantedmachine;

import com.jerry.mekanism_extras.api.ExtraUpgrade;

import astral_mekanism.block.blockentity.basemachine.BEAMEAntiprotonicNucleosynthesizer;
import astral_mekanism.enums.AMEUpgrade;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.Upgrade;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.chemical.variable.VariableCapacityChemicalTankBuilder;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.inventory.container.sync.SyncableLong;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEEnchantedAntiprotonicNucleosynthesizer
        extends BEAMEAntiprotonicNucleosynthesizer<BEEnchantedAntiprotonicNucleosynthesizer> {
    private int baselineMaxOperations = 100;
    private long inputTankCapacity = 100 * 5000;

    public BEEnchantedAntiprotonicNucleosynthesizer(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
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
        container.track(SyncableLong.create(this::getInputTankCapacity, v -> inputTankCapacity = v));
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == AMEUpgrade.HYPER_SPEED.getValue() || upgrade == ExtraUpgrade.STACK) {
            baselineMaxOperations = 100 << (upgradeComponent.getUpgrades(AMEUpgrade.HYPER_SPEED.getValue())
                    + upgradeComponent.getUpgrades(ExtraUpgrade.STACK));
            inputTankCapacity = 5000l * baselineMaxOperations;
        }
    }

    private long getInputTankCapacity() {
        return inputTankCapacity;
    }

    @Override
    protected IGasTank createInputTank(IContentsListener recipeCacheListener) {
        return VariableCapacityChemicalTankBuilder.GAS.create(this::getInputTankCapacity,
                (gas, a) -> a == AutomationType.MANUAL,
                (gas, a) -> containsRecipeBA(inputSlot.getStack(), gas),
                this::containsRecipeB,
                ChemicalAttributeValidator.ALWAYS_ALLOW, recipeCacheListener);
    }

}
