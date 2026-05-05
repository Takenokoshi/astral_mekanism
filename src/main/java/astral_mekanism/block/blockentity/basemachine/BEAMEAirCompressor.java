package astral_mekanism.block.blockentity.basemachine;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import com.fxd927.mekanismelements.common.registries.MSGases;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IConfigurable;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.Upgrade;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.capabilities.resolver.BasicCapabilityResolver;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.chemical.GasInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.base.SubstanceType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.util.ChemicalUtil;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UpgradeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class BEAMEAirCompressor extends TileEntityConfigurableMachine implements IConfigurable {
    public final GasStack COMPRESSED_AIR_STACK = new GasStack(MSGases.COMPRESSED_AIR, 2000);

    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerChemicalTankWrapper.class, methodNames = {
            "getGas", "getGasCapacity", "getGasNeeded", "getGasFilledPercentage" }, docPlaceholder = "buffer tank")
    public IGasTank gasTank;

    private MachineEnergyContainer<BEAMEAirCompressor> energyContainer;

    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getInputItem", docPlaceholder = "")
    GasInventorySlot inputSlot;

    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getOutputItem", docPlaceholder = "")
    GasInventorySlot outputSlot;

    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getEnergyItem", docPlaceholder = "")
    private EnergyInventorySlot energySlot;

    public BEAMEAirCompressor(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        addCapabilityResolver(BasicCapabilityResolver.constant(Capabilities.CONFIGURABLE, this));
        addCapabilityResolver(BasicCapabilityResolver.constant(Capabilities.CONFIG_CARD, this));
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.GAS,
                TransmissionType.ENERGY);
        configComponent.setupItemIOConfig(List.of(inputSlot), List.of(outputSlot), energySlot, true);
        configComponent.setupOutputConfig(TransmissionType.GAS, gasTank);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM)
                .setCanEject(type -> MekanismUtils.canFunction(this));
        ejectorComponent.setOutputData(configComponent, TransmissionType.GAS)
                .setCanEject(type -> MekanismUtils.canFunction(this));
    }

    @Nonnull
    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper.forSide(this::getDirection);
        builder.addTank(gasTank = ChemicalTankBuilder.GAS.output(0x7fffffff, listener));
        return builder.build();
    }

    @Nonnull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSide(this::getDirection);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener));
        return builder.build();
    }

    @Override
    public boolean getActive() {
        return super.getActive();
    }

    @Nonnull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(inputSlot = GasInventorySlot.drain(gasTank, listener, 28, 20));
        builder.addSlot(outputSlot = GasInventorySlot.drain(gasTank, listener, 28, 51));
        builder.addSlot(
                energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 143, 35));
        outputSlot.setSlotType(ContainerSlotType.OUTPUT);
        inputSlot.setSlotOverlay(SlotOverlay.MINUS);
        outputSlot.setSlotOverlay(SlotOverlay.PLUS);
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        outputSlot.drainTank();

        boolean isGeneratingCompressedAir = false;

        Direction frontDirection = RelativeSide.FRONT.getDirection(getDirection());
        BlockPos frontPos = getBlockPos().relative(frontDirection);
        boolean isBlocked = !level.isEmptyBlock(frontPos);

        if (!isBlocked && MekanismUtils.canFunction(this) && COMPRESSED_AIR_STACK.getAmount() <= gasTank.getNeeded()) {
            FloatingLong energyPerTick = energyContainer.getEnergyPerTick();
            if (energyContainer.extract(energyPerTick, Action.SIMULATE, AutomationType.INTERNAL)
                    .equals(energyPerTick)) {
                energyContainer.extract(energyPerTick, Action.EXECUTE, AutomationType.INTERNAL);
                gasTank.insert(COMPRESSED_AIR_STACK, Action.EXECUTE, AutomationType.INTERNAL);
                isGeneratingCompressedAir = true;
            }
        }

        if (!gasTank.isEmpty()) {
            ChemicalUtil.emit(Collections.singleton(Direction.UP), gasTank, this, Long.MAX_VALUE);
        }

        setActive(isGeneratingCompressedAir);
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag nbtTags) {
        super.saveAdditional(nbtTags);
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
    }

    @Override
    public InteractionResult onSneakRightClick(Player player) {
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult onRightClick(Player player) {
        return InteractionResult.PASS;
    }

    @Override
    public boolean canPulse() {
        return true;
    }

    @Override
    public int getRedstoneLevel() {
        return MekanismUtils.redstoneLevelFromContents(gasTank.getStored(), gasTank.getCapacity());
    }

    @Override
    protected boolean makesComparatorDirty(@Nullable SubstanceType type) {
        return type == SubstanceType.GAS;
    }

    @Override
    public List<Component> getInfo(Upgrade upgrade) {
        return UpgradeUtils.getMultScaledInfo(this, upgrade);
    }

    public MachineEnergyContainer<BEAMEAirCompressor> getEnergyContainer() {
        return energyContainer;
    }

    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableLong.create(COMPRESSED_AIR_STACK::getAmount, COMPRESSED_AIR_STACK::setAmount));
    }
}