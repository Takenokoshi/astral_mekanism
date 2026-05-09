package astral_mekanism.block.blockentity.enchantedmachine;

import java.util.function.Predicate;

import com.jerry.mekanism_extras.api.ExtraUpgrade;

import astral_mekanism.block.blockentity.basemachine.BEAMESolidifier;
import astral_mekanism.integration.AMEEmpowered;
import mekanism.api.AutomationType;
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

public class BEEnchantedSolidifier extends BEAMESolidifier {

    int baselineMaxOperations = 200;
    private int inputTankCapacity = 200 * 5000;

    public BEEnchantedSolidifier(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    private int getInputTankCapacity() {
        return inputTankCapacity;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableInt.create(this::getBaselineMaxOperations, v -> baselineMaxOperations = v));
        container.track(SyncableInt.create(this::getInputTankCapacity, v -> inputTankCapacity = v));
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
        inputTankCapacity = MathUtils.clampToInt(5000l * baselineMaxOperations);
    }

    @Override
    protected BasicFluidTank createFluidTank(Predicate<FluidStack> canInsert, Predicate<FluidStack> validator,
            IContentsListener listener) {
        return VariableCapacityFluidTank.create(this::getInputTankCapacity,
                (fluid, type) -> type == AutomationType.MANUAL,
                (fluid, type) -> canInsert.test(fluid), validator, listener);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return baselineMaxOperations;
    }

}
