package astral_mekanism.registries;

import astral_mekanism.AstralMekanismID;
import net.pedroksl.ae2addonlib.registry.ItemRegistry;

public final class AstralMekanismItemDefinitions extends ItemRegistry {

    public static final AstralMekanismItemDefinitions INSTANCE = new AstralMekanismItemDefinitions();

    public AstralMekanismItemDefinitions() {
        super(AstralMekanismID.MODID);
    }

}
