package astral_mekanism.block.blockentity.enchantedmachine;

import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import com.jerry.mekanism_extras.api.ExtraUpgrade;

import astral_mekanism.block.blockentity.basemachine.BEAMEElectrolyticSeparator;
import astral_mekanism.integration.AMEEmpowered;
import mekanism.api.IContentsListener;
import mekanism.api.Upgrade;
import mekanism.api.math.MathUtils;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.fluid.VariableCapacityFluidTank;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BEEnchantedElectrolyticSeparator extends BEAMEElectrolyticSeparator {

    private int baselineMaxOperations = 200;
    private int fluidTankCapacity = 200 * 5000;

    public BEEnchantedElectrolyticSeparator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
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
        container.track(SyncableInt.create(this::getFluidTankCapacity, v -> fluidTankCapacity = v));
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
        fluidTankCapacity = MathUtils.clampToInt(5000l * baselineMaxOperations);
    }

    private int getFluidTankCapacity() {
        return fluidTankCapacity;
    }

    @Override
    protected BasicFluidTank createFluidTank(Predicate<FluidStack> validator, @Nullable IContentsListener listener) {
        return VariableCapacityFluidTank.input(this::getFluidTankCapacity, validator, listener);
    }

}
