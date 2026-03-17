package astral_mekanism.network.to_server;

import astral_mekanism.block.blockentity.storage.BEXpTank;
import mekanism.common.network.IMekanismPacket;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class PacketGuiXpTank implements IMekanismPacket {

    private final BlockPos pos;
    private final boolean val;
    private final int val2;

    public PacketGuiXpTank(BlockPos pos, boolean val, int val2) {
        this.pos = pos;
        this.val = val;
        this.val2 = val2;
    }

    @Override
    public void handle(Context context) {
        ServerPlayer player = context.getSender();
        if (player == null) {
            return;
        }
        if (WorldUtils.getTileEntity(player.level(), pos) instanceof BEXpTank xpTank) {
            if (val) {
                xpTank.giveXp(player, val2);
            } else {
                xpTank.receiveXP(player, val2);
            }
        }
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeBoolean(val);
        buffer.writeInt(val2);
    }

    public static PacketGuiXpTank decode(FriendlyByteBuf buf) {
        return new PacketGuiXpTank(buf.readBlockPos(), buf.readBoolean(), buf.readInt());
    }

}
