package astral_mekanism.block.blockentity.base;

import mekanism.api.tier.BaseTier;
import mekanism.api.tier.ITier;

public enum AstralMekanismFactoryTier implements ITier {
    ESSENTIAL("essential", 12),
    BASIC("basic_standard", 16),
    ADVANCED("advanced", 24),
    ELITE("elite", 32),
    ULTIMATE("enchanted_ultimate", 48),
    ABSOLUTE("absolute_overclocked", 64),
    SUPREME("supreme_quantum", 96),
    COSMIC("cosmic_dense", 128),
    INFINITE("infinite_multiversal", 192),
    ASTRAL("astronomical", "astral", 256);

    public final String nameForAstral;
    public final String nameForNormal;
    public final int processes;

    private AstralMekanismFactoryTier(String nameForAstral, String nameForNormal, int processes) {
        this.nameForNormal = nameForNormal;
        this.nameForAstral = nameForAstral;
        this.processes = processes;
    };

    private AstralMekanismFactoryTier(String name, int processes) {
        this(name, name, processes);
    }

    @Override
    public BaseTier getBaseTier() {
        return BaseTier.ULTIMATE;
    }
}
