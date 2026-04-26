package astral_mekanism.block.blockentity.enchantedmachine;

import com.jerry.mekanism_extras.api.ExtraUpgrade;

import astral_mekanism.block.blockentity.prefab.BEAbstractAntiprotonicNucleosynthesizer;
import astral_mekanism.enums.AMEUpgrade;
import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEEnchantedAntiprotonicNucleosynthesizer
        extends BEAbstractAntiprotonicNucleosynthesizer<BEEnchantedAntiprotonicNucleosynthesizer> {
    private int baselineMaxOperations = 100;

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
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == AMEUpgrade.HYPER_SPEED.getValue() || upgrade == ExtraUpgrade.STACK) {
            baselineMaxOperations = 100 << (upgradeComponent.getUpgrades(AMEUpgrade.HYPER_SPEED.getValue())
                    + upgradeComponent.getUpgrades(ExtraUpgrade.STACK));
        }
    }

}
