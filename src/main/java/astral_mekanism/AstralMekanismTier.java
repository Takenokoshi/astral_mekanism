package astral_mekanism;

import astral_mekanism.enumexpansion.AstralMekanismBaseTier;
import mekanism.api.tier.BaseTier;
import mekanism.api.tier.ITier;

public enum AstralMekanismTier implements ITier {
    ESSENTIAL("essential", 12, 16, AstralMekanismBaseTier.ESSENTIAL),
    BASIC("basic_standard", 16, 20, BaseTier.BASIC),
    ADVANCED("advanced", 24, 24, BaseTier.ADVANCED),
    ELITE("elite", 32, 28, BaseTier.ELITE),
    ULTIMATE("enchanted_ultimate", 48, 32, BaseTier.ULTIMATE),
    ABSOLUTE("absolute_overclocked", 64, 36, AstralMekanismBaseTier.ABSOLUTE),
    SUPREME("supreme_quantum", 96, 40, AstralMekanismBaseTier.SUPREME),
    COSMIC("cosmic_dense", 128, 48, AstralMekanismBaseTier.COSMIC),
    INFINITE("infinite_multiversal", 192, 56, AstralMekanismBaseTier.INFINITE),
    ASTRAL("astronomical", "astral", 256, 63, AstralMekanismBaseTier.ASTRAL);

    public final String nameForAstral;
    public final String nameForNormal;
    public final int processes;
    public final int ex;
    private final BaseTier tier;

    private AstralMekanismTier(String nameForAstral, String nameForNormal, int processes, int ex, BaseTier tier) {
        this.nameForNormal = nameForNormal;
        this.nameForAstral = nameForAstral;
        this.processes = processes;
        this.ex = ex;
        this.tier = tier;
    };

    private AstralMekanismTier(String name, int processes, int ex, BaseTier tier) {
        this(name, name, processes, ex, tier);
    }

    @Override
    public BaseTier getBaseTier() {
        return tier;
    }

    public long exToLong() {
        if (ex >= 63) {
            return Long.MAX_VALUE;
        }
        return (long) Math.pow(2, ex);
    }

    public int exToInt() {
        if (ex >= 62) {
            return 0x7fffffff;
        }
        return (int) Math.pow(2, 16 + (ex - 16) / 3);
    }
}
