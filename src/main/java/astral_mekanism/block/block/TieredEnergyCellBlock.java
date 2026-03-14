package astral_mekanism.block.block;

import appeng.block.networking.EnergyCellBlock;
import astral_mekanism.AstralMekanismTier;

public class TieredEnergyCellBlock extends EnergyCellBlock {

    private final AstralMekanismTier tier;

    public TieredEnergyCellBlock(double maxPower, double chargeRate, int priority, AstralMekanismTier tier) {
        super(maxPower, chargeRate, priority);
        this.tier = tier;
    }

    public TieredEnergyCellBlock(AstralMekanismTier tier) {
        this(Math.pow(8, tier.ordinal() + 1) * 12800000d, Math.pow(2, tier.ordinal() + 1) * 3200d,
                20001 + tier.ordinal(), tier);
    }

}
