package astral_mekanism.network.to_server;

import astral_mekanism.block.blockentity.compact.BECompactTEP;
import mekanism.api.math.FloatingLong;
import mekanism.common.network.IMekanismPacket;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent.Context;

public class PacketGuiCompactTEP implements IMekanismPacket {

    private final BlockPos pos;
    private final FloatingLong value;

    public PacketGuiCompactTEP(BlockPos pos, FloatingLong value) {
        this.pos = pos;
        this.value = value;
    }

    @Override
    public void handle(Context context) {
        Player player = context.getSender();
        if (player == null) {
            return;
        }
        if (WorldUtils.getTileEntity(player.level(), pos) instanceof BECompactTEP tep) {
            tep.setEnergyUsageFromPacket(value);
        }
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        value.writeToBuffer(buffer);
    }

    public static PacketGuiCompactTEP decode(FriendlyByteBuf buf) {
        return new PacketGuiCompactTEP(buf.readBlockPos(), FloatingLong.readFromBuffer(buf));
    }

}
