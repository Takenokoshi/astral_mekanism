package astral_mekanism.network.to_server;

import astral_mekanism.block.blockentity.base.BlockEntityProgressFactory;
import mekanism.common.network.IMekanismPacket;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent.Context;

public class PacketGuiProgressFactory implements IMekanismPacket {

    private final BlockPos pos;

    public PacketGuiProgressFactory(BlockPos pos) {
        this.pos = pos;
    }

    public static PacketGuiProgressFactory decode(FriendlyByteBuf buf){
        return new PacketGuiProgressFactory(buf.readBlockPos());
    }

    @Override
    public void handle(Context context) {
        Player player = context.getSender();
        if (player == null) {
            return;
        }
        if (WorldUtils.getTileEntity(player.level(), pos) instanceof BlockEntityProgressFactory factory) {
            factory.toggleSorting();
        }
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
    }

}
