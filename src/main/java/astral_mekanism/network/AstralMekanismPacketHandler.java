package astral_mekanism.network;

import astral_mekanism.AstralMekanism;
import astral_mekanism.AstralMekanismID;
import astral_mekanism.network.to_server.PacketGuiEssentialSmelter;
import astral_mekanism.network.to_server.PacketGuiSetLong;
import mekanism.common.network.BasePacketHandler;
import net.minecraftforge.network.simple.SimpleChannel;

public class AstralMekanismPacketHandler extends BasePacketHandler {

    private final SimpleChannel channel = createChannel(AstralMekanismID.rl(AstralMekanismID.MODID),
            AstralMekanism.instance.version);

    @Override
    protected SimpleChannel getChannel() {
        return channel;
    }

    @Override
    public void initialize() {
        registerClientToServer(PacketGuiSetLong.class, PacketGuiSetLong::decode);
        registerClientToServer(PacketGuiEssentialSmelter.class, PacketGuiEssentialSmelter::decode);
    }

}
