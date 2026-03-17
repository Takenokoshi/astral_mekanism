package astral_mekanism;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(AMEConstants.MODID)
public class AMEBootstrap {
    public AMEBootstrap() {
        DistExecutor.unsafeRunForDist(() -> AMEClient::new, () -> AstralMekanism::new);
    }
}
