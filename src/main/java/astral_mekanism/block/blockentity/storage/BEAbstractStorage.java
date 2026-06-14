package astral_mekanism.block.blockentity.storage;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import astral_mekanism.block.blockentity.elements.energyContainer.UniversalStorageEnergyContainer;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedEnergyInventorySlot;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedFluidInventorySlot;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedGasInventorySlot;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedInfusionInventorySlot;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedOutputInventorySlot;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedPigmentInventorySlot;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedSlurryInventorySlot;
import astral_mekanism.enums.AMEUpgrade;
import astral_mekanism.integration.AMEEmpowered;
import mekanism.api.Action;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.Upgrade;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.pigment.IPigmentTank;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.ISlurryTank;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.api.heat.HeatAPI.HeatTransfer;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.chemical.variable.VariableCapacityChemicalTankBuilder;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.fluid.VariableCapacityFluidTank;
import mekanism.common.capabilities.heat.BasicHeatCapacitor;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.heat.HeatCapacitorHelper;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableBoolean;
import mekanism.common.inventory.container.sync.SyncableDouble;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo;
import mekanism.common.tile.component.config.slot.FluidSlotInfo;
import mekanism.common.tile.component.config.slot.HeatSlotInfo;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BEAbstractStorage extends TileEntityConfigurableMachine {

    public static final int TANKS = 4;

    protected VariableCapacityFluidTank[] fluidTanks;
    protected PagedFluidInventorySlot[] fluidInsertSlots;
    protected PagedFluidInventorySlot[] fluidExtractSlots;
    protected PagedOutputInventorySlot[] fluidReturnSlots;
    protected IGasTank[] gasTanks;
    protected PagedGasInventorySlot[] gasInsertSlots;
    protected PagedGasInventorySlot[] gasExtractSlots;
    protected IInfusionTank[] infusionTanks;
    protected PagedInfusionInventorySlot[] infusionInsertSlots;
    protected PagedInfusionInventorySlot[] infusionExtractSlots;
    protected IPigmentTank[] pigmentTanks;
    protected PagedPigmentInventorySlot[] pigmentInsertSlots;
    protected PagedPigmentInventorySlot[] pigmentExtractSlots;
    protected ISlurryTank[] slurryTanks;
    protected PagedSlurryInventorySlot[] slurryInsertSlots;
    protected PagedSlurryInventorySlot[] slurryExtractSlots;
    protected UniversalStorageEnergyContainer energyContainer;
    protected PagedEnergyInventorySlot energyInsertSlot;
    protected PagedEnergyInventorySlot energyExtractSlot;
    protected double lastEnvironmentLoss;
    protected double lastTransferLoss;
    protected BasicHeatCapacitor heatCapacitor;

    protected int fluidTankCapacity = (1 << 15) - 1;
    protected long chemicalTankCapacity = (1l << 15) - 1;

    public boolean tankCapacityChanged;

    public BEAbstractStorage(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        configComponent = new TileComponentConfig(this, TransmissionType.values());
        setUpItemConfig(configComponent.getConfig(TransmissionType.ITEM));
        configComponent.setupIOConfig(TransmissionType.ENERGY, energyContainer, RelativeSide.RIGHT);
        ConfigInfo fluidConfig = configComponent.getConfig(TransmissionType.FLUID);
        fluidConfig.addSlotInfo(DataType.INPUT, new FluidSlotInfo(true, false, fluidTanks));
        fluidConfig.addSlotInfo(DataType.OUTPUT, new FluidSlotInfo(false, true, fluidTanks));
        fluidConfig.addSlotInfo(DataType.INPUT_OUTPUT, new FluidSlotInfo(true, true, fluidTanks));
        fluidConfig.setCanEject(true);
        ConfigInfo gasConfig = configComponent.getConfig(TransmissionType.GAS);
        gasConfig.addSlotInfo(DataType.INPUT, new ChemicalSlotInfo.GasSlotInfo(true, false, gasTanks));
        gasConfig.addSlotInfo(DataType.OUTPUT, new ChemicalSlotInfo.GasSlotInfo(false, true, gasTanks));
        gasConfig.addSlotInfo(DataType.INPUT_OUTPUT, new ChemicalSlotInfo.GasSlotInfo(true, true, gasTanks));
        gasConfig.setCanEject(true);
        ConfigInfo infusionConfig = configComponent.getConfig(TransmissionType.INFUSION);
        infusionConfig.addSlotInfo(DataType.INPUT, new ChemicalSlotInfo.InfusionSlotInfo(true, false, infusionTanks));
        infusionConfig.addSlotInfo(DataType.OUTPUT, new ChemicalSlotInfo.InfusionSlotInfo(false, true, infusionTanks));
        infusionConfig.addSlotInfo(DataType.INPUT_OUTPUT,
                new ChemicalSlotInfo.InfusionSlotInfo(true, true, infusionTanks));
        infusionConfig.setCanEject(true);
        ConfigInfo pigmentConfig = configComponent.getConfig(TransmissionType.PIGMENT);
        pigmentConfig.addSlotInfo(DataType.INPUT, new ChemicalSlotInfo.PigmentSlotInfo(true, false, pigmentTanks));
        pigmentConfig.addSlotInfo(DataType.OUTPUT, new ChemicalSlotInfo.PigmentSlotInfo(false, true, pigmentTanks));
        pigmentConfig.addSlotInfo(DataType.INPUT_OUTPUT,
                new ChemicalSlotInfo.PigmentSlotInfo(true, true, pigmentTanks));
        pigmentConfig.setCanEject(true);
        ConfigInfo slurryConfig = configComponent.getConfig(TransmissionType.SLURRY);
        slurryConfig.addSlotInfo(DataType.INPUT, new ChemicalSlotInfo.SlurrySlotInfo(true, false, slurryTanks));
        slurryConfig.addSlotInfo(DataType.OUTPUT, new ChemicalSlotInfo.SlurrySlotInfo(false, true, slurryTanks));
        slurryConfig.addSlotInfo(DataType.INPUT_OUTPUT, new ChemicalSlotInfo.SlurrySlotInfo(true, true, slurryTanks));
        slurryConfig.setCanEject(true);
        ConfigInfo heatConfig = configComponent.getConfig(TransmissionType.HEAT);
        heatConfig.addSlotInfo(DataType.INPUT_OUTPUT, new HeatSlotInfo(true, true, List.of(heatCapacitor)));
        ejectorComponent = new TileComponentEjector(this, () -> Long.MAX_VALUE, () -> 0x7fffffff,
                () -> FloatingLong.MAX_VALUE)
                .setOutputData(configComponent, TransmissionType.values());
    }

    protected abstract void setUpItemConfig(ConfigInfo itemConfig);

    @Override
    protected final @NotNull IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(
                energyContainer = new UniversalStorageEnergyContainer(getInitialBaseEnergyCapacity(), listener));
        return builder.build();
    }

    protected abstract FloatingLong getInitialBaseEnergyCapacity();

    @Override
    protected final @NotNull IFluidTankHolder getInitialFluidTanks(IContentsListener listener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        fluidTanks = new VariableCapacityFluidTank[4];
        for (int index = 0; index < 4; index++) {
            builder.addTank(fluidTanks[index] = VariableCapacityFluidTank.create(this::getFluidTankCapacity,
                    (stack, type) -> true,
                    (stack, type) -> true,
                    stack -> true, listener));
        }
        return builder.build();
    }

    protected int getFluidTankCapacity() {
        return fluidTankCapacity;
    };

    @Override
    public final @NotNull IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper
                .forSideGasWithConfig(this::getDirection, this::getConfig);
        gasTanks = new IGasTank[4];
        for (int index = 0; index < 4; index++) {
            builder.addTank(gasTanks[index] = VariableCapacityChemicalTankBuilder.GAS.create(
                    this::getChemicalTankCapacity,
                    (stack, type) -> true,
                    (stack, type) -> true,
                    stack -> true, ChemicalAttributeValidator.ALWAYS_ALLOW, listener));
        }
        return builder.build();
    }

    @Override
    public final @NotNull IChemicalTankHolder<InfuseType, InfusionStack, IInfusionTank> getInitialInfusionTanks(
            IContentsListener listener) {
        ChemicalTankHelper<InfuseType, InfusionStack, IInfusionTank> builder = ChemicalTankHelper
                .forSideInfusionWithConfig(this::getDirection, this::getConfig);
        infusionTanks = new IInfusionTank[4];
        for (int index = 0; index < 4; index++) {
            builder.addTank(infusionTanks[index] = VariableCapacityChemicalTankBuilder.INFUSION.create(
                    this::getChemicalTankCapacity,
                    (stack, type) -> true,
                    (stack, type) -> true,
                    stack -> true, ChemicalAttributeValidator.ALWAYS_ALLOW, listener));
        }
        return builder.build();
    }

    @Override
    public final @NotNull IChemicalTankHolder<Pigment, PigmentStack, IPigmentTank> getInitialPigmentTanks(
            IContentsListener listener) {
        ChemicalTankHelper<Pigment, PigmentStack, IPigmentTank> builder = ChemicalTankHelper
                .forSidePigmentWithConfig(this::getDirection, this::getConfig);
        pigmentTanks = new IPigmentTank[4];
        for (int index = 0; index < 4; index++) {
            builder.addTank(pigmentTanks[index] = VariableCapacityChemicalTankBuilder.PIGMENT.create(
                    this::getChemicalTankCapacity,
                    (stack, type) -> true,
                    (stack, type) -> true,
                    stack -> true, ChemicalAttributeValidator.ALWAYS_ALLOW, listener));
        }
        return builder.build();
    }

    @Override
    public @NotNull IChemicalTankHolder<Slurry, SlurryStack, ISlurryTank> getInitialSlurryTanks(
            IContentsListener listener) {
        ChemicalTankHelper<Slurry, SlurryStack, ISlurryTank> builder = ChemicalTankHelper
                .forSideSlurryWithConfig(this::getDirection, this::getConfig);
        slurryTanks = new ISlurryTank[4];
        for (int index = 0; index < 4; index++) {
            builder.addTank(slurryTanks[index] = VariableCapacityChemicalTankBuilder.SLURRY.create(
                    this::getChemicalTankCapacity,
                    (stack, type) -> true,
                    (stack, type) -> true,
                    stack -> true, ChemicalAttributeValidator.ALWAYS_ALLOW, listener));
        }
        return builder.build();
    }

    protected long getChemicalTankCapacity() {
        return chemicalTankCapacity;
    };

    @Override
    protected final @NotNull IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        addSlots(builder, listener);
        int page = getPagesForItem();
        fluidInsertSlots = new PagedFluidInventorySlot[TANKS];
        fluidExtractSlots = new PagedFluidInventorySlot[TANKS];
        fluidReturnSlots = new PagedOutputInventorySlot[TANKS];
        for (int fluidIndex = 0; fluidIndex < TANKS; fluidIndex++) {
            builder.addSlot(fluidInsertSlots[fluidIndex] = PagedFluidInventorySlot.fill(
                    fluidTanks[fluidIndex], listener, 32 + 36 * fluidIndex, 15, page))
                    .setSlotOverlay(SlotOverlay.MINUS);
        }
        for (int fluidIndex = 0; fluidIndex < TANKS; fluidIndex++) {
            builder.addSlot(fluidExtractSlots[fluidIndex] = PagedFluidInventorySlot.drain(
                    fluidTanks[fluidIndex], listener, 32 + 36 * fluidIndex, 51, page));
        }
        for (int fluidIndex = 0; fluidIndex < TANKS; fluidIndex++) {
            builder.addSlot(fluidReturnSlots[fluidIndex] = PagedOutputInventorySlot.at(
                    listener, 32 + 36 * fluidIndex, 33, page))
                    .setSlotOverlay(SlotOverlay.PLUS);
        }
        final int chemicalInsertSlotY = 18;
        final int chemicalExtractSlotY = 36;
        page++;
        gasInsertSlots = new PagedGasInventorySlot[TANKS];
        gasExtractSlots = new PagedGasInventorySlot[TANKS];
        for (int gasIndex = 0; gasIndex < TANKS; gasIndex++) {
            builder.addSlot(gasInsertSlots[gasIndex] = PagedGasInventorySlot.fillOrConvert(gasTanks[gasIndex],
                    this::getLevel, listener, 32 + 36 * gasIndex, chemicalInsertSlotY, page))
                    .setSlotOverlay(SlotOverlay.MINUS);
        }
        for (int gasIndex = 0; gasIndex < TANKS; gasIndex++) {
            builder.addSlot(gasExtractSlots[gasIndex] = PagedGasInventorySlot.drain(gasTanks[gasIndex],
                    listener, 32 + 36 * gasIndex, chemicalExtractSlotY, page))
                    .setSlotOverlay(SlotOverlay.PLUS);
        }
        page++;
        infusionInsertSlots = new PagedInfusionInventorySlot[TANKS];
        infusionExtractSlots = new PagedInfusionInventorySlot[TANKS];
        for (int infusionIndex = 0; infusionIndex < TANKS; infusionIndex++) {
            builder.addSlot(infusionInsertSlots[infusionIndex] = PagedInfusionInventorySlot.fillOrConvert(
                    infusionTanks[infusionIndex], this::getLevel,
                    listener, 32 + 36 * infusionIndex, chemicalInsertSlotY, page))
                    .setSlotOverlay(SlotOverlay.MINUS);
        }
        for (int infusionIndex = 0; infusionIndex < TANKS; infusionIndex++) {
            builder.addSlot(infusionExtractSlots[infusionIndex] = PagedInfusionInventorySlot.drain(
                    infusionTanks[infusionIndex],
                    listener, 32 + 36 * infusionIndex, chemicalExtractSlotY, page))
                    .setSlotOverlay(SlotOverlay.PLUS);
        }
        page++;
        pigmentInsertSlots = new PagedPigmentInventorySlot[TANKS];
        pigmentExtractSlots = new PagedPigmentInventorySlot[TANKS];
        for (int pigmentIndex = 0; pigmentIndex < TANKS; pigmentIndex++) {
            builder.addSlot(pigmentInsertSlots[pigmentIndex] = PagedPigmentInventorySlot.fill(
                    pigmentTanks[pigmentIndex],
                    listener, 32 + 36 * pigmentIndex, chemicalInsertSlotY, page))
                    .setSlotOverlay(SlotOverlay.MINUS);
        }
        for (int pigmentIndex = 0; pigmentIndex < TANKS; pigmentIndex++) {
            builder.addSlot(pigmentExtractSlots[pigmentIndex] = PagedPigmentInventorySlot.drain(
                    pigmentTanks[pigmentIndex],
                    listener, 32 + 36 * pigmentIndex, chemicalExtractSlotY, page))
                    .setSlotOverlay(SlotOverlay.PLUS);
        }
        page++;
        slurryInsertSlots = new PagedSlurryInventorySlot[TANKS];
        slurryExtractSlots = new PagedSlurryInventorySlot[TANKS];
        for (int slurryIndex = 0; slurryIndex < TANKS; slurryIndex++) {
            builder.addSlot(slurryInsertSlots[slurryIndex] = PagedSlurryInventorySlot.fill(
                    slurryTanks[slurryIndex], listener, 32 + 36 * slurryIndex, chemicalInsertSlotY, page))
                    .setSlotOverlay(SlotOverlay.MINUS);
        }
        for (int slurryIndex = 0; slurryIndex < TANKS; slurryIndex++) {
            builder.addSlot(slurryExtractSlots[slurryIndex] = PagedSlurryInventorySlot.drain(
                    slurryTanks[slurryIndex], listener, 32 + 36 * slurryIndex, chemicalExtractSlotY, page))
                    .setSlotOverlay(SlotOverlay.PLUS);
        }
        page++;
        builder.addSlot(energyInsertSlot = PagedEnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel,
                listener, 17, 35, page))
                .setSlotOverlay(SlotOverlay.POWER);
        builder.addSlot(energyExtractSlot = PagedEnergyInventorySlot.drain(energyContainer,
                listener, 143, 35, page))
                .setSlotOverlay(SlotOverlay.POWER);
        return builder.build();
    }

    protected abstract void addSlots(InventorySlotHelper builder, IContentsListener listener);

    public abstract int getPagesForItem();

    @NotNull
    @Override
    protected IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener,
            CachedAmbientTemperature ambientTemperature) {
        HeatCapacitorHelper builder = HeatCapacitorHelper.forSide(this::getDirection);
        builder.addCapacitor(
                heatCapacitor = BasicHeatCapacitor.create(100, 5, 10, ambientTemperature, listener));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        if (tankCapacityChanged) {
            for (int p = 0; p < TANKS; p++) {
                if (fluidTanks[p].getFluidAmount() > fluidTankCapacity) {
                    fluidTanks[p].setStackSize(fluidTankCapacity, Action.EXECUTE);
                }
                if (gasTanks[p].getStored() > chemicalTankCapacity) {
                    gasTanks[p].setStackSize(chemicalTankCapacity, Action.EXECUTE);
                }
                if (infusionTanks[p].getStored() > chemicalTankCapacity) {
                    infusionTanks[p].setStackSize(chemicalTankCapacity, Action.EXECUTE);
                }
                if (pigmentTanks[p].getStored() > chemicalTankCapacity) {
                    pigmentTanks[p].setStackSize(chemicalTankCapacity, Action.EXECUTE);
                }
                if (slurryTanks[p].getStored() > chemicalTankCapacity) {
                    slurryTanks[p].setStackSize(chemicalTankCapacity, Action.EXECUTE);
                }
            }
            tankCapacityChanged = false;
        }
        super.onUpdateServer();
        for (int p = 0; p < TANKS; p++) {
            fluidInsertSlots[p].fillTank(fluidReturnSlots[p]);
            fluidExtractSlots[p].drainTank(fluidReturnSlots[p]);
            gasInsertSlots[p].fillTankOrConvert();
            infusionInsertSlots[p].fillTankOrConvert();
            pigmentInsertSlots[p].fillTank();
            slurryInsertSlots[p].fillTank();
            gasExtractSlots[p].drainTank();
            infusionExtractSlots[p].drainTank();
            pigmentExtractSlots[p].drainTank();
            slurryExtractSlots[p].drainTank();
        }
        energyInsertSlot.fillContainerOrConvert();
        energyExtractSlot.drainContainer();
        HeatTransfer loss = simulate();
        lastEnvironmentLoss = loss.environmentTransfer();
        lastTransferLoss = loss.adjacentTransfer();
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == Upgrade.ENERGY || AMEEmpowered.isEmpoweredEnergy(upgrade)) {
            energyContainer.recaluculateEnergyUpgrade(
                    upgradeComponent.getUpgrades(Upgrade.ENERGY) + AMEEmpowered.getEmpoweredEnergies(this));
        } else if (upgrade == AMEUpgrade.TANK_CAPACITY.getValue()
                || upgrade == AMEUpgrade.ADVANCED_TANK_CAPACITY.getValue()) {
            fluidTankCapacity = (1 << 15
                    + upgradeComponent.getUpgrades(AMEUpgrade.TANK_CAPACITY.getValue())
                    + upgradeComponent.getUpgrades(AMEUpgrade.ADVANCED_TANK_CAPACITY.getValue())) - 1;
            chemicalTankCapacity = (1l << 15
                    + upgradeComponent.getUpgrades(AMEUpgrade.TANK_CAPACITY.getValue()) * 2
                    + upgradeComponent.getUpgrades(AMEUpgrade.ADVANCED_TANK_CAPACITY.getValue()) * 4) - 1;
            tankCapacityChanged = true;
        }
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
        container.track(SyncableDouble.create(this::getLastTransferLoss, value -> lastTransferLoss = value));
        container.track(SyncableDouble.create(this::getLastEnvironmentLoss, value -> lastEnvironmentLoss = value));
        container.track(SyncableInt.create(this::getFluidTankCapacity, value -> fluidTankCapacity = value));
        container.track(SyncableLong.create(this::getChemicalTankCapacity, value -> chemicalTankCapacity = value));
        container.track(SyncableBoolean.create(() -> tankCapacityChanged, value -> tankCapacityChanged = value));
    }

    public UniversalStorageEnergyContainer getEnergyContainer() {
        return energyContainer;
    }

    public BasicFluidTank getFluidTank(int index) {
        return fluidTanks[index % TANKS];
    }

    public IGasTank getGasTank(int index) {
        return gasTanks[index % TANKS];
    }

    public IInfusionTank getInfusionTank(int index) {
        return infusionTanks[index % TANKS];
    }

    public IPigmentTank getPigmentTank(int index) {
        return pigmentTanks[index % TANKS];
    }

    public ISlurryTank getSlurryTank(int index) {
        return slurryTanks[index % TANKS];
    }

    public BasicHeatCapacitor getHeatCapacitor() {
        return this.heatCapacitor;
    }

}
