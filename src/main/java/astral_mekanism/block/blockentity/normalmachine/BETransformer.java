package astral_mekanism.block.blockentity.normalmachine;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import appeng.recipes.transform.TransformRecipe;
import astral_mekanism.block.blockentity.prefab.BEAbstractTransformer;
import astral_mekanism.generalrecipe.cachedrecipe.GeneralCachedRecipe;
import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
import mekanism.api.NBTConstants;
import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UpgradeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class BETransformer extends BEAbstractTransformer {

    private int operatingTicks;
    protected int baseTicksRequired = 200;
    public int ticksRequired;

    public BETransformer(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int fluidTankCapacity() {
        return 20000;
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

    public double getScaledProgress() {
        return getOperatingTicks() / (double) ticksRequired;
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
        if (upgrade == Upgrade.SPEED) {
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
    }

    @Override
    protected GeneralCachedRecipe<TransformRecipe> operateCachedRecipe(
            GeneralCachedRecipe<TransformRecipe> cachedRecipe) {
        return cachedRecipe
                .setRequiredTicks(this::getTicksRequired)
                .setOperatingTicksChanged(this::setOperatingTicks);
    }

    @Override
    protected CachedRecipe<MekanicalTransformRecipe> operateCachedRecipe(
            CachedRecipe<MekanicalTransformRecipe> cachedRecipe) {
        return cachedRecipe
                .setRequiredTicks(this::getTicksRequired)
                .setOperatingTicksChanged(this::setOperatingTicks);
    }

    @Override
    public void changeMode() {
        operatingTicks = 0;
        super.changeMode();
    }

}
