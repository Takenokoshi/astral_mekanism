package astral_mekanism.config;

import mekanism.api.math.FloatingLong;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedFloatingLongValue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class AstMekUsageConfig extends BaseMekanismConfig {
    private final ForgeConfigSpec configSpec;
    public final CachedFloatingLongValue greenhouse;
    public final CachedFloatingLongValue mekanicalCherger;
    public final CachedFloatingLongValue mekanicalInscriber;
    public final CachedFloatingLongValue transformer;
    public final CachedFloatingLongValue essentialCrafter;
    public final CachedFloatingLongValue fluidInfuser;

    AstMekUsageConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Machine Energy Usage Config. This config is synced from server to client.").push("usage");
        greenhouse = CachedFloatingLongValue.define(this, builder, "Energy per operation tick (Joules).",
                "greenhouse", FloatingLong.createConst(50));
        mekanicalCherger = CachedFloatingLongValue.define(this, builder, "Energy per operation tick (Joules).",
                "mekanicalCherger", FloatingLong.createConst(50));
        mekanicalInscriber = CachedFloatingLongValue.define(this, builder, "Energy per operation tick (Joules).",
                "mekanicalInscriber", FloatingLong.createConst(50));
        transformer = CachedFloatingLongValue.define(this, builder, "Energy per operation tick (Joules).",
                "transformer", FloatingLong.createConst(50));
        essentialCrafter = CachedFloatingLongValue.define(this, builder, "Energy per operation tick (Joules).",
                "essentialCrafter", FloatingLong.createConst(50));
        fluidInfuser = CachedFloatingLongValue.define(this, builder, "Energy per operation tick (Joules).",
                "fluidInfuser", FloatingLong.createConst(200));

        builder.pop();
        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "astral-mekanism-machine-usage";
    }

    @Override
    public ForgeConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public Type getConfigType() {
        return Type.SERVER;
    }

    @Override
    public boolean addToContainer() {
        return false;
    }

}
