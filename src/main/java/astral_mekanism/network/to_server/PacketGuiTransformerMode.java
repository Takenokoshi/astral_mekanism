package astral_mekanism.network.to_server;

import astral_mekanism.block.blockentity.prefab.BEAbstractTransformer;
import mekanism.common.network.IMekanismPacket;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent.Context;

public class PacketGuiTransformerMode implements IMekanismPacket {

    private final BlockPos pos;

    public PacketGuiTransformerMode(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void handle(Context context) {
        Player player = context.getSender();
        if (player == null) {
            return;
        }
        if (WorldUtils.getTileEntity(player.level(), pos) instanceof BEAbstractTransformer transformer) {
            transformer.changeMode();
        }
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
    }

    public static PacketGuiTransformerMode decode(FriendlyByteBuf buffer) {
        return new PacketGuiTransformerMode(buffer.readBlockPos());
    }

}
