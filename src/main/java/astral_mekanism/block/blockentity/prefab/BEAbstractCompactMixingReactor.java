package astral_mekanism.block.blockentity.prefab;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.AMETier;
import astral_mekanism.block.blockentity.elements.ExtendedComponentEjector;
import astral_mekanism.block.blockentity.interf.IPacketReceiverSetLong;
import astral_mekanism.enumexpansion.AMEDataType;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
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
import mekanism.api.providers.IBlockProvider;
import mekanism.api.providers.IGasProvider;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.heat.VariableHeatCapacitor;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.heat.HeatCapacitorHelper;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableDouble;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo.GasSlotInfo;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NBTUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

public abstract class BEAbstractCompactMixingReactor extends TileEntityConfigurableMachine
        implements IPacketReceiverSetLong {
            
    private static final double caseHeatCapacity = 1;
    private static final double inverseInsulation = 100_000;

    private AMETier tier;
    private long mixingRate;
    public IHeatCapacitor heatCapacitor;
    private double lastEnvironmentLoss;
    private double lastTransferLoss;
    protected IGasTank leftFuelTank;
    protected IGasTank mixedFuelTank;
    protected IGasTank rightFuelTank;
    protected BasicFluidTank waterTank;
    protected IGasTank steamTank;

    public BEAbstractCompactMixingReactor(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        configComponent = new TileComponentConfig(this, TransmissionType.GAS, TransmissionType.FLUID,
                TransmissionType.HEAT);
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
        ejectorComponent = new ExtendedComponentEjector(this, () -> Long.MAX_VALUE)
                .setOutputData(configComponent, TransmissionType.GAS, TransmissionType.HEAT)
                .setCanChemicalTankEject((tank, type) -> tank == steamTank && type == AMEDataType.STEAM);
        mixingRate = 0;
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
    protected IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener,
            CachedAmbientTemperature ambientTemperature) {
        HeatCapacitorHelper builder = HeatCapacitorHelper.forSideWithConfig(this::getDirection, this::getConfig);
        double biomeAmbientTemp = HeatAPI.getAmbientTemp(getLevel(),getTilePos());
        builder.addCapacitor(
                heatCapacitor = VariableHeatCapacitor.create(caseHeatCapacity, this::getInverseConductionCoefficient,
                () -> inverseInsulation, () -> biomeAmbientTemp, this));
        return builder.build();
    }

    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper
                .forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(leftFuelTank = ChemicalTankBuilder.GAS.create(tier.longValue,
                (gas, a) -> false,
                (gas, a) -> leftFuel().getChemical() == gas,
                gas -> leftFuel().getChemical() == gas,
                ChemicalAttributeValidator.ALWAYS_ALLOW, listener));
        builder.addTank(mixedFuelTank = ChemicalTankBuilder.GAS.create(tier.longValue,
                (gas, a) -> false,
                (gas, a) -> mixedFuel().getChemical() == gas,
                gas -> mixedFuel().getChemical() == gas,
                ChemicalAttributeValidator.ALWAYS_ALLOW, listener));
        builder.addTank(rightFuelTank = ChemicalTankBuilder.GAS.create(tier.longValue,
                (gas, a) -> false,
                (gas, a) -> rightFuel().getChemical() == gas,
                gas -> rightFuel().getChemical() == gas,
                ChemicalAttributeValidator.ALWAYS_ALLOW, listener));
        builder.addTank(steamTank = ChemicalTankBuilder.GAS.output(Math.max(tier.longValue, 0x7fffffff), listener));
        return builder.build();
    }

    @Override
    public IFluidTankHolder getInitialFluidTanks(IContentsListener listener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(waterTank = BasicFluidTank.input(0x7fffffff,
                stack -> stack.isFluidEqual(new FluidStack(Fluids.WATER, 1)), listener));
        return builder.build();
    }

    protected abstract IGasProvider leftFuel();

    protected abstract IGasProvider rightFuel();

    protected abstract IGasProvider mixedFuel();

    protected abstract void heat(long usedFuel);

    protected abstract void generateSteam(long usedFuel);

    protected abstract double workableTemp();

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        mixFuel();
        HeatTransfer loss = simulate();
        lastEnvironmentLoss = loss.environmentTransfer();
        lastTransferLoss = loss.adjacentTransfer();
        long usedFuel = 0;
        if (MekanismUtils.canFunction(this)
                && heatCapacitor.getTemperature() > workableTemp()
                && heatCapacitor.getTemperature() < Double.MAX_VALUE / 4
                && !mixedFuelTank.isEmpty()) {
            usedFuel = mixedFuelTank.getStored();
            heat(usedFuel);
            mixedFuelTank.setEmpty();
        }
        generateSteam(usedFuel);
    }

    private void mixFuel() {
        if (leftFuelTank.isEmpty() || rightFuelTank.isEmpty() || mixedFuelTank.getNeeded() < 2 || mixingRate < 2) {
            return;
        }
        if (leftFuelTank.getType() != leftFuel().getChemical()
                || rightFuelTank.getType() != rightFuel().getChemical()) {
            return;
        }
        if (!mixedFuelTank.isEmpty() && mixedFuelTank.getType() != mixedFuel().getChemical()) {
            return;
        }
        long amount = Math.min(Math.min(mixingRate / 2, mixedFuelTank.getNeeded() / 2),
                Math.min(leftFuelTank.getStored(), rightFuelTank.getStored()));
        leftFuelTank.shrinkStack(amount, Action.EXECUTE);
        rightFuelTank.shrinkStack(amount, Action.EXECUTE);
        mixedFuelTank.insert(mixedFuel().getStack(amount * 2), Action.EXECUTE, AutomationType.INTERNAL);
    }

    public void receive(int num, long value) {
        mixingRate = value;
    }

    public long getMixingRate() {
        return mixingRate;
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
        container.track(SyncableLong.create(this::getMixingRate, value -> mixingRate = value));
        container.track(SyncableDouble.create(this::getLastTransferLoss, value -> lastTransferLoss = value));
        container.track(SyncableDouble.create(this::getLastEnvironmentLoss, value -> lastEnvironmentLoss = value));
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        NBTUtils.setLongIfPresent(nbt, "mixingRate", value -> mixingRate = value);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags) {
        super.saveAdditional(nbtTags);
        nbtTags.putLong("mixingRate", mixingRate);
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

}
