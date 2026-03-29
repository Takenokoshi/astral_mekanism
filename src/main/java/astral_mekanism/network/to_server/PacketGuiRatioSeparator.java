package astral_mekanism.network.to_server;

import astral_mekanism.block.blockentity.storage.BERatioSeparator;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.network.IMekanismPacket;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

public class PacketGuiRatioSeparator implements IMekanismPacket {

    private final BlockPos pos;
    private final TransmissionType type;
    private final boolean isA;
    private final long value;

    public PacketGuiRatioSeparator(BlockPos pos, TransmissionType type, boolean isA, long value) {
        this.pos = pos;
        this.type = type;
        this.isA = isA;
        this.value = value;
    }

    @Override
    public void handle(Context context) {
        if (WorldUtils.getTileEntity(context.getSender().level(), pos) instanceof BERatioSeparator separator) {
            separator.setValue(type, isA, value);
        }
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeEnum(type);
        buffer.writeBoolean(isA);
        buffer.writeLong(value);
    }

    public static PacketGuiRatioSeparator decode(FriendlyByteBuf buf) {
        return new PacketGuiRatioSeparator(buf.readBlockPos(), buf.readEnum(TransmissionType.class), buf.readBoolean(),
                buf.readLong());
    }

}
