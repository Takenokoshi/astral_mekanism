package astral_mekanism.config;

import mekanism.api.math.FloatingLong;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedFloatingLongValue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class AMEStorageConfig extends BaseMekanismConfig {

    private final ForgeConfigSpec configSpec;
    public final CachedFloatingLongValue greenHouse;
    public final CachedFloatingLongValue mekanicalCherger;
    public final CachedFloatingLongValue mekanicalInscriber;
    public final CachedFloatingLongValue transformer;
    public final CachedFloatingLongValue essentialCrafter;
    public final CachedFloatingLongValue fluidInfuser;
    public final CachedFloatingLongValue aaeReactionChamber;

    AMEStorageConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Machine Energy Storage Config. This config is synced from server to client.").push("storage");
        greenHouse = CachedFloatingLongValue.define(this, builder, "Base energy storage (Joules).",
                "greenHouse", FloatingLong.createConst(20000));
        mekanicalCherger = CachedFloatingLongValue.define(this, builder, "Base energy storage (Joules).",
                "mekanicalCherger", FloatingLong.createConst(20000));
        mekanicalInscriber = CachedFloatingLongValue.define(this, builder, "Base energy storage (Joules).",
                "mekanicalInscriber", FloatingLong.createConst(20000));
        transformer = CachedFloatingLongValue.define(this, builder, "Base energy storage (Joules).",
                "transformer", FloatingLong.createConst(20000));
        essentialCrafter = CachedFloatingLongValue.define(this, builder, "Base energy storage (Joules).",
                "essentialCrafter", FloatingLong.createConst(20000));
        fluidInfuser = CachedFloatingLongValue.define(this, builder, "Base energy storage (Joules).",
                "fluidInfuser", FloatingLong.createConst(80000));
        aaeReactionChamber = CachedFloatingLongValue.define(this, builder,
                "Base energy storage (Joules).",
                "aaeReactionChamber", FloatingLong.createConst(8000000000l));

        builder.pop();
        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "astral-mekanism-machine-storage";
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
