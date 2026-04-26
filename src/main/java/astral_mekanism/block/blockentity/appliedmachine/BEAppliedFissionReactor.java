package astral_mekanism.block.blockentity.appliedmachine;

import org.jetbrains.annotations.NotNull;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import appeng.util.ConfigInventory;
import astral_mekanism.block.blockentity.base.BENetworkConfigurableMachine;
import astral_mekanism.block.blockentity.interf.IPacketReceiverSetLong;
import astral_mekanism.item.recipecard.CoolantCardItem;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.attribute.GasAttributes.CooledCoolant;
import mekanism.api.heat.HeatAPI.HeatTransfer;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.heat.BasicHeatCapacitor;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.holder.heat.HeatCapacitorHelper;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableDouble;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.registries.MekanismGases;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.util.HeatUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NBTUtils;
import mekanism.generators.common.config.MekanismGeneratorsConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class BEAppliedFissionReactor extends BENetworkConfigurableMachine implements IPacketReceiverSetLong {

    private BasicHeatCapacitor heatCapacitor;
    private double lastEnvironmentLoss;
    private double lastTransferLoss;
    private long efficiency;
    private AEKey cooledCoolantKey;
    private AEKey heatedCoolantKey;
    private double thermalEnthalpy;
    private InputInventorySlot slot;
    private final MekanismKey fuel;
    private final MekanismKey waste;

    public BEAppliedFissionReactor(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        configComponent = new TileComponentConfig(this, TransmissionType.HEAT);
        configComponent.setupOutputConfig(TransmissionType.HEAT, heatCapacitor);
        ejectorComponent = new TileComponentEjector(this, () -> Long.MAX_VALUE);
        lastEnvironmentLoss = 0d;
        lastTransferLoss = 0d;
        efficiency = 0l;
        this.fuel = MekanismKey.of(MekanismGases.FISSILE_FUEL.getStack(1));
        this.waste = MekanismKey.of(MekanismGases.NUCLEAR_WASTE.getStack(1));
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(slot = InputInventorySlot.at(stack -> stack.getItem() instanceof CoolantCardItem, () -> {
            listener.onContentsChanged();
            recalculateCoolant();
        }, 131, 54));
        return builder.build();
    }

    @NotNull
    @Override
    protected IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener,
            CachedAmbientTemperature ambientTemperature) {
        HeatCapacitorHelper builder = HeatCapacitorHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addCapacitor(
                heatCapacitor = BasicHeatCapacitor.create(1000000d, 20d, 10d, ambientTemperature, listener));
        return builder.build();
    }

    private void recalculateCoolant() {
        if (slot.isEmpty() || !(slot.getStack().getItem() instanceof CoolantCardItem cardItem)) {
            cooledCoolantKey = null;
            heatedCoolantKey = null;
            thermalEnthalpy = 0;
            return;
        }
        ConfigInventory inv = cardItem.getConfigInventory(slot.getStack());
        if (inv == null || inv.isEmpty()) {
            cooledCoolantKey = null;
            heatedCoolantKey = null;
            thermalEnthalpy = 0;
            return;
        }
        AEKey key = inv.getKey(0);
        if (key instanceof MekanismKey mekanismKey) {
            if (mekanismKey.getForm() == MekanismKey.GAS && mekanismKey.getStack() instanceof GasStack gasStack) {
                if (gasStack.has(CooledCoolant.class)) {
                    cooledCoolantKey = mekanismKey;
                    gasStack.ifAttributePresent(CooledCoolant.class, coolant -> {
                        heatedCoolantKey = MekanismKey.of(coolant.getHeatedGas().getStack(1));
                        thermalEnthalpy = coolant.getThermalEnthalpy();
                    });
                    return;
                }
            }
        } else if (key instanceof AEFluidKey fluidKey) {
            if (fluidKey.getFluid().equals(Fluids.WATER)) {
                cooledCoolantKey = fluidKey;
                heatedCoolantKey = MekanismKey.of(MekanismGases.STEAM.getStack(1));
                thermalEnthalpy = HeatUtils.getWaterThermalEnthalpy();
                return;
            }
        }
        cooledCoolantKey = null;
        heatedCoolantKey = null;
        thermalEnthalpy = 0;
        return;
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        HeatTransfer loss = simulate();
        lastEnvironmentLoss = loss.environmentTransfer();
        lastTransferLoss = loss.adjacentTransfer();
        if (MekanismUtils.canFunction(this)) {
            fission();
            heatCoolant();
        }
    }

    private void fission() {
        MEStorage meStorage = getMeStorage();
        if (meStorage == null) {
            return;
        }
        long toUse = efficiency;
        toUse = meStorage.extract(fuel, toUse, Actionable.SIMULATE, IActionSource.ofMachine(this));
        toUse = meStorage.insert(waste, toUse, Actionable.SIMULATE, IActionSource.ofMachine(this));
        if (toUse > 0) {
            heatCapacitor
                    .handleHeat(toUse * MekanismGeneratorsConfig.generators.energyPerFissionFuel.get().doubleValue());
            meStorage.extract(fuel, toUse, Actionable.MODULATE, IActionSource.ofMachine(this));
            meStorage.insert(waste, toUse, Actionable.MODULATE, IActionSource.ofMachine(this));
        }
    }

    private void heatCoolant() {
        MEStorage meStorage = getMeStorage();
        if (meStorage == null || cooledCoolantKey == null || heatedCoolantKey == null || thermalEnthalpy == 0) {
            return;
        }
        long toHeat = (long) (heatCapacitor.getHeat() / thermalEnthalpy);
        toHeat = meStorage.extract(cooledCoolantKey, toHeat, Actionable.SIMULATE, IActionSource.ofMachine(this));
        toHeat = meStorage.insert(heatedCoolantKey, toHeat, Actionable.SIMULATE, IActionSource.ofMachine(this));
        if (toHeat > 0) {
            heatCapacitor.handleHeat(-toHeat * thermalEnthalpy);
            meStorage.extract(cooledCoolantKey, toHeat, Actionable.MODULATE, IActionSource.ofMachine(this));
            meStorage.insert(heatedCoolantKey, toHeat, Actionable.MODULATE, IActionSource.ofMachine(this));
        }
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

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableLong.create(this::getEfficiency, value -> efficiency = value));
        container.track(SyncableDouble.create(this::getLastTransferLoss, value -> lastTransferLoss = value));
        container.track(SyncableDouble.create(this::getLastEnvironmentLoss, value -> lastEnvironmentLoss = value));
    }

    public BasicHeatCapacitor getHeatCapacitor(){
        return heatCapacitor;
    }

    @Override
    public void receive(int num, long value) {
        efficiency = value;
        markForSave();
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        NBTUtils.setLongIfPresent(nbt, "efficiency", value -> efficiency = value);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags) {
        super.saveAdditional(nbtTags);
        nbtTags.putLong("efficiency", efficiency);
    }

    public AEKey getFuelkey(){
        return fuel;
    }

    public AEKey getWastekey(){
        return waste;
    }

    public AEKey getCooledCoolantKey(){
        return cooledCoolantKey;
    }

    public AEKey getHeatedCoolantKey(){
        return heatedCoolantKey;
    }
}
