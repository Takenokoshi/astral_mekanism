package astral_mekanism.block.blockentity.appliedmachine.prefab;

import org.jetbrains.annotations.NotNull;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.storage.MEStorage;
import astral_mekanism.block.blockentity.base.BENetworkConfigurableMachine;
import astral_mekanism.block.blockentity.interf.IPacketReceiverSetLong;
import astral_mekanism.enums.AppliedMixingReactorMode;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.IContentsListener;
import mekanism.api.heat.HeatAPI;
import mekanism.api.heat.HeatAPI.HeatTransfer;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.heat.BasicHeatCapacitor;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.heat.VariableHeatCapacitor;
import mekanism.common.capabilities.holder.heat.HeatCapacitorHelper;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableDouble;
import mekanism.common.inventory.container.sync.SyncableEnum;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.interfaces.IHasMode;
import mekanism.common.util.NBTUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public abstract class BEAbstractAppliedMixingReactor extends BENetworkConfigurableMachine
        implements IPacketReceiverSetLong, IHasMode {

    private static final double caseHeatCapacity = 1;
    private static final double inverseInsulation = 100_000;
    private long efficiency;
    protected BasicHeatCapacitor heatCapacitor;
    private double lastEnvironmentLoss;
    private double lastTransferLoss;
    public final MekanismKey mixedFuelKey;
    public final MekanismKey leftFuelKey;
    public final MekanismKey rightFuelKey;
    public final AEFluidKey waterKey;
    public final MekanismKey steamKey;
    private AppliedMixingReactorMode mode;

    public BEAbstractAppliedMixingReactor(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        configComponent = new TileComponentConfig(this, TransmissionType.HEAT);
        configComponent.setupOutputConfig(TransmissionType.HEAT, heatCapacitor);
        ejectorComponent = new TileComponentEjector(this, () -> Long.MAX_VALUE);
        efficiency = 0;
        lastEnvironmentLoss = 0d;
        lastTransferLoss = 0d;
        this.mixedFuelKey = getMixedFuelKey();
        this.leftFuelKey = getLeftFuelKey();
        this.rightFuelKey = getRightFuelKey();
        this.waterKey = AEFluidKey.of(Fluids.WATER);
        this.steamKey = getSteamKey();
        this.mode = AppliedMixingReactorMode.MIXED_ONLY;
    }

    protected abstract MekanismKey getMixedFuelKey();

    protected abstract MekanismKey getLeftFuelKey();

    protected abstract MekanismKey getRightFuelKey();

    protected abstract MekanismKey getSteamKey();

    @NotNull
    @Override
    protected IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener,
            CachedAmbientTemperature ambientTemperature) {
        HeatCapacitorHelper builder = HeatCapacitorHelper.forSideWithConfig(this::getDirection, this::getConfig);
        double biomeAmbientTemp = HeatAPI.getAmbientTemp(getLevel(), getTilePos());
        builder.addCapacitor(
                heatCapacitor = VariableHeatCapacitor.create(caseHeatCapacity, this::getInverseConductionCoefficient,
                        () -> inverseInsulation, () -> biomeAmbientTemp, this));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        HeatTransfer loss = simulate();
        lastEnvironmentLoss = loss.environmentTransfer();
        lastTransferLoss = loss.adjacentTransfer();
        long burned = burn();
        heat(burned);
        generateSteam(burned);
    }

    protected long burn() {
        MEStorage storage = getMeStorage();
        if (storage == null || heatCapacitor.getTemperature() < workableTemp()) {
            return 0;
        }
        IActionSource source = IActionSource.ofMachine(this);
        if (mode == AppliedMixingReactorMode.MIXED_ONLY) {
            return storage.extract(mixedFuelKey, efficiency, Actionable.MODULATE, source);
        } else if (mode == AppliedMixingReactorMode.MIXED_PRIORITIZE) {
            long mixed = storage.extract(mixedFuelKey, efficiency, Actionable.MODULATE, source);
            if (mixed + 1 < efficiency) {
                long unmixed = (efficiency - mixed) >> 1;
                unmixed = burnUnmixed(storage, unmixed, Actionable.SIMULATE, source);
                burnUnmixed(storage, unmixed, Actionable.MODULATE, source);
                return (unmixed << 1) + mixed;
            } else {
                return mixed;
            }
        } else if (mode == AppliedMixingReactorMode.UNMIXED_PRIORITIZE) {
            long unmixed = burnUnmixed(storage, efficiency >> 1, Actionable.SIMULATE, source);
            burnUnmixed(storage, unmixed, Actionable.MODULATE, source);
            if (unmixed << 1 < efficiency) {
                return (unmixed << 1)
                        + storage.extract(mixedFuelKey, efficiency - (unmixed << 1), Actionable.MODULATE, source);
            } else {
                return unmixed << 1;
            }
        } else {
            long unmixed = burnUnmixed(storage, efficiency >> 1, Actionable.SIMULATE, source);
            burnUnmixed(storage, unmixed, Actionable.MODULATE, source);
            return unmixed << 1;
        }
    }

    private long burnUnmixed(MEStorage storage, long value, Actionable actionable, IActionSource source) {
        value = storage.extract(leftFuelKey, value, actionable, source);
        value = storage.extract(rightFuelKey, value, actionable, source);
        return value;
    }

    protected abstract void heat(long burned);

    protected abstract void generateSteam(long usedFuel);

    protected abstract double workableTemp();

    public void receive(int num, long value) {
        efficiency = value;
    }

    public long getEfficiency() {
        return efficiency;
    }

    public double getLastTransferLoss() {
        return lastTransferLoss;
    }

    public double getLastEnvironmentLoss() {
        return lastEnvironmentLoss;
    }

    public BasicHeatCapacitor getHeatCapacitor() {
        return heatCapacitor;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableLong.create(this::getEfficiency, value -> efficiency = value));
        container.track(SyncableDouble.create(this::getLastTransferLoss, value -> lastTransferLoss = value));
        container.track(SyncableDouble.create(this::getLastEnvironmentLoss, value -> lastEnvironmentLoss = value));
        container.track(SyncableEnum.create(AppliedMixingReactorMode::byIndex, AppliedMixingReactorMode.MIXED_ONLY,
                this::getMode, v -> mode = v));
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        try {
            NBTUtils.setLongIfPresent(nbt, "mixingRate", value -> efficiency = value);
            NBTUtils.setStringIfPresent(nbt, "mode", v -> mode = AppliedMixingReactorMode.valueOf(v));
        } catch (Exception e) {
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags) {
        super.saveAdditional(nbtTags);
        nbtTags.putLong("mixingRate", efficiency);
        nbtTags.putString("mode", mode.name());
    }

    protected abstract double getInverseConductionCoefficient();

    public abstract ResourceLocation getJEICategoryName();

    public void nextMode() {
        mode = mode.getNext();
    };

    public void previousMode() {
        mode = mode.getPrev();
    };

    public AppliedMixingReactorMode getMode() {
        return mode;
    }
}
