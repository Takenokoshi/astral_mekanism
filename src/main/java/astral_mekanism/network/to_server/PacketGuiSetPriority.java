package astral_mekanism.network.to_server;

import astral_mekanism.block.blockentity.interf.AMEPriorityHost;
import mekanism.common.network.IMekanismPacket;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent.Context;

public class PacketGuiSetPriority implements IMekanismPacket {

    private final BlockPos pos;
    private final int value;

    public PacketGuiSetPriority(BlockPos pos, int value) {
        this.pos = pos;
        this.value = value;
    }

    @Override
    public void handle(Context context) {
        BlockEntity entity = WorldUtils.getTileEntity(BlockEntity.class, context.getSender().level(), pos);
        if (entity instanceof AMEPriorityHost priorityHost) {
            priorityHost.setPriority(value);
        }
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeInt(value);
    }

    public static PacketGuiSetPriority decode(FriendlyByteBuf buf) {
        return new PacketGuiSetPriority(buf.readBlockPos(), buf.readInt());
    }

}
