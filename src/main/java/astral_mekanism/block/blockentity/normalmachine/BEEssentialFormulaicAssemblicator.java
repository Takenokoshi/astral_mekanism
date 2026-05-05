package astral_mekanism.block.blockentity.normalmachine;

import org.jetbrains.annotations.NotNull;

import com.jerry.mekanism_extras.api.ExtraUpgrade;

import astral_mekanism.block.blockentity.basemachine.BEAMEFormulaicAssemblicator;
import astral_mekanism.integration.AMEEmpowered;
import mekanism.api.NBTConstants;
import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class BEEssentialFormulaicAssemblicator extends BEAMEFormulaicAssemblicator {

    int baselineMaxOperations = 1;
    int ticksRequired = 200;
    int operatingTicks = 0;

    public BEEssentialFormulaicAssemblicator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected boolean runCraft() {
        if (operatingTicks < ticksRequired) {
            operatingTicks++;
            return true;
        } else {
            operatingTicks = 0;
            return super.runCraft();
        }
    }

    @Override
    protected int getBaselineMaxOperations() {
        return baselineMaxOperations;
    }

    @Override
    public double getScaledProgress() {
        return operatingTicks / (double) ticksRequired;
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        operatingTicks = nbt.getInt(NBTConstants.PROGRESS);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags) {
        super.saveAdditional(nbtTags);
        nbtTags.putInt(NBTConstants.PROGRESS, operatingTicks);
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == ExtraUpgrade.STACK) {
            ticksRequired = 1 << upgradeComponent.getUpgrades(ExtraUpgrade.STACK);
        } else if (AMEEmpowered.empoweredIsLoaded()) {
            if (upgrade == Upgrade.ENERGY || AMEEmpowered.isEmpoweredSpeed(upgrade)) {
                ticksRequired = AMEEmpowered.getTicks(200, this);
            }
        } else if (upgrade == Upgrade.SPEED) {
            ticksRequired = MekanismUtils.getTicks(this, 200);
        }
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableInt.create(this::getBaselineMaxOperations, v -> baselineMaxOperations = v));
        container.track(SyncableInt.create(() -> this.operatingTicks, v -> operatingTicks = v));
        container.track(SyncableInt.create(() -> this.ticksRequired, v -> ticksRequired = v));
    }

}
