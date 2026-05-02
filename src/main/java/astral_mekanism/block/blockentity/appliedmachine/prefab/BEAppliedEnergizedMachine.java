package astral_mekanism.block.blockentity.appliedmachine.prefab;

import com.glodblock.github.appflux.common.me.key.FluxKey;
import com.glodblock.github.appflux.common.me.key.type.EnergyType;
import com.glodblock.github.appflux.config.AFConfig;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.storage.MEStorage;
import astral_mekanism.block.blockentity.base.BENetworkMekanismMachine;
import astral_mekanism.integration.AMEEmpowered;
import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableDouble;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEAppliedEnergizedMachine extends BENetworkMekanismMachine {

    private final long fePerProcess;
    private final int fePerByte;
    private double feBytesPerProcess;
    public final FluxKey feKey;

    public BEAppliedEnergizedMachine(IBlockProvider blockProvider, BlockPos pos, BlockState state, long fePerProcess) {
        super(blockProvider, pos, state);
        feKey = FluxKey.of(EnergyType.FE);
        this.fePerProcess = fePerProcess;
        this.fePerByte = AFConfig.getFluxPerByte();
        this.feBytesPerProcess = ((double) this.fePerProcess) / fePerByte;
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        if (AMEEmpowered.empoweredIsLoaded()) {
            if (upgrade == Upgrade.ENERGY || AMEEmpowered.isEmpoweredEnergy(upgrade)) {
                feBytesPerProcess = fePerProcess
                        * Math.pow(MekanismConfig.general.maxUpgradeMultiplier.get(),
                                -upgradeComponent.getUpgrades(Upgrade.ENERGY) / 8d)
                        * Math.pow(20, -AMEEmpowered.getEmpoweredEnergies(this) / 8d)
                        / fePerByte;
            }
        } else if (upgrade == Upgrade.ENERGY) {
            feBytesPerProcess = fePerProcess
                    * Math.pow(MekanismConfig.general.maxUpgradeMultiplier.get(),
                            -upgradeComponent.getUpgrades(Upgrade.ENERGY) / 8d)
                    / fePerByte;
        }
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableDouble.create(() -> feBytesPerProcess, value -> feBytesPerProcess = value));
    }

    protected long getSupportableOperations(MEStorage storage, IActionSource source) {
        return (long) (storage.extract(feKey, Long.MAX_VALUE, Actionable.SIMULATE, source) / feBytesPerProcess);
    }

    protected void consumeEnergy(MEStorage storage, IActionSource source, long operations) {
        storage.extract(feKey, (long) (operations * feBytesPerProcess), Actionable.MODULATE, source);
    }
}
