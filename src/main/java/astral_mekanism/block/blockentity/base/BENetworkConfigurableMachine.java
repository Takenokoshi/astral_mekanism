package astral_mekanism.block.blockentity.base;

import appeng.api.networking.GridHelper;
import appeng.api.networking.IGrid;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.storage.IStorageService;
import appeng.api.storage.MEStorage;
import appeng.api.util.AECableType;
import appeng.hooks.ticking.TickHandler;
import appeng.me.helpers.BlockEntityNodeListener;
import appeng.me.helpers.IGridConnectedBlockEntity;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class BENetworkConfigurableMachine extends TileEntityConfigurableMachine
        implements IGridConnectedBlockEntity {

    protected final IManagedGridNode mainNode;
    private boolean setChangedQueued = false;

    public BENetworkConfigurableMachine(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        this.mainNode = this.createMainNode()
                .setVisualRepresentation(this.blockProvider)
                .setInWorldNode(true)
                .setTagName("proxy");
    }

    protected IManagedGridNode createMainNode() {
        return GridHelper.createManagedNode(this, BlockEntityNodeListener.INSTANCE);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        loadTag(tag);
    }

    public void loadTag(CompoundTag data) {
        this.getMainNode().loadFromNBT(data);
    }

    public void saveAdditional(CompoundTag data) {
        super.saveAdditional(data);
        this.getMainNode().saveToNBT(data);
    }

    public final IManagedGridNode getMainNode() {
        return this.mainNode;
    }

    public AECableType getCableConnectionType(Direction dir) {
        return AECableType.SMART;
    }

    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        this.getMainNode().destroy();
    }

    protected void scheduleInit() {
        GridHelper.onFirstTick(this, BENetworkConfigurableMachine::onReady);
    }

    public void onReady() {
        this.getMainNode().create(this.getLevel(), this.getBlockPos());
    }

    public void setRemoved() {
        super.setRemoved();
        this.getMainNode().destroy();
    }

    public void clearRemoved() {
        super.clearRemoved();
        this.scheduleInit();
    }

    @Override
    public void saveChanges() {
        if (this.level != null) {
            if (this.level.isClientSide) {
                this.setChanged();
            } else {
                this.level.blockEntityChanged(this.worldPosition);
                if (!this.setChangedQueued) {
                    TickHandler.instance().addCallable((LevelAccessor) null, this::setChangedAtEndOfTick);
                    this.setChangedQueued = true;
                }
            }
        }
    }

    private Object setChangedAtEndOfTick(Level level) {
        this.setChanged();
        this.setChangedQueued = false;
        return null;
    }

    public MEStorage getMeStorage() {
        IGrid grid = mainNode.getGrid();
        if (grid == null) {
            return null;
        }
        IStorageService storageService = grid.getStorageService();
        if (storageService == null) {
            return null;
        }
        return storageService.getInventory();
    }

}
