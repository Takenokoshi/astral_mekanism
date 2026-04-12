package astral_mekanism.block.blockentity.base;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.jerry.mekanism_extras.api.ExtraUpgrade;

import astral_mekanism.integration.AMEEmpowered;
import mekanism.api.NBTConstants;
import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UpgradeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BEAppliedProgressMachine<RECIPE extends Recipe<?>> extends BEAppliedRecipeMachine<RECIPE> {

    private int operatingTicks;
    protected int baseTicksRequired;
    public int ticksRequired;
    protected int baselineMaxOperations = 1;

    public BEAppliedProgressMachine(IBlockProvider blockProvider, BlockPos pos, BlockState state,
            int baseTicksRequired) {
        super(blockProvider, pos, state);
        this.baseTicksRequired = baseTicksRequired;
        ticksRequired = this.baseTicksRequired;
        baselineMaxOperations = 1;
    }

    public double getScaledProgress() {
        return getOperatingTicks() / (double) ticksRequired;
    }

    protected void setOperatingTicks(int ticks) {
        this.operatingTicks = ticks;
    }

    public int getOperatingTicks() {
        return operatingTicks;
    }

    public int getTicksRequired() {
        return ticksRequired;
    }

    @Override
    public int getSavedOperatingTicks(int cacheIndex) {
        return getOperatingTicks();
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        operatingTicks = nbt.getInt(NBTConstants.PROGRESS);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags) {
        super.saveAdditional(nbtTags);
        nbtTags.putInt(NBTConstants.PROGRESS, getOperatingTicks());
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == ExtraUpgrade.STACK) {
            baselineMaxOperations = 1 << upgradeComponent.getUpgrades(ExtraUpgrade.STACK);
        } else if (AMEEmpowered.empoweredIsLoaded()) {
            AMEEmpowered.recalculateUpgrades(this, upgrade, baseTicksRequired, v -> ticksRequired = v);
        } else if (upgrade == Upgrade.SPEED) {
            ticksRequired = MekanismUtils.getTicks(this, baseTicksRequired);
        }
    }

    @NotNull
    @Override
    public List<Component> getInfo(@NotNull Upgrade upgrade) {
        return UpgradeUtils.getMultScaledInfo(this, upgrade);
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableInt.create(this::getOperatingTicks, this::setOperatingTicks));
        container.track(SyncableInt.create(this::getTicksRequired, value -> ticksRequired = value));
        container.track(SyncableInt.create(this::getBaselineMaxOperations, v -> baselineMaxOperations = v));
    }

    protected int getBaselineMaxOperations() {
        return baselineMaxOperations;
    }

}
