package astral_mekanism.block.blockentity.compact;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.AMETier;
import astral_mekanism.block.blockentity.elements.ExtendedComponentEjector;
import astral_mekanism.block.blockentity.interf.IPacketReceiverSetLong;
import astral_mekanism.enumexpansion.AMEDataType;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.NBTConstants;
import mekanism.api.RelativeSide;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.api.heat.IHeatCapacitor;
import mekanism.api.heat.HeatAPI;
import mekanism.api.heat.HeatAPI.HeatTransfer;
import mekanism.api.math.FloatingLong;
import mekanism.api.math.MathUtils;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.providers.IGasProvider;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.fluid.VariableCapacityFluidTank;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.heat.VariableHeatCapacitor;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.heat.HeatCapacitorHelper;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.common.config.value.CachedDoubleValue;
import mekanism.common.config.value.CachedFloatingLongValue;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableDouble;
import mekanism.common.inventory.container.sync.SyncableFloatingLong;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tags.MekanismTags;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo.GasSlotInfo;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.util.HeatUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NBTUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BECompactMixingReactor extends TileEntityConfigurableMachine
        implements IPacketReceiverSetLong {

    private final double burnTemperature = initBurnTemperature();
    private static final double plasmaHeatCapacity = 100;
    private static final double caseHeatCapacity = 1;
    private static final double inverseInsulation = 100_000;
    private static final double plasmaCaseConductivity = 0.2;

    private AMETier tier;
    private long injectionRate;
    public IHeatCapacitor heatCapacitor;
    protected IGasTank leftFuelTank;
    protected IGasTank mixedFuelTank;
    protected IGasTank rightFuelTank;
    protected BasicFluidTank waterTank;
    protected IGasTank steamTank;
    protected BasicEnergyContainer energyContainer;
    protected long lastBurnedFuel = 0;

    private double lastPlasmaTemperature;
    private double lastCaseTemperature;
    private double lastEnvironmentLoss;
    private double lastTransferLoss;
    public double plasmaTemperature;
    private double biomeAmbientTemp;

    private int maxWater;
    public FloatingLong energyGeneration;

    private final CachedDoubleValue casingThermalConductivity;
    private final CachedDoubleValue thermocoupleEfficiency;
    private final CachedFloatingLongValue energyPerFuel;
    private final CachedDoubleValue waterHeatingRatio;

    public BECompactMixingReactor(IBlockProvider blockProvider, BlockPos pos, BlockState state,
            CachedDoubleValue casingThermalConductivity, CachedDoubleValue thermocoupleEfficiency,
            CachedFloatingLongValue energyPerFuel, CachedDoubleValue waterHeatingRatio) {
        super(blockProvider, pos, state);
        this.casingThermalConductivity = casingThermalConductivity;
        this.thermocoupleEfficiency = thermocoupleEfficiency;
        this.energyPerFuel = energyPerFuel;
        this.waterHeatingRatio = waterHeatingRatio;
        configComponent = new TileComponentConfig(this, TransmissionType.GAS, TransmissionType.FLUID,
                TransmissionType.HEAT, TransmissionType.ENERGY);
        ConfigInfo gasInfo = configComponent.getConfig(TransmissionType.GAS);
        gasInfo.addSlotInfo(AMEDataType.LEFT_FUEL, new GasSlotInfo(true, false, leftFuelTank));
        gasInfo.addSlotInfo(AMEDataType.MIXED_FUEL, new GasSlotInfo(true, false, mixedFuelTank));
        gasInfo.addSlotInfo(AMEDataType.RIGHT_FUEL, new GasSlotInfo(true, false, rightFuelTank));
        gasInfo.addSlotInfo(AMEDataType.STEAM, new GasSlotInfo(false, true, steamTank));
        gasInfo.setCanEject(true);
        gasInfo.setDataType(AMEDataType.MIXED_FUEL, RelativeSide.values());
        gasInfo.setDataType(AMEDataType.STEAM, RelativeSide.RIGHT);
        configComponent.setupInputConfig(TransmissionType.FLUID, waterTank);
        configComponent.setupIOConfig(TransmissionType.HEAT, heatCapacitor, heatCapacitor,
                RelativeSide.RIGHT, true, true);
        configComponent.setupOutputConfig(TransmissionType.ENERGY, energyContainer, RelativeSide.values());
        ejectorComponent = new ExtendedComponentEjector(this, () -> Long.MAX_VALUE, () -> 0,
                () -> FloatingLong.MAX_VALUE)
                .setOutputData(configComponent, TransmissionType.GAS, TransmissionType.HEAT, TransmissionType.ENERGY)
                .setCanChemicalTankEject((tank, type) -> tank == steamTank && type == AMEDataType.STEAM);
        injectionRate = 0;
        lastEnvironmentLoss = 0d;
        lastTransferLoss = 0d;
    }

    @Override
    protected void presetVariables() {
        super.presetVariables();
        tier = Attribute.getTier(getBlockType(), AMETier.class);
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(energyContainer = BasicEnergyContainer.output(initEnergyCapacity(), listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener,
            CachedAmbientTemperature ambientTemperature) {
        HeatCapacitorHelper builder = HeatCapacitorHelper.forSideWithConfig(this::getDirection, this::getConfig);
        biomeAmbientTemp = HeatAPI.getAmbientTemp(getLevel(), getTilePos());
        builder.addCapacitor(
                heatCapacitor = VariableHeatCapacitor.create(caseHeatCapacity, this::getInverseConductionCoefficient,
                        () -> inverseInsulation, () -> biomeAmbientTemp, listener));
        return builder.build();
    }

    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper
                .forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(leftFuelTank = ChemicalTankBuilder.GAS.create(tier.longValue,
                (gas, a) -> false,
                (gas, a) -> isLeftFuel(gas),
                this::isLeftFuel,
                ChemicalAttributeValidator.ALWAYS_ALLOW, listener));
        builder.addTank(mixedFuelTank = ChemicalTankBuilder.GAS.create(tier.longValue,
                (gas, a) -> false,
                (gas, a) -> isMixedFuel(gas),
                this::isMixedFuel,
                ChemicalAttributeValidator.ALWAYS_ALLOW, listener));
        builder.addTank(rightFuelTank = ChemicalTankBuilder.GAS.create(tier.longValue,
                (gas, a) -> false,
                (gas, a) -> isRightFuel(gas),
                this::isRightFuel,
                ChemicalAttributeValidator.ALWAYS_ALLOW, listener));
        builder.addTank(steamTank = ChemicalTankBuilder.GAS.output(Math.max(tier.longValue, 0x7fffffff), listener));
        return builder.build();
    }

    @Override
    public IFluidTankHolder getInitialFluidTanks(IContentsListener listener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(waterTank = VariableCapacityFluidTank.input(this::getMaxWater,
                fluid -> MekanismTags.Fluids.WATER_LOOKUP.contains(fluid.getFluid()), listener));
        return builder.build();
    }

    protected abstract FloatingLong initEnergyCapacity();

    protected abstract boolean isLeftFuel(Gas gas);

    protected abstract boolean isRightFuel(Gas gas);

    protected abstract IGasProvider mixedFuel();

    protected abstract boolean isMixedFuel(Gas gas);

    protected abstract double initBurnTemperature();

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        if (MekanismUtils.canFunction(this)) {
            mixFuel();
            burnFuel();
            if (lastBurnedFuel < 1) {
                setActive(false);
            } else {
                setActive(true);
            }
        } else {
            setActive(false);
        }
        transferHeat();
        updateHeatCapacitors(null);
        updateTemperatures();
    }

    private void burnFuel() {
        long fuelBurned = MathUtils
                .clampToLong(Mth.clamp((lastPlasmaTemperature - burnTemperature), 0, mixedFuelTank.getStored()));
        mixedFuelTank.shrinkStack(fuelBurned, Action.EXECUTE);
        setPlasmaTemp(
                getPlasmaTemp() + energyPerFuel.get().multiply(fuelBurned).divide(plasmaHeatCapacity).doubleValue());
        lastBurnedFuel = fuelBurned;
        return;
    }

    private void transferHeat() {
        double plasmaCaseHeat = plasmaCaseConductivity * (lastPlasmaTemperature - lastCaseTemperature);
        if (Math.abs(plasmaCaseHeat) > HeatAPI.EPSILON) {
            setPlasmaTemp(getPlasmaTemp() - plasmaCaseHeat / plasmaHeatCapacity);
            heatCapacitor.handleHeat(plasmaCaseHeat);
        }
        double caseWaterHeat = waterHeatingRatio.get() * (lastCaseTemperature - biomeAmbientTemp);
        if (Math.abs(caseWaterHeat) > HeatAPI.EPSILON) {
            int waterToSteam = (int) (HeatUtils.getSteamEnergyEfficiency() * caseWaterHeat
                    / HeatUtils.getWaterThermalEnthalpy());
            waterToSteam = Math.min(waterToSteam,
                    Math.min(waterTank.getFluidAmount(), MathUtils.clampToInt(steamTank.getNeeded())));
            if (waterToSteam > 0) {
                MekanismUtils.logMismatchedStackSize(waterTank.shrinkStack(waterToSteam, Action.EXECUTE), waterToSteam);
                generateSteam(waterToSteam);
                caseWaterHeat = waterToSteam * HeatUtils.getWaterThermalEnthalpy()
                        / HeatUtils.getSteamEnergyEfficiency();
                heatCapacitor.handleHeat(-caseWaterHeat);
            }
        }

        HeatTransfer heatTransfer = simulate();
        lastEnvironmentLoss = heatTransfer.environmentTransfer();
        lastTransferLoss = heatTransfer.adjacentTransfer();

        double caseAirHeat = casingThermalConductivity.get() * (lastCaseTemperature - biomeAmbientTemp);
        if (Math.abs(caseAirHeat) > HeatAPI.EPSILON) {
            heatCapacitor.handleHeat(-caseAirHeat);
            energyContainer.insert(energyGeneration = FloatingLong.create(caseAirHeat * thermocoupleEfficiency.get()),
                    Action.EXECUTE, AutomationType.INTERNAL);
        }
    }

    protected abstract void generateSteam(int waterToSteam);

    public void updateTemperatures() {
        lastPlasmaTemperature = getPlasmaTemp();
        lastCaseTemperature = heatCapacitor.getTemperature();
    }

    public void setPlasmaTemp(double temp) {
        if (plasmaTemperature != temp) {
            plasmaTemperature = temp;
        }
    }

    public void setLastPlasmaTemp(double temp) {
        lastPlasmaTemperature = temp;
    }

    public double getLastPlasmaTemp() {
        return lastPlasmaTemperature;
    }

    public double getLastCaseTemp() {
        return lastCaseTemperature;
    }

    public double getPlasmaTemp() {
        return plasmaTemperature;
    }

    public double getCaseTemp() {
        return heatCapacitor.getTemperature();
    }

    public int getMaxWater() {
        return maxWater;
    }

    public void setInjectionRate(long rate) {
        if (injectionRate != rate) {
            injectionRate = rate;
            maxWater = injectionRate > 2147 ? 0x7fffffff : (int) injectionRate * 1000000;
            if (level != null && !isRemote()) {
                if (!waterTank.isEmpty()) {
                    waterTank.setStackSize(Math.min(waterTank.getFluidAmount(), waterTank.getCapacity()),
                            Action.EXECUTE);
                }
            }
        }
    }

    private void mixFuel() {
        if (leftFuelTank.isEmpty() || rightFuelTank.isEmpty() || mixedFuelTank.getNeeded() < 2 || injectionRate < 2) {
            return;
        }
        if (!mixedFuelTank.isEmpty()) {
            return;
        }
        long amount = Math.min(Math.min(injectionRate / 2, mixedFuelTank.getNeeded() / 2),
                Math.min(leftFuelTank.getStored(), rightFuelTank.getStored()));
        leftFuelTank.shrinkStack(amount, Action.EXECUTE);
        rightFuelTank.shrinkStack(amount, Action.EXECUTE);
        mixedFuelTank.insert(mixedFuel().getStack(amount * 2), Action.EXECUTE, AutomationType.INTERNAL);
    }

    public void receive(int num, long value) {
        setInjectionRate(value);
    }

    public long getInjectionRate() {
        return injectionRate;
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
        container.track(SyncableLong.create(this::getInjectionRate, value -> injectionRate = value));
        container.track(SyncableDouble.create(this::getLastTransferLoss, value -> lastTransferLoss = value));
        container.track(SyncableDouble.create(this::getLastEnvironmentLoss, value -> lastEnvironmentLoss = value));
        container.track(SyncableInt.create(this::getMaxWater, v -> maxWater = v));
        container.track(SyncableLong.create(() -> lastBurnedFuel, v -> lastBurnedFuel = v));
        container.track(SyncableDouble.create(this::getLastPlasmaTemp, this::setLastPlasmaTemp));
        container.track(SyncableFloatingLong.create(() -> energyGeneration, v -> energyGeneration = v));
        container.track(SyncableDouble.create(this::getPlasmaTemp, this::setPlasmaTemp));
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        try {
            super.load(nbt);
            NBTUtils.setLongIfPresent(nbt, "mixingRate", this::setInjectionRate);
            NBTUtils.setDoubleIfPresent(nbt, NBTConstants.PLASMA_TEMP, this::setPlasmaTemp);
        } catch (Exception e) {
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags) {
        super.saveAdditional(nbtTags);
        nbtTags.putLong("mixingRate", injectionRate);
        nbtTags.putDouble(NBTConstants.PLASMA_TEMP, getPlasmaTemp());
    }

    public IGasTank getLeftFuelTank() {
        return leftFuelTank;
    }

    public IGasTank getMixedFuelTank() {
        return mixedFuelTank;
    }

    public IGasTank getRightFuelTank() {
        return rightFuelTank;
    }

    public IGasTank getSteamTank() {
        return steamTank;
    }

    public IExtendedFluidTank getWaterTank() {
        return waterTank;
    }

    protected abstract double getInverseConductionCoefficient();

    public abstract ResourceLocation getJEICategoryName();

    public BasicEnergyContainer getEnergyContainer() {
        return energyContainer;
    }

}
