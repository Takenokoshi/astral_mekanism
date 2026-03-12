package astral_mekanism;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(AstralMekanismID.MODID)
public class AstralMekanismBootstrap {
    public AstralMekanismBootstrap() {
        DistExecutor.unsafeRunForDist(() -> AstralMekanismClient::new, () -> AstralMekanism::new);
    }
}
