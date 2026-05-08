package astral_mekanism.block.blockentity.enchantedmachine;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import com.jerry.mekanism_extras.api.ExtraUpgrade;

import astral_mekanism.block.blockentity.basemachine.BEAMERotaryCondensentrator;
import astral_mekanism.integration.AMEEmpowered;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.Upgrade;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.math.MathUtils;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.chemical.variable.VariableCapacityChemicalTankBuilder;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.fluid.VariableCapacityFluidTank;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.inventory.container.sync.SyncableLong;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BEEnchantedRotaryCondensentrator extends BEAMERotaryCondensentrator {

    private int baselineMaxOperations = 200;
    private int fluidTankCapacity = 200 * 5000;
    private long gasTankCapacity = 200 * 5000;

    public BEEnchantedRotaryCondensentrator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
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
        container.track(SyncableInt.create(this::getFluidTankCapacity, v -> {
            if (mode) {
                fluidTankCapacity = v;
            }
        }));
        container.track(SyncableLong.create(this::getGasTankCapacity, v -> {
            if (!mode) {
                gasTankCapacity = v;
            }
        }));
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
        fluidTankCapacity = MathUtils.clampToInt(gasTankCapacity);
    }

    private int getFluidTankCapacity() {
        return mode ? fluidTankCapacity : 0x7fffffff;
    }

    private long getGasTankCapacity() {
        return mode ? Long.MAX_VALUE : gasTankCapacity;
    }

    @Override
    protected IGasTank createGasTank(BiPredicate<Gas, AutomationType> canExtract,
            BiPredicate<Gas, AutomationType> canInsert, Predicate<Gas> validator,
            @Nullable ChemicalAttributeValidator attributeValidator, @Nullable IContentsListener listener) {
                return VariableCapacityChemicalTankBuilder.GAS.create(this::getGasTankCapacity, canExtract, canInsert, validator, attributeValidator, listener);
    }

    @Override
    protected BasicFluidTank createFluidTank(BiPredicate<FluidStack, AutomationType> canExtract,
            BiPredicate<FluidStack, AutomationType> canInsert, Predicate<FluidStack> validator,
            @Nullable IContentsListener listener) {
                return VariableCapacityFluidTank.create(this::getFluidTankCapacity, canExtract, canInsert, validator, listener);
    }

}
