package astral_mekanism.block.blockentity.base;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import appeng.api.config.Actionable;
import appeng.api.networking.GridHelper;
import appeng.api.networking.IGrid;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.security.IActionSource;
import appeng.api.networking.storage.IStorageService;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import appeng.api.util.AECableType;
import appeng.hooks.ticking.TickHandler;
import appeng.me.helpers.BlockEntityNodeListener;
import appeng.me.helpers.IGridConnectedBlockEntity;
import astral_mekanism.block.blockentity.elements.aekey.AEKeySlot;
import mekanism.api.Action;
import mekanism.api.IContentsListener;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AppliedConfigurableBlockEntity extends TileEntityConfigurableMachine
        implements IGridConnectedBlockEntity {

    private static final String AE_KEY_SLOTS = "aeKeySlots";

    private final List<AEKeySlot<?>> aeKeySlots;
    private final IManagedGridNode mainNode;
    private boolean setChangedQueued = false;
    private byte queuedForReady = 0;
    private byte readyInvoked = 0;

    public AppliedConfigurableBlockEntity(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        this.aeKeySlots = Collections.unmodifiableList(getInitialAeKeySlots(this::markForSave));
        this.configComponent = createConfigComponent();
        this.ejectorComponent = createEjectorComponent(configComponent);
        this.mainNode = createMainNode()
                .setVisualRepresentation(blockProvider)
                .setInWorldNode(true)
                .setTagName("proxy");
    }

    protected abstract TileComponentConfig createConfigComponent();

    protected abstract TileComponentEjector createEjectorComponent(TileComponentConfig config);

    protected abstract List<AEKeySlot<?>> getInitialAeKeySlots(IContentsListener listener);

    protected IManagedGridNode createMainNode() {
        return GridHelper.createManagedNode(this, BlockEntityNodeListener.INSTANCE);
    }

    public void setAEKeyByIndex(int index, AEKey key) {
        aeKeySlots.get(index).setKey(key);
    }

    @Override
    public void load(CompoundTag data) {
        super.load(data);
        this.getMainNode().loadFromNBT(data);
        ListTag listTag = data.getList(AE_KEY_SLOTS, 10);
        for (int i = 0; i < aeKeySlots.size() && i < listTag.size(); i++) {
            aeKeySlots.get(i).readFromTag(listTag.getCompound(i));
        }
    }

    @Override
    public void saveAdditional(CompoundTag data) {
        super.saveAdditional(data);
        this.getMainNode().saveToNBT(data);
        ListTag listTag = new ListTag();
        for (int i = 0; i < aeKeySlots.size(); i++) {
            listTag.add(aeKeySlots.get(i).saveToTag());
        }
        data.put(AE_KEY_SLOTS, listTag);
    }

    public final IManagedGridNode getMainNode() {
        return this.mainNode;
    }

    @Override
    public AECableType getCableConnectionType(Direction dir) {
        return AECableType.SMART;
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        this.getMainNode().destroy();
    }

    protected final void onGridConnectableSidesChanged() {
        getMainNode().setExposedOnSides(EnumSet.allOf(Direction.class));
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        this.getMainNode().destroy();
    }

    public void onReady() {
        readyInvoked++;
        this.getMainNode().create(getLevel(), getBlockPos());
    }

    protected void scheduleInit() {
        queuedForReady++;
        GridHelper.onFirstTick(this, AppliedConfigurableBlockEntity::onReady);
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
        scheduleInit();
    }

    private Object setChangedAtEndOfTick(Level level) {
        this.setChanged();
        this.setChangedQueued = false;
        return null;
    }

    @Override
    public void saveChanges() {
        if (this.level == null) {
            return;
        }
        if (this.level.isClientSide) {
            this.setChanged();
        } else {
            this.level.blockEntityChanged(this.worldPosition);
            if (!this.setChangedQueued) {
                TickHandler.instance().addCallable(null, this::setChangedAtEndOfTick);
                this.setChangedQueued = true;
            }
        }
    }

    public byte getQueuedForReady() {
        return queuedForReady;
    }

    public byte getReadyInvoked() {
        return readyInvoked;
    }

    @Nullable
    protected final MEStorage getMeStorage() {

        IGrid grid = getMainNode().getGrid();
        if (grid == null) {
            return null;
        }

        IStorageService storageService = grid.getStorageService();
        if (storageService == null) {
            return null;
        }
        return storageService.getInventory();
    }

    public long getStoredAmount(AEKey key) {// public for gui
        if (key == null) {
            return 0;
        }
        MEStorage inventory = getMeStorage();
        if (inventory == null) {
            return 0;
        }
        return inventory.getAvailableStacks()
                .get(key);
    }

    protected long insertToStorage(AEKey key, long amount, Action action) {// return value is inserted amount
        MEStorage meStorage = getMeStorage();
        if (meStorage == null) {
            return 0;
        }
        return meStorage.insert(key, amount, action == Action.EXECUTE ? Actionable.MODULATE : Actionable.SIMULATE,
                IActionSource.ofMachine(this));
    }

    protected long consumeFromStorage(AEKey key, long amount, Action action) {// return value is extracted amount
        MEStorage meStorage = getMeStorage();
        if (meStorage == null) {
            return 0;
        }
        return meStorage.extract(key, amount, action == Action.EXECUTE ? Actionable.MODULATE : Actionable.SIMULATE,
                IActionSource.ofMachine(this));
    }

    public List<AEKeySlot<?>> getAeKeySlots() {
        return Collections.unmodifiableList(aeKeySlots);
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        aeKeySlots.forEach(slot -> slot.trackContainer(container));
    }

}
