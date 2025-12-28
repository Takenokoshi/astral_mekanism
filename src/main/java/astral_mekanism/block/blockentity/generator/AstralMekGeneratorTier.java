package astral_mekanism.block.blockentity.generator;

import mekanism.api.math.FloatingLong;
import mekanism.api.tier.BaseTier;
import mekanism.api.tier.ITier;

public enum AstralMekGeneratorTier implements ITier {
    ESSENTIAL("essential", FloatingLong.createConst(200000)),
    COSMIC("cosmic", FloatingLong.createConst(2000000000000l)),
    ASTRAL("astral", FloatingLong.MAX_VALUE);

    private AstralMekGeneratorTier(String name, FloatingLong energyCapacity) {
        this.name = name;
        this.energyCapacity = energyCapacity;
    }

    public final String name;
    public final FloatingLong energyCapacity;
    public long getGastankCapacity() {
        if (this == ASTRAL) {
            return Long.MAX_VALUE;
        } else if (this == COSMIC) {
            return 500000000000l;
        } else {
            return 20000l;
        }
    }

    @Override
    public BaseTier getBaseTier() {
        return BaseTier.ULTIMATE;
    }
}
