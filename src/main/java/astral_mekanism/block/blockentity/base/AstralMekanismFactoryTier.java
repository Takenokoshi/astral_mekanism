package astral_mekanism.block.blockentity.base;

import mekanism.api.tier.BaseTier;
import mekanism.api.tier.ITier;

public enum AstralMekanismFactoryTier implements ITier {
    ESSENTIAL("essential", 2),
    BASIC("basic_standard", 4),
    ADVANCED("advanced", 6),
    ELITE("elite", 8),
    ULTIMATE("enchanted_ultimate", 10),
    ABSOLUTE("absolute_overclocked", 12),
    SUPREME("supreme_quantum", 16),
    COSMIC("cosmic_dence", 20),
    INFINITE("infinite_multiversal", 24),
    ASTRAL("astronomical", "astral", 32);

    public final String nameForAstral;
    public final String nameForNormal;
    public final int processes;
    public final int verticalProcesses;
    public final int horizontalProcesses;

    private AstralMekanismFactoryTier(String nameForAstral, String nameForNormal, int processes) {
        this.nameForNormal = nameForNormal;
        this.nameForAstral = nameForAstral;
        this.processes = processes;
        this.verticalProcesses = processes > 16 ? 2 : 1;
        this.horizontalProcesses = processes > 16 ? processes / 2 : processes;
    };

    private AstralMekanismFactoryTier(String name, int processes) {
        this(name, name, processes);
    }

    @Override
    public BaseTier getBaseTier() {
        return BaseTier.ULTIMATE;
    }
}
