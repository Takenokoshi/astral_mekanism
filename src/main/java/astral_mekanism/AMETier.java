package astral_mekanism;

import java.util.EnumMap;
import java.util.function.Function;

import astral_mekanism.enumexpansion.AstralMekanismBaseTier;
import mekanism.api.tier.BaseTier;
import mekanism.api.tier.ITier;

public enum AMETier implements ITier {
    ESSENTIAL("essential", "logic", 12, 65536, 65536, AstralMekanismBaseTier.ESSENTIAL),
    BASIC("basic_standard", "calculation", 16, 131072, 1048576, BaseTier.BASIC),
    ADVANCED("advanced", "engineering", 24, 262144, 0x1000000, BaseTier.ADVANCED),
    ELITE("elite", "accumulation", 32, 0x100000, 268435456, BaseTier.ELITE),
    ULTIMATE("enchanted_ultimate", "photon", 48, 0x200000, 4294967296l, BaseTier.ULTIMATE),
    ABSOLUTE("absolute_overclocked", "quantum", 64, 0x400000, 68719476736l, AstralMekanismBaseTier.ABSOLUTE),
    SUPREME("supreme_quantum", "composite", 96, 0x1000000, 1099511627776l, AstralMekanismBaseTier.SUPREME),
    COSMIC("cosmic_dense", "origin", 128, 0x4000000, 281474976710656l, AstralMekanismBaseTier.COSMIC),
    INFINITE("infinite_multiversal", "autonomy", 192, 0x20000000, 72057594037927936l, AstralMekanismBaseTier.INFINITE),
    ASTRAL("astronomical", "astral", "firmament", 256, 0x7fffffff, 0x7fffffffffffffffl, AstralMekanismBaseTier.ASTRAL);

    public final String nameForAstral;
    public final String nameForNormal;
    public final String nameForAE;
    public final int processes;
    public final int intValue;
    public final long longValue;
    private final BaseTier tier;

    private AMETier(String nameForAstral, String nameForNormal, String nameForAE,
            int processes, int intValue, long longValue, BaseTier tier) {
        this.nameForNormal = nameForNormal;
        this.nameForAstral = nameForAstral;
        this.nameForAE = nameForAE;
        this.processes = processes;
        this.intValue = intValue;
        this.longValue = longValue;
        this.tier = tier;
    };

    private AMETier(String name, String nameForAE, int processes, int intValue, long longValue,
            BaseTier tier) {
        this(name, name, nameForAE, processes, intValue, longValue, tier);
    }

    @Override
    public BaseTier getBaseTier() {
        return tier;
    }

    public static class AMETierMap<T> extends EnumMap<AMETier, T> {

        public AMETierMap(Function<AMETier, T> creator) {
            super(AMETier.class);
            for (AMETier tier : AMETier.values()) {
                put(tier, creator.apply(tier));
            }
        }

    }
}
