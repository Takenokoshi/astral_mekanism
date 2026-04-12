package astral_mekanism.network.to_server;

import appeng.api.stacks.AEKey;
import astral_mekanism.block.blockentity.base.AppliedConfigurableBlockEntity;
import mekanism.common.network.IMekanismPacket;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

public class PacketGuiAEKeySlotSet implements IMekanismPacket {

    private final BlockPos pos;
    private final int slotIndex;
    private final AEKey key;

    public PacketGuiAEKeySlotSet(BlockPos pos, int slotIndex, AEKey key) {
        this.pos = pos;
        this.slotIndex = slotIndex;
        this.key = key;
    }

    @Override
    public void handle(Context context) {
        context.getSender().level();
        if (WorldUtils.getTileEntity(context.getSender().level(), pos) instanceof AppliedConfigurableBlockEntity be) {
            be.setAEKeyByIndex(slotIndex, key);
        }
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeInt(slotIndex);
        AEKey.writeKey(buffer, key);
    }

    public static PacketGuiAEKeySlotSet decode(FriendlyByteBuf buf) {
        return new PacketGuiAEKeySlotSet(buf.readBlockPos(), buf.readInt(), AEKey.readKey(buf));
    }

}
