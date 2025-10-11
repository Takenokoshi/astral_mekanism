package astral_mekanism.block.blockentity.prefab;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.core.BlockEntityUtils;
import astral_mekanism.block.blockentity.elements.AstralMekDataType;
import astral_mekanism.registries.AstralMekanismItems;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mekanism.api.Action;
import mekanism.api.IContentsListener;
import mekanism.api.NBTConstants;
import mekanism.api.RelativeSide;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.merged.MergedChemicalTank;
import mekanism.api.chemical.pigment.IPigmentTank;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.ISlurryTank;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.api.heat.HeatAPI.HeatTransfer;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
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
import mekanism.common.integration.computer.annotation.SyntheticComputerMethod;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableDouble;
import mekanism.common.inventory.container.sync.SyncableEnum;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.inventory.slot.chemical.MergedChemicalInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.TileEntityChemicalTank;
import mekanism.common.tile.TileEntityChemicalTank.GasMode;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo.GasSlotInfo;
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo.InfusionSlotInfo;
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo.PigmentSlotInfo;
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo.SlurrySlotInfo;
import mekanism.common.tile.component.config.slot.FluidSlotInfo;
import mekanism.common.tile.interfaces.IHasGasMode;
import mekanism.common.tile.interfaces.ISustainedData;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.util.NBTUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BEAbstractStorage extends TileEntityConfigurableMachine
        implements ISustainedData, IHasGasMode {

    @SyntheticComputerMethod(getter = "getDumpingMode", getterDescription = "Get the current Dumping configuration")
    GasMode dumping0;
    GasMode dumping1;
    BasicEnergyContainer energyContainer;
    BasicFluidTank fluidTank0;
    BasicFluidTank fluidTank1;
    MergedChemicalTank mergedChemicalTank0;
    MergedChemicalTank mergedChemicalTank1;

    EnergyInventorySlot energyInput;
    EnergyInventorySlot energyOutput;
    FluidInventorySlot fluidInput0;
    FluidInventorySlot fluidInput1;
    FluidInventorySlot fluidOutput0;
    FluidInventorySlot fluidOutput1;
    MergedChemicalInventorySlot<MergedChemicalTank> chemicalInput0;
    MergedChemicalInventorySlot<MergedChemicalTank> chemicalInput1;
    MergedChemicalInventorySlot<MergedChemicalTank> chemicalOutput0;
    MergedChemicalInventorySlot<MergedChemicalTank> chemicalOutput1;

    private double lastEnvironmentLoss;
    private double lastTransferLoss;
    BasicHeatCapacitor heatCapacitor;

    protected static final Predicate<ItemStack> isInsertUpgrade = item -> ItemStack.isSameItem(item,
            AstralMekanismItems.INSERT_UPGRADE.getItemStack());
    protected static final Predicate<ItemStack> isSingularityUpgrade = item -> ItemStack.isSameItem(item,
            AstralMekanismItems.SINGULARITY_UPGRADE.getItemStack());

    public BEAbstractStorage(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        this.dumping0 = GasMode.IDLE;
        this.dumping1 = GasMode.IDLE;
        this.configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.ENERGY,
                TransmissionType.FLUID, TransmissionType.GAS, TransmissionType.INFUSION,
                TransmissionType.PIGMENT, TransmissionType.SLURRY, TransmissionType.HEAT);
        ConfigInfo itemConfig = this.configComponent.getConfig(TransmissionType.ITEM);
        if (itemConfig != null) {
            setUpItemConfig(itemConfig);
        }
        configComponent.setupIOConfig(TransmissionType.ENERGY, energyContainer, RelativeSide.FRONT);
        configComponent.setupIOConfig(TransmissionType.HEAT, heatCapacitor, RelativeSide.FRONT);
        ConfigInfo fluidConfig = this.configComponent.getConfig(TransmissionType.FLUID);
        if (fluidConfig != null) {
            fluidConfig.addSlotInfo(DataType.OUTPUT_1, new FluidSlotInfo(false, true, fluidTank0));
            fluidConfig.addSlotInfo(DataType.OUTPUT_2, new FluidSlotInfo(false, true, fluidTank1));
            fluidConfig.addSlotInfo(DataType.INPUT_1, new FluidSlotInfo(true, false, fluidTank0));
            fluidConfig.addSlotInfo(DataType.INPUT_2, new FluidSlotInfo(true, false, fluidTank1));
            fluidConfig.addSlotInfo(AstralMekDataType.INPUT1_OUTPUT1,
                    new FluidSlotInfo(true, false, fluidTank0));
            fluidConfig.addSlotInfo(AstralMekDataType.INPUT1_OUTPUT2,
                    new FluidSlotInfo(true, false, fluidTank0));
            fluidConfig.addSlotInfo(AstralMekDataType.INPUT2_OUTPUT1,
                    new FluidSlotInfo(true, false, fluidTank1));
            fluidConfig.addSlotInfo(AstralMekDataType.INPUT2_OUTPUT2,
                    new FluidSlotInfo(true, false, fluidTank1));
            fluidConfig.addSlotInfo(AstralMekDataType.OUTPUT1low,
                    new FluidSlotInfo(false, false, fluidTank0));
            fluidConfig.addSlotInfo(AstralMekDataType.OUTPUT2low,
                    new FluidSlotInfo(false, false, fluidTank1));
            fluidConfig.setEjecting(true);
        }
        ConfigInfo gasConfig = this.configComponent.getConfig(TransmissionType.GAS);
        if (gasConfig != null) {
            gasConfig.addSlotInfo(DataType.OUTPUT_1,
                    new GasSlotInfo(false, true, mergedChemicalTank0.getGasTank()));
            gasConfig.addSlotInfo(DataType.OUTPUT_2,
                    new GasSlotInfo(false, true, mergedChemicalTank1.getGasTank()));
            gasConfig.addSlotInfo(DataType.INPUT_1,
                    new GasSlotInfo(true, false, mergedChemicalTank0.getGasTank()));
            gasConfig.addSlotInfo(DataType.INPUT_2,
                    new GasSlotInfo(true, false, mergedChemicalTank1.getGasTank()));
            gasConfig.addSlotInfo(AstralMekDataType.INPUT1_OUTPUT1,
                    new GasSlotInfo(true, false, mergedChemicalTank0.getGasTank()));
            gasConfig.addSlotInfo(AstralMekDataType.INPUT1_OUTPUT2,
                    new GasSlotInfo(true, false, mergedChemicalTank0.getGasTank()));
            gasConfig.addSlotInfo(AstralMekDataType.INPUT2_OUTPUT1,
                    new GasSlotInfo(true, false, mergedChemicalTank1.getGasTank()));
            gasConfig.addSlotInfo(AstralMekDataType.INPUT2_OUTPUT2,
                    new GasSlotInfo(true, false, mergedChemicalTank1.getGasTank()));
            gasConfig.addSlotInfo(AstralMekDataType.OUTPUT1low,
                    new GasSlotInfo(false, false, mergedChemicalTank0.getGasTank()));
            gasConfig.addSlotInfo(AstralMekDataType.OUTPUT2low,
                    new GasSlotInfo(false, false, mergedChemicalTank1.getGasTank()));
            gasConfig.setEjecting(true);
        }
        ConfigInfo infusionConfig = this.configComponent.getConfig(TransmissionType.INFUSION);
        if (infusionConfig != null) {
            infusionConfig.addSlotInfo(DataType.OUTPUT_1,
                    new InfusionSlotInfo(false, true, mergedChemicalTank0.getInfusionTank()));
            infusionConfig.addSlotInfo(DataType.OUTPUT_2,
                    new InfusionSlotInfo(false, true, mergedChemicalTank1.getInfusionTank()));
            infusionConfig.addSlotInfo(DataType.INPUT_1,
                    new InfusionSlotInfo(true, false, mergedChemicalTank0.getInfusionTank()));
            infusionConfig.addSlotInfo(DataType.INPUT_2,
                    new InfusionSlotInfo(true, false, mergedChemicalTank1.getInfusionTank()));
            infusionConfig.addSlotInfo(AstralMekDataType.INPUT1_OUTPUT1,
                    new InfusionSlotInfo(true, false, mergedChemicalTank0.getInfusionTank()));
            infusionConfig.addSlotInfo(AstralMekDataType.INPUT1_OUTPUT2,
                    new InfusionSlotInfo(true, false, mergedChemicalTank0.getInfusionTank()));
            infusionConfig.addSlotInfo(AstralMekDataType.INPUT2_OUTPUT1,
                    new InfusionSlotInfo(true, false, mergedChemicalTank1.getInfusionTank()));
            infusionConfig.addSlotInfo(AstralMekDataType.INPUT2_OUTPUT2,
                    new InfusionSlotInfo(true, false, mergedChemicalTank1.getInfusionTank()));
            infusionConfig.addSlotInfo(AstralMekDataType.OUTPUT1low,
                    new InfusionSlotInfo(false, false, mergedChemicalTank0.getInfusionTank()));
            infusionConfig.addSlotInfo(AstralMekDataType.OUTPUT2low,
                    new InfusionSlotInfo(false, false, mergedChemicalTank1.getInfusionTank()));
            infusionConfig.setEjecting(true);
        }
        ConfigInfo pigmentConfig = this.configComponent.getConfig(TransmissionType.PIGMENT);
        if (pigmentConfig != null) {
            pigmentConfig.addSlotInfo(DataType.OUTPUT_1,
                    new PigmentSlotInfo(false, true, mergedChemicalTank0.getPigmentTank()));
            pigmentConfig.addSlotInfo(DataType.OUTPUT_2,
                    new PigmentSlotInfo(false, true, mergedChemicalTank1.getPigmentTank()));
            pigmentConfig.addSlotInfo(DataType.INPUT_1,
                    new PigmentSlotInfo(true, false, mergedChemicalTank0.getPigmentTank()));
            pigmentConfig.addSlotInfo(DataType.INPUT_2,
                    new PigmentSlotInfo(true, false, mergedChemicalTank1.getPigmentTank()));
            pigmentConfig.addSlotInfo(AstralMekDataType.INPUT1_OUTPUT1,
                    new PigmentSlotInfo(true, false, mergedChemicalTank0.getPigmentTank()));
            pigmentConfig.addSlotInfo(AstralMekDataType.INPUT1_OUTPUT2,
                    new PigmentSlotInfo(true, false, mergedChemicalTank0.getPigmentTank()));
            pigmentConfig.addSlotInfo(AstralMekDataType.INPUT2_OUTPUT1,
                    new PigmentSlotInfo(true, false, mergedChemicalTank1.getPigmentTank()));
            pigmentConfig.addSlotInfo(AstralMekDataType.INPUT2_OUTPUT2,
                    new PigmentSlotInfo(true, false, mergedChemicalTank1.getPigmentTank()));
            pigmentConfig.addSlotInfo(AstralMekDataType.OUTPUT1low,
                    new PigmentSlotInfo(false, false, mergedChemicalTank0.getPigmentTank()));
            pigmentConfig.addSlotInfo(AstralMekDataType.OUTPUT2low,
                    new PigmentSlotInfo(false, false, mergedChemicalTank1.getPigmentTank()));
            pigmentConfig.setEjecting(true);
        }
        ConfigInfo slurryConfig = this.configComponent.getConfig(TransmissionType.SLURRY);
        if (slurryConfig != null) {
            slurryConfig.addSlotInfo(DataType.OUTPUT_1,
                    new SlurrySlotInfo(false, true, mergedChemicalTank0.getSlurryTank()));
            slurryConfig.addSlotInfo(DataType.OUTPUT_2,
                    new SlurrySlotInfo(false, true, mergedChemicalTank1.getSlurryTank()));
            slurryConfig.addSlotInfo(DataType.INPUT_1,
                    new SlurrySlotInfo(true, false, mergedChemicalTank0.getSlurryTank()));
            slurryConfig.addSlotInfo(DataType.INPUT_2,
                    new SlurrySlotInfo(true, false, mergedChemicalTank1.getSlurryTank()));
            slurryConfig.addSlotInfo(AstralMekDataType.INPUT1_OUTPUT1,
                    new SlurrySlotInfo(true, false, mergedChemicalTank0.getSlurryTank()));
            slurryConfig.addSlotInfo(AstralMekDataType.INPUT1_OUTPUT2,
                    new SlurrySlotInfo(true, false, mergedChemicalTank0.getSlurryTank()));
            slurryConfig.addSlotInfo(AstralMekDataType.INPUT2_OUTPUT1,
                    new SlurrySlotInfo(true, false, mergedChemicalTank1.getSlurryTank()));
            slurryConfig.addSlotInfo(AstralMekDataType.INPUT2_OUTPUT2,
                    new SlurrySlotInfo(true, false, mergedChemicalTank1.getSlurryTank()));
            slurryConfig.addSlotInfo(AstralMekDataType.OUTPUT1low,
                    new SlurrySlotInfo(false, false, mergedChemicalTank0.getSlurryTank()));
            slurryConfig.addSlotInfo(AstralMekDataType.OUTPUT2low,
                    new SlurrySlotInfo(false, false, mergedChemicalTank1.getSlurryTank()));
            slurryConfig.setEjecting(true);
        }
        ejectorComponent = new TileComponentEjector(this, () -> Long.MAX_VALUE, () -> 200000,
                () -> FloatingLong.MAX_VALUE);
        ejectorComponent.setOutputData(this.configComponent, TransmissionType.ITEM, TransmissionType.ENERGY,
                TransmissionType.FLUID, TransmissionType.GAS, TransmissionType.INFUSION,
                TransmissionType.PIGMENT,
                TransmissionType.SLURRY)
                .setCanTankEject((tank) -> {
                    if (tank == this.mergedChemicalTank0.getGasTank()) {
                        return this.dumping0 != GasMode.DUMPING;
                    } else if (tank == this.mergedChemicalTank1.getGasTank()) {
                        return this.dumping1 != GasMode.DUMPING;
                    } else {
                        return true;
                    }
                });
    }

    protected abstract FloatingLong energyCapacitySetter();

    protected abstract int fluidTankCapacitySetter();

    protected abstract long chemicalTankCapacitySetter();

    protected abstract void setUpItemConfig(ConfigInfo itemConfig);

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = addUniqueSlots(InventorySlotHelper.forSideWithConfig(this::getDirection,
                this::getConfig), listener);
        builder.addSlot(energyInput = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel,
                listener, 206, 162));
        builder.addSlot(energyOutput = EnergyInventorySlot.drain(energyContainer, listener, 224, 162));
        builder.addSlot(fluidInput0 = FluidInventorySlot.fill(fluidTank0, listener, 224, 44));
        builder.addSlot(fluidOutput0 = FluidInventorySlot.drain(fluidTank0, listener, 224, 62));
        builder.addSlot(fluidInput1 = FluidInventorySlot.fill(fluidTank1, listener, 224, 120));
        builder.addSlot(fluidOutput1 = FluidInventorySlot.drain(fluidTank1, listener, 224, 138));
        builder.addSlot(chemicalInput0 = MergedChemicalInventorySlot.fill(mergedChemicalTank0,
                listener, 278, 44));
        builder.addSlot(chemicalOutput0 = MergedChemicalInventorySlot.drain(mergedChemicalTank0,
                listener, 278, 62));
        builder.addSlot(chemicalInput1 = MergedChemicalInventorySlot.fill(mergedChemicalTank1,
                listener, 278, 120));
        builder.addSlot(chemicalOutput1 = MergedChemicalInventorySlot.drain(mergedChemicalTank1,
                listener, 278, 138));
        energyInput.setSlotOverlay(SlotOverlay.POWER);
        energyOutput.setSlotOverlay(SlotOverlay.POWER);
        fluidInput0.setSlotOverlay(SlotOverlay.MINUS);
        fluidInput1.setSlotOverlay(SlotOverlay.MINUS);
        fluidOutput0.setSlotOverlay(SlotOverlay.PLUS);
        fluidOutput1.setSlotOverlay(SlotOverlay.PLUS);
        chemicalInput0.setSlotOverlay(SlotOverlay.MINUS);
        chemicalInput1.setSlotOverlay(SlotOverlay.MINUS);
        chemicalOutput0.setSlotOverlay(SlotOverlay.PLUS);
        chemicalOutput1.setSlotOverlay(SlotOverlay.PLUS);
        return builder.build();
    }

    protected abstract InventorySlotHelper addUniqueSlots(InventorySlotHelper builder, IContentsListener listener);

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection,
                this::getConfig);
        builder.addContainer(energyContainer = BasicEnergyContainer.create(energyCapacitySetter(),
                listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(fluidTank0 = BasicFluidTank.create(fluidTankCapacitySetter(), fluid -> true, listener));
        builder.addTank(fluidTank1 = BasicFluidTank.create(fluidTankCapacitySetter(), fluid -> true, listener));
        return builder.build();
    }

    @Override
    protected void presetVariables() {
        super.presetVariables();
        long capacity = chemicalTankCapacitySetter();
        mergedChemicalTank0 = MergedChemicalTank.create(
                ChemicalTankBuilder.GAS.create(capacity, gas -> true, this),
                ChemicalTankBuilder.INFUSION.create(capacity, infusion -> true, this),
                ChemicalTankBuilder.PIGMENT.create(capacity, pigment -> true, this),
                ChemicalTankBuilder.SLURRY.create(capacity, slurry -> true, this));
        mergedChemicalTank1 = MergedChemicalTank.create(
                ChemicalTankBuilder.GAS.create(capacity, gas -> true, this),
                ChemicalTankBuilder.INFUSION.create(capacity, infusion -> true, this),
                ChemicalTankBuilder.PIGMENT.create(capacity, pigment -> true, this),
                ChemicalTankBuilder.SLURRY.create(capacity, slurry -> true, this));
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper
                .forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(mergedChemicalTank0.getGasTank());
        builder.addTank(mergedChemicalTank1.getGasTank());
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<InfuseType, InfusionStack, IInfusionTank> getInitialInfusionTanks(
            IContentsListener listener) {
        ChemicalTankHelper<InfuseType, InfusionStack, IInfusionTank> builder = ChemicalTankHelper
                .forSideInfusionWithConfig(this::getDirection, this::getConfig);
        builder.addTank(mergedChemicalTank0.getInfusionTank());
        builder.addTank(mergedChemicalTank1.getInfusionTank());
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Pigment, PigmentStack, IPigmentTank> getInitialPigmentTanks(
            IContentsListener listener) {
        ChemicalTankHelper<Pigment, PigmentStack, IPigmentTank> builder = ChemicalTankHelper
                .forSidePigmentWithConfig(this::getDirection, this::getConfig);
        builder.addTank(mergedChemicalTank0.getPigmentTank());
        builder.addTank(mergedChemicalTank1.getPigmentTank());
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Slurry, SlurryStack, ISlurryTank> getInitialSlurryTanks(IContentsListener listener) {
        ChemicalTankHelper<Slurry, SlurryStack, ISlurryTank> builder = ChemicalTankHelper
                .forSideSlurryWithConfig(this::getDirection, this::getConfig);
        builder.addTank(mergedChemicalTank0.getSlurryTank());
        builder.addTank(mergedChemicalTank1.getSlurryTank());
        return builder.build();
    }

    @NotNull
    @Override
    protected IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener,
            CachedAmbientTemperature ambientTemperature) {
        HeatCapacitorHelper builder = HeatCapacitorHelper.forSide(this::getDirection);
        builder.addCapacitor(
                heatCapacitor = BasicHeatCapacitor.create(100, 5, 10, ambientTemperature, listener));
        return builder.build();
    }

    protected abstract void onUSUnique();

    @Override
    protected void onUpdateServer() {
        energyInput.fillContainerOrConvert();
        energyOutput.drainContainer();
        fluidInput0.fillTank();
        fluidInput1.fillTank();
        fluidOutput0.drainTank(fluidOutput0);
        fluidOutput1.drainTank(fluidOutput1);
        chemicalInput0.fillChemicalTanks();
        chemicalInput1.fillChemicalTanks();
        chemicalOutput0.drainChemicalTanks();
        chemicalOutput1.drainChemicalTanks();
        HeatTransfer loss = simulate();
        lastEnvironmentLoss = loss.environmentTransfer();
        lastTransferLoss = loss.adjacentTransfer();
        super.onUpdateServer();
        onUSUnique();
        BlockEntityUtils.fluidEject(this,
                List.of(AstralMekDataType.INPUT1_OUTPUT1, AstralMekDataType.INPUT2_OUTPUT1,
                        AstralMekDataType.OUTPUT1low),
                fluidTank0);
        BlockEntityUtils.fluidEject(this,
                List.of(AstralMekDataType.INPUT1_OUTPUT2, AstralMekDataType.INPUT2_OUTPUT2,
                        AstralMekDataType.OUTPUT2low),
                fluidTank1);
        BlockEntityUtils.gasEject(this,
                List.of(AstralMekDataType.INPUT1_OUTPUT1, AstralMekDataType.INPUT2_OUTPUT1,
                        AstralMekDataType.OUTPUT1low),
                mergedChemicalTank0.getGasTank());
        BlockEntityUtils.gasEject(this,
                List.of(AstralMekDataType.INPUT1_OUTPUT2, AstralMekDataType.INPUT2_OUTPUT2,
                        AstralMekDataType.OUTPUT2low),
                mergedChemicalTank1.getGasTank());
        BlockEntityUtils.infusionEject(this,
                List.of(AstralMekDataType.INPUT1_OUTPUT1, AstralMekDataType.INPUT2_OUTPUT1,
                        AstralMekDataType.OUTPUT1low),
                mergedChemicalTank0.getInfusionTank());
        BlockEntityUtils.infusionEject(this,
                List.of(AstralMekDataType.INPUT1_OUTPUT2, AstralMekDataType.INPUT2_OUTPUT2,
                        AstralMekDataType.OUTPUT2low),
                mergedChemicalTank1.getInfusionTank());
        BlockEntityUtils.pigmentEject(this,
                List.of(AstralMekDataType.INPUT1_OUTPUT1, AstralMekDataType.INPUT2_OUTPUT1,
                        AstralMekDataType.OUTPUT1low),
                mergedChemicalTank0.getPigmentTank());
        BlockEntityUtils.pigmentEject(this,
                List.of(AstralMekDataType.INPUT1_OUTPUT2, AstralMekDataType.INPUT2_OUTPUT2,
                        AstralMekDataType.OUTPUT2low),
                mergedChemicalTank1.getPigmentTank());
        BlockEntityUtils.slurryEject(this,
                List.of(AstralMekDataType.INPUT1_OUTPUT1, AstralMekDataType.INPUT2_OUTPUT1,
                        AstralMekDataType.OUTPUT1low),
                mergedChemicalTank0.getSlurryTank());
        BlockEntityUtils.slurryEject(this,
                List.of(AstralMekDataType.INPUT1_OUTPUT2, AstralMekDataType.INPUT2_OUTPUT2,
                        AstralMekDataType.OUTPUT2low),
                mergedChemicalTank1.getSlurryTank());
        this.handleTank(this.mergedChemicalTank0.getGasTank(), dumping0);
        this.handleTank(this.mergedChemicalTank1.getGasTank(), dumping1);
    }

    private void handleTank(IGasTank tank, TileEntityChemicalTank.GasMode mode) {
        long dump_level = (long) Math.floor(tank.getCapacity() * 0.96);
        if (!tank.isEmpty()) {
            if (mode == GasMode.DUMPING) {
                tank.shrinkStack(tank.getStored(), Action.EXECUTE);
            } else if (mode == GasMode.DUMPING_EXCESS && tank.getStored() > dump_level) {
                tank.shrinkStack((long) (tank.getStored() - dump_level), Action.EXECUTE);
            }
        }
    }

    @Override
    public void nextMode(int tank) {
        if (tank == 0) {
            dumping0 = dumping0.getNext();
            markForSave();
        } else if (tank == 1) {
            dumping1 = dumping1.getNext();
            markForSave();
        }
    }

    @Override
    public Map<String, String> getTileDataRemap() {
        Map<String, String> remap = new Object2ObjectOpenHashMap<>();
        remap.put(NBTConstants.DUMP_LEFT, NBTConstants.DUMP_LEFT);
        remap.put(NBTConstants.DUMP_RIGHT, NBTConstants.DUMP_RIGHT);
        return remap;
    }

    @Override
    public void readSustainedData(CompoundTag dataMap) {
        NBTUtils.setEnumIfPresent(dataMap, NBTConstants.DUMP_LEFT, GasMode::byIndexStatic,
                mode -> dumping0 = mode);
        NBTUtils.setEnumIfPresent(dataMap, NBTConstants.DUMP_RIGHT, GasMode::byIndexStatic,
                mode -> dumping1 = mode);
    }

    @Override
    public void writeSustainedData(CompoundTag dataMap) {
        NBTUtils.writeEnum(dataMap, NBTConstants.DUMP_LEFT, dumping0);
        NBTUtils.writeEnum(dataMap, NBTConstants.DUMP_RIGHT, dumping1);
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
        container.track(
                SyncableEnum.create(GasMode::byIndexStatic, GasMode.IDLE, () -> dumping0,
                        value -> dumping0 = value));
        container.track(
                SyncableEnum.create(GasMode::byIndexStatic, GasMode.IDLE, () -> dumping1,
                        value -> dumping1 = value));
        container.track(SyncableDouble.create(this::getLastTransferLoss, value -> lastTransferLoss = value));
        container.track(SyncableDouble.create(this::getLastEnvironmentLoss,
                value -> lastEnvironmentLoss = value));
    }

    public BasicEnergyContainer getEnergyContainer() {
        return this.energyContainer;
    }

    public List<BasicEnergyContainer> getEnergyContainers() {
        return List.of(energyContainer);
    }

    public BasicFluidTank getFluidTank0() {
        return this.fluidTank0;
    }

    public BasicFluidTank getFluidTank1() {
        return this.fluidTank1;
    }

    public List<IExtendedFluidTank> getFluidTanks() {
        return List.of(fluidTank0, fluidTank1);
    }

    public MergedChemicalTank getChemicalTank0() {
        return this.mergedChemicalTank0;
    }

    public MergedChemicalTank getChemicalTank1() {
        return this.mergedChemicalTank1;
    }

    public BasicHeatCapacitor getHeatCapacitor() {
        return this.heatCapacitor;
    }

    public GasMode getGasMode0() {
        return dumping0;
    }

    public GasMode getGasMode1() {
        return dumping1;
    }

    public EnergyInventorySlot[] getEnergyInventorySlots() {
        EnergyInventorySlot[] result = { energyInput, energyOutput };
        return result;
    }

    public FluidInventorySlot[] getFluidInventorySlots() {
        FluidInventorySlot[] result = { fluidInput0, fluidInput1, fluidOutput0, fluidOutput1 };
        return result;
    }

    public MergedChemicalInventorySlot<?>[] getChemicalInventorySlots() {
        MergedChemicalInventorySlot<?>[] result = { chemicalInput0, chemicalInput1, chemicalOutput0, chemicalOutput1 };
        return result;
    }

}
