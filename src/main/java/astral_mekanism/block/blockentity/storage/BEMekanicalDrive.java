package astral_mekanism.block.blockentity.storage;

import java.util.EnumSet;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import appeng.api.networking.GridFlags;
import appeng.api.networking.GridHelper;
import appeng.api.networking.IManagedGridNode;
import appeng.api.storage.IStorageMounts;
import appeng.api.storage.IStorageProvider;
import appeng.api.storage.StorageCells;
import appeng.api.storage.cells.StorageCell;
import appeng.api.util.AECableType;
import appeng.block.storage.DriveBlock;
import appeng.blockentity.storage.DriveBlockEntity;
import appeng.core.definitions.AEBlocks;
import appeng.me.helpers.BlockEntityNodeListener;
import appeng.me.helpers.IGridConnectedBlockEntity;
import appeng.me.storage.DriveWatcher;
import appeng.menu.ISubMenu;
import astral_mekanism.AMETier;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedBasicInventorySlot;
import astral_mekanism.block.blockentity.interf.AMEPriorityHost;
import mekanism.api.IContentsListener;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BEMekanicalDrive extends TileEntityConfigurableMachine
        implements IGridConnectedBlockEntity, IStorageProvider, AMEPriorityHost<BEMekanicalDrive> {
    private static final String PRIORITY_NBT_KEY = "priority";
    private final IManagedGridNode mainNode;
    private AMETier tier;
    private int priority = 0;
    private PagedBasicInventorySlot[] slots;
    private final DriveWatcher[] invBySlot;
    private boolean setChangedQueued = false;
    private byte queuedForReady = 0;
    private byte readyInvoked = 0;

    DriveBlockEntity p;
    DriveBlock q;

    public BEMekanicalDrive(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        mainNode = this.createMainNode()
                .setVisualRepresentation(blockProvider)
                .setInWorldNode(true)
                .setTagName("proxy")
                .addService(IStorageProvider.class, this)
                .setFlags(new GridFlags[] { GridFlags.REQUIRE_CHANNEL })
                .setExposedOnSides(EnumSet.allOf(Direction.class))
                .setIdlePowerUsage(tier.processes * 2.0F);
        invBySlot = new DriveWatcher[tier.processes * 27];
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM);
        ConfigInfo itemInfo = configComponent.getConfig(TransmissionType.ITEM);
        itemInfo.addSlotInfo(DataType.INPUT, new InventorySlotInfo(true, false, List.<IInventorySlot>of(slots)));
        itemInfo.addSlotInfo(DataType.OUTPUT, new InventorySlotInfo(false, true, List.<IInventorySlot>of(slots)));
        itemInfo.addSlotInfo(DataType.INPUT_OUTPUT, new InventorySlotInfo(true, true, List.<IInventorySlot>of(slots)));
        ejectorComponent = new TileComponentEjector(this).setOutputData(configComponent, TransmissionType.ITEM);
    }

    public AMETier getTier() {
        return tier;
    }

    @Override
    protected void presetVariables() {
        super.presetVariables();
        tier = Attribute.getTier(getBlockType(), AMETier.class);
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        this.slots = new PagedBasicInventorySlot[tier.processes * 27];
        for (int i = 0; i < slots.length; i++) {
            int index = i;
            builder.addSlot(slots[index] = PagedBasicInventorySlot.at(StorageCells::isCellHandled,
                    () -> {
                        listener.onContentsChanged();
                        markForSave();
                        onContentsChanged();
                        updateSlot(index);
                    },
                    index % 9 * 18 + 8,
                    index % 27 / 9 * 18 + 17,
                    index / 27));
        }
        return builder.build();
    }

    private void updateSlot(int index) {
        if (slots[index].isEmpty()) {
            invBySlot[index] = null;
        } else {
            StorageCell cell = StorageCells.getCellInventory(slots[index].getStack(), this::onContentsChanged);
            if (cell == null) {
                invBySlot[index] = null;
            } else {
                invBySlot[index] = new DriveWatcher(cell, () -> {
                });
            }
        }
    }

    protected IManagedGridNode createMainNode() {
        return GridHelper.createManagedNode(this, BlockEntityNodeListener.INSTANCE);
    }

    @Override
    public IManagedGridNode getMainNode() {
        return mainNode;
    }

    @Override
    public void saveChanges() {
        markForSave();
    }

    @Override
    public void mountInventories(IStorageMounts mounts) {
        if (mainNode.isOnline()) {
            for (DriveWatcher watcher : invBySlot) {
                if (watcher != null) {
                    mounts.mount(watcher, priority);
                }
            }
        }
    }

    @Override
    public void onContentsChanged() {
        super.onContentsChanged();
        if (!level.isClientSide) {
            IStorageProvider.requestUpdate(mainNode);
        }
    }

    public void setPriority(int value) {
        priority = value;
        markForSave();
    }

    @Override
    public void load(CompoundTag tag) {
        mainNode.loadFromNBT(tag);
        super.load(tag);
        if (tag.contains(PRIORITY_NBT_KEY)) {
            priority = tag.getInt(PRIORITY_NBT_KEY);
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.putInt(PRIORITY_NBT_KEY, priority);
        mainNode.saveToNBT(tag);
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableInt.create(() -> priority, this::setPriority));
    }

    @Override
    public ItemStack getMainMenuIcon() {
        return AEBlocks.DRIVE.stack();
    }

    @Override
    public void returnToMainMenu(Player arg0, ISubMenu arg1) {// This drive doesn't use submenu. So this method won't be
                                                              // called.
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        if (!level.isClientSide) {
            mainNode.destroy();
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!level.isClientSide) {
            mainNode.create(level, worldPosition);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();

        if (!level.isClientSide) {
            mainNode.destroy();
        }
    }

    @Override
    public AECableType getCableConnectionType(Direction dir) {
        return AECableType.SMART;
    }

}
