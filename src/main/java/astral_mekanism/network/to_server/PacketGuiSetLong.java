package astral_mekanism.network.to_server;

import astral_mekanism.block.blockentity.interf.IPacketReceiverSetLong;
import mekanism.common.network.IMekanismPacket;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent.Context;

public class PacketGuiSetLong implements IMekanismPacket {

    private final int num;
    private final long value;
    private final BlockPos pos;

    public PacketGuiSetLong(int num, long value, BlockPos pos) {
        this.num = num;
        this.value = value;
        this.pos = pos;
    }

    @Override
    public void handle(Context context) {
        Player player = context.getSender();
        if (player == null) {
            return;
        }
        if (WorldUtils.getTileEntity(TileEntityMekanism.class, player.level(),
                pos) instanceof IPacketReceiverSetLong be) {
            be.receive(num, value);
        }
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(num);
        buffer.writeLong(value);
        buffer.writeBlockPos(pos);
    }

    public static PacketGuiSetLong decode(FriendlyByteBuf buffer) {
        return new PacketGuiSetLong(buffer.readInt(), buffer.readLong(), buffer.readBlockPos());
    }

}
